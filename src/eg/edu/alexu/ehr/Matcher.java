package eg.edu.alexu.ehr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Matcher {
	final int tau = 2;
	final int k = 10;

	public Matcher(PivotalTrie t) {
		trie = t;
	}

	private String typedWord;
	private String previousWord = "";
	private List<Integer> prev_invList = null;//should be replaced with cursor

	private List<Integer> word_invList = null;
	private Map<BasicTrieNode, PivotalActiveNode> activenodes = null;
	private PivotalTrie trie;

	HashMap<Integer, List<Integer>> diffList = new HashMap<>();

	List<Integer> deletion(String queryString, String previousQueryString) {

		List<Integer> cur_invList = new Vector<>();
		int word_cnt = queryString.split(" ").length;
		int previous_word_cnt = previousQueryString.split(" ").length;
		if (word_cnt == 0)
			return cur_invList;

		if (word_cnt == previous_word_cnt) {
			String prefix = Utils.getLastPrefix(queryString);
			String old_prefix = Utils.getLastPrefix(previousQueryString);

			activenodes = trie.matchPrefixInc(prefix, old_prefix, activenodes, tau);
			List<Integer> candidatesrecords = trie.getRecordsIDs(activenodes);
			cur_invList = Utils.intersectList(candidatesrecords, prev_invList);
			word_invList = cur_invList;
			if (Utils.verbose1)
				System.out.println("back to " + word_invList);
			return word_invList;

		} else if (word_cnt < previous_word_cnt) {
			List<Integer> restored = new Vector<>();
			for (int i = word_cnt; i < previous_word_cnt; i++) {
				restored.addAll(diffList.get(i));
			}
			word_invList = prev_invList;
			if (word_cnt == 1)
				prev_invList = null;
			else
				prev_invList = Utils.UnionList(prev_invList, restored);

			if (Utils.verbose1) {
				System.out.println("restored  to " + restored);
				System.out.println("	prev_invList  to " + prev_invList);

			}
			return word_invList;
		} else
			return null;
	}

	List<Integer> incremental(String queryString, String previousQueryString) {
		List<Integer> cur_invList = new Vector<>();

		String prefix = Utils.getLastPrefix(queryString);
		String old_prefix = Utils.getLastPrefix(previousQueryString);

		int word_cnt = queryString.split(" ").length;
		int previous_word_cnt = previousQueryString.split(" ").length;

		if (word_cnt > previous_word_cnt) {
			diffList.put(previous_word_cnt, Utils.diffList(word_invList, prev_invList));
			prev_invList = word_invList;
		}

		activenodes = trie.matchPrefixInc(prefix, old_prefix, activenodes, tau);
		RecordIterator r=new RecordIterator(trie,activenodes);
		//cur_invList = trie.getRecordsIDs(activenodes);
		for(int i=0;i<100;i++){
			int n=r.next();
			if(n!=-1){
			System.out.println ("*"+n+" *"+trie.forward.get(n));
			cur_invList.add(n);
			}
		}
		List<Integer> intersection = Utils.intersectList(cur_invList, prev_invList);

		if (Utils.verbose1) {
			int t = -1;
			if (prev_invList != null)
				t = prev_invList.size();
			System.out.println(queryString + " " + cur_invList.size() + "::::" + t + ">>>>" + intersection.size());
		}
		word_invList = intersection;
		return intersection;
	}

	List<String> getRecordsString(List<Integer> ids) {
		List<String> records = new Vector<>();
		for (int r : ids) {		
			records.add(trie.forward.get(r));
		}
		return records;
	}

	public List<String> getCandidate(String s) {

		typedWord = Utils.normalize(s.trim());

		if (typedWord.equals(previousWord))
			return null;

		List<Integer> candidaterecords = new Vector<>();

		if (Utils.verbose)
			System.out.print("Typed word:" + typedWord + "\n");
		if (Utils.verbose)
			System.out.print("Previous word:" + previousWord + "\n");

		if (typedWord.startsWith(previousWord) && !typedWord.equals(previousWord)) {

			candidaterecords = incremental(typedWord, previousWord);

		} else if (previousWord.startsWith(typedWord)) {

			candidaterecords = deletion(typedWord, previousWord);
		}
		if (Utils.verbose)
			System.out.println("Candidaten" + candidaterecords);
		if (Utils.verbose2)
			System.out.println(typedWord + "\t" + candidaterecords.size() + "\t" + candidaterecords);
		previousWord = typedWord;
		List<String> ret = new Vector<>();
		List<String> candidateRecordString = getRecordsString(candidaterecords);
		int l = 0;
		for (String word : candidateRecordString) {
			ret.add(word);
			if (l > k)
				break;
			l++;
		}

		return ret;
	}
}

package eg.edu.alexu.ehr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Matcher {

	final int tau = 2;
	final int k = 10;

	ResultIterator res;

	public Matcher(PivotalTrie t) {
		trie = t;
		res = new ResultIterator();
	}

	private String typedWord;
	private String previousWord = "";

	private Map<BasicTrieNode, PivotalActiveNode> activenodes = null;
	private PivotalTrie trie;

	private int wrd_cnt(String queryString) {
		queryString = queryString.trim();
		if (queryString.length() == 0)
			return 0;
		else
			return queryString.split(" ").length;

	}

	void deletion(String queryString, String previousQueryString) {

		int word_cnt = wrd_cnt(queryString);
		int previous_word_cnt = wrd_cnt(previousQueryString);

		if (word_cnt == previous_word_cnt) {
			String prefix = Utils.getLastPrefix(queryString);
			String old_prefix = Utils.getLastPrefix(previousQueryString);
			activenodes = new HashMap<>();
			activenodes = trie.matchPrefixInc(prefix, old_prefix, activenodes, tau);
			RecordIterator r = new RecordIterator(trie, activenodes);
			res.replaceRecordIterator(r);
		} else if (word_cnt < previous_word_cnt) {
			for (int i = word_cnt; i < previous_word_cnt; i++) {
				res.remove(i);
			}
			res.resetAll();
		}

	}

	void incremental(String queryString, String previousQueryString) {
		String prefix = Utils.getLastPrefix(queryString);
		String old_prefix = Utils.getLastPrefix(previousQueryString);

		int word_cnt = wrd_cnt(queryString);
		int previous_word_cnt = wrd_cnt(previousQueryString);

		boolean newword = false;
		if (word_cnt > previous_word_cnt) {
			newword = true;
		}

		activenodes = trie.matchPrefixInc(prefix, old_prefix, activenodes, tau);
		RecordIterator r = new RecordIterator(trie, activenodes);

		if (newword) {
			res.addRecordIterator(r);
		} else {
			res.replaceRecordIterator(r);
		}
	}

	void buildfromscratch(String queryString) {
		int wrd_cnt = wrd_cnt(queryString);
		String[] word = queryString.split(" ");
		res = new ResultIterator();
		for (int i = 0; i < wrd_cnt; i++) {
			activenodes = trie.matchPrefix(word[i], tau);
			RecordIterator r = new RecordIterator(trie, activenodes);
			res.addRecordIterator(r);
		}
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

		if (typedWord.equals(""))
			return null;

		List<Integer> candidaterecords = new Vector<>();

		if (typedWord.startsWith(previousWord) && !typedWord.equals(previousWord)) {
			incremental(typedWord, previousWord);
		} else if (previousWord.startsWith(typedWord)) {
			deletion(typedWord, previousWord);
		} else {
			// need to rebuild all
			buildfromscratch(typedWord);
		}
		Set<Integer> lookup = new HashSet<>();

		for (int i = 0; res.hasNext() && i < k; i++) {
			Integer n = res.next();
			if (n != null) {
				if (!lookup.contains(n)) {
					lookup.add(n);
					candidaterecords.add(n);
				}
			}
		}

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
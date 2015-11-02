import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;



public class FuzzyTrie extends Trie {
	
	private  TrieNode insertString(TrieNode root, String s) {

		TrieNode v = root;

		TrieNode next = v;
		for (char ch : s.toCharArray()) {
			next = v.children.get(ch);

			if (next == null)
				v.children.put(ch, next = new FuzzyTrieNode(v, ch));

			v = next;
		}

		v.leaf = true;
		return v;
	}

	public  void Init(String fileName) {
		root = new FuzzyTrieNode(null, '\0', 0);
		try {
			File file = new File(fileName);
			FileInputStream fIn = new FileInputStream(file);
			BufferedReader in = new BufferedReader(new InputStreamReader(fIn));

			while (true) {
				String line = in.readLine();
				if (line == null || line.equals(""))
					break;

				insertString(root, line);

			}
		} catch (Exception e) {

			root = null;
		}

	}

	
	public FuzzyTrie() {
		// TODO Auto-generated constructor stub
	}


	static void buildRootActiveNodes(TrieNode node, Map<TrieNode, ProbED> activeNodes, int depth, int limit) {
		if (depth > limit)
			return;

		activeNodes.put(node,new ProbED( depth,node.prob,node.maxlen));

		for (char c : node.children.keySet()) {
			buildRootActiveNodes(node.children.get(c), activeNodes, depth + 1, limit);
		}

	}

	private static Map<TrieNode, ProbED> IncrementalBuildActiveNode(char ch, Map<TrieNode, ProbED> cparentActiveNodes,
			int depth) {
		Map<TrieNode, ProbED> curactiveNodes = new HashMap<TrieNode, ProbED>();
		// deletion
		// add all p active node to this, with distance +1 if possible
		for (TrieNode n : cparentActiveNodes.keySet()) {
			ProbED l = cparentActiveNodes.get(n);

			if (l.tau < depth)
				curactiveNodes.put(n, new ProbED(cparentActiveNodes.get(n).tau + 1, n.prob, n.maxlen));

			for (TrieNode node : n.children.values()) {
					FuzzyTrieNode child=(FuzzyTrieNode)node;
				// insertion
				if (child.fromParent == ch) {// we have a match
					child.getDescendant(curactiveNodes, depth, l.tau);
				} else if (l.tau <= depth) {

					int m = child.min(l.tau + 1, curactiveNodes.get(child));
					if (m <= depth)
						curactiveNodes.put(child,new ProbED( m,child.prob,child.maxlen));
				}
			}
		}

		return curactiveNodes;

	}

	// Entry point for matching
	public  Set<String> GetSimilarStrings(String s, int k) {
		Set<String> similarWords = new HashSet<String>();
		for (int tau = 0; tau <= 10; tau++) {

			similarWords.addAll(matchString(root, s, tau));
			if (similarWords.size() >= k)
				break;
		}
		return similarWords;
	}

	private static List<String> matchString(TrieNode root, String s, int depth) {
		FuzzyTrieNode f=(FuzzyTrieNode)root;
		buildRootActiveNodes(root, f.activeNodes, 0, depth);

		FuzzyTrieNode v = (FuzzyTrieNode)root;;
		Map<TrieNode, ProbED> activenodes = v.activeNodes;

		TrieNode next = v;
		int indx = 0;
		boolean b = false;
		TrieNode k;
		for (char ch : s.toCharArray()) {
			next = v.children.get(ch);
			indx++;
			if (next == null) {
				b = true;
				break;
			}

			k = next;
			List<TrieNode> myparents = new Vector<TrieNode>();
			List<TrieNode> invertedList = new Vector<TrieNode>();
			while (k.parent != v) {
				myparents.add(k.parent);
				k = k.parent;
			}

			for (int i = myparents.size() - 1; i >= 0; i--) {
				invertedList.add(myparents.get(i));
			}

			for (TrieNode y : invertedList) {

				// activenodes=y.buildActiveNodes(depth);
				activenodes = IncrementalBuildActiveNode(y.fromParent, activenodes, depth);
			}
			// activenodes=next.buildActiveNodes(depth);
			activenodes = IncrementalBuildActiveNode(ch, activenodes, depth);
			v = (FuzzyTrieNode) next;

		}

		if (b == true) {

			for (int i = indx - 1; i < s.length(); i++) {

				char ch = s.charAt(i);

				activenodes = IncrementalBuildActiveNode(ch, activenodes, depth);
				

			}
		}
	

		String sim = null;
		
		List<String> similarWords = new Vector<String>();
		for (TrieNode t : activenodes.keySet()) {
			if (t.leaf == true) {
				sim = "";
				while (t.id != 0) {
					char c = t.fromParent;
					sim = c + sim;
					t = t.parent;
				}

				similarWords.add(sim);
				
			}
		}

		return similarWords;
	}
}

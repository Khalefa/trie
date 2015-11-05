package eg.edu.alexu.ehr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

public class FuzzyTrie extends Trie {

	TrieNode CreateTrieNode(TrieNode v, char ch) {
		return TrieNodeFactory.createTrieNode(v, ch, 1);
	}

	public FuzzyTrie() {
		// TODO Auto-generated constructor stub
	}

	static void buildRootActiveNodes(TrieNode node, Map<TrieNode, IDistanceMetric> activeNodes, int depth, int limit) {
		if (depth > limit)
			return;

		activeNodes.put(node, new ProbED(depth, node.prob, node.maxlen));

		for (char c : node.children.keySet()) {
			buildRootActiveNodes(node.children.get(c), activeNodes, depth + 1, limit);
		}

	}

	private static Map<TrieNode, IDistanceMetric> IncrementalBuildActiveNode(char ch, Map<TrieNode, IDistanceMetric> cparentActiveNodes,
			int depth) {
		Map<TrieNode, IDistanceMetric> curactiveNodes = new HashMap<TrieNode, IDistanceMetric>();
		// deletion
		// add all p active node to this, with distance +1 if possible
		for (TrieNode n : cparentActiveNodes.keySet()) {
			IDistanceMetric l = cparentActiveNodes.get(n);

			if (l.GetDistance() < depth)
				curactiveNodes.put(n, new ProbED((int) (cparentActiveNodes.get(n).GetDistance() + 1), n.prob, n.maxlen));

			for (TrieNode node : n.children.values()) {
				FuzzyTrieNode child = (FuzzyTrieNode) node;
				// insertion
				if (child.fromParent == ch) {// we have a match
					Map<TrieNode, IDistanceMetric> tmp_activenodes = new HashMap<TrieNode, IDistanceMetric>();
					child.getDescendant(tmp_activenodes, depth, (int)l.GetDistance());
					for (Entry<TrieNode, IDistanceMetric> entry : tmp_activenodes.entrySet()) {
						TrieNode key = entry.getKey();
						IDistanceMetric val = entry.getValue();
						ProbED p = new ProbED((int) val.GetDistance(), key.prob, key.maxlen);
						curactiveNodes.put(key, p);
					}
				} else if (l.GetDistance() <= depth) {
					IDistanceMetric p = curactiveNodes.get(child);
					int m = (int)l.GetDistance() + 1;
					if (p != null && p.GetDistance() < m)
						m = (int) p.GetDistance();
					if (m <= depth)
						curactiveNodes.put(child, new ProbED(m, child.prob, child.maxlen));
				}
			}
		}
		return curactiveNodes;
	}

	// Entry point for matching
	public Map<String, Double> GetSimilarStrings(String s, int k) {
		Map<String, Double> similarWords = new HashMap<String, Double>();
		for (int tau = 1; tau <= 10; tau++) {

			similarWords.putAll(matchString(root, s, tau));
			if (similarWords.size() >= k)
				break;
		}
		return similarWords;
	}

	private static Map<String, Double> matchString(TrieNode root, String s, int depth) {
		FuzzyTrieNode f = (FuzzyTrieNode) root;
		buildRootActiveNodes(root, f.activeNodes, 0, depth);

		FuzzyTrieNode v = (FuzzyTrieNode) root;
		;
		Map<TrieNode, IDistanceMetric> activenodes = v.activeNodes;

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

		Map<String, Double> similarWords = new HashMap<String, Double>();
		for (TrieNode t : activenodes.keySet()) {

			if (t.leaf == true) {
				TrieNode leaf = t;
				sim = "";
				while (t.id != 0) {
					char c = t.fromParent;
					sim = c + sim;
					t = t.parent;
				}
				IDistanceMetric p = activenodes.get(leaf);
				similarWords.put(sim, p.GetLimit());
			}
		}

		return similarWords;
	}
}

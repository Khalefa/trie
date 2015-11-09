package eg.edu.alexu.ehr;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FuzzyTrie extends Trie {

	public FuzzyTrie(String filename) {
		super(filename);		
	}


	TrieNode CreateTrieNode(TrieNode v, char ch) {
		return TrieNodeFactory.createTrieNode(v, ch, 1);
	}

	
	static void buildRootActiveNodes(TrieNode node, Map<TrieNode, IDistanceMetric> activeNodes, int depth, int limit) {
		if (depth > limit)
			return;

		activeNodes.put(node, new ProbED(depth, node.prob, node.maxlen));

		for (char c : node.children.keySet()) {
			buildRootActiveNodes(node.children.get(c), activeNodes, depth + 1, limit);
		}
	}

	private static Map<TrieNode, IDistanceMetric> IncrementalBuildActiveNode(char ch,
			Map<TrieNode, IDistanceMetric> cparentActiveNodes, int depth) {
		Map<TrieNode, IDistanceMetric> curactiveNodes = new HashMap<TrieNode, IDistanceMetric>();
		// deletion
		// add all p active node to this, with distance +1 if possible
		for (TrieNode n : cparentActiveNodes.keySet()) {
			IDistanceMetric l = cparentActiveNodes.get(n);

			if (l.GetDistance() < depth)
				curactiveNodes.put(n,
						new ProbED((int) (cparentActiveNodes.get(n).GetDistance() + 1), n.prob, n.maxlen));

			for (TrieNode node : n.children.values()) {
				FuzzyTrieNode child = (FuzzyTrieNode) node;
				// insertion
				if (child.fromParent == ch) {// we have a match
					Map<TrieNode, IDistanceMetric> tmp_activenodes = new HashMap<TrieNode, IDistanceMetric>();
					child.getDescendant(tmp_activenodes, depth, (int) l.GetDistance());
					for (Entry<TrieNode, IDistanceMetric> entry : tmp_activenodes.entrySet()) {
						TrieNode key = entry.getKey();
						IDistanceMetric val = entry.getValue();
						ProbED p = new ProbED((int) val.GetDistance(), key.prob, key.maxlen);
						curactiveNodes.put(key, p);
					}
				} else if (l.GetDistance() <= depth) {
					IDistanceMetric p = curactiveNodes.get(child);
					int m = (int) l.GetDistance() + 1;
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
		return matchString(root, s, k);
	}

	private static Map<String, Double> matchString(TrieNode trie, String s, int depth) {
		FuzzyTrieNode root = (FuzzyTrieNode) trie;
		buildRootActiveNodes(root, root.activeNodes, 0, depth);
		Map<TrieNode, IDistanceMetric> activenodes = root.activeNodes;
		for (char ch : s.toCharArray()) {
			activenodes = IncrementalBuildActiveNode(ch, activenodes, depth);			
		}
		//add leafs of active nodes
		Map<TrieNode, IDistanceMetric> leafs=new HashMap<TrieNode, IDistanceMetric>();
		for(TrieNode an:activenodes.keySet()){
			leafs.putAll(an.getLeafs(activenodes.get(an)));
		}
		
		String sim = null;
		Map<String, Double> similarWords = new HashMap<String, Double>();
		for (TrieNode t : leafs.keySet()) {
				TrieNode leaf = t;
				sim = "";
				while (t.id != 0) {
					char c = t.fromParent;
					sim = c + sim;
					t = t.parent;
				}
				IDistanceMetric p = leafs.get(leaf);
				similarWords.put(sim, p.GetLimit());
		}

		return similarWords;
	}
}

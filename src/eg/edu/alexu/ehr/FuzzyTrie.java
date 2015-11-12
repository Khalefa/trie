package eg.edu.alexu.ehr;

import java.util.*;
import java.util.Map.Entry;

public class FuzzyTrie extends Trie {

	public FuzzyTrie(String filename) {
		super(filename);
	}
	void buildRootActiveNodes(BasicTrieNode node, Map<BasicTrieNode, IDistanceMetric> activeNodes, int depth,
			int limit) {
		// if (depth > limit)
		// return;
		//
		// activeNodes.put(node, new ProbED(depth, node.prob,
		// node.rLength.getMax()));
		//
		// for (char c : node.children.keySet()) {
		// buildRootActiveNodes(node.children.get(c), activeNodes, depth + 1,
		// limit);
		// }
	}

	Map<BasicTrieNode, IDistanceMetric> IncrementalBuildActiveNode(char ch,
			Map<BasicTrieNode, IDistanceMetric> cparentActiveNodes, int depth) {
		Map<BasicTrieNode, IDistanceMetric> curactiveNodes = new HashMap<BasicTrieNode, IDistanceMetric>();
		// // deletion
		// // add all p active node to this, with distance +1 if possible
		// for (BasicTrieNode n : cparentActiveNodes.keySet()) {
		// IDistanceMetric l = cparentActiveNodes.get(n);
		//
		// if (l.GetDistance() < depth)
		// curactiveNodes.put(n,
		// new ProbED((int) (cparentActiveNodes.get(n).GetDistance() + 1),
		// n.prob, n.rLength.getMax()));
		//
		// for (BasicTrieNode child : n.children.values()) {
		// //FuzzyTrieNode child = (FuzzyTrieNode) node;
		// // insertion
		// if (child.fromParent == ch) {// we have a match
		// child.getDescendant(curactiveNodes, depth, (int) l.GetDistance());
		// } else if (l.GetDistance() <= depth) {
		// IDistanceMetric p = curactiveNodes.get(child);
		// int m = (int) l.GetDistance() + 1;
		// if (p != null && p.GetDistance() < m)
		// m = (int) p.GetDistance();
		// if (m <= depth)
		// curactiveNodes.put(child, new ProbED(m, child.prob,
		// child.rLength.getMax()));
		// }
		// }
		// }
		return curactiveNodes;
	}

	//
	public List<TrieNodewithDistance> matchPrefix(String prefix, int lower, int upper) {
		List<TrieNodewithDistance> nodes = new Vector<>();
		// FuzzyTrieNode r=(FuzzyTrieNode)root;
		int tau = prefix.length();
		Map<BasicTrieNode, IDistanceMetric> activenodes = new HashMap<>();
		buildRootActiveNodes(root, activenodes, 0, tau);
		System.out.println("Size of root active node" + activenodes.size());
		// System.out.println(r); printActiveNode(activenodes);
		for (char ch : prefix.toCharArray()) {
			activenodes = IncrementalBuildActiveNode(ch, activenodes, tau);
			System.out.println("Size of  active node" + activenodes.size());
			// System.out.print("Active nodes ");
			// printActiveNode(activenodes);
		}
		for (Entry<BasicTrieNode, IDistanceMetric> e : activenodes.entrySet()) {
			nodes.add(new TrieNodewithDistance(e.getKey(), e.getValue()));
		}
		Collections.sort(nodes);
		return nodes;
	}
	
}

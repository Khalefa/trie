package eg.edu.alexu.ehr;

import java.util.HashMap;
import java.util.Map;

public class PropTrie extends FuzzyTrie {

	public PropTrie(String filename) {
		super(filename);
	}
	@Override
	void buildRootActiveNodes(TrieNode node, Map<TrieNode, IDistanceMetric> activeNodes, int depth, int limit) {
		if (depth > limit)
			return;
		activeNodes.put(node, new ED(depth));
		for (char c : node.children.keySet()) {
			buildRootActiveNodes(node.children.get(c), activeNodes, depth + 1, limit);
		}
	}

	Map<TrieNode, IDistanceMetric> IncrementalBuildActiveNode(char ch,
			Map<TrieNode, IDistanceMetric> cparentActiveNodes, int depth) {
		Map<TrieNode, IDistanceMetric> curactiveNodes = new HashMap<TrieNode, IDistanceMetric>();

		// add all p active node to this, with distance +1 if possible
		for (TrieNode n : cparentActiveNodes.keySet()) {
			IDistanceMetric l = cparentActiveNodes.get(n);
			int ll = (int) l.GetDistance();
			if (ll < depth) {
				ED t = new ED((int) (cparentActiveNodes.get(n).GetDistance() + 1));
				Util.AddActiveNode(curactiveNodes, n, t);
			}

			for (TrieNode child : n.children.values()) {
				// insertion
				if (child.fromParent == ch) {// we have a match
					child.getDescendant(curactiveNodes, depth, ll);
				} else if (ll <= depth) {
					Util.AddActiveNode(curactiveNodes, child, new ED(ll + 1));
				}
			}
		}
		return curactiveNodes;
	}
}

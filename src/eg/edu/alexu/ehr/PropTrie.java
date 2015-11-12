//package eg.edu.alexu.ehr;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class PropTrie extends FuzzyTrie {
//
//	public PropTrie(String filename) {
//		super(filename);
//	}
//	//@Override
//	void buildRootActiveNodes(BasicTrieNode node, Map<BasicTrieNode, IDistanceMetric> activeNodes, int depth, int limit) {
//		if (depth > limit)
//			return;
//		activeNodes.put(node, new ED(depth));
//		for (char c : node.children.keySet()) {
//			buildRootActiveNodes(node.children.get(c), activeNodes, depth + 1, limit);
//		}
//	}
//
//	Map<BasicTrieNode, IDistanceMetric> IncrementalBuildActiveNode(char ch,
//			Map<BasicTrieNode, IDistanceMetric> cparentActiveNodes, int depth) {
//		Map<BasicTrieNode, IDistanceMetric> curactiveNodes = new HashMap<BasicTrieNode, IDistanceMetric>();
//
//		// add all p active node to this, with distance +1 if possible
//		for (BasicTrieNode n : cparentActiveNodes.keySet()) {
//			IDistanceMetric l = cparentActiveNodes.get(n);
//			int ll = (int) l.GetDistance();
//			if (ll < depth) {
//				ED t = new ED((int) (cparentActiveNodes.get(n).GetDistance() + 1));
//				Util.AddActiveNode(curactiveNodes, n, t);
//			}
//
//			for (BasicTrieNode child : n.children.values()) {
//				// insertion
//				if (child.fromParent == ch) {// we have a match
//					child.getDescendant(curactiveNodes, depth, ll);
//				} else if (ll <= depth) {
//					Util.AddActiveNode(curactiveNodes, child, new ED(ll + 1));
//				}
//			}
//		}
//		return curactiveNodes;
//	}
//}

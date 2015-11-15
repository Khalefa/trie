package eg.edu.alexu.ehr;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Vector;

public class FuzzyTrie extends Trie {
	// @Override
	BasicTrieNode CreateTrieNode(BasicTrieNode v, char ch) {
		return new TrieNode(v, ch);
	}

	static class ActiveNode implements Comparable<ActiveNode> {
		BasicTrieNode node = null;
		IDistance tau = new ED(0);

		public ActiveNode(BasicTrieNode n, IDistance dist) {
			this.node = n;
			this.tau = dist;
		}

		public ActiveNode(BasicTrieNode n, int dist) {
			this.node = n;
			this.tau = new ED(dist);
		}

		@Override
		public String toString() {
			return "(" + node + ", " + tau + ")";
		}

		@Override
		public int compareTo(ActiveNode anode) {
			return tau.compareTo(anode.tau);
		}

		public int getDistance() {
			return tau.getDistance();
		}
	}

	static private void AddActiveNode(Map<BasicTrieNode, ActiveNode> nodes, BasicTrieNode n, IDistance dist) {
		if (nodes.get(n) == null) {
			nodes.put(n, new ActiveNode(n, dist));
		} else {
			ActiveNode old_entry = nodes.get(n);
			if (old_entry.getDistance() > dist.getDistance())
				nodes.put(n, new ActiveNode(n, dist));
		}
	}

	public FuzzyTrie(String filename) {
		super(filename);
	}

	void buildRootActiveNodes(BasicTrieNode node, Map<BasicTrieNode, ActiveNode> activeNodes, int depth, int limit) {
		if (depth > limit)
			return;

		activeNodes.put(node, new ActiveNode(node, depth));
		for (char c : node.children.keySet()) {
			buildRootActiveNodes(node.children.get(c), activeNodes, depth + 1, limit);
		}
	}

	Map<BasicTrieNode, ActiveNode> IncrementalBuildActiveNode(char ch,
			Map<BasicTrieNode, ActiveNode> cparentActiveNodes, int depth) {
		Map<BasicTrieNode, ActiveNode> curactiveNodes = new HashMap<BasicTrieNode, ActiveNode>();

		// add all p active node to this, with distance +1 if possible
		for (ActiveNode n : cparentActiveNodes.values()) {
			int ll = n.getDistance();
			if (ll < depth) {
				ED t = new ED((int) (n.getDistance() + 1));
				AddActiveNode(curactiveNodes, n.node, t);
			}

			for (BasicTrieNode child : n.node.children.values()) {
				// insertion
				if (child.fromParent == ch) {// we have a match
					getDescendants(child, curactiveNodes, depth, ll);
				} else if (ll <= depth) {
					AddActiveNode(curactiveNodes, child, new ED(ll + 1));
				}
			}
		}
		return curactiveNodes;
	}

	private void getDescendants(BasicTrieNode x, Map<BasicTrieNode, ActiveNode> descendents, int d, int limit) {
		if (limit < d)
			return;

		AddActiveNode(descendents, x, new ED(d));
		for (char c : x.children.keySet()) {
			getDescendants(x.children.get(c), descendents, d + 1, limit);
		}
	}

	private void printActiveNode(Map<BasicTrieNode, ActiveNode> activeNodes) {
		String s = "";
		for (ActiveNode node : activeNodes.values()) {
			s = s + node.toString();
		}
		s = s + "\n";
		System.out.println(s);
	}

	public Map<BasicTrieNode, ActiveNode> matchPrefix(String prefix, int tau) {
		Map<BasicTrieNode, ActiveNode> activenodes = new HashMap<>();
		buildRootActiveNodes(root, activenodes, 0, tau);
		// System.out.print("Active nodes");
		// System.out.println(r); printActiveNode(activenodes);
		for (char ch : prefix.toCharArray()) {
			activenodes = IncrementalBuildActiveNode(ch, activenodes, tau);
			// System.out.print("Active nodes ");
			// printActiveNode(activenodes);
		}
		return activenodes;
	}

	public List<String> getStrings(Map<BasicTrieNode, ActiveNode> activenodes) {
		List<ActiveNode> sorted_nodes = new Vector<>();
		for (ActiveNode node : activenodes.values()) {
			if (node.node.leaf)
				sorted_nodes.add(node);
		}
		Collections.sort(sorted_nodes);
		Set<String> strings = new HashSet<>();
		List<String> sim = new Vector<>();
		for (ActiveNode p : sorted_nodes) {
			TrieNode n = (TrieNode) p.node;
			for (int i = n.rID.min; i <= n.rID.max; i++) {
				String s = dictionary.get(i);
				if (!strings.contains(s)) {
					strings.add(s);
					sim.add(s);
				}
			}
		}
		return sim;
	}

	public List<String> matchString(String prefix, int tau) {
		return getStrings(matchPrefix(prefix, tau));
	}
}

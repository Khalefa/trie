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
	@Override
	BasicTrieNode CreateTrieNode(BasicTrieNode v, char ch) {
		return new TrieNode(v, ch);
	}

	static void write(String s) {
		// System.out.println(s);
	}

	static private void AddActiveNode(Map<BasicTrieNode, ActiveNode> nodes, BasicTrieNode n, int dist) {
		if (nodes.get(n) == null) {
			nodes.put(n, new ActiveNode(n, dist));
		} else {
			ActiveNode old_entry = nodes.get(n);
			if (old_entry.getDistance() > dist)
				nodes.put(n, new ActiveNode(n, dist));
		}
	}

	public FuzzyTrie(String filename) {
		super(filename);
	}

	public FuzzyTrie(String filename, boolean truncate) {
		super(filename, truncate);
	}

	void buildRootActiveNodes(BasicTrieNode node, Map<BasicTrieNode, ActiveNode> activeNodes, int depth, int limit) {
		if (depth > limit)
			return;

		activeNodes.put(node, new ActiveNode(node, depth));
		for (char c : node.children.keySet()) {
			buildRootActiveNodes(node.children.get(c), activeNodes, depth + 1, limit);
		}
	}

	Map<BasicTrieNode, ActiveNode> ICAN(String s, Map<BasicTrieNode, ActiveNode> cparentActiveNodes, int tau) {

		Map<BasicTrieNode, ActiveNode> curactiveNodes = new HashMap<BasicTrieNode, ActiveNode>();
		char ch = s.charAt(s.length() - 1);

		// add all p active node to this, with distance +1 if possible
		for (ActiveNode n : cparentActiveNodes.values()) {
			int dist = n.getDistance();
			if (dist < tau) {
				AddActiveNode(curactiveNodes, n.node, dist + 1);
			}

			for (BasicTrieNode child : n.node.children.values()) {
				// insertion
				if (child.fromParent == ch) {// we have a match
					getDescendants(child, curactiveNodes, dist, tau - dist);
				} else if (dist < tau) {
					AddActiveNode(curactiveNodes, child, dist + 1);
				}
			}
		}
		return curactiveNodes;
	}

	private void getDescendants(BasicTrieNode x, Map<BasicTrieNode, ActiveNode> descendents, int dep, int limit) {
		if (limit < 0)
			return;

		AddActiveNode(descendents, x, dep);
		for (char c : x.children.keySet()) {
			getDescendants(x.children.get(c), descendents, dep + 1, limit - 1);
		}
	}

	private void printActiveNode(Map<BasicTrieNode, ActiveNode> activeNodes) {
		String s = "";
		for (ActiveNode node : activeNodes.values()) {
			s = s + node.toString();
		}
		s = s + "\n";
		write(s);
	}

	public Map<BasicTrieNode, ActiveNode> match(String prefix, int tau) {
		Map<BasicTrieNode, ActiveNode> activenodes = new HashMap<>();
		buildRootActiveNodes(root, activenodes, 0, tau);
		String px = "";
		for (char ch : prefix.toCharArray()) {
			write("Active nodes ");
			printActiveNode(activenodes);
			px = px + ch;
			activenodes = ICAN(px, activenodes, tau);
		}
		write("Active nodes ");
		printActiveNode(activenodes);
		return activenodes;
	}

	public List<String> getStrings(Map<BasicTrieNode, ActiveNode> activenodes, boolean prefix, int k) {
		List<ActiveNode> sorted_nodes = new Vector<>();
		for (ActiveNode node : activenodes.values()) {
			if (prefix == false) {
				if (node.node.leaf)
					sorted_nodes.add(node);
			} else
				sorted_nodes.add(node);
		}
		Collections.sort(sorted_nodes);
		Set<String> strings = new HashSet<>();
		List<String> sim = new Vector<>();
		for (ActiveNode p : sorted_nodes) {
			TrieNode n = (TrieNode) p.node;
			for (int i = n.rID.min; i <= n.rID.max; i++) {
				boolean add = true;
				String s = dictionary.get(i);
				if (prefix == false) {
					if (s.length() != n.depth)
						add = false; // only add string when its complete
				}

				if (add && !strings.contains(s)) {
					strings.add(s);
					sim.add(s);
					if (k > 0)
						if (k > sim.size())
							break;
				}
			}
		}
		return sim;
	}

	public Map<BasicTrieNode, ActiveNode> matchInc(String newquery, String previousquery,
			Map<BasicTrieNode, ActiveNode> pactivenodes, int tau) {
		if ((newquery.length() > 1) && (previousquery.equals(newquery.substring(0, newquery.length() - 1)))) {
			return ICAN(newquery, pactivenodes, tau);
		} else {
			return match(newquery, tau);
		}
	}

	public List<String> getFuzzyStrings(String s, int tau) {
		return getStrings(match(s, tau), false, 0);
	}

	public List<String> getFuzzyPrefix(String s, int tau) {
		return getStrings(match(s, tau), true, 0);
	}

	public List<String> getFuzzyStrings(String s, int tau, int k) {
		return getStrings(match(s, tau), false, k);
	}

	public List<String> getFuzzyPrefix(String s, int tau, int k) {
		return getStrings(match(s, tau), true, k);
	}
}

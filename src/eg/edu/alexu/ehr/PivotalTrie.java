package eg.edu.alexu.ehr;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class PivotalTrie extends Trie {
	static boolean verbose = false;

	static void write(String s) {
		System.out.print(s);
	}

	static void writeln(String s) {
		write(s + "\n");
	}

	public PivotalTrie(String filename) {
		super(filename, false);
	}

	void updatePivotal(Map<BasicTrieNode, PivotalActiveNode> activenodes, PivotalActiveNode pnew) {
		PivotalActiveNode old = activenodes.get(pnew.node);
		if (old == null) {
			activenodes.put(pnew.node, pnew);
		} else if (old.tau_px > pnew.tau_px) {
			activenodes.put(pnew.node, pnew);
		} else if (old.tau_px == pnew.tau_px)
			if (old.pi.length() > pnew.pi.length()) {
				activenodes.put(pnew.node, pnew);
			}
	}

	Map<BasicTrieNode, PivotalActiveNode> ICPAN(String px, Map<BasicTrieNode, PivotalActiveNode> cparentActiveNodes,
			int tau) {

		Map<BasicTrieNode, PivotalActiveNode> activenodes = new HashMap<>();
		for (PivotalActiveNode pn : cparentActiveNodes.values()) {
			// deletion
			BasicTrieNode n = pn.node;
			PivotalActiveNode del = new PivotalActiveNode(n);
			del.tau_px = pn.tau_px + 1;
			del.tau_pi = pn.tau_pi;
			del.pi = pn.pi;
			if (del.tau_px <= tau) {
				updatePivotal(activenodes, del);
			}

			// Descendants of n
			List<BasicTrieNode> descendants = new Vector<>();
			char ch = px.charAt(px.length() - 1);
			n.getDescendant(descendants, tau - pn.tau_pi + 1, ch);

			for (BasicTrieNode n_dash : descendants) {
				PivotalActiveNode match = new PivotalActiveNode(n_dash);
				int ed_px_1 = pn.tau_pi + Math.max(n_dash.depth - n.depth - 1, px.length() - pn.pi.length() - 1);
				match.tau_pi = match.tau_px = ed_px_1;
				match.pi = px;
				if (ed_px_1 <= tau) {
					updatePivotal(activenodes, match);
				}
			}
		}
				
		return activenodes;
	}

	// @Override
	BasicTrieNode CreateTrieNode(BasicTrieNode v, char ch) {
		return new TrieNode(v, ch);
	}

	public Map<BasicTrieNode, PivotalActiveNode> matchPrefixInc(String newquery, String previousquery,
			Map<BasicTrieNode, PivotalActiveNode> pactivenodes, int tau) {
		if ((newquery.length() > 1) && (previousquery.equals(newquery.substring(0, newquery.length() - 1)))) {
				return ICPAN(newquery, pactivenodes, tau);
		} else {
			return matchPrefix(newquery, tau);
		}
	}

	public List<String> GetStrings(Map<BasicTrieNode, PivotalActiveNode> nodes, int k) {
		// sorting the nodes based on tau_px

		List<PivotalActiveNode> sorted_nodes = new Vector<>();
		sorted_nodes.addAll(nodes.values());
	
		Collections.sort(sorted_nodes);
		
		Set<String> strings = new HashSet<>();
		List<String> sim=new Vector<>();
		for (PivotalActiveNode p : sorted_nodes) {
			TrieNode n = (TrieNode) p.node;
			for (int i = n.rID.min; i <= n.rID.max; i++) {
				String s = dictionary.get(i);
				if (!strings.contains(s)) {
					strings.add(s);
					sim.add(s);
					if (strings.size() > k)
						break;
				}
			}
		}
		return sim;
	}

	public Map<BasicTrieNode, PivotalActiveNode> matchPrefix(String query, int tau) {
		Map<BasicTrieNode, PivotalActiveNode> activenodes = new HashMap<>();
		activenodes.put(root, new PivotalActiveNode(root));
		String px = "";
		for (char ch : query.toCharArray()) {
			px = px + ch;
			activenodes = ICPAN(px, activenodes, tau);
		}
		return activenodes;
	}
}


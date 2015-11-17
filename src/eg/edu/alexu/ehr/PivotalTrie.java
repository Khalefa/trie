package eg.edu.alexu.ehr;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class PivotalTrie extends FuzzyTrie {
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

	// TODO combine with AddActiveNode
	static void AddActiveNode(Map<BasicTrieNode, ActiveNode> activenodes, PivotalActiveNode pnew) {
		PivotalActiveNode old = (PivotalActiveNode) activenodes.get(pnew.node);
		if (old == null) {
			activenodes.put(pnew.node, pnew);
		} else if (old.get_tau_px() > pnew.get_tau_px()) {
			activenodes.put(pnew.node, pnew);
		} else if (old.get_tau_px() == pnew.get_tau_px())
			if (old.pi.length() > pnew.pi.length()) {
				activenodes.put(pnew.node, pnew);
			}
	}
@Override
	void buildRootActiveNodes(BasicTrieNode node, Map<BasicTrieNode, ActiveNode> activeNodes, int depth, int limit) {
		activeNodes.put(node, new PivotalActiveNode(node,depth));
	}

	Map<BasicTrieNode, ActiveNode> ICAN(String px, Map<BasicTrieNode, ActiveNode> cparentActiveNodes, int tau) {

		Map<BasicTrieNode, ActiveNode> activenodes = new HashMap<>();
		for (ActiveNode n : cparentActiveNodes.values()) {
			PivotalActiveNode pn = (PivotalActiveNode) n;
			// deletion
			BasicTrieNode node = pn.node;
			PivotalActiveNode del = new PivotalActiveNode(node,pn.get_tau_px() + 1);
			
			del.tau_pi = pn.tau_pi;
			del.pi = pn.pi;
			if (del.get_tau_px() <= tau) {
				AddActiveNode(activenodes, del);
			}

			// Descendants of n
			List<BasicTrieNode> descendants = new Vector<>();
			char ch = px.charAt(px.length() - 1);
			n.node.getDescendant(descendants, tau - pn.tau_pi + 1, ch);

			for (BasicTrieNode n_dash : descendants) {				
				int ed_px_1 = pn.tau_pi + Math.max(n_dash.depth - n.node.depth - 1, px.length() - pn.pi.length() - 1);
				PivotalActiveNode match = new PivotalActiveNode(n_dash,ed_px_1);
				match.tau_pi = ed_px_1;				
				match.pi = px;
				if (ed_px_1 <= tau) {
					AddActiveNode(activenodes, match);
				}
			}
		}

		return activenodes;
	}

	@Override
	BasicTrieNode CreateTrieNode(BasicTrieNode v, char ch) {
		return new TrieNode(v, ch);
	}
}

package eg.edu.alexu.ehr;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import eg.edu.alexu.ehr.PivotalTrie.PivotalActiveNode;

public class PivotalTrie extends Trie {
	static class PivotalActiveNode {
		BasicTrieNode node = null;
		int tau_px = 0;
		String pi = "";
		int tau_pi = 0;

		// public PivotalActiveNode() {
		// // TODO Auto-generated constructor stub
		// }
		PivotalActiveNode(BasicTrieNode r) {
			node = r;
		}

		@Override
		public String toString() {

			return "(" + node.id + "," + tau_px + "," + pi + "," + tau_pi + ")";
		}
	}

	public PivotalTrie(String filename) {
		super(filename);
	}

	List<PivotalActiveNode> ICPAN(String px, List<PivotalActiveNode> cparentActiveNodes, int tau) {
		System.out.println("PivotalTrie.ICPAN("+px+","+tau+")");
		//System.out.println("px  " + px);
		List<PivotalActiveNode> activenodes = new Vector<>();
		for (PivotalActiveNode pn : cparentActiveNodes) {
			System.out.println("Considering " + pn);
			// deletion
			BasicTrieNode n = pn.node;
			PivotalActiveNode del = new PivotalActiveNode(n);
			del.tau_px = pn.tau_px + 1;
			del.tau_pi=pn.tau_pi;
			del.pi=pn.pi;
			if (del.tau_px <= tau) {
				activenodes.add(del);
				System.out.println("\tAdding[del] " + del);
			}

			// Descendants of n
			List<BasicTrieNode> descendants = new Vector<>();
			n.getDescendant(descendants, tau - pn.tau_pi + 1);

			for (BasicTrieNode n_dash : descendants) {
				if (n_dash.fromParent == px.charAt(px.length() - 1)) {
					PivotalActiveNode match = new PivotalActiveNode(n_dash);
					int ed_px_1 = pn.tau_pi + Math.max(n_dash.depth - n.depth - 1, px.length() - pn.pi.length()-1);
					match.tau_pi = match.tau_px = ed_px_1;
					match.pi = px;
					if (ed_px_1 <= tau) {
						activenodes.add(match);
						System.out.println("\tAdding[match] " + match);
					}
				}
			}

		}
		System.out.println("Active nodes are:");
		for (PivotalActiveNode p : activenodes)
			System.out.print(p + ",");
		System.out.println("");
		System.out.println("************************************************************");
		return activenodes;
	}

	public List<TrieNodewithDistance> matchPrefix(String query, int tau) {
		List<PivotalActiveNode> activenodes = new Vector<>();
		activenodes.add(new PivotalActiveNode(root));
		// System.out.println("Size of root active node" + activenodes.size());

		String px = "";
		for (char ch : query.toCharArray()) {
			px = px + ch;
			activenodes = ICPAN(px, activenodes, tau);
			// System.out.println("Size of active node" + activenodes.size());
			// System.out.print("Active nodes ");
			// printActiveNode(activenodes);
		}
		return null;
		// for (Entry<BasicTrieNode, IDistanceMetric> e :
		// activenodes.entrySet()) {
		// nodes.add(new TrieNodewithDistance(e.getKey(), e.getValue()));
		// }
		// Collections.sort(nodes);
		// return nodes;
	}

}

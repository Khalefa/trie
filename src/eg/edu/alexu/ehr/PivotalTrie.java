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

	void write(String s){
	//	System.out.print(s);
	}
	void writeln(String s){
	//	System.out.println(s);
	}	
	public PivotalTrie(String filename) {
		super(filename);
	}

	void updatePivotal(Map<BasicTrieNode, PivotalActiveNode> activenodes, PivotalActiveNode pnew) {
		PivotalActiveNode old = activenodes.get(pnew.node);
		if (old == null) {
			activenodes.put(pnew.node, pnew);
			writeln(pnew.toString());
		} else if (old.tau_px > pnew.tau_px) {
			activenodes.put(pnew.node, pnew);
			writeln("\t;:" + pnew.toString());
		} else if (old.tau_px == pnew.tau_px)
			if (old.pi.length() > pnew.pi.length()) {
				activenodes.put(pnew.node, pnew);
				writeln("\t::" + pnew.toString());
			}
	}

	Map<BasicTrieNode, PivotalActiveNode> ICPAN(String px, Map<BasicTrieNode, PivotalActiveNode> cparentActiveNodes,
			int tau) {
		writeln("PivotalTrie.ICPAN(" + px + "," + tau + ")");
		Map<BasicTrieNode, PivotalActiveNode> activenodes = new HashMap<>();
		for (PivotalActiveNode pn : cparentActiveNodes.values()) {
			writeln("Considering " + pn);
			// deletion
			BasicTrieNode n = pn.node;
			PivotalActiveNode del = new PivotalActiveNode(n);
			del.tau_px = pn.tau_px + 1;
			del.tau_pi = pn.tau_pi;
			del.pi = pn.pi;
			if (del.tau_px <= tau) {
				write("\tAdding[del]");
				updatePivotal(activenodes, del);
			}

			// Descendants of n
			List<BasicTrieNode> descendants = new Vector<>();
			n.getDescendant(descendants, tau - pn.tau_pi + 1);

			for (BasicTrieNode n_dash : descendants) {
				if (n_dash.fromParent == px.charAt(px.length() - 1)) {
					PivotalActiveNode match = new PivotalActiveNode(n_dash);
					int ed_px_1 = pn.tau_pi + Math.max(n_dash.depth - n.depth - 1, px.length() - pn.pi.length() - 1);
					match.tau_pi = match.tau_px = ed_px_1;
					match.pi = px;
					if (ed_px_1 <= tau) {
						write("\tAdding[match]");
						updatePivotal(activenodes, match);
					}
				}
			}

		}
		writeln("Active nodes are:");
		for (PivotalActiveNode p : activenodes.values())
			write(p + ",");
		writeln("");
		writeln("************************************************************");
		return activenodes;
	}

	public List<TrieNodewithDistance> matchPrefix(String query, int tau) {
		Map<BasicTrieNode, PivotalActiveNode> activenodes = new HashMap<>();
		activenodes.put(root, new PivotalActiveNode(root));
		// writeln("Size of root active node" + activenodes.size());

		String px = "";
		for (char ch : query.toCharArray()) {
			px = px + ch;
			activenodes = ICPAN(px, activenodes, tau);
			System.out.println("Size of active node" + activenodes.size());
			// write("Active nodes ");
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

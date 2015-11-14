package eg.edu.alexu.ehr;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class PivotalTrie extends Trie {
	static boolean verbose = false;

	static class PivotalActiveNode implements Comparable<PivotalActiveNode> {
		BasicTrieNode node = null;
		int tau_px = 0;
		String pi = "";
		int tau_pi = 0;

		PivotalActiveNode(BasicTrieNode r) {
			node = r;
		}

		@Override
		public String toString() {
			return "(" + node.id + "," + tau_px + "," + pi + "," + tau_pi + ")";
		}

		@Override
		public int compareTo(PivotalActiveNode o) {
			return Integer.compare(node.getID(), o.node.getID());
		}
	}

	static void write(String s) {
		System.out.print(s);
	}

	static void writeln(String s) {
		write(s + "\n");
	}

	public PivotalTrie(String filename) {
		super(filename, true);
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

		long startTime = System.nanoTime();
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
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		int count = 0;
		for (PivotalActiveNode p : activenodes.values()) {
			{
				TrieNode n = (TrieNode) p.node;
				count += n.rID.max - n.rID.min + 1;
			}
		}
		// writeln("Activenode size=" + activenodes.size()+"\t"+
		// duration/1000.0/1000.0+" count = "+count);

		if (verbose) {
			for (PivotalActiveNode p : activenodes.values()) {
				// writeln(p.toString());

			}
		}
		return activenodes;
	}

	// @Override
	BasicTrieNode CreateTrieNode(BasicTrieNode v, char ch) {
		return new TrieNode(v, ch);
	}

	public List<String> matchPrefix(String query, int tau) {
		Map<BasicTrieNode, PivotalActiveNode> activenodes = new HashMap<>();
		activenodes.put(root, new PivotalActiveNode(root));
		String px = "";
		for (char ch : query.toCharArray()) {
			px = px + ch;
			activenodes = ICPAN(px, activenodes, tau);
		}
		// now retrive these strings

		HashMap<String, Integer> strings = new HashMap<String, Integer>();
		List<String> sim = new Vector<>();
		for (PivotalActiveNode p : activenodes.values()) {
			TrieNode n = (TrieNode) p.node;
			for (int i = n.rID.min; i <= n.rID.max; i++) {
				// System.out.println(dictionary.get(i)+"@"+p.tau_px);
				if (!strings.containsKey(dictionary.get(i)))
					strings.put(dictionary.get(i), p.tau_px);

			}
		}
		
		//System.out.println(strings.entrySet());
		TreeMap<String, Integer> sortedMap = SortByValue(strings);  
		//System.out.println(sortedMap.entrySet());
		sim.addAll(sortedMap.keySet());
	

		return sim;
	}

	public static TreeMap<String, Integer> SortByValue(HashMap<String, Integer> map) {
		ValueComparator vc = new ValueComparator(map);
		TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(vc);
		sortedMap.putAll(map);
		return sortedMap;
	}

}

class ValueComparator implements Comparator<String> {

	Map<String, Integer> map;

	public ValueComparator(Map<String, Integer> base) {
		this.map = base;
	}

	public int compare(String a, String b) {
		if (map.get(a)<= map.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}

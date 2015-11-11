package eg.edu.alexu.ehr;

import java.util.*;
import java.util.Map.Entry;

public class FuzzyTrie extends Trie {

	public FuzzyTrie(String filename) {
		super(filename);		
	}

//	TrieNode CreateTrieNode(TrieNode v, char ch) {
//		return TrieNodeFactory.createTrieNode(v, ch, 1);
//	}
	private void printActiveNode(Map<TrieNode, IDistanceMetric> activeNodes) {
		String s="";
		for(Entry<TrieNode, IDistanceMetric> entry: activeNodes.entrySet()){
			s=s+"("+entry.getKey().id+ ","+entry.getValue().GetDistance()+")";
		}
		s=s+"\n";
		System.out.println(s);
	}
	
	 void buildRootActiveNodes(TrieNode node, Map<TrieNode, IDistanceMetric> activeNodes, int depth, int limit) {
		if (depth > limit)
			return;

		activeNodes.put(node, new ProbED(depth, node.prob, node.rLength.getMax()));

		for (char c : node.children.keySet()) {
			buildRootActiveNodes(node.children.get(c), activeNodes, depth + 1, limit);
		}
	}

	
	 
	  Map<TrieNode, IDistanceMetric> IncrementalBuildActiveNode(char ch,
			Map<TrieNode, IDistanceMetric> cparentActiveNodes, int depth) {
		Map<TrieNode, IDistanceMetric> curactiveNodes = new HashMap<TrieNode, IDistanceMetric>();
		// deletion
		// add all p active node to this, with distance +1 if possible
		for (TrieNode n : cparentActiveNodes.keySet()) {
			IDistanceMetric l = cparentActiveNodes.get(n);

			if (l.GetDistance() < depth)
				curactiveNodes.put(n,
						new ProbED((int) (cparentActiveNodes.get(n).GetDistance() + 1), n.prob, n.rLength.getMax()));

			for (TrieNode child : n.children.values()) {
				//FuzzyTrieNode child = (FuzzyTrieNode) node;
				// insertion
				if (child.fromParent == ch) {// we have a match
					child.getDescendant(curactiveNodes, depth, (int) l.GetDistance());
				} else if (l.GetDistance() <= depth) {
					IDistanceMetric p = curactiveNodes.get(child);
					int m = (int) l.GetDistance() + 1;
					if (p != null && p.GetDistance() < m)
						m = (int) p.GetDistance();
					if (m <= depth)
						curactiveNodes.put(child, new ProbED(m, child.prob, child.rLength.getMax()));
				}
			}
		}
		return curactiveNodes;
	}

	public List<TrieNodewithDistance> matchPrefix(String prefix){ 
		List<TrieNodewithDistance> nodes=new Vector<>();
		//FuzzyTrieNode r=(FuzzyTrieNode)root;
		int tau=prefix.length();
		Map<TrieNode, IDistanceMetric> activenodes =new HashMap<>();
		buildRootActiveNodes(root, activenodes, 0, tau);
	//	System.out.print("Active nodes");
	//	System.out.println(r); printActiveNode(activenodes);
		for (char ch : prefix.toCharArray()) {
			activenodes = IncrementalBuildActiveNode(ch, activenodes, tau);
	//		System.out.print("Active nodes  ");
		//	printActiveNode(activenodes);
		}
		for (Entry<TrieNode,IDistanceMetric> e : activenodes.entrySet()) {
			nodes.add(new TrieNodewithDistance(e.getKey(), e.getValue()));
		}
		Collections.sort(nodes);
		return nodes;
	}
	// Entry point for matching
//
//	public  Map<String, Double> matchPrefix(TrieNode trie, String prefix) {
//		FuzzyTrieNode root = (FuzzyTrieNode) trie;
//		int tau=prefix.length();
//		buildRootActiveNodes(root, root.activeNodes, 0, tau);
//		Map<TrieNode, IDistanceMetric> activenodes = root.activeNodes;
//		for (char ch : prefix.toCharArray()) {
//			activenodes = IncrementalBuildActiveNode(ch, activenodes, tau);
//		}
//
//		// add leafs of active nodes
//		Map<TrieNode, IDistanceMetric> leafs = new HashMap<TrieNode, IDistanceMetric>();
//		for (TrieNode an : activenodes.keySet()) {
//			leafs.putAll(an.getLeafs(activenodes.get(an)));
//		}
//
//		List<TrieNodewithDistance> v = new Vector<TrieNodewithDistance>(activenodes.size());
//		for (Entry<TrieNode, IDistanceMetric> e : activenodes.entrySet()) {
//			v.add(new TrieNodewithDistance(e.getKey(), e.getValue()));
//		}
//		Collections.sort(v);
//		IDistanceMetric pre_tau = v.get(0).getD();
//		List<TrieNodewithDistance> tier = new Vector<TrieNodewithDistance>();
//		List<Range> covered = new Vector<Range>();
//		int count=0;
//
//		for (int i = 0; i < v.size(); i++) {
//			TrieNodewithDistance t = v.get(i);
//			if (pre_tau == t.getD()) {
//				if(!Ranges.isCovered(covered, t.getTrieNode().getRange())){
//				tier.add(v.get(i));
//				covered.add(t.getTrieNode().getRange());
//			} else {
//				//process tier of tau
//				
//			}
//			pre_tau = t.getD();
//		}
//
//		String sim = null;
//		Map<String, Double> similarWords = new HashMap<String, Double>();
//		for (TrieNode t : leafs.keySet()) {
//			TrieNode leaf = t;
//			sim = "";
//			while (t.id != 0) {
//				char c = t.fromParent;
//				sim = c + sim;
//				t = t.parent;
//			}
//			IDistanceMetric p = leafs.get(leaf);
//			similarWords.put(sim, p.GetLimit());
//		}
//
//		return similarWords;
//	}
}

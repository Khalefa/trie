package eg.edu.alexu.ehr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TopKTrie extends FuzzyTrie {

	public TopKTrie(String filename) {
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
		// deletion
		// add all p active node to this, with distance +1 if possible
		for (TrieNode n : cparentActiveNodes.keySet()) {
			IDistanceMetric l = cparentActiveNodes.get(n);

			if (l.GetDistance() < depth)
				curactiveNodes.put(n,
						new ED((int) (cparentActiveNodes.get(n).GetDistance() + 1)));

			for (TrieNode node : n.children.values()) {
				FuzzyTrieNode child = (FuzzyTrieNode) node;
				// insertion
				if (child.fromParent == ch) {// we have a match
					Map<TrieNode, IDistanceMetric> tmp_activenodes = new HashMap<TrieNode, IDistanceMetric>();
					child.getDescendant(tmp_activenodes, depth, (int) l.GetDistance());
					for (Entry<TrieNode, IDistanceMetric> entry : tmp_activenodes.entrySet()) {
						TrieNode key = entry.getKey();
						IDistanceMetric val = entry.getValue();
						ED p = new ED((int) val.GetDistance());
						curactiveNodes.put(key, p);
					}
				} else if (l.GetDistance() <= depth) {
					IDistanceMetric p = curactiveNodes.get(child);
					int m = (int) l.GetDistance() + 1;
					if (p != null && p.GetDistance() < m)
						m = (int) p.GetDistance();
					if (m <= depth)
						curactiveNodes.put(child, new ED(m));
				}
			}
		}
		return curactiveNodes;
	}
   public Map<String, IDistanceMetric > matchPrefix(String prefix, float alpha){
	   List<TrieNodewithDistance> nodes=super.matchPrefix(prefix);
	   for(TrieNodewithDistance n:nodes){
		   System.out.println(n);
	   }
	   //sorted_id
	   //Apply TA Algorithm
//	  double threshold=Double.MAX_VALUE;
//	  while(true){
//		  //access nodes
//		  
//		  
//		  //access string
//		  
//	  }
//	   
	   return null;
   }
}

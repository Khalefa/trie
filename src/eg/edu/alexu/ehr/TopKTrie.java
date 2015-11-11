package eg.edu.alexu.ehr;

import java.io.FileWriter;
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

		// add all p active node to this, with distance +1 if possible
		for (TrieNode n : cparentActiveNodes.keySet()) {
			IDistanceMetric l = cparentActiveNodes.get(n);
			int ll = (int) l.GetDistance();
			if (ll < depth) {
				ED t = new ED((int) (cparentActiveNodes.get(n).GetDistance() + 1));
				Util.AddActiveNode(curactiveNodes, n, t);
			}

			for (TrieNode child : n.children.values()) {				
				if (child.fromParent == ch) {// we have a match
					child.getDescendant(curactiveNodes, depth, ll);
				} else if (ll <= depth) {
					Util.AddActiveNode(curactiveNodes, child, new ED(ll + 1));
				}
			}
		}
		return curactiveNodes;
	}

	public Map<String, IDistanceMetric> matchPrefix(String prefix, float alpha) {
		List<TrieNodewithDistance> nodes = super.matchPrefix(prefix);
		try{
			FileWriter w=new FileWriter("c:\\data\\output\\a.o");
		for (TrieNodewithDistance n : nodes) {
			w.write(n+"\n");
		}
		w.close();
		}catch(Exception e){
			
		}
//		try{
//		FileWriter w=new FileWriter("c:\\data\\output\\a");
//		for(int i:sorted_id)
//			w.write(i+"\t"+dictionary.get(i)+"\n");
//		w.close();
//		}catch(Exception e){
//			
//		}
		//	System.out.println(i+"\t"+dictionary.get(i));
		// Apply TA Algorithm
		// double threshold=Double.MAX_VALUE;
		// while(true){
		// //access nodes
		//
		//
		// //access string
		//
		// }
		//
		return null;
	}
}

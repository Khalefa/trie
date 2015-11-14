package eg.edu.alexu.ehr;
/*package eg.edu.alexu.ehr;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TopKTrie extends FuzzyTrie {

	public TopKTrie(String filename) {
		super(filename);
	}

	BasicTrieNode CreateTrieNode(BasicTrieNode v, char ch) {
		return new TrieNode(v, ch);
	}

//	 @Override
	 void buildRootActiveNodesI(BasicTrieNode node, Map<BasicTrieNode,
	 IDistanceMetric> activeNodes, int depth,
	 int limit) {
	 class pair {
	 public BasicTrieNode n;
	 public int depth;
	
	 public pair(BasicTrieNode n, int depth) {
	 this.n = n;
	 this.depth = depth;
	 }
	 }
	 if (depth > limit)
	 return;
	 activeNodes.put(node, new ED(depth));
	 List<pair> q = new Vector<pair>();
	 q.add(new pair(node, depth));
	 while (!q.isEmpty()) {
	 pair btn = q.remove(0);
	 if (btn.depth <= limit) {
	
	 for (BasicTrieNode n : node.children.values())
	 if (btn.depth < limit) {
	 activeNodes.put(n, new ED(btn.depth + 1));
	 q.add(new pair(n, btn.depth + 1));
	 }
	 }
	 }
	 }

	void buildRootActiveNodesR(BasicTrieNode node, Map<BasicTrieNode, IDistanceMetric> activeNodes, int depth,
			int limit) {
		if (depth > limit)
			return;

		activeNodes.put(node, new ED(depth));

		for (char c : node.children.keySet()) {
			buildRootActiveNodesR(node.children.get(c), activeNodes, depth + 1, limit);
		}
	}
	void buildRootActiveNodes(BasicTrieNode node, Map<BasicTrieNode, IDistanceMetric> activeNodes, int depth,
			int limit) {
		buildRootActiveNodesI(node, activeNodes, depth, limit);
	}

	Map<BasicTrieNode, IDistanceMetric> IncrementalBuildActiveNode(char ch,
			Map<BasicTrieNode, IDistanceMetric> cparentActiveNodes, int depth) {
		Map<BasicTrieNode, IDistanceMetric> curactiveNodes = new HashMap<BasicTrieNode, IDistanceMetric>();

		// add all p active node to this, with distance +1 if possible
		for (BasicTrieNode n : cparentActiveNodes.keySet()) {
			IDistanceMetric l = cparentActiveNodes.get(n);
			int ll = (int) l.GetDistance();
			if (ll < depth) {
				ED t = new ED((int) (cparentActiveNodes.get(n).GetDistance() + 1));
				Util.AddActiveNode(curactiveNodes, n, t);
			}

			for (BasicTrieNode child : n.children.values()) {
				if (child.fromParent == ch) {// we have a match
					child.getDescendant(curactiveNodes, depth, ll);
				} else if (ll < depth) {
					Util.AddActiveNode(curactiveNodes, child, new ED(ll + 1));
				}
			}
		}
		return curactiveNodes;
	}

	public Map<String, IDistanceMetric> matchPrefix(String prefix, float alpha) {
		List<TrieNodewithDistance> nodes = super.matchPrefix(prefix, 0, 1);
		try {
			FileWriter w = new FileWriter("c:\\data\\output\\a.o");
			for (TrieNodewithDistance n : nodes) {
				w.write(n + "\n");
			}
			w.close();
		} catch (Exception e) {

		}
		// try{
		// FileWriter w=new FileWriter("c:\\data\\output\\a");
		// for(int i:sorted_id)
		// w.write(i+"\t"+dictionary.get(i)+"\n");
		// w.close();
		// }catch(Exception e){
		//
		// }
		// System.out.println(i+"\t"+dictionary.get(i));
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
*/
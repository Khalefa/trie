package eg.edu.alexu.ehr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class BasicTrieNode {

	static int counter = 0;
	int id;
	protected Map<Character, BasicTrieNode> children = new TreeMap<Character, BasicTrieNode>();
	boolean leaf;
	BasicTrieNode parent;
	char fromParent;
	
	public int getID() {
		return id;
	}
	void adjust(int id, int len, float prob) {
		
	}
	public BasicTrieNode(BasicTrieNode p, char x) {
		this.id = counter;
		counter++;
		parent = p;
		fromParent = x;// holding current node character
	}
	
	public Map<BasicTrieNode, IDistanceMetric> getLeafs(IDistanceMetric dist) {
		class pair {
			public BasicTrieNode n;
			public int depth;

			public pair(BasicTrieNode n, int depth) {
				this.n = n;
				this.depth = depth;
			}
		}
		Map<BasicTrieNode, IDistanceMetric> leafs = new HashMap<BasicTrieNode, IDistanceMetric>();
		ArrayList<pair> queue = new ArrayList<pair>();
		queue.add(new pair(this, 0));
		if (leaf)
			leafs.put(this, dist);
		while (!queue.isEmpty()) {
			pair p = queue.remove(0);
			for (BasicTrieNode c : p.n.children.values()) {
				if (c.leaf)
					leafs.put(c,  new ED(p.depth + 1+(int)dist.GetDistance()));
				queue.add(new pair(c, p.depth + 1));
			}
		}
		return leafs;
	}
	Map<BasicTrieNode, IDistanceMetric> getDescendant(Map<BasicTrieNode, IDistanceMetric> descendents, int depth, int k) {
		class pair {
			public BasicTrieNode n;
			public int depth;
	
			public pair(BasicTrieNode n, int depth) {
				this.n = n;
				this.depth = depth;
			}
		}
	
		ArrayList<pair> queue = new ArrayList<pair>();
		queue.add(new pair(this, k));
		if (k > depth)
			return descendents;
		
		descendents.put(this, new ED(k));
	
		while (!queue.isEmpty()) {
			// get the first node of the queue
			pair p = queue.remove(0);
			// add children to the queue
			if (p.depth < depth) {
				for (BasicTrieNode c : p.n.children.values()) {
					IDistanceMetric v = descendents.get(c);
					int vv = p.depth + 1; 					
					if (vv <= depth) {							
						Util.AddActiveNode(descendents, c, new ED(p.depth + 1));
						queue.add(new pair(c, vv));
					}
				}
			}
		}
		return descendents;
	}
//	void getDescendant(Map<BasicTrieNode, IDistanceMetric> descendents, int depth, int limit) {
//		
//		if (depth > limit)
//			return ;
//		
//		descendents.put(this, new ED(depth));
//		
//		for (char c : children.keySet()) {
//			children.get(c).getDescendant(descendents, depth + 1, limit);
//		}
//	}
	@Override
	public String toString() {
		return "BTN[" + id + "]";
	}

	public String toString(int l) {
		String s = this.toString();
		String tabs = "";
		for (int k = 0; k < l; k++)
			tabs = tabs + "\t";
		for (Entry<Character, BasicTrieNode> child : children.entrySet()) {
			s = s + tabs + child.getKey() + "\t" + child.getValue().toString(l + 1);
		}
		return s+"";
	}
}

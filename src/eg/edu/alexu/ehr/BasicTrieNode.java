package eg.edu.alexu.ehr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	int depth=0;
	
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
	
	private void getDescendants(List<BasicTrieNode> descendents, int d,int limit){
		if( limit <d)
			return;
		descendents.add(this);
		for (char c : children.keySet()) {
			children.get(c).getDescendants(descendents, d + 1, limit);
		}
	}
	
void getDescendant(List<BasicTrieNode> descendents, int limit) {		
		descendents.add(this);
		
		for (char c : children.keySet()) {
			children.get(c).getDescendants(descendents,  1, limit);
		}
	}
	
//	void getDescendant(Map<BasicTrieNode, IDistanceMetric> descendents, int depth, int limit) {
//	  getDescendantR(descendents, depth, limit);
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

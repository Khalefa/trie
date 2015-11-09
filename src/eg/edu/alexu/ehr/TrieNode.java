package eg.edu.alexu.ehr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TrieNode {

	static int counter = 0;
	int id;
	Map<Character, TrieNode> children = new TreeMap<Character, TrieNode>();
	boolean leaf;
	TrieNode parent;
	char fromParent;
	float prob;
	public int maxlen;
    Range r;
	public int getID() {
		return id;
	}
	public void setRange(Range range){
		r=range;
	}
	public Range getRange(){
		return r;
	}
	public void updateRange(Range range){
		if(this.r==null)
			this.r=new Range(range);
		else 
			this.r=this.r.addRange(range);
	}
	public TrieNode(TrieNode p, char x) {
		this.id = counter;
		counter++;
		parent = p;

		fromParent = x;// holding current node character
	}

	public TrieNode(TrieNode p, char x, float prb) {
		this(p, x);
		this.prob = prb;
	}

	Map<TrieNode, IDistanceMetric> getDescendant(Map<TrieNode, IDistanceMetric> descendents, int depth, int k) {
		class pair {
			public TrieNode n;
			public int depth;

			public pair(TrieNode n, int depth) {
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
				for (TrieNode c : p.n.children.values()) {
					Object v = descendents.get(c);
					int vv = p.depth + 1;
					if (v != null && (Integer) v < vv)
						vv = (Integer) v;
					if (vv <= depth) {
						descendents.put(c, new ED(vv));
						queue.add(new pair(c, vv));
					}
				}
			}
		}
		return descendents;
	}

	public Map<TrieNode, IDistanceMetric> getLeafs(IDistanceMetric dist) {
		class pair {
			public TrieNode n;
			public int depth;

			public pair(TrieNode n, int depth) {
				this.n = n;
				this.depth = depth;
			}
		}
		Map<TrieNode, IDistanceMetric> leafs = new HashMap<TrieNode, IDistanceMetric>();
		ArrayList<pair> queue = new ArrayList<pair>();
		queue.add(new pair(this, 0));
		if (leaf)
			leafs.put(this, dist);
		while (!queue.isEmpty()) {
			pair p = queue.remove(0);
			for (TrieNode c : p.n.children.values()) {
				if (c.leaf)
					leafs.put(c, dist.add(p.depth + 1));
				queue.add(new pair(c, p.depth + 1));
			}
		}
		return leafs;
	}

	@Override
	public String toString() {
		return "TrieNode id=" + id + ":P " + prob + " L:" + maxlen+ " "+ r ;
	}

	public String toString(int l) {
		String s = this.toString();
		String tabs = "";
		for (int k = 0; k < l; k++)
			tabs = tabs + "\t";
		for (Entry<Character, TrieNode> child : children.entrySet()) {
			s = s + tabs + child.getKey() + "\t" + child.getValue().toString(l + 1);
		}
		return s;
	}
}

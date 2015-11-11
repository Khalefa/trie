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
    Range rID;
    Range rLength;
    
	public int getID() {
		return id;
	}
	
	 void adjustTrieNode(int id, int len,float prob){
		if(rID==null)
			rID=new Range(id,id);
		else rID.extend(id);
		if(rLength==null)
			rLength=new Range(len,len);
		else rLength.extend(len);
		if(prob>this.prob)
			this.prob=prob;
		
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
					leafs.put(c,  new ED(p.depth + 1+(int)dist.GetDistance()));
				queue.add(new pair(c, p.depth + 1));
			}
		}
		return leafs;
	}

	@Override
	public String toString() {
		return "TrieNode id=" + id + ":P " + prob + " L:" + rLength+ " "+ rID ;
	}

	public String toString(int l) {
		String s = this.toString();
		String tabs = "";
		for (int k = 0; k < l; k++)
			tabs = tabs + "\t";
		for (Entry<Character, TrieNode> child : children.entrySet()) {
			s = s + tabs + child.getKey() + "\t" + child.getValue().toString(l + 1);
		}
		return s+"";
	}
}

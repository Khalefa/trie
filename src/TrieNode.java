import java.util.ArrayList;
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
		
		//Map<TrieNode, Integer> piactiveNodes = new HashMap<Trie.TrieNode, Integer>();

		public TrieNode(TrieNode p, char x) {
			this.id = counter;
			counter++;
			parent = p;

			fromParent = x;// holding current node character
		}

		public TrieNode(TrieNode p, char x, float contxt) {
			this.id = counter;
			counter++;
			parent = p;
			this.prob = contxt;
			fromParent = x;// holding current node character
		}

		

	
		private Map<TrieNode, Integer> getDescendant(Map<TrieNode, Integer> descendents,
				int depth, int k) {
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
			descendents.put(this, k);
			while (!queue.isEmpty()) {
				// get the first node of the queue
				pair p = queue.remove(0);
				// add children to the queue
				if (p.depth < depth) {
					for (TrieNode c : p.n.children.values()) {
						Object v = descendents.get(c);
						int vv = p.depth + 1;
						 if( v!=null && (Integer)v<vv)vv=(Integer)v;
						if (vv <= depth) {
							descendents.put(c, vv);
							queue.add(new pair(c, vv));
						}
					}
				}
			}
			return descendents;
		}

		
		@Override
		public String toString() {

			String s= "TrieNode id=" + id + " :P "+prob+" L "+maxlen+"\n" ;
			for(Entry<Character, TrieNode> child:children.entrySet()){
				s=s+child.getKey()+ "\t"+child.getValue();
			}
			return s;
		}
	}



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FuzzyTrieNode extends TrieNode {

	public FuzzyTrieNode(TrieNode p, char x, float contxt) {
		super(p, x, contxt);
		// TODO Auto-generated constructor stub
	}

	public FuzzyTrieNode(TrieNode p, char x) {
		super(p, x);
		// TODO Auto-generated constructor stub
	}

	//@Override
	protected Map<TrieNode, ProbED> getDescendant(Map<TrieNode, ProbED> descendents, 
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
		descendents.put(this, new ProbED(k));
		while (!queue.isEmpty()) {
			// get the first node of the queue
			pair p = queue.remove(0);
			// add children to the queue
			if (p.depth < depth) {
				for (TrieNode c : p.n.children.values()) {
					ProbED v = descendents.get(c);
					
					int vv = p.depth + 1;
					if( v!=null && v.tau < vv)vv= v.tau;
					if (vv <= depth) {
						descendents.put(c, new ProbED(vv));
						queue.add(new pair(c, vv));
					}
				}
			}
		}
		return descendents;
	}

	public Map<TrieNode, ProbED> activeNodes = new HashMap<TrieNode, ProbED>();

}
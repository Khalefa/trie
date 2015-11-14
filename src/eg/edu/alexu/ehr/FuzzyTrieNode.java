package eg.edu.alexu.ehr;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FuzzyTrieNode extends TrieNode {

	public FuzzyTrieNode(TrieNode p, char x, float contxt) {
		super(p, x);	
	}

	public FuzzyTrieNode(TrieNode p, char x) {
		super(p, x);
	}

	public Map<TrieNode, IDistanceMetric> activeNodes = new HashMap<TrieNode, IDistanceMetric>();
	@Override
	public String toString() {
		String s="Fuzzy"+super.toString();
		for(Entry<TrieNode, IDistanceMetric> entry: activeNodes.entrySet()){
			s=s+"("+entry.getKey().id+ ","+entry.getValue().GetDistance()+")";
		}
		s=s+"\n";
		return s;
	}

}
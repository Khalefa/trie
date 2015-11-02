import java.util.HashMap;
import java.util.Map;

public class Util {
static Map<TrieNode, ProbED> convert(Map<TrieNode, Integer> map){
	Map<TrieNode, ProbED> rv=new HashMap<TrieNode,ProbED>();
	for(TrieNode n:map.keySet()){
		rv.put(n,new ProbED(map.get(n), n.prob, n.maxlen));
	}
	return rv;
	
	
}
}

package eg.edu.alexu.ehr;

import java.util.Map;

public class Util {
	static public void AddActiveNode( Map<TrieNode, IDistanceMetric>  nodes, TrieNode n, IDistanceMetric dist){
		 if(nodes.get(n)==null){
			 nodes.put(n,dist);
		 }else {
			 IDistanceMetric old_dist=nodes.get(n);
			 if(old_dist.GetDistance() >dist.GetDistance()) 
				 nodes.put(n,dist);			 
		 }
	 }
}

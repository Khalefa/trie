package eg.edu.alexu.ehr;

public class TrieNodewithDistance implements Comparable<TrieNodewithDistance> {
BasicTrieNode n;
IDistanceMetric d;
public TrieNodewithDistance(BasicTrieNode n, IDistanceMetric d){
	this.n=n;
	this.d=d;
}
@Override
public int compareTo(TrieNodewithDistance o) {	
	return this.d.compareTo(o.d);
}
public IDistanceMetric getD(){
	return d;
}
public BasicTrieNode getTrieNode(){
	return n;
}

@Override
	public String toString() {
		return n.toString()+" @ "+d.toString();
	}
}

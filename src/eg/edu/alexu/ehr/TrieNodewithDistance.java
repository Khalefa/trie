package eg.edu.alexu.ehr;

public class TrieNodewithDistance implements Comparable<TrieNodewithDistance> {
TrieNode n;
IDistanceMetric d;
public TrieNodewithDistance(TrieNode n, IDistanceMetric d){
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
public TrieNode getTrieNode(){
	return n;
}

@Override
	public String toString() {
		return n.toString()+" @ "+d.toString();
	}
}

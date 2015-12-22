package eg.edu.alexu.ehr;

public class Pair implements Comparable<Pair> {
	Integer id;
	float n;
	public Pair(){}
	public Pair(Integer id, float weight) {
		this.id=id;
		this.n=weight;
	}
	@Override
	public boolean equals(Object obj) {
		Pair p=(Pair) obj;
		return id.equals(p.id);
	}
	@Override
	public int compareTo(Pair o) {
		return Float.compare(n, o.n);
	}
}

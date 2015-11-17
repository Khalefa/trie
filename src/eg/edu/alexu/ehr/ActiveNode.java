package eg.edu.alexu.ehr;

public class ActiveNode implements Comparable<ActiveNode> {
	BasicTrieNode node = null;
	int tau = 0;

	public ActiveNode(BasicTrieNode n, int dist) {
		this.node = n;
		this.tau = dist;
	}

	@Override
	public String toString() {
		return "(" + node + ", " + tau + ")";
	}

	@Override
	public int compareTo(ActiveNode anode) {
		return Integer.compare(tau, anode.tau);
	}

	public int getDistance() {
		return tau;
	}
}
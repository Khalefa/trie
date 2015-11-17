package eg.edu.alexu.ehr;

public class PivotalActiveNode extends ActiveNode {

	int get_tau_px() {
		return tau;
	}

	void set_tau_px(int t) {
		tau = t;
	}

	String pi = "";
	int tau_pi = 0;
	PivotalActiveNode(BasicTrieNode r,int d) {
		super(r, d);
	}
//	PivotalActiveNode(BasicTrieNode r) {
//		super(r, 0);
//	}

	@Override
	public String toString() {
		return "(" + node.id + "," + get_tau_px() + "," + pi + "," + tau_pi + ")";
	}

}

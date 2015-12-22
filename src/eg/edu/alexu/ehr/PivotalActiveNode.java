package eg.edu.alexu.ehr;

public class PivotalActiveNode implements Comparable<PivotalActiveNode> {
		BasicTrieNode node = null;
		int tau_px = 0;
		String pi = "";
		int tau_pi = 0;

		PivotalActiveNode(BasicTrieNode r) {
			node = r;
		}

		int tau(){
			return tau_pi;
		}
		@Override
		public String toString() {
			return "(" + node.id + "," + tau_px + "," + pi + "," + tau_pi + ")";
		}

		@Override
		public int compareTo(PivotalActiveNode o) {
			return Integer.compare(tau_px, o.tau_px);
		}
	}

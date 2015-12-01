package eg.edu.alexu.ehr;



public class TrieNode extends BasicTrieNode {

	public TrieNode(BasicTrieNode p, char x) {
		super(p, x);
	}

	Range rID;
	

	void adjust(int id, int len, float prob) {
		if (rID == null)
			rID = new Range(id, id);
		else
			rID.extend(id);
		
		
	}

	@Override
	public String toString() {
		return "TN[" + id + "] "+ " R:" + rID;
	}

}

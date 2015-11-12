package eg.edu.alexu.ehr;

import java.util.ArrayList;
import java.util.Map;

public class TrieNode extends BasicTrieNode {

	public TrieNode(BasicTrieNode p, char x) {
		super(p, x);
	}

	float prob = 0;
	Range rID;
	Range rLength;

	void adjust(int id, int len, float prob) {
		if (rID == null)
			rID = new Range(id, id);
		else
			rID.extend(id);
		if (rLength == null)
			rLength = new Range(len, len);
		else
			rLength.extend(len);
		if (prob > this.prob)
			this.prob = prob;

	}

	@Override
	public String toString() {
		return "TN[" + id + "] :P " + prob + " L:" + rLength + " R:" + rID;
	}

}

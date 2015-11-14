package eg.edu.alexu.ehr;
public class TrieNodeFactory {
	public static TrieNode createTrieNode(TrieNode n, char x, int type) {
		if (type == 0)
			return new TrieNode(n, x);
		else
			return new FuzzyTrieNode(n, x);
	}
	
}
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Trie {
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "";
		s += root.toString();

		return s;
	}

	private static TrieNode insertString(TrieNode root, String s, float prob) {
		int len = s.length();
		TrieNode v = root;
		if (v.prob < prob)
			v.prob = prob;
		if (v.maxlen < len)
			v.maxlen = len;
		TrieNode next = v;
		for (char ch : s.toCharArray()) {
			next = v.children.get(ch);
			if (next == null)
				v.children.put(ch, next = new TrieNode(v, ch));
			if (v.prob < prob)
				v.prob = prob;
			if (v.maxlen < len)
				v.maxlen = len;
			v = next;
		}
		// just in case
		if (v.prob < prob)
			v.prob = prob;
		if (v.maxlen < len)
			v.maxlen = len;
		v.leaf = true;
		return v;
	}

	public void Init(String fileName) {
		root = new TrieNode(null, '\0', 0);
		try {
			File file = new File(fileName);
			FileInputStream fIn = new FileInputStream(file);
			BufferedReader in = new BufferedReader(new InputStreamReader(fIn));

			while (true) {
				String line = in.readLine();
				if (line == null || line.equals(""))
					break;
				String[] inputS = line.split(",");
				float prob = 1;
				if (inputS.length > 1) {
					prob = Float.parseFloat(inputS[1]);
				}
				insertString(root, inputS[0], prob);

			}
		} catch (Exception e) {

			root = null;
		}

	}

	TrieNode root;

}

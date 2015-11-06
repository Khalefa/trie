package eg.edu.alexu.ehr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Trie {
	@Override
	public String toString() {
		String s = "";
		s += root.toString(0);
		return s;
	}

	public TrieNode exactSearch(String s) {
		TrieNode cur = root;
		for (char ch : s.toCharArray()) {
			TrieNode next = cur.children.get(ch);
			if (next == null)
				return cur;
			cur = next;
		}
		return cur;
	}

	TrieNode insertString(TrieNode root, String s, float prob) {
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
				v.children.put(ch, next = CreateTrieNode(v, ch));
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

	TrieNode CreateTrieNode(TrieNode v, char ch) {
		return TrieNodeFactory.createTrieNode(v, ch, 0);
	}

	public void Init(String fileName) {

		root = CreateTrieNode(null, '\0');
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
			in.close();
		} catch (Exception e) {

			root = null;
		}

	}

	TrieNode root;

}

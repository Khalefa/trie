package eg.edu.alexu.ehr;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Trie {

	boolean looked = false;
	// Map<Integer, String> dictionary = new HashMap<Integer, String>();
	List<Integer> sorted_id = null;
	BasicTrieNode root = null;

	@Override
	public String toString() {
		String s = "";
		s += root.toString(0);
		return s;
	}

	public BasicTrieNode exactSearch(String s) {
		BasicTrieNode cur = root;
		for (char ch : s.toCharArray()) {
			BasicTrieNode next = cur.children.get(ch);
			if (next == null)
				return cur;
			cur = next;
		}
		return cur;
	}

	BasicTrieNode insertString(BasicTrieNode root, String s, int id, float prob, int len) {
		if (looked)
			return null;
		BasicTrieNode v = root;
		int d = 1;
		v.adjust(id, len, prob);
		BasicTrieNode next = v;
		for (char ch : s.toCharArray()) {
			next = v.children.get(ch);
			if (next == null)
				v.children.put(ch, next = CreateTrieNode(v, ch));
			next.depth = d;
			d++;
			next.adjust(id, len, prob);
			v = next;
		}
		v.adjust(id, len, prob);
		v.leaf = true;
		return v;
	}

	BasicTrieNode CreateTrieNode(BasicTrieNode v, char ch) {
		return new BasicTrieNode(v, ch);
	}

	class pair implements Comparable<pair> {
		int id;
		String s;

		pair(int id, String s) {
			this.id = id;
			this.s = s;
		}

		@Override
		public int compareTo(pair o) {
			if (s.length() == o.s.length())
				return s.compareTo(o.s);
			return Integer.compare(s.length(), o.s.length());
		}

		public Integer getId() {
			return id;
		}
	}

	private void sortbyLength(List<pair> pairs) {
		Collections.sort(pairs);
		sorted_id = new Vector<>(pairs.size());
		for (int i = 0; i < pairs.size(); i++) {
			sorted_id.add(pairs.get(i).getId());
		}
	}

	private List<String> readandsortFile(String fileName) {
		try {
			List<String> lines = new Vector<String>();
			File file = new File(fileName);
			FileInputStream fIn = new FileInputStream(file);
			BufferedReader in = new BufferedReader(new InputStreamReader(fIn));

			while (true) {
				String line = in.readLine();
				if (line == null || line.equals(""))
					break;
				lines.add(line);
			}
			in.close();
			Collections.sort(lines);
			return lines;
		} catch (Exception e) {

		}
		return null;
	}

	private void Init(String fileName, boolean truncate) {
		int id = 0;
		root = CreateTrieNode(null, '\0');
		try {
			List<String> lines = readandsortFile(fileName);
			List<pair> pairs = new Vector<>(lines.size());
			for (String line : lines) {
				String[] inputS = line.split("\t");
				float prob = 1;
				if (inputS.length > 1) {
					prob = Float.parseFloat(inputS[1]);
				}

				String s = inputS[0];
				int l = s.length();
				if (truncate)
					if (s.length() > 10)
						s = s.substring(0, 10);
				insertString(root, s, id, prob, l);
				pairs.add(new pair(id, s));
				id++;
			}
			sortbyLength(pairs);
		} catch (Exception e) {
			root = null;
			System.err.println("Error reading the input file");
			System.err.println(e.getMessage());
		}

		looked = true;
	}

	public Trie(String filename) {
		Init(filename, false);
	}

	public Trie(String filename, boolean truncate) {
		Init(filename, true);
	}

}

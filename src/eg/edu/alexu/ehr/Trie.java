package eg.edu.alexu.ehr;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Trie {

	static int string_id = 0;
	boolean looked = false;
	Map<Integer, String> dictionary = new HashMap<Integer, String>();
	List<Integer> sorted_id = null;
	TrieNode root = null;

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
		if (looked)
			return null;

		int len = s.length();
		TrieNode v = root;
		if (v.prob < prob)
			v.prob = prob;
		if (v.rLength == null)
			v.rLength = new Range(len, len);
		else
			v.rLength.include(len);

		TrieNode next = v;
		for (char ch : s.toCharArray()) {
			next = v.children.get(ch);
			if (next == null)
				v.children.put(ch, next = CreateTrieNode(v, ch));
			if (v.prob < prob)
				v.prob = prob;
			if (v.rLength == null)
				v.rLength = new Range(len, len);
			else
				v.rLength.include(len);
			v = next;
		}
		// just in case
		if (v.prob < prob)
			v.prob = prob;
		if (v.rLength == null)
			v.rLength = new Range(len, len);
		else
			v.rLength.include(len);
		v.leaf = true;
		return v;
	}

	TrieNode CreateTrieNode(TrieNode v, char ch) {
		return TrieNodeFactory.createTrieNode(v, ch, 0);
	}

	private void sortbyLength() {
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
		List<pair> pairs = new Vector<>(dictionary.size());
		for (Entry<Integer, String> e : dictionary.entrySet()) {
			pairs.add(new pair(e.getKey(), e.getValue()));
		}
		Collections.sort(pairs);
		sorted_id = new Vector<>(dictionary.size());
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

	private void adjustRanges(LinkedBlockingQueue<TrieNode> leafs) {
		while (!leafs.isEmpty()) {
			try {
				TrieNode l = leafs.take();

				Range r = l.getRange();
				if (l.parent != null) {
					l.parent.updateRange(r);
					leafs.put(l.parent);
				}
			} catch (Exception e) {

			}
		}

	}

	private void Init(String fileName) {
		LinkedBlockingQueue<TrieNode> leafs = new LinkedBlockingQueue<TrieNode>();
		root = CreateTrieNode(null, '\0');
		try {

			List<String> lines = readandsortFile(fileName);
			for (String line : lines) {

				String[] inputS = line.split(",");
				float prob = 1;
				if (inputS.length > 1) {
					prob = Float.parseFloat(inputS[1]);
				}
				TrieNode leaf = insertString(root, inputS[0], prob);
				leaf.setRange(new Range(string_id, string_id));
				leafs.put(leaf);
				dictionary.put(string_id++, inputS[0]);
			}

		} catch (Exception e) {

			root = null;
		}
		adjustRanges(leafs);
		sortbyLength();
		looked = true;
	}

	public Trie(String filename) {
		Init(filename);
	}

}

package eg.edu.alexu.ehr;

import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;

public class Trie {

	boolean looked = false;
	public static Map<Integer, String> dictionary = new HashMap<Integer, String>();
	public Map<String, List<Integer>> inverted_list = new HashMap<>();
	public static Map<Integer, List<Integer>> forward = new HashMap<>();
	//public static List<String> words = new Vector<>();
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
		dictionary.put(id, s);
		// System.out.println(id+s);
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
		List<String> words = new Vector<>();
		int linenumber = 0;
		try {

			File file = new File(fileName);
			FileInputStream fIn = new FileInputStream(file);
			BufferedReader in = new BufferedReader(new InputStreamReader(fIn));
			List<Integer> records = new Vector<>();

			while (true) {
				String line = in.readLine();

				if (line == null || line.equals(""))
					break;
				linenumber++;
				line = Utils.normalize(line);

				//System.out.println(line);
				String[] ll = line.split(" ");
				for (String l : ll) {
					if (inverted_list.get(l) == null) {
						records = new Vector<>();
						records.add(linenumber);
						inverted_list.put(l, records);
					} else {
						records = inverted_list.get(l);
						records.add(linenumber);
					}

				}
			}

			for (String g : inverted_list.keySet()) {
				// System.out.println(g+inverted_list.get(g));
				words.add(g);
			}

			Collections.sort(words);
			// for (String d:words)
			// System.out.println("up"+d+":"+( words.indexOf(d)+1));

			// forward list

			fIn.getChannel().position(0);
			in = new BufferedReader(new InputStreamReader(fIn));
			linenumber = 0;
			while (true) {
				String line = in.readLine();

				if (line == null || line.equals(""))
					break;
				linenumber++;
				line = Utils.normalize(line);
				String[] ll = line.split(" ");
				List<Integer> word_ID = new Vector<>();
				for (String s : ll) {
					if (forward.get(linenumber) == null) {
						word_ID = new Vector<>();
						word_ID.add(words.indexOf(s) + 1);
						forward.put(linenumber, word_ID);
					} else {
						word_ID = forward.get(linenumber);
						word_ID.add(words.indexOf(s) + 1);
					}
				}
			}
			//
			// for (int g:forward.keySet()){
			// System.out.println(g+" :"+forward.get(g));
			// }
			in.close();
			return words;
		} catch (Exception e) {

		}
		return null;
	}

	private void Init(String fileName, boolean truncate) {
		// word ID
		int id = 0;
		root = CreateTrieNode(null, '\0');
		try {
			List<String> wrds = readandsortFile(fileName);
			// for(String s:lines)
			/// System.out.println(s);
			List<pair> pairs = new Vector<>(wrds.size());
			for (String wrd : wrds) {
				String[] inputS = wrd.split("\t");
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
		Init(filename, truncate);
	}

}

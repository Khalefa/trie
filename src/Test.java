import java.awt.Color;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import eg.edu.alexu.ehr.ED;
import eg.edu.alexu.ehr.FuzzyTrie;
import eg.edu.alexu.ehr.IDistanceMetric;
import eg.edu.alexu.ehr.TrieNode;

/**
 * @author David
 */
public class Test {

	public Test() {

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTextField f = new JTextField(10);

		AutoSuggestor autoSuggestor = new AutoSuggestor(f, frame, t, Color.WHITE.brighter(), Color.BLUE, Color.RED,
				0.75f) {
			// see

		};

		JPanel p = new JPanel();

		p.add(f);

		frame.add(p);

		frame.pack();
		frame.setVisible(true);
	}

	static FuzzyTrie t;

	static void printSimilarString(String s,  int k) {
		System.out.println("Getting  string similar to " + s);
		Map<String, Double> sim = t.GetSimilarStrings(s, k);
		for (Map.Entry<String, Double> ss : sim.entrySet()) {
			System.out.println("\t**" + ss.getKey() + ":" + ss.getValue());
		}
	}

	public static void main(String[] args) {
		t = new FuzzyTrie();
		t.Init("c:\\data\\author.data");
		//System.out.println(t);
		/*
		  SwingUtilities.invokeLater(new Runnable() {
		  
		  @Override public void run() { new Test(); } }); 
		 */
		Map<TrieNode, IDistanceMetric> m = t.exactSearch("l").getLeafs(new ED(0));
		for (Entry<TrieNode, IDistanceMetric> e : m.entrySet()) {
			System.out.println("*" + e.getKey().getID() + "," + e.getValue().GetDistance());
		}
		printSimilarString("li", 2);

		printSimilarString("nliu", 3);
	}

}
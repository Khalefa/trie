import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import eg.edu.alexu.ehr.*;

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

	static void printSimilarString(String s, int k) {
		System.out.println("Getting  string similar to " + s);
		Map<String, Double> sim = t.GetSimilarStrings(s, k);
		try {
			PrintWriter w = new PrintWriter("c:\\data\\o.txt", "UTF-8");
			for (Map.Entry<String, Double> ss : sim.entrySet()) {
				w.println("" + ss.getKey() + "\t" + ss.getValue());
			}
			w.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	public static void testRanges() {
		List<Range> ranges = new Vector<Range>();
		ranges.add(new Range(1, 3));
		ranges.add(new Range(2, 3));
		ranges.add(new Range(0, 3));
		ranges.add(new Range(1, 4));
		ranges.add(new Range(5, 6));
		ranges.add(new Range(10, 13));
		ranges.add(new Range(11, 13));
		ranges.add(new Range(1, 3));
		ranges.add(new Range(100, 300));
	
		for (Range r : ranges) {
			System.out.println(r);
		}
		
		List<Range> ranges_combined=Range.combineRanges(ranges);
		for (Range r : ranges_combined) {
			System.out.println("* "+ r);
		}
	}

	public static void main(String[] args) {

		testRanges();
		// long startTime = System.nanoTime();
		// t = new FuzzyTrie("c:\\data\\author.data");
		// // System.out.println(t);
		//
		// long endTime = System.nanoTime();
		//// //System.out.println(t);
		//// /*
		//// SwingUtilities.invokeLater(new Runnable() {
		////
		//// @Override public void run() { new Test(); } });
		//// */
		//// Map<TrieNode, IDistanceMetric> m = t.exactSearch("l").getLeafs(new
		// ED(0));
		//// for (Entry<TrieNode, IDistanceMetric> e : m.entrySet()) {
		//// System.out.println("*" + e.getKey().getID() + "," +
		// e.getValue().GetDistance());
		//// }
		////
		////
		// long duration = (endTime - startTime);
		// System.out.println("build time "+duration/1000000.0 );
		// startTime = System.nanoTime();
		// printSimilarString("li", 1);
		// endTime = System.nanoTime();
		// duration=endTime-startTime;
		// System.out.println("access time "+duration/1000000.0);
		//// //printSimilarString("nliu", 3);
		//

	}

}
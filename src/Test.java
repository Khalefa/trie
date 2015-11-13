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
 * @author Basio
 */
public class Test {

	public Test() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTextField f = new JTextField(10);
		AutoSuggestor autoSuggestor = new AutoSuggestor(f, frame, (FuzzyTrie) t, Color.WHITE.brighter(), Color.BLUE,
				Color.RED, 0.75f);
		JPanel p = new JPanel();
		p.add(f);
		frame.add(p);
		frame.pack();
		frame.setVisible(true);
	}

	static Trie t;

	public static void testTopk() {
		long startTime = System.nanoTime();
		t = new Trie("c:\\data\\author.data");
		// System.out.println(t);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("build time " + duration / 1000000.0);
		startTime = System.nanoTime();
		// ((TopKTrie) t).matchPrefix("abddefg", 2);
		endTime = System.nanoTime();
		duration = (endTime - startTime);
		System.out.println("access time " + duration / 1000000.0);
	}

	public static void testProp() {
		// t = new PropTrie("c:\\data\\ptest.data");
		// System.out.println(t);
		// // t.matchPrefix("ab",2);
	}

	public static void main(String[] args) {
		// testTopk();
		long startTime = System.nanoTime();
		PivotalTrie t = new PivotalTrie("c:\\data\\test.data");
		System.out.println(t);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("build time " + duration / 1000000.0);

		// System.out.println(t.toString());
		//for (int i = 0; i <= 5; i++) {
		int i=2;	
		System.err.println("******" + i + "******");
			startTime = System.nanoTime();
			t.matchPrefix("nlis", i);
			endTime = System.nanoTime();
			duration = (endTime - startTime);
			System.out.println("access time " + duration / 1000000.0);
	//	}

	}

}
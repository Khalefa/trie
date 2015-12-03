
import java.awt.Color;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import eg.edu.alexu.ehr.*;

/**
 * @author Khalefa
 */
public class Test {

	public Test() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTextField f = new JTextField(10);
	AutoSuggestor autoSuggestor = new AutoSuggestor(f, frame, (PivotalTrie) t, Color.WHITE.brighter(), Color.BLUE,
				Color.RED, 0.75f);
		JPanel p = new JPanel();
		p.add(f);
		frame.add(p);
		frame.pack();
		frame.setVisible(true);
	}

	static Trie t;

	public static void main(String[] args) {
		
		String []filename={"icd10cm edited.txt", "test.txt","words.txt","test2.txt"};
    
		t = new PivotalTrie("c:\\data\\Edit\\"+filename[0]);
		
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Test();
			}
		});
	}

}
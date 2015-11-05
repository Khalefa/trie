import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import eg.edu.alexu.ehr.FuzzyTrie;
import eg.edu.alexu.ehr.ProbED;

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
     static void printSimilarString(String s, int k, float m_tau){
    	System.out.println("Getting "+k+" string similar to "+s);
    	Map<String, Double> sim=t.GetSimilarStrings(s, k);
    	 for(Map.Entry<String, Double> ss : sim.entrySet()){
    		 System.out.println("\t**"+ss.getKey()+":"+ss.getValue());
    	 }
     }
	public static void main(String[] args) {
		ProbED p=new ProbED(1,0.7f,4);
		System.out.println(p);;
		
		
		t = new FuzzyTrie();
		t.Init("c:\\data\\ptest.data");
		System.out.println(t);
		/*SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Test();
			}
		});*/
		printSimilarString("li", 2,0); 
		
		printSimilarString("liu", 3,0);
	}


}
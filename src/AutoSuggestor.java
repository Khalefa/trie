
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.rmi.CORBA.Util;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import eg.edu.alexu.ehr.*;

class AutoSuggestor {
	final int tau = 2;
	final int k = 1020;
	private final JTextField textField;
	private final Window container;
	private JPanel suggestionsPanel;
	private JWindow autoSuggestionPopUpWindow;
	private String typedWord;
	private String newtypedWord;
	private String previousWord = "";
	private List<Integer> prev_invList = null; // ew Vector<>();

	private List<Integer> word_invList = null; // ew Vector<>();
	private Map<BasicTrieNode, PivotalActiveNode> activenodes = null;
	private PivotalTrie trie;
	private int tW, tH;
	private DocumentListener documentListener = new DocumentListener() {
		@Override
		public void insertUpdate(DocumentEvent de) {
			checkForAndShowSuggestions();
		}

		@Override
		public void removeUpdate(DocumentEvent de) {
			checkForAndShowSuggestions();
		}

		@Override
		public void changedUpdate(DocumentEvent de) {
			checkForAndShowSuggestions();
		}
	};
	private final Color suggestionsTextColor;
	private final Color suggestionFocusedColor;

	public AutoSuggestor(JTextField textField, Window mainWindow, PivotalTrie t, Color popUpBackground, Color textColor,
			Color suggestionFocusedColor, float opacity) {
		this.textField = textField;
		this.suggestionsTextColor = textColor;
		this.container = mainWindow;
		this.suggestionFocusedColor = suggestionFocusedColor;
		this.textField.getDocument().addDocumentListener(documentListener);

		trie = t;

		typedWord = "";

		newtypedWord = "";

		tW = 0;
		tH = 0;

		autoSuggestionPopUpWindow = new JWindow(mainWindow);
		autoSuggestionPopUpWindow.setOpacity(opacity);

		suggestionsPanel = new JPanel();
		suggestionsPanel.setLayout(new GridLayout(0, 1));
		suggestionsPanel.setBackground(popUpBackground);

		addKeyBindingToRequestFocusInPopUpWindow();
	}

	private void addKeyBindingToRequestFocusInPopUpWindow() {
		textField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true),
				"Down released");
		textField.getActionMap().put("Down released", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
					if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
						((SuggestionLabel) suggestionsPanel.getComponent(i)).setFocused(true);
						autoSuggestionPopUpWindow.toFront();
						autoSuggestionPopUpWindow.requestFocusInWindow();
						suggestionsPanel.requestFocusInWindow();
						suggestionsPanel.getComponent(i).requestFocusInWindow();
						break;
					}
				}
			}
		});
		suggestionsPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Down released");
		suggestionsPanel.getActionMap().put("Down released", new AbstractAction() {
			int lastFocusableIndex = 0;

			@Override
			public void actionPerformed(ActionEvent ae) {

				ArrayList<SuggestionLabel> sls = getAddedSuggestionLabels();
				int max = sls.size();

				if (max > 1) {// more than 1 suggestion
					for (int i = 0; i < max; i++) {
						SuggestionLabel sl = sls.get(i);
						if (sl.isFocused()) {
							if (lastFocusableIndex == max - 1) {
								lastFocusableIndex = 0;
								sl.setFocused(false);
								autoSuggestionPopUpWindow.setVisible(false);
								setFocusToTextField();
								checkForAndShowSuggestions();
							} else {
								sl.setFocused(false);
								lastFocusableIndex = i;
							}
						} else if (lastFocusableIndex <= i) {
							if (i < max) {
								sl.setFocused(true);
								autoSuggestionPopUpWindow.toFront();
								autoSuggestionPopUpWindow.requestFocusInWindow();
								suggestionsPanel.requestFocusInWindow();
								suggestionsPanel.getComponent(i).requestFocusInWindow();
								lastFocusableIndex = i;
								break;
							}
						}
					}
				} else {// only a single suggestion was given
					autoSuggestionPopUpWindow.setVisible(false);
					setFocusToTextField();
					checkForAndShowSuggestions();// fire method as if document
													// listener change occured
													// and fired it
				}
			}
		});
	}

	private void setFocusToTextField() {
		container.toFront();
		container.requestFocusInWindow();
		textField.requestFocusInWindow();
	}

	HashMap<Integer, List<Integer>> diffList = new HashMap<>();

	public ArrayList<SuggestionLabel> getAddedSuggestionLabels() {
		ArrayList<SuggestionLabel> sls = new ArrayList<>();
		for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
			if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
				SuggestionLabel sl = (SuggestionLabel) suggestionsPanel.getComponent(i);
				sls.add(sl);
			}
		}
		return sls;
	}

	// Edit here
	List<Integer> deletion(String queryString, String previousQueryString) {

		List<Integer> cur_invList = new Vector<>();
		int word_cnt = queryString.split(" ").length;
		int previous_word_cnt = previousQueryString.split(" ").length;
        if(word_cnt==0) return cur_invList;
        
		if (word_cnt == previous_word_cnt) {
			String prefix = Utils.getLastPrefix(queryString);
			String old_prefix = Utils.getLastPrefix(previousQueryString);

			activenodes = trie.matchPrefixInc(prefix, old_prefix, activenodes, tau);
			List<Integer> candidatesrecords = trie.getRecordsIDs(activenodes, k);
			cur_invList = Utils.intersectList(candidatesrecords, prev_invList);
			word_invList = cur_invList;
			if (Utils.verbose1)
				System.out.println("back to " + word_invList);
			return word_invList;

		} else if (word_cnt < previous_word_cnt) {
			List<Integer> restored = new Vector<>();
				for (int i = word_cnt; i < previous_word_cnt; i++) {
					restored.addAll(diffList.get(i));
			}
			word_invList = prev_invList;
			if(word_cnt==1)
				prev_invList=null;
			else
			prev_invList = Utils.UnionList(prev_invList, restored);
			
			if (Utils.verbose1) {
				System.out.println("restored  to " + restored);
				System.out.println("	prev_invList  to " + prev_invList);

			}
			return word_invList;
		} else
			return null;
	}

	List<Integer> incremental(String queryString, String previousQueryString) {
		List<Integer> cur_invList = new Vector<>();

		String prefix = Utils.getLastPrefix(queryString);
		String old_prefix = Utils.getLastPrefix(previousQueryString);

		int word_cnt = queryString.split(" ").length;
		int previous_word_cnt = previousQueryString.split(" ").length;

		if (word_cnt > previous_word_cnt) {
			diffList.put(previous_word_cnt, Utils.diffList(word_invList, prev_invList));
			prev_invList = word_invList;
		}

		activenodes = trie.matchPrefixInc(prefix, old_prefix, activenodes, tau);
		cur_invList = trie.getRecordsIDs(activenodes, k);
		List<Integer> intersection = Utils.intersectList(cur_invList, prev_invList);

		if (Utils.verbose1) {
			int t = -1;
			if (prev_invList != null)
				t = prev_invList.size();
			System.out.println(queryString + " " + cur_invList.size() + "::::" + t + ">>>>" + intersection.size());
		}
		word_invList = intersection;
		return intersection;
	}

	List<String> getRecordsString(List<Integer> ids) {
		List<String> records = new Vector<>();
		List<Integer> wordsID = new Vector<>();

		for (int r : ids) {
			String s = "";
			wordsID = Trie.forward.get(r);
			for (int w : wordsID) {
				s = s + " " + Trie.dictionary.get(w - 1);
			}
			records.add(s);
		}
		return records;
	}

	private void checkForAndShowSuggestions() {
		
		typedWord = Utils.normalize(textField.getText().trim());
		suggestionsPanel.removeAll();
		tW = 0;
		tH = 0;
		
		if(typedWord.length()==0) {
			HidePopUpWindow();			
			return;
		}
		
		if (typedWord.equals(previousWord))
			return;
		

		List<Integer> candidaterecords = new Vector<>();

		if (Utils.verbose)
			System.out.print("Typed word:" + typedWord + "\n");
		if (Utils.verbose)
			System.out.print("Previous word:" + previousWord + "\n");

		if (typedWord.startsWith(previousWord) && !typedWord.equals(previousWord)) {
		
			candidaterecords = incremental(typedWord, previousWord);

		} else if (previousWord.startsWith(typedWord)) {
	
			candidaterecords = deletion(typedWord, previousWord);
		}
		if (Utils.verbose)
			System.out.println("Candidaten" + candidaterecords);
        if(Utils.verbose2)
        	System.out.println(typedWord+"\t"+candidaterecords.size()+"\t"+ candidaterecords);
		previousWord = typedWord;
		if (candidaterecords.size() == 0) {
			if (autoSuggestionPopUpWindow.isVisible()) {
				autoSuggestionPopUpWindow.setVisible(false);
			}
		} else {
			List<String> candidateRecordString = getRecordsString(candidaterecords);
			for (String word : candidateRecordString) {
				addWordToSuggestions(word);
			}

			showPopUpWindow();
			setFocusToTextField();
		}
	}

	protected void addWordToSuggestions(String word) {
		SuggestionLabel suggestionLabel = new SuggestionLabel(word, suggestionFocusedColor, suggestionsTextColor, this);

		calculatePopUpWindowSize(suggestionLabel);

		suggestionsPanel.add(suggestionLabel);
	}

	private void calculatePopUpWindowSize(JLabel label) {
		// so we can size the JWindow correctly
		if (tW < label.getPreferredSize().width) {
			tW = label.getPreferredSize().width;
		}
		tH += label.getPreferredSize().height;
	}

	private void HidePopUpWindow(){
		autoSuggestionPopUpWindow.setSize(tW, tH);
		autoSuggestionPopUpWindow.setVisible(false);
		autoSuggestionPopUpWindow.revalidate();
		autoSuggestionPopUpWindow.repaint();
	}
	private void showPopUpWindow() {
		autoSuggestionPopUpWindow.getContentPane().add(suggestionsPanel);
		autoSuggestionPopUpWindow.setMinimumSize(new Dimension(textField.getWidth(), 30));
		autoSuggestionPopUpWindow.setSize(tW, tH);
		autoSuggestionPopUpWindow.setVisible(true);

		int windowX = 0;
		int windowY = 0;

		windowX = container.getX() + textField.getX() + 5;
		if (suggestionsPanel.getHeight() > autoSuggestionPopUpWindow.getMinimumSize().height) {
			windowY = container.getY() + textField.getY() + textField.getHeight()
					+ autoSuggestionPopUpWindow.getMinimumSize().height;
		} else {
			windowY = container.getY() + textField.getY() + textField.getHeight()
					+ autoSuggestionPopUpWindow.getHeight();
		}

		autoSuggestionPopUpWindow.setLocation(windowX, windowY);
		autoSuggestionPopUpWindow.setMinimumSize(new Dimension(textField.getWidth(), 30));
		autoSuggestionPopUpWindow.revalidate();
		autoSuggestionPopUpWindow.repaint();

	}

	// see1

	public JWindow getAutoSuggestionPopUpWindow() {
		return autoSuggestionPopUpWindow;
	}

	public Window getContainer() {
		return container;
	}

	public JTextField getTextField() {
		return textField;
	}

}

class SuggestionLabel extends JLabel {

	private boolean focused = false;
	private final JWindow autoSuggestionsPopUpWindow;
	private final JTextField textField;
	private final AutoSuggestor autoSuggestor;
	private Color suggestionsTextColor, suggestionBorderColor;

	public SuggestionLabel(String string, final Color borderColor, Color suggestionsTextColor,
			AutoSuggestor autoSuggestor) {
		super(string);

		this.suggestionsTextColor = suggestionsTextColor;
		this.autoSuggestor = autoSuggestor;
		this.textField = autoSuggestor.getTextField();
		this.suggestionBorderColor = borderColor;
		this.autoSuggestionsPopUpWindow = autoSuggestor.getAutoSuggestionPopUpWindow();

		initComponent();
	}

	private void initComponent() {
		setFocusable(true);
		setForeground(suggestionsTextColor);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				super.mouseClicked(me);

				replaceWithSuggestedText();

				autoSuggestionsPopUpWindow.setVisible(false);
			}
		});

		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "Enter released");
		getActionMap().put("Enter released", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				replaceWithSuggestedText();
				autoSuggestionsPopUpWindow.setVisible(false);
			}
		});
	}

	public void setFocused(boolean focused) {
		if (focused) {
			setBorder(new LineBorder(suggestionBorderColor));
		} else {
			setBorder(null);
		}
		repaint();
		this.focused = focused;
	}

	public boolean isFocused() {
		return focused;
	}

	private void replaceWithSuggestedText() {
		String suggestedWord = getText();
		String text = textField.getText();
		String typedWord = text;
		String t = text.substring(0, text.lastIndexOf(typedWord));
		String tmp = t + text.substring(text.lastIndexOf(typedWord)).replace(typedWord, suggestedWord);
		textField.setText(tmp + " ");
	}
}
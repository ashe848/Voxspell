package voxspell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import voxspell.Voxspell.PanelID;

@SuppressWarnings({ "serial" })

public class ListBuilder extends JPanel {
	private Voxspell parent_frame;

	private JTextField list_name_field;
	private JTextField word_field;
	JTextArea words_added;
	JTextArea sample_sentence;

	private String list_name;
	private ArrayList<String> words_to_add = new ArrayList<String>();
	private ArrayList<String> sentences_to_add = new ArrayList<String>();

	/**
	 * Constructor, initialise panel parameters and GUI elements
	 */
	public ListBuilder(Voxspell parent){
		setSize(800,600);
		setLayout(null);

		parent_frame = parent;

		setupTitle();		
		setupWordsAdded();		
		setupEnterName();		
		setupEnterWord();		
		setupEnterSentence();
		setupAddButton();
		setupSaveButton();
		setupDiscardButton();
	}

	private void setupTitle() {
		JLabel title = new JLabel("Build Your Own Custom List");
		title.setBounds(288, 26, 327, 15);
		add(title);
	}

	private void setupWordsAdded() {
		JLabel words_added_label = new JLabel("Words Added:");
		words_added_label.setBounds(10, 84, 97, 15);
		add(words_added_label);

		words_added = new JTextArea();
		words_added.setText("");
		words_added.setEditable(false);
		words_added.setLineWrap(true);
		words_added.setWrapStyleWord(true);

		JScrollPane words_scroll_pane = new JScrollPane(words_added);
		words_scroll_pane.setBounds(10, 108, 302, 418);
		add(words_scroll_pane);
	}

	private void setupEnterName() {
		JLabel list_name_label = new JLabel("Name of List:");
		list_name_label.setBounds(350, 90, 97, 15);
		add(list_name_label);

		list_name_field = new JTextField();
		list_name_field.setBounds(457, 81, 302, 34);
		add(list_name_field);
		list_name_field.setColumns(10);
	}

	private void setupEnterWord() {
		JLabel word_label = new JLabel("Word:");
		word_label.setBounds(350, 154, 97, 15);
		add(word_label);

		word_field = new JTextField();
		word_field.setColumns(10);
		word_field.setBounds(457, 145, 302, 34);
		add(word_field);
	}

	private void setupEnterSentence() {
		JLabel sample_sentence_label = new JLabel("Sample sentence (optional):");
		sample_sentence_label.setBounds(350, 218, 176, 15);
		add(sample_sentence_label);

		sample_sentence = new JTextArea();
		sample_sentence.setText("");
		sample_sentence.setEditable(true);
		sample_sentence.setLineWrap(true);
		sample_sentence.setWrapStyleWord(true);

		JScrollPane sentence_scroll_pane = new JScrollPane(sample_sentence);
		sentence_scroll_pane.setBounds(350, 243, 409, 171);
		add(sentence_scroll_pane);
	}

	private void setupAddButton() {
		JButton add_word_button = new JButton("Add This Word");
		add_word_button.setBounds(510, 438, 120, 23);
		add_word_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String word=word_field.getText();
				if (!word.matches(".*[a-zA-Z]+.*")){
					JOptionPane.showMessageDialog(null, "Word must have at least 1 alphabetical character", "Word Format Error", JOptionPane.WARNING_MESSAGE);
				} else {
					words_to_add.add(word);
					if (sample_sentence.getText().trim().equals("")){
						sentences_to_add.add(" ");
					} else {
						sentences_to_add.add(sample_sentence.getText());
					}
					word_field.setText("");
					sample_sentence.setText("");
					words_added.append(word+"\n");
				}
			}	
		});
		add(add_word_button);
	}

	private void setupSaveButton() {
		JButton save_Button = new JButton("Save");
		save_Button.setBounds(392, 497, 93, 23);
		save_Button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!list_name_field.getText().matches("[a-zA-Z]+")){
					JOptionPane.showMessageDialog(null, "List name must only consist of alphabetical characters", "Name Format Error", JOptionPane.WARNING_MESSAGE);				
				} else if(words_to_add.size()==0){
					JOptionPane.showMessageDialog(null, "Spelling list must contain at least 1 word", "Empty List Error", JOptionPane.WARNING_MESSAGE);				
				} else {
					list_name=list_name_field.getText();
					File to_write_to = new File(System.getProperty("user.dir")+"/spellinglists/"+list_name+".txt");
					if(to_write_to.exists()){
						JOptionPane.showMessageDialog(null, "A list with the same name already exists\nPlease rename.", "Duplicate List Name", JOptionPane.WARNING_MESSAGE);				
					} else {
						if (askForConfirmation("Will save into spellinglists folder\nas "+list_name+".txt", "Confirm Save")){
							parent_frame.getDataHandler().writeCustomList(list_name, words_to_add, sentences_to_add);
							parent_frame.changePanel(PanelID.MainMenu);
						}	
					}
				}
			}
		});
		add(save_Button);
	}

	private void setupDiscardButton() {
		JButton discard_button = new JButton("Discard");
		discard_button.setBounds(626, 497, 93, 23);
		discard_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (askForConfirmation("Are you sure you want to discard this list?", "Discard")){
					parent_frame.changePanel(PanelID.MainMenu);
				}

			}
		});
		add(discard_button);
	}

	/**
	 * Prompt that asks user if they want to return to main menu
	 * @returns boolean		true if they clicked yes, otherwise false
	 */
	private boolean askForConfirmation(String message, String title){
		//http://stackoverflow.com/questions/8689122/joptionpane-yes-no-options-confirm-dialog-box-issue-java
		int ask_leave_prompt = JOptionPane.YES_NO_OPTION;
		int ask_leave_result = JOptionPane.showConfirmDialog(this, message,title, ask_leave_prompt);
		if (ask_leave_result == JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}
}
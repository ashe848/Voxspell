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

	private ArrayList<String> words_to_add = new ArrayList<String>();
	private ArrayList<String> sentences_to_add = new ArrayList<String>();
	private String list_name;
	
	private Voxspell parent_frame;
	private JTextField textField;
	private JTextField textField_1;
	JTextArea txtrWordsAdded;
	JTextArea textArea;

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
		JLabel lblBuildYourOwn = new JLabel("Build Your Own Custom List");
		lblBuildYourOwn.setBounds(288, 26, 327, 15);
		add(lblBuildYourOwn);
	}
	
	private void setupWordsAdded() {
		JLabel lblWordsAdded = new JLabel("Words Added:");
		lblWordsAdded.setBounds(10, 84, 97, 15);
		add(lblWordsAdded);
		
		txtrWordsAdded = new JTextArea();
		txtrWordsAdded.setText("");
		txtrWordsAdded.setEditable(false);
		txtrWordsAdded.setLineWrap(true);
		txtrWordsAdded.setWrapStyleWord(true);
		
		JScrollPane sPane = new JScrollPane(txtrWordsAdded);
		sPane.setBounds(10, 108, 302, 418);
		add(sPane);
	}

	private void setupEnterName() {
		JLabel lblNameOfList = new JLabel("Name of List:");
		lblNameOfList.setBounds(350, 90, 97, 15);
		add(lblNameOfList);
		
		textField = new JTextField();
		textField.setBounds(457, 81, 302, 34);
		add(textField);
		textField.setColumns(10);
	}

	private void setupEnterWord() {
		JLabel lblWord = new JLabel("Word:");
		lblWord.setBounds(350, 154, 97, 15);
		add(lblWord);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(457, 145, 302, 34);
		add(textField_1);
	}

	private void setupEnterSentence() {
		JLabel lblSampleSentenceoptional = new JLabel("Sample sentence (optional):");
		lblSampleSentenceoptional.setBounds(350, 218, 176, 15);
		add(lblSampleSentenceoptional);
		
		textArea = new JTextArea();
		textArea.setText("");
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		JScrollPane sp = new JScrollPane(textArea);
		sp.setBounds(350, 243, 409, 171);
		add(sp);
	}

	private void setupAddButton() {
		JButton btnAddThisWord = new JButton("Add This Word");
		btnAddThisWord.setBounds(510, 438, 120, 23);
		btnAddThisWord.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String word=textField_1.getText();
				if (!word.matches(".*[a-zA-Z]+.*")){
					JOptionPane.showMessageDialog(null, "Word must have at least 1 alphabetical character", "Word Format Error", JOptionPane.WARNING_MESSAGE);
				} else {
					words_to_add.add(word);
					if (textArea.getText().trim().equals("")){
						sentences_to_add.add(" ");
					} else {
						sentences_to_add.add(textArea.getText());
					}
					textField_1.setText("");
					textArea.setText("");
					txtrWordsAdded.append(word+"\n");
				}
			}	
		});
		add(btnAddThisWord);
	}

	private void setupSaveButton() {
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(392, 497, 93, 23);
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!textField.getText().matches("[a-zA-Z]+")){
					JOptionPane.showMessageDialog(null, "List name must only consist of alphabetical characters", "Name Format Error", JOptionPane.WARNING_MESSAGE);				
				} else if(words_to_add.size()==0){
					JOptionPane.showMessageDialog(null, "Spelling list must contain at least 1 word", "Empty List Error", JOptionPane.WARNING_MESSAGE);				
				} else {
					list_name=textField.getText();
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
		add(btnSave);
	}

	private void setupDiscardButton() {
		JButton btnDiscard = new JButton("Discard");
		btnDiscard.setBounds(626, 497, 93, 23);
		btnDiscard.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (askForConfirmation("Are you sure you want to discard this list?", "Discard")){
					parent_frame.changePanel(PanelID.MainMenu);
				}
				
			}
		});
		add(btnDiscard);
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
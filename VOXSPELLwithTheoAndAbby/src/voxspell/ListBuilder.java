package voxspell;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import voxspell.Voxspell.PanelID;

@SuppressWarnings({ "serial" })

public class ListBuilder extends JPanel {
	private Voxspell parent_frame;

	private JTextField list_name_field;
	private JTextField word_field;
	private JTextArea words_added;
	private JTextArea sample_sentence;

	private String list_name;
	private ArrayList<String> words_to_add = new ArrayList<String>();
	private ArrayList<String> sentences_to_add = new ArrayList<String>();

	/**
	 * Constructor, initialise panel parameters and GUI elements
	 */
	public ListBuilder(Voxspell parent){
		setLayout(null);
		setBackground(new Color(235, 235, 235));

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
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setForeground(new Color(254, 157, 79));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(32, 24, 1284, 119);
		add(title);
	}

	private void setupWordsAdded() {
		JLabel words_added_label = new JLabel("Words Added:");
		words_added_label.setFont(new Font("Arial", Font.PLAIN, 25));
		words_added_label.setBounds(32, 180, 223, 45);
		add(words_added_label);

		words_added = new JTextArea();
		words_added.setText("");
		words_added.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		words_added.setEditable(false);
		words_added.setLineWrap(true);
		words_added.setWrapStyleWord(true);

		JScrollPane words_scroll_pane = new JScrollPane(words_added);
		words_scroll_pane.setBounds(32, 235, 337, 463);
		add(words_scroll_pane);
	}

	private void setupEnterName() {
		JLabel list_name_label = new JLabel("Name of List:");
		list_name_label.setFont(new Font("Arial", Font.PLAIN, 25));
		list_name_label.setBounds(420, 180, 223, 45);
		add(list_name_label);

		list_name_field = new JTextField();
		list_name_field.setBounds(688, 180, 383, 45);
		list_name_field.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		add(list_name_field);
		list_name_field.setColumns(10);
	}

	private void setupEnterWord() {
		JLabel word_label = new JLabel("Word:");
		word_label.setFont(new Font("Arial", Font.PLAIN, 25));
		word_label.setBounds(420, 269, 223, 45);
		add(word_label);

		word_field = new JTextField();
		word_field.setColumns(10);
		word_field.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		word_field.setBounds(688, 269, 383, 45);
		add(word_field);
	}

	private void setupEnterSentence() {
		JLabel sample_sentence_label = new JLabel("Sample sentence (optional):");
		sample_sentence_label.setFont(new Font("Arial", Font.PLAIN, 25));
		sample_sentence_label.setBounds(420, 399, 369, 45);
		add(sample_sentence_label);

		sample_sentence = new JTextArea();
		sample_sentence.setText("");
		sample_sentence.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		sample_sentence.setEditable(true);
		sample_sentence.setLineWrap(true);
		sample_sentence.setWrapStyleWord(true);

		JScrollPane sentence_scroll_pane = new JScrollPane(sample_sentence);
		sentence_scroll_pane.setBounds(420, 461, 651, 237);
		add(sentence_scroll_pane);
	}

	private void setupAddButton() {
		ImageIcon add_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "addword.png");
		JButton add_word_button = new JButton("", add_button_image);
		add_word_button.setBounds(1139, 180, 177, 100);
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
		add_word_button.addMouseListener(new VoxMouseAdapter(add_word_button,null));
		add(add_word_button);
	}

	private void setupSaveButton() {
		ImageIcon save_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "save.png");
		JButton save_Button = new JButton("", save_button_image);
		save_Button.setBounds(1139, 461, 177, 100);
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
		save_Button.addMouseListener(new VoxMouseAdapter(save_Button,null));
		add(save_Button);
	}

	private void setupDiscardButton() {
		ImageIcon discard_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "discard.png");
		JButton discard_button = new JButton("",discard_button_image);
		discard_button.setBounds(1139, 598, 177, 100);
		discard_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (askForConfirmation("Are you sure you want to discard this list?", "Discard")){
					parent_frame.changePanel(PanelID.MainMenu);
				}
			}
		});
		discard_button.addMouseListener(new VoxMouseAdapter(discard_button,null));
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
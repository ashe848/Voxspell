package voxspell;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import voxspell.Voxspell.PanelID;

@SuppressWarnings("serial")

public class Quiz extends JPanel {
	private Voxspell parent_frame;
	private PanelID quiz_type;

	private JTextArea display_to_user;
	private JTextField input_from_user;

	private ArrayList<String> words_to_spell;
	private int current_word_number;
	private int current_attempt_number;
	private String word_is;
	private int words_in_quiz=3; //////////////////////CHANGED FOR EASE OF TESTING

	private ArrayList<String> words_mastered;
	private ArrayList<String> words_faulted;
	private ArrayList<String> words_failed;

	/**
	 * constructor for panel, sets up paramaters of panel and various fields
	 * @param parent	frame
	 * @param titletext	text to display in title, determines what kind of test it is
	 */
	public Quiz(Voxspell parent, PanelID type){
		setSize(800,600);
		setLayout(null);

		parent_frame = parent;
		quiz_type = type;
		
		initialiseWordsToSpell();
		
		if (words_to_spell.size()!=0){
			setupTitle();
			setupProgressTextArea();
			setupSpellHereLabel();
			setupSpellHereField();

			setupSubmitButton();
			setupSayAgainButton();
			setupBackButton();

			current_attempt_number = 1;
			current_word_number = 0;
			word_is = "Mastered";

			words_mastered = new ArrayList<String>();
			words_faulted = new ArrayList<String>();
			words_failed = new ArrayList<String>();

			startQuiz();
		}
	}

	/**
	 * sets up title at top of panel
	 */
	private void setupTitle(){
		JLabel title_to_display = new JLabel(quiz_type.toString()+" (Level: "+parent_frame.getFileIO().getCurrentLevel()+")"); 
		title_to_display.setFont(new Font("Courier New", Font.BOLD, 50));

		add(title_to_display);
		title_to_display.setLocation(50, 20);
		title_to_display.setSize(700, 50);
		title_to_display.setOpaque(true);
	}

	/**
	 * Sets up text area that shows user history of guesses and progress of quiz
	 */
	private void setupProgressTextArea(){
		display_to_user = new JTextArea();
		display_to_user.setFont(new Font("Courier New", Font.PLAIN, 18));
		display_to_user.setEditable(false);

		JScrollPane scrolling_pane = new JScrollPane(display_to_user);
		add(scrolling_pane);
		scrolling_pane.setSize(700, 250);
		scrolling_pane.setLocation(50, 80);
	}

	/**
	 * Adds label next to field where user types
	 */
	private void setupSpellHereLabel(){
		JLabel spell_here_text = new JLabel("SPELL HERE");
		spell_here_text.setFont(new Font("Courier New", Font.BOLD, 30));

		add(spell_here_text);
		spell_here_text.setLocation(50, 340);
		spell_here_text.setSize(300, 50);
		spell_here_text.setOpaque(true);
	}

	/**
	 * Adds the field in which user types word
	 */
	private void setupSpellHereField(){
		input_from_user = new JTextField();
		input_from_user.setFont(new Font("Courier New", Font.BOLD, 25));
		input_from_user.setEditable(true);

		add(input_from_user);
		input_from_user.setSize(400, 40);
		input_from_user.setLocation(280, 340);
	}

	/**
	 * sets up button that user presses to submit their spelling guess
	 */
	private void setupSubmitButton() {
		ImageIcon submit_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "submit_button.png");
		JButton submit_button = new JButton("", submit_button_image);

		submit_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkCorrectSpelling(input_from_user.getText());
			}
		});

		add(submit_button);
		submit_button.setSize(300,150);
		submit_button.setLocation(50,400);
	}

	/**
	 * adds button that lets user re-hear word to spell
	 */
	private void setupSayAgainButton() {
		ImageIcon sayagain_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "sayagain_button.png");
		JButton sayagain_button = new JButton("", sayagain_button_image);

		sayagain_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sayWord(words_to_spell.get(current_word_number));
			}
		});

		add(sayagain_button);
		sayagain_button.setSize(150,150);
		sayagain_button.setLocation(450,400);
	}

	/**
	 * Adds button that allows user to go back to main menu (user prompted before actually doing so)
	 */
	/**
	 * Back button to return to previous panel
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);

		back_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean leave_result = askToLeave();
				if (leave_result){
					parent_frame.changePanel(PanelID.MainMenu);
				}
			}
		});

		add(back_button);
		back_button.setSize(50,50);
		back_button.setLocation(700,500);
	}

	/**
	 * Prompt that asks user if they want to return to main menu
	 * @returns boolean		true if they clicked yes, otherwise false
	 */
	private boolean askToLeave(){
		//http://stackoverflow.com/questions/8689122/joptionpane-yes-no-options-confirm-dialog-box-issue-java
		int ask_leave_prompt = JOptionPane.YES_NO_OPTION;
		int ask_leave_result = JOptionPane.showConfirmDialog(this, "Would you like to return to main menu?\nYou will lose all progress", "Return To Main Menu", ask_leave_prompt);
		if (ask_leave_result == JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}

	/**
	 * @returns first word of title, SPELLING is normal quiz, REVIEW is review quiz
	 */
	private PanelID getQuizType(){
		return quiz_type;
	}

	/**
	 * Begins quiz based on current word and current attempt fields of object
	 * Says word to spell and updates text progress area
	 */
	private void startQuiz(){
		sayWord("Please Spell "+words_to_spell.get(current_word_number));
		display_to_user.append("Please spell word "+(current_word_number+1)+" out of "+words_to_spell.size()+"\tAttempt: "+(current_attempt_number)+"/2\n");
	}

	/**
	 * Spelling checked when user pressed submit button
	 * @param attempt	string that user typed into field, is compared with list of words to spell
	 */
	private void checkCorrectSpelling(String attempt){

		input_from_user.setText("");//clear input field
		display_to_user.append("\tYour guess was: "+attempt);//updates progress area with user guess

		//user enters nothing
		if(attempt.equals("")){
			display_to_user.append("\n\t\tCan't have empty input, try again\n");

			//user enters something
		} else{
			//if correct spelling (ignoring case)
			if(attempt.equals(words_to_spell.get(current_word_number))){
				sayWord("Correct");

				//adds to respective arraylist based on which attempt they get it right
				if(word_is.equals("Mastered")){
					words_mastered.add(words_to_spell.get(current_word_number));
				} else {//words is faulted
					words_faulted.add(words_to_spell.get(current_word_number));
				}
				display_to_user.append("\tCORRECT\n");
				current_word_number+=1;
				current_attempt_number=1;
				word_is = "Mastered";
				//incorrect spelling
			} else{
				sayWord("Incorrect");
				display_to_user.append("\tINCORRECT\n");
				//second time getting it wrong(failed)
				if(current_attempt_number == 2){
					words_failed.add(words_to_spell.get(current_word_number));
					current_attempt_number=1;
					current_word_number+=1;
					word_is = "Mastered";
					//first time getting it wrong(faulted so far, maybe failed later)
				} else{
					sayWord("Please try again");
					word_is="Faulted";
					current_attempt_number+=1;
				}
			}
		}

		/* End clause when words to spell array exhausted
		 * Updates the IOContent fields and relevant files
		 * Also prompts user with notification that test is over and that they will be returned to main menu
		 */
		if (current_word_number == words_to_spell.size()){
			parent_frame.getFileIO().processQuizResults(words_mastered,words_faulted,words_failed,quiz_type);
			parent_frame.changePanel(PanelID.QuizComplete);
			//Otherwise keep going with quiz
		} else{
			startQuiz();
		}
	}

	/**
	 * Method that calls festival in bash to say out text
	 * @param text to say out
	 */
	private void sayWord(String text){
		parent_frame.festival.speak(text);
	}

	/**
	 * Method that creates the words_to_spell field that will be tested in quiz based on type of quiz
	 */
	private void initialiseWordsToSpell(){
		//If SPELLING then normal quiz
		if(this.getQuizType()==PanelID.Quiz){
			words_to_spell = parent_frame.getFileIO().getWordsForSpellingQuiz(words_in_quiz, PanelID.Quiz);
		} else { //If REVIEW (else clause) must be review quiz
			words_to_spell = parent_frame.getFileIO().getWordsForSpellingQuiz(words_in_quiz, PanelID.Review);
		}		 
	}
}

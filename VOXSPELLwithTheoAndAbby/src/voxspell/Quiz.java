package voxspell;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import voxspell.Festival.FestivalSpeed;
import voxspell.Festival.FestivalVoice;
import voxspell.Voxspell.PanelID;

@SuppressWarnings("serial")

/**
 * JPanel class for screen displayed when user does quiz (normal & review)
 * Displays a title, with a text area below showing quiz progress and
 * previous guesses.
 * User has a text field where they enter their attempt with a submit
 * button and say again (in case they want to hear word again) button 
 * available to press.
 * Return to main menu button also present on panel.
 * 
 * Based on Theo's A2 code
 */
public class Quiz extends JPanel {
	private Voxspell parent_frame;
	private Image bg_image;
	private PanelID quiz_type; //Distinguishes if quiz is normal or review quiz

	private JTextArea display_to_user; //progress text area to show previous information
	private JTextField input_from_user; //what user puts as guess for spelling quiz
	
	private ArrayList<String> words_to_spell; //list of words to spell in quiz
	private int current_word_number; //indicates which word the user is up to in quiz
	private int current_attempt_number; //indicates which attempt user is up to when spelling
	private boolean attempted_once; //flag indicating which attempt user is up to
	

	private ArrayList<String> words_mastered; //list of words user got first try in quiz
	private ArrayList<String> words_faulted; //list of words user got second try in quiz
	private ArrayList<String> words_failed; //list of words user didn't get right in quiz

	/**
	 * constructor for panel, sets up paramaters of panel and various fields
	 * @param parent	frame
	 * @param 
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
			setupChangeVoice();
			setupChangeSpeed();
			setupAddToReviewButton();
			setupBackButton();
			setupBackground();

			current_attempt_number = 1;
			current_word_number = 0;
			attempted_once = true;

			words_mastered = new ArrayList<String>();
			words_faulted = new ArrayList<String>();
			words_failed = new ArrayList<String>();

			startQuiz(); //begins quiz logic
		}
	}


	/**
	 * Method that determines what will be tested in quiz based on type of quiz
	 */
	private void initialiseWordsToSpell(){
		//normal quiz
		if(quiz_type==PanelID.Quiz){
			words_to_spell = parent_frame.getDataHandler().getWordsForSpellingQuiz(parent_frame.getDataHandler().words_in_quiz, PanelID.Quiz);
		} else { //review quiz
			words_to_spell = parent_frame.getDataHandler().getWordsForSpellingQuiz(parent_frame.getDataHandler().words_in_quiz, PanelID.Review);
		}		 
	}

	/**
	 * sets up title at top of panel
	 */
	private void setupTitle(){
		JLabel title_to_display = new JLabel(quiz_type.toString()+": "+parent_frame.getDataHandler().level_names.get(parent_frame.getDataHandler().getCurrentLevel())); 
		title_to_display.setFont(new Font("Courier New", Font.BOLD, 50));

		add(title_to_display);
		title_to_display.setLocation(50, 20);
		title_to_display.setSize(700, 50);
		title_to_display.setOpaque(false);
	}

	/**
	 * Sets up text area that shows user history of guesses and progress of quiz
	 */
	private void setupProgressTextArea(){
		display_to_user = new JTextArea();
		display_to_user.setFont(new Font("Courier New", Font.BOLD, 18));
		display_to_user.setEditable(false);
		display_to_user.setOpaque(true);
		
		JScrollPane scrolling_pane = new JScrollPane(display_to_user);
		add(scrolling_pane);
		scrolling_pane.setSize(700, 250);
		scrolling_pane.setLocation(50, 80);
		scrolling_pane.setBackground(Color.WHITE);
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
		spell_here_text.setOpaque(false);
	}

	/**
	 * Adds the field in which user types word
	 */
	private void setupSpellHereField(){
		input_from_user = new JTextField();
		input_from_user.setFont(new Font("Courier New", Font.BOLD, 25));
		input_from_user.setEditable(true);
		input_from_user.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkCorrectSpelling(input_from_user.getText());
				input_from_user.requestFocusInWindow();
			}
		});
		
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
				input_from_user.requestFocusInWindow();
			}
		});

		add(submit_button);
		submit_button.setSize(230,150);
		submit_button.setLocation(50,400);
	}

	/**
	 * adds button that lets user re-hear word to spell
	 */
	private void setupSayAgainButton() {
		ImageIcon sayagain_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "sayagain_button_alt.png");
		JButton sayagain_button = new JButton("", sayagain_button_image);
		sayagain_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.getFestival().speak(words_to_spell.get(current_word_number),true);
			}
		});

		add(sayagain_button);
		sayagain_button.setSize(150,150);
		sayagain_button.setLocation(450,400);
	}
	
	/**
	 * @author Abby S
	 */
	private void setupChangeVoice() {
		FestivalVoice[] voices={FestivalVoice.American, FestivalVoice.Kiwi};
		final JComboBox voice_chooser = new JComboBox(voices);
		voice_chooser.setForeground(Color.BLACK);
		voice_chooser.setBackground(Color.WHITE);

		//set shown item to be the current voice
		voice_chooser.setSelectedItem(parent_frame.getFestival().getFestivalVoice());
		voice_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((FestivalVoice)voice_chooser.getSelectedItem()!=null){
					parent_frame.getFestival().setFestivalVoice((FestivalVoice)voice_chooser.getSelectedItem());
				}
				
			}
		});

		voice_chooser.setBounds(622, 400, 140, 40);
		add(voice_chooser);
	}

	/**
	 * @author Abby S
	 */
	private void setupChangeSpeed() {
		FestivalSpeed[] speeds={FestivalSpeed.slow, FestivalSpeed.normal, FestivalSpeed.fast};
		final JComboBox speed_chooser = new JComboBox(speeds);
		speed_chooser.setForeground(Color.BLACK);
		speed_chooser.setBackground(Color.WHITE);

		//set shown item to be the current voice
		speed_chooser.setSelectedItem(parent_frame.getFestival().getFestivalSpeed());
		speed_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((FestivalSpeed)speed_chooser.getSelectedItem()!=null){
					parent_frame.getFestival().setFestivalSpeed((FestivalSpeed)speed_chooser.getSelectedItem());
				}
			}
		});

		speed_chooser.setBounds(622, 450, 140, 40);
		add(speed_chooser);
	}
	
	/**
	 * Doesn't affect stats
	 * @author Abby S
	 */
	private void setupAddToReviewButton() {
		JButton btnAddToReview = new JButton("Add to review");
		btnAddToReview.setBounds(290, 400, 130, 23);
		btnAddToReview.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> word_to_add=new ArrayList<>();
				word_to_add.add(words_to_spell.get(current_word_number));
				parent_frame.getDataHandler().addToReviewList(word_to_add);
				
			}
		});
		add(btnAddToReview);
	}


	/**
	 * Back button to return to previous panel (user prompted before actually doing so)
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);

		back_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean leave_result = askToLeave();
				if (leave_result){
					//no point speaking any more words if exiting
					parent_frame.getFestival().emptyWorkerQueue();
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
	 * Begins quiz based on current word and current attempt fields of object
	 * Says word to spell and updates text progress area
	 */
	private void startQuiz(){
		parent_frame.getFestival().speak("Please spell "+words_to_spell.get(current_word_number),false);
		display_to_user.append("Please spell word "+(current_word_number+1)+" out of "+words_to_spell.size()+"\tAttempt: "+(current_attempt_number)+"/2\n");
	}

	/**
	 * Spelling checked when user pressed submit button
	 * @param attempt	string that user typed into field, is compared with list of words to spell
	 */
	private void checkCorrectSpelling(String attempt){
		input_from_user.setText("");//clear input field	
		
		if(!attempt.matches(".*[a-zA-Z]+.*")){ //user doesn't enters any alphabetical characters
			display_to_user.append("\t\tWord includes alphabet characters, try again\n\n");
		}
		else{
			display_to_user.append("\tYour guess was: "+attempt);//updates progress area with user guess
			
			//if correct spelling (case-sensitive)
			if(attempt.equals(words_to_spell.get(current_word_number))){
				parent_frame.getFestival().speak("Correct", false);

				//adds to respective arraylist based on which attempt they get it right
				if(attempted_once==true){
					words_mastered.add(words_to_spell.get(current_word_number));
				} else {//words is faulted
					words_faulted.add(words_to_spell.get(current_word_number));
				}

				display_to_user.append("\tCORRECT\n\n");
				current_word_number+=1;
				current_attempt_number=1;
				attempted_once = true;
			} else{//incorrect spelling
				parent_frame.getFestival().speak("Incorrect", false);
				display_to_user.append("\tINCORRECT\n\n");

				//second time getting it wrong(failed)
				if(current_attempt_number == 2){
					words_failed.add(words_to_spell.get(current_word_number));
					current_attempt_number=1;
					current_word_number+=1;
					attempted_once = true;
				} else{	//first time getting it wrong(faulted so far, maybe failed later)
					parent_frame.getFestival().speak("Please try again", false);
					attempted_once=false;
					current_attempt_number+=1;
				}
			}
		}

		//When words to spell array exhausted, asks DataHandler to process results
		if (current_word_number == words_to_spell.size()){
			//no point speaking any more things if quiz has already completed
			parent_frame.getFestival().emptyWorkerQueue();
			parent_frame.getDataHandler().processQuizResults(words_mastered,words_faulted,words_failed,quiz_type);
			parent_frame.changePanel(PanelID.QuizComplete);
		} else{ //Otherwise keep going with quiz
			startQuiz();
		}
	}
	
	/**
	 * Puts the background image, overriding paintComponent method(below) to ensure functionality
	 */
	private void setupBackground(){
		//http://stackoverflow.com/questions/1466240/how-to-set-an-image-as-a-background-for-frame-in-swing-gui-of-java
		try {
			bg_image = ImageIO.read(new File(parent_frame.getResourceFileLocation() + "quiz_bg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setLocation(0,0);
		setSize(800, 600);
	}
	
	/**
	 * Overriding the paintComponent method to place background
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bg_image, 0, 0, this);
	}
}
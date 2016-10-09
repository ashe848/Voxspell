package voxspell;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import voxspell.Festival.FestivalSpeed;
import voxspell.Festival.FestivalVoice;
import voxspell.Voxspell.PanelID;
import windowbuilder.VoxMouseAdapter;

@SuppressWarnings({ "static-access", "serial", "unchecked", "rawtypes" })

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

	private JProgressBar progress_bar;
	private JTextArea feedback_display; //progress text area to show previous information
	private JTextField input_from_user; //what user puts as guess for spelling quiz
	
	private ArrayList<String> words_to_spell; //list of words to spell in quiz
	private int current_word_number; //indicates which word the user is up to in quiz
	private int current_attempt_number; //indicates which attempt user is up to when spelling

	private ArrayList<String> words_mastered; //list of words user got first try in quiz
	private ArrayList<String> words_faulted; //list of words user got second try in quiz
	private ArrayList<String> words_failed; //list of words user didn't get right in quiz

	/**
	 * constructor for panel, sets up paramaters of panel and various fields
	 * @param parent	frame
	 * @param 
	 */
	public Quiz(Voxspell parent, PanelID type){
		setSize(1366,745);
		setLayout(null);
		setBackground(new Color(235, 235, 235));

		parent_frame = parent;
		quiz_type = type;

		initialiseWordsToSpell();

		if (words_to_spell.size()!=0){
			setupTitle();
			setupProgressBar();
			setupFeedbackDisplayTextArea();
			setupSpellHereLabel();
			setupSpellHereField();
			setupSubmitButton();
			setupSayAgainButton();
			setupChangeVoice();
			setupChangeSpeed();
			setupAddToReviewButton();
			setupBackButton();

			ImageIcon help_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "help.png");
			JButton help_button = new JButton("",help_button_image);
			help_button.setBorderPainted(false);
			help_button.setBounds(1216, 24, 100, 100);
			help_button.addMouseListener(new VoxMouseAdapter(help_button,null));
			add(help_button);
			
			current_attempt_number = 1;
			current_word_number = 0;

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
			words_to_spell = parent_frame.getDataHandler().getWordsForSpellingQuiz(parent_frame.getDataHandler().getNumWordsInQuiz(), PanelID.Quiz);
		} else { //review quiz
			words_to_spell = parent_frame.getDataHandler().getWordsForSpellingQuiz(parent_frame.getDataHandler().getNumWordsInQuiz(), PanelID.Review);
		}	
	}

	/**
	 * sets up title at top of panel
	 */
	private void setupTitle(){
		JLabel title = new JLabel(quiz_type.toString()+": "+parent_frame.getDataHandler().getLevelNames().get(parent_frame.getDataHandler().getCurrentLevel())); 
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setForeground(new Color(254, 157, 79));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		add(title);
		title.setBounds(32, 24, 1136, 119);
		title.setOpaque(false);
	}

	/**
	 * Colour is result of last word, because text area clears once move on
	 * But for the colour-blind, they can hear the result said by festival anyway
	 * Using JLabel only seems to work if it's a local variable, but that doesn't do the job as it can't be changed
	 * @author Abby S
	 */
	private void setupProgressBar() {
		progress_bar = new JProgressBar(0,words_to_spell.size());
		progress_bar.setBounds(32, 170, 1284, 34);
		progress_bar.setBorderPainted(false);
		progress_bar.setBackground(Color.WHITE);
		add(progress_bar);
	}

	/**
	 * Sets up text area that shows user history of guesses and progress of quiz
	 */
	private void setupFeedbackDisplayTextArea(){
		feedback_display = new JTextArea();
		feedback_display.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		feedback_display.setEditable(false);
		feedback_display.setLineWrap(true);
		feedback_display.setWrapStyleWord(true);
		feedback_display.setOpaque(true);

		JScrollPane scrolling_pane = new JScrollPane(feedback_display);
		add(scrolling_pane);
		scrolling_pane.setBounds(32, 222, 1284, 252);
		scrolling_pane.setBackground(Color.WHITE);
	}

	/**
	 * Adds label next to field where user types
	 */
	private void setupSpellHereLabel(){
		JLabel spell_here_text = new JLabel("SPELL HERE");
		spell_here_text.setFont(new Font("Arial", Font.BOLD, 45));
		spell_here_text.setForeground(new Color(254, 157, 79));
		add(spell_here_text);
		spell_here_text.setBounds(32, 484, 332, 74);
		spell_here_text.setOpaque(false);
	}

	/**
	 * Adds the field in which user types word
	 */
	private void setupSpellHereField(){
		input_from_user = new JTextField();
		input_from_user.setFont(new Font("Calibri Light", Font.PLAIN, 45));
		input_from_user.setEditable(true);
		input_from_user.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkCorrectSpelling(input_from_user.getText());
				input_from_user.requestFocusInWindow();
			}
		});
		add(input_from_user);
		input_from_user.setBounds(374, 484, 942, 74);
	}

	/**
	 * sets up button that user presses to submit their spelling guess
	 */
	private void setupSubmitButton() {
		ImageIcon submit_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "submit.png");
		JButton submit_button = new JButton("", submit_button_image);
		submit_button.setBorderPainted(false);
		submit_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkCorrectSpelling(input_from_user.getText());
				input_from_user.requestFocusInWindow();
			}
		});
		submit_button.addMouseListener(new VoxMouseAdapter(submit_button,null));
		add(submit_button);
		submit_button.setBounds(32, 598, 177, 100);
	}

	/**
	 * adds button that lets user re-hear word to spell
	 */
	private void setupSayAgainButton() {
		ImageIcon sayagain_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "sayagain.png");
		JButton sayagain_button = new JButton("", sayagain_button_image);
		sayagain_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//says the word slowly
				parent_frame.getFestival().speak(words_to_spell.get(current_word_number),true);

				//says the sample sentence at user's preferred speed
				if(parent_frame.getDataHandler().hasSampleSentences()){
					int index=parent_frame.getDataHandler().getWordlistWords().get(parent_frame.getDataHandler().getCurrentLevel()).indexOf(words_to_spell.get(current_word_number));
					parent_frame.getFestival().speak(parent_frame.getDataHandler().getSampleSentences().get(parent_frame.getDataHandler().getCurrentLevel()).get(index),false);
				}

				//says the word slowly again
				parent_frame.getFestival().speak(words_to_spell.get(current_word_number),true);
			}
		});
		sayagain_button.addMouseListener(new VoxMouseAdapter(sayagain_button,null));
		add(sayagain_button);
		sayagain_button.setBounds(667, 598, 177, 100);
	}

	/**
	 * @author Abby S
	 */
	private void setupChangeVoice() {
		FestivalVoice[] voices={FestivalVoice.Kiwi, FestivalVoice.British, FestivalVoice.American};
		final JComboBox voice_chooser = new JComboBox(voices);
		voice_chooser.setFont(new Font("Arial", Font.PLAIN, 20));
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
		voice_chooser.setBounds(919, 600, 154, 40);
		add(voice_chooser);
	}

	/**
	 * @author Abby S
	 */
	private void setupChangeSpeed() {
		FestivalSpeed[] speeds={FestivalSpeed.slow, FestivalSpeed.normal, FestivalSpeed.fast};
		final JComboBox speed_chooser = new JComboBox(speeds);
		speed_chooser.setFont(new Font("Arial", Font.PLAIN, 20));
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
		speed_chooser.setBounds(919, 658, 154, 40);
		add(speed_chooser);
	}

	/**
	 * Doesn't affect stats
	 * @author Abby S
	 */
	private void setupAddToReviewButton() {
		ImageIcon addreview_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "addtoreview.png");
		JButton add_to_review = new JButton("", addreview_button_image);
		add_to_review.setBounds(374, 598, 177, 100);
		add_to_review.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> word_to_add=new ArrayList<>();
				word_to_add.add(words_to_spell.get(current_word_number));
				parent_frame.getDataHandler().addToReviewList(word_to_add);
			}
		});
		add_to_review.addMouseListener(new VoxMouseAdapter(add_to_review,null));
		add(add_to_review);
	}

	/**
	 * Back button to return to previous panel (user prompted before actually doing so)
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.setBorderPainted(false);
		back_button.setContentAreaFilled(false);
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
		back_button.addMouseListener(new VoxMouseAdapter(back_button,null));
		add(back_button);
		back_button.setBounds(1216, 598, 100, 100);
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
		parent_frame.getFestival().speak("Please spell the word... "+words_to_spell.get(current_word_number),false);

		//says sample sentence if there is one
		if(parent_frame.getDataHandler().hasSampleSentences()){
			int index=parent_frame.getDataHandler().getWordlistWords().get(parent_frame.getDataHandler().getCurrentLevel()).indexOf(words_to_spell.get(current_word_number));
			String sentence=parent_frame.getDataHandler().getSampleSentences().get(parent_frame.getDataHandler().getCurrentLevel()).get(index);
			if (!sentence.trim().isEmpty()){
				parent_frame.getFestival().speak(sentence,false);
			}
		}

		feedback_display.append("Word: "+(current_word_number+1)+" out of "+words_to_spell.size()+"\nAttempt: "+(current_attempt_number)+" out of 2\n");
		progress_bar.setValue(current_word_number);
	}

	/**
	 * Spelling checked when user pressed submit button
	 * @param attempt	string that user typed into field, is compared with list of words to spell
	 */
	private void checkCorrectSpelling(String attempt){
		input_from_user.setText("");//clear input field	

		if(!attempt.matches(".*[a-zA-Z]+.*")){ //user doesn't enters any alphabetical characters
			feedback_display.append("Feedback: Word includes alphabet characters, try again\n\n");
		} else if(!attempt.equals(words_to_spell.get(current_word_number))&&(attempt.toLowerCase()).equals(words_to_spell.get(current_word_number).toLowerCase())){ //differ by capitalisation
			feedback_display.append("Feedback: \""+attempt+"\" is spelt correctly but incorrect capitalisation, try again\n\n");
		} else{
			feedback_display.append("Feedback: \""+attempt+"\" is ");//updates progress area with user guess

			//if correct spelling (case-sensitive)
			if(attempt.equals(words_to_spell.get(current_word_number))){
				parent_frame.getFestival().speak("Correct", false);

				//adds to respective arraylist based on which attempt they get it right
				if (current_attempt_number==1){
					words_mastered.add(words_to_spell.get(current_word_number));
				} else {//words is faulted
					words_faulted.add(words_to_spell.get(current_word_number));
				}

				progress_bar.setForeground(Color.GREEN);
				current_word_number+=1;
				current_attempt_number=1;
				feedback_display.setText("");//clear display
			} else{//incorrect spelling
				parent_frame.getFestival().speak("Incorrect", false);
				feedback_display.append("INCORRECT\n\n");

				//second time getting it wrong(failed)
				if(current_attempt_number == 2){
					words_failed.add(words_to_spell.get(current_word_number));
					current_attempt_number=1;
					current_word_number+=1;
					feedback_display.setText("");//clear display
					progress_bar.setForeground(Color.RED);
				} else{	//first time getting it wrong(faulted so far, maybe failed later)
					parent_frame.getFestival().speak("Please try again", false);
					current_attempt_number+=1;
				}
			}
		}

		//When words to spell array exhausted, asks DataHandler to process results
		if (current_word_number == words_to_spell.size()){
			//no point speaking any more things if quiz has already completed
			parent_frame.getFestival().emptyWorkerQueue();
			parent_frame.getDataHandler().processQuizResults(words_mastered,words_faulted,words_failed,quiz_type,words_to_spell.size());
			parent_frame.changePanel(PanelID.QuizComplete);
		} else{ //Otherwise keep going with quiz
			startQuiz();
		}
	}

	/**
	 * Overriding the paintComponent method to place background
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bg_image, 0, 0, this);
	}
}
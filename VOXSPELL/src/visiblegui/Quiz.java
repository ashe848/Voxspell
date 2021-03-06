package visiblegui;

import java.awt.Color;
import java.awt.Font;
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

import audio.Festival.FestivalSpeed;
import audio.Festival.FestivalVoice;
import vox.VoxMouseAdapter;
import vox.Voxspell;
import vox.Voxspell.PanelID;

@SuppressWarnings({ "static-access", "serial", "unchecked", "rawtypes" })

/**
 * JPanel class for screen displayed when user does quiz (normal & review)
 */
public class Quiz extends JPanel {
	private Voxspell parent_frame;
	private PanelID quiz_type; //Distinguishes if quiz is normal or review quiz

	private JProgressBar progress_bar; //progress through how many words there are
	private JTextArea feedback_display; //progress text area to show previous information
	private JTextField input_from_user; //what user puts as guess for spelling quiz

	private ArrayList<String> words_to_spell; //list of words to spell in quiz
	private int current_word_number; //indicates which word the user is up to in quiz
	private int current_attempt_number; //indicates which attempt user is up to when spelling

	private ArrayList<String> words_to_add_to_review;//list of words users wanted to manually add to review
	private ArrayList<String> words_mastered; //list of words user got first try in quiz
	private ArrayList<String> words_faulted; //list of words user got second try in quiz
	private ArrayList<String> words_failed; //list of words user didn't get right in quiz

	/**
	 * constructor for panel, sets up paramaters of panel and various fields
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
			setupHelpButton();
			setupBackButton();

			current_attempt_number = 1;
			current_word_number = 0;

			words_to_add_to_review=new ArrayList<>();
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
			words_to_spell = parent_frame.getPreQuizHandler().getWordsForSpellingQuiz(parent_frame.getDataHandler().getNumWordsInQuiz(), PanelID.Quiz);
		} else { //review quiz
			words_to_spell = parent_frame.getPreQuizHandler().getWordsForSpellingQuiz(parent_frame.getDataHandler().getNumWordsInQuiz(), PanelID.Review);
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
	 * Shows progress through words in this quiz
	 * @author Abby S
	 */
	private void setupProgressBar() {
		progress_bar = new JProgressBar(0,words_to_spell.size());
		progress_bar.setBounds(32, 170, 1284, 34);
		progress_bar.setBorderPainted(false);
		progress_bar.setBackground(Color.WHITE);
		progress_bar.setStringPainted(true);
		progress_bar.setString("");
		add(progress_bar);
	}

	/**
	 * Sets up text area that shows feedback and user attempts for the current word
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
	 * "Enter" key also submits attempt
	 */
	private void setupSpellHereField(){
		input_from_user = new JTextField();
		input_from_user.setFont(new Font("Calibri Light", Font.PLAIN, 45));
		input_from_user.setEditable(true);
		input_from_user.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//only enable submitting if festival has finished to avoid delayed voice prompts
				if(parent_frame.getFestival().isLocked()){
					feedback_display.append("\tPlease submit after voice prompt has finished.\n");
				} else {
					processAttempt(input_from_user.getText());
					input_from_user.requestFocusInWindow(); //gets focus back on the field
				}
			}
		});
		add(input_from_user);
		input_from_user.setBounds(374, 484, 942, 74);
	}

	/**
	 * sets up button that user presses to submit their spelling attempt
	 */
	private void setupSubmitButton() {
		ImageIcon submit_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "submit.png");
		JButton submit_button = new JButton("", submit_button_image);
		submit_button.setBorderPainted(false);
		submit_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//only enable submitting if festival has finished to avoid delayed voice prompts
				if(parent_frame.getFestival().isLocked()){
					feedback_display.append("\tPlease submit after voice prompt has finished.\n");
				} else {
					processAttempt(input_from_user.getText());
				}
				input_from_user.requestFocusInWindow();//gets focus back to the spell here field
			}
		});
		submit_button.addMouseListener(new VoxMouseAdapter(submit_button,null));
		add(submit_button);
		submit_button.setBounds(32, 598, 177, 100);
	}

	/**
	 * adds button that lets user re-hear word to spell without penalty
	 * will speak the word using slow speed, then the sample sentence (if there is one) at user's preferred speed
	 */
	private void setupSayAgainButton() {
		ImageIcon sayagain_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "sayagain.png");
		JButton sayagain_button = new JButton("", sayagain_button_image);
		sayagain_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				input_from_user.requestFocusInWindow();//gets focus back to the spell here field
				
				//says the word slowly
				parent_frame.getFestival().speak(words_to_spell.get(current_word_number),true);

				//says the sample sentence at user's preferred speed there is one @author Abby S
				if(parent_frame.getDataHandler().hasSampleSentences()){
					int index=parent_frame.getDataHandler().getWordlistWords().get(parent_frame.getDataHandler().getCurrentLevel()).indexOf(words_to_spell.get(current_word_number));
					String sentence=parent_frame.getDataHandler().getSampleSentences().get(parent_frame.getDataHandler().getCurrentLevel()).get(index);
					if (!sentence.trim().isEmpty()){
						parent_frame.getFestival().speak(sentence,false);
					}
				}
			}
		});
		sayagain_button.addMouseListener(new VoxMouseAdapter(sayagain_button,null));
		add(sayagain_button);
		sayagain_button.setBounds(667, 598, 177, 100);
	}

	/**
	 * Drop down to change preferred voice (will be saved)
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
				input_from_user.requestFocusInWindow();//gets focus back to the spell here field
				if((FestivalVoice)voice_chooser.getSelectedItem()!=null){
					parent_frame.getFestival().setFestivalVoice((FestivalVoice)voice_chooser.getSelectedItem());
				}
			}
		});
		voice_chooser.setBounds(919, 600, 154, 40);
		add(voice_chooser);
	}

	/**
	 * Drop down change preferred speed (will be saved)
	 * @author Abby S
	 */
	private void setupChangeSpeed() {
		FestivalSpeed[] speeds={FestivalSpeed.slow, FestivalSpeed.normal, FestivalSpeed.fast};
		final JComboBox speed_chooser = new JComboBox(speeds);
		speed_chooser.setFont(new Font("Arial", Font.PLAIN, 20));
		speed_chooser.setForeground(Color.BLACK);
		speed_chooser.setBackground(Color.WHITE);

		//set shown item to be the current speed
		speed_chooser.setSelectedItem(parent_frame.getFestival().getFestivalSpeed());
		speed_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				input_from_user.requestFocusInWindow();//gets focus back to the spell here field
				if((FestivalSpeed)speed_chooser.getSelectedItem()!=null){
					parent_frame.getFestival().setFestivalSpeed((FestivalSpeed)speed_chooser.getSelectedItem());
				}
			}
		});
		speed_chooser.setBounds(919, 658, 154, 40);
		add(speed_chooser);
	}

	/**
	 * Adds to word to review list if user decides they want to practice that word more
	 * Doesn't affect stats
	 * @author Abby S
	 */
	private void setupAddToReviewButton() {
		ImageIcon addreview_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "addtoreview.png");
		JButton add_to_review = new JButton("", addreview_button_image);
		add_to_review.setBounds(374, 598, 177, 100);
		add_to_review.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				words_to_add_to_review.add(words_to_spell.get(current_word_number));
				input_from_user.requestFocusInWindow();//gets focus back to the spell here field
			}
		});
		add_to_review.addMouseListener(new VoxMouseAdapter(add_to_review,null));
		add(add_to_review);
	}

	/**
	 * Displays pop up help frame
	 * @author Abby S
	 */
	private void setupHelpButton() {
		ImageIcon help_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "help.png");
		JButton help_button = new JButton("",help_button_image);
		help_button.setBorderPainted(false);
		help_button.setBounds(1216, 24, 100, 100);
		help_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Help help_frame=new Help(PanelID.Quiz);
				help_frame.setVisible(true);
				input_from_user.requestFocusInWindow();//gets focus back to the spell here field
			}
		});
		help_button.addMouseListener(new VoxMouseAdapter(help_button,null));
		add(help_button);
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
	 * http://stackoverflow.com/questions/8689122/joptionpane-yes-no-options-confirm-dialog-box-issue-java
	 * @returns boolean		true if they clicked yes, otherwise false
	 */
	private boolean askToLeave(){
		int ask_leave_prompt = JOptionPane.YES_NO_OPTION;
		int ask_leave_result = JOptionPane.showConfirmDialog(this, "Would you like to return to main menu?\nYou will lose all progress", "Return To Main Menu", ask_leave_prompt);
		if (ask_leave_result == JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}

	/**
	 * Begins quiz based on current word and current attempt fields of object
	 * Says word to spell, sample sentence (if there is one) and updates text progress area
	 */
	private void startQuiz(){	
		parent_frame.getFestival().speak("Please spell... "+words_to_spell.get(current_word_number),false);

		feedback_display.append("Word: "+(current_word_number+1)+" out of "+words_to_spell.size()+"\nAttempt: "+current_attempt_number+" out of 2\n");
		progress_bar.setValue(current_word_number);
	}

	/**
	 * Spelling checked when user pressed submit button
	 * @param attempt	string that user typed into field, is compared with list of words to spell
	 * 
	 * Logic based on Theo's A2 code
	 */
	private void processAttempt(String attempt){
		input_from_user.setText("");//clear input field	

		if(!attempt.matches(".*[a-zA-Z]+.*")){ 
			//user doesn't enters any alphabetical characters
			feedback_display.append("Feedback: Word includes alphabet characters, try again\n\n");
		} else if(!attempt.equals(words_to_spell.get(current_word_number))&&(attempt.toLowerCase()).equals(words_to_spell.get(current_word_number).toLowerCase())){ 
			//differs only by capitalisation
			feedback_display.append("Feedback: \""+attempt+"\" is spelt correctly but incorrect capitalisation, try again\n\n");
		} else{
			feedback_display.append("Feedback: \""+attempt+"\" is ");//updates progress area with user guess

			if(attempt.equals(words_to_spell.get(current_word_number))){
				correctSpelling();
			} else{
				incorrectSpelling();
			}
		}

		//When words to spell array exhausted, asks DataHandler to process results
		if (current_word_number == words_to_spell.size()){
			//no point speaking any more things if quiz has already completed
			parent_frame.getFestival().emptyWorkerQueue();
			parent_frame.getPostQuizHandler().processQuizResults(words_mastered,words_faulted,words_failed,quiz_type,words_to_spell.size());
			
			//after processing results, so if got word correct after clicking Add To Review, word is still added
			parent_frame.getPostQuizHandler().addToReviewList(words_to_add_to_review);
			parent_frame.changePanel(PanelID.QuizComplete);
		} else{ 
			//Otherwise keep going with quiz
			startQuiz();
		}
	}

	/**
	 * If correct spelling (case-sensitive)
	 */
	private void correctSpelling() {
		parent_frame.getFestival().speak("Correct", false);

		//adds to respective arraylist based on which attempt they get it right
		if (current_attempt_number==1){
			words_mastered.add(words_to_spell.get(current_word_number));
		} else {//words is faulted
			words_faulted.add(words_to_spell.get(current_word_number));
		}

		current_word_number+=1;
		current_attempt_number=1;
		progress_bar.setForeground(Color.GREEN);
		progress_bar.setString("word "+current_word_number +" was CORRECT");
		feedback_display.setText("");//clear display
	}

	/**
	 * incorrect spelling
	 */
	private void incorrectSpelling() {
		parent_frame.getFestival().speak("Incorrect", false);
		feedback_display.append("INCORRECT\n\n");

		//second time getting it wrong(failed)
		if(current_attempt_number == 2){
			words_failed.add(words_to_spell.get(current_word_number));
			current_word_number+=1;
			current_attempt_number=1;
			progress_bar.setForeground(Color.RED);
			progress_bar.setString("word "+current_word_number+" was INCORRECT");
			feedback_display.setText("");//clear display
		} else{	//first time getting it wrong(faulted so far, maybe failed later)
			parent_frame.getFestival().speak("Try again", false);
			current_attempt_number+=1;
		}
	}
}
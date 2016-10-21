package visiblegui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import vox.Voxspell.PanelID;

@SuppressWarnings({ "serial" })

/**
 * Pop up frame displaying help message for current panel functionality
 * @author Abby S
 */
public class Help extends JFrame {
	private JPanel content_pane;
	private PanelID from_panel;
	JTextArea help_text;

	/**
	 * Creates the frame which will pop up over main frame
	 * @author Abby S
	 */
	public Help(PanelID from) {
		setTitle("Help");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(600, 165, 683, 745);

		from_panel=from;

		content_pane = new JPanel();
		setContentPane(content_pane);
		content_pane.setLayout(null);
		content_pane.setBackground(new Color(235, 235, 235));

		setupTitle();
		setupTextArea();
	}

	/**
	 * Title 
	 * @author Abby S
	 */
	private void setupTitle() {
		JLabel title = new JLabel("Help");
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setForeground(new Color(254, 157, 79));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(32, 24, 606, 119);
		content_pane.add(title);
	}

	/**
	 * Displays help message for current panel functionality
	 * @author Abby S
	 */
	private void setupTextArea() {
		help_text = new JTextArea();
		help_text.setEditable(false);
		help_text.setLineWrap(true);
		help_text.setWrapStyleWord(true);
		help_text.setFont(new Font("Calibri Light", Font.PLAIN, 20));
		help_text.setText("");

		switch(from_panel){
		case MainMenu:
			populateForMainMenu();
			break;
		case StatSelection:
			populateForStatSelection();
			break;
		case PersistentAllStats: case SessionAllStats:
			populateForGeneralStats();
			break;
		case PersistentLevelStats: case SessionLevelStats:
			populateForLevelStats();
			break;
		case Quiz: case Review:
			populateForQuizzing();
			break;
		case QuizComplete:
			populateForQuizComplete();
			break;
		case ListBuilder:
			populateForListBuilder();
			break;
		default:
			break;
		}

		help_text.append("Consult user manual for detailed descriptions.");
		help_text.setCaretPosition(0); //scrolled to the top

		JScrollPane scroll_pane = new JScrollPane(help_text);
		scroll_pane.setBounds(32, 164, 606, 534);
		content_pane.add(scroll_pane);
	}

	/**
	 * Populates text for main menu
	 * @author Abby S
	 */
	private void populateForMainMenu() {
		help_text.append("Welcome to VOXSPELL!\n\n");
		help_text.append("The main menu is the go-to place for any functionality.\n\n");
		help_text.append("Stats: To see your statistics from previous quizzes displayed in a table. \n\n");
		help_text.append("Settings: To configure VOXSPELL to just how you want it. The values shown are the current"
				+ " settings. On your first time playing, these settings will be at the defaults, you can keep them "
				+ "as is, or simply select your preferred values. The buttons are named with what they do, and "
				+ "clicking them will bring up pop ups, guiding you through the next steps. \n\n");
		help_text.append("Log in: To register and/or log in with your name, so your data, progress and settings "
				+ "will be saved. This means that multiple users can play VOXSPELL, and you will not interfere "
				+ "with each other's progress. By default, VOXSPELL starts being logged in as Visitor, so if you "
				+ "don't want to keep your data, feel free to continue with Visitor\n\n");
		help_text.append("New Quiz and Review starts a quiz at your current level, with words being chosen randomly. "
				+ "Review will specifically select words out of those you didn't quite get, so you can review them. "
				+ "Otherwise, the quizzes proceed in exactly the same way.\n\n");
		help_text.append("You can use List Builder to build a custom list, and then choose to be quizzed on your own "
				+ "list in Settings. This is helpful if there is a category of words you'd like to specifically practice "
				+ "the spelling for, such as GrandchildrensNames, or VegesInMyGarden.\n\n");
		help_text.append("If you'd like to exit VOXSPELL, click the Power button on the bottom right corner. This is the "
				+ "only way to quit the application, and will ensure that all your data is correctly saved before exiting.\n\n");
	}

	/**
	 * Populates text for stat selection pane
	 * @author Abby S
	 */
	private void populateForStatSelection() {
		help_text.append("Statistics are saved for each list attempted, and you will be able to view your statistics"
				+ " for the current list. \n\n");
		help_text.append("There are 4 types of statistics to select from:\n\n");
		help_text.append("All sessions all levels: All the words you have attempted in this list. \n\n");
		help_text.append("All sessions by level: All the words you have attempted in this list, but selecting which "
				+ "level to view. \n\n");
		help_text.append("This session all levels and This session by level are exactly like the above, but just for"
				+ " the current sessions, i.e. it will only include those words attempted since the most recent time you started VOXSPELL.\n\n");
	}

	/**
	 * Populates text for all stats
	 * @author Abby S
	 */
	private void populateForGeneralStats() {
		help_text.append("By default, the words are sorted in ascending order by their numeric level (from 1 onwards, "
				+ "representing difficulty).\n\n");
		help_text.append("Words are categorised into Strike! (got the correct on the first try), Spare! (incorrect the"
				+ " first try but correct on the second), and Didn't get it (incorrect on both tries). They are displayed"
				+ " in a table, with the numbers in each column representing how many times each word you've attempted fell"
				+ " into those categories. You can sort the tables by clicking on the column headers, or drag them to resize"
				+ " the columns if longer words aren't fully showing.\n\n");
		help_text.append("If no words have been attempted, the table will consist of only the column headers, and be empty.\n\n");
	}

	/**
	 * Populates text for stats by level
	 * @author Abby S
	 */
	private void populateForLevelStats() {
		help_text.append("The default dropdown selection is your current level, and you can change it to any level you like."
				+ " The levels are listed by their names, in increasing level of difficulty.\n\n");
		help_text.append("Words are categorised into Strike! (got the correct on the first try), Spare! (incorrect the first"
				+ " try but correct on the second), and Didn't get it (incorrect on both tries). They are displayed in a table,"
				+ " with the numbers in each column representing how many times each word you've attempted fell into those "
				+ "categories. You can sort the tables by clicking on the column headers, or drag them to resize the columns "
				+ "if longer words aren't fully showing.\n\n");
		help_text.append("If no words have been attempted, the table will consist of only the column headers, and be empty.\n\n");
	}

	/**
	 * Populates text for quiz and review
	 * @author Abby S
	 */
	private void populateForQuizzing() {
		help_text.append("Type your attempt into the field next to 'SPELL HERE' and either click the Submit button, or the "
				+ "Enter key on the keyboard to register your attempt. If you got it incorrect on your first try, you get a "
				+ "second chance, after which VOXSPELL will move on to the next word. The text on the progress bar will show "
				+ "your result for the previous word, and be coloured green or red depending on whether you got it correct.\n\n");
		help_text.append("If you feel you'd like to practice a word more, click the Add to Review button to manually add the"
				+ " current word to the list for Review.\n\n");
		help_text.append("If you did not hear a word clearly, feel free to click the Say Again button to hear it be spoken slowly,"
				+ " followed by a sample sentence (if the word has one). You can also change the voice and speed settings for "
				+ "the audio prompts using the dropdowns. If you are using the Kiwi voice and no audio prompt is given, change "
				+ "to a different voice, because the Kiwi voice can't say certain uncommon words, e.g. expelliarmus.\n\n");
		help_text.append("VOXSPELL quizzes are case sensitive, but it will not take up an attempt if you got the spelling correct,"
				+ " but just capitalisation wrong. You will stay on the same attempt number, and the voice will prompt you to "
				+ "spell it again. The same goes for if your attempt didn't contain any alphabetical characters at all - it would"
				+ " most probably be an accidental click on the Submit button.\n\n");
	}

	/**
	 * Populates text for quiz complete
	 * @author Abby S
	 */
	private void populateForQuizComplete() {
		help_text.append("Congratulations for completing a quiz! Here you can see a table summarising the words that were "
				+ "quizzed, below which is a summary of your cumulative progress through that level.\n\n");
		help_text.append("If you got more than 90% of the words in that quiz correct, you will see the Play Video button to "
				+ "play the reward video. If you also have to option to Level Up, you can choose to move up to the next level,"
				+ " or remain in the current level and practice more words. It is suggested to consider your progress through "
				+ "the current level in deciding whether to move up, but you can always change to a lower level in Settings if "
				+ "you find the words too difficult.\n\n");
		help_text.append("Scores are calculated in VOXSPELL as an average over the number of words in the quiz you completed. "
				+ "3 points are awarded for each strike, 1 for each spare, and none if you didn't get it. This average is then "
				+ "multiplied by the level number (indicating difficulty) to get your score for the quiz. If you bet your "
				+ "personal best, you'll see a message displayed on the left of this screen.\n\n");
	}

	/**
	 * Populates text for list builder
	 * @author Abby S
	 */
	private void populateForListBuilder() {
		help_text.append("To build your own custom list, enter a name for the list consisting of only alphabetical characters."
				+ " The list will be saved to your spellinglists folder under this name, which will also serve as the level name.\n\n");
		help_text.append("Type in the word, and also add a sample sentence if you wish (else leave it empty), then click the "
				+ "Add Word button. You will see the list of words added being updated on the left of the screen.\n\n");
		help_text.append("When you're done, click Save to save the list, or Discard if you don't want it. Once you've saved "
				+ "the list, you can choose to be quizzed on it via the Settings screen.\n\n");
	}
}
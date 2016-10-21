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
 * Pop up frame displaying help message for current panel (functionality)
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
	 * Displays help message for current panel (functionality)
	 * 
	 * TODO: THIS PART IS NOT COMPLETE. The stuff is just placeholder text so I know it shows the right thing
	 * 
	 * @author Abby S
	 */
	private void setupTextArea() {
		help_text = new JTextArea();
		help_text.setEditable(false);
		help_text.setLineWrap(true);
		help_text.setWrapStyleWord(true);
		help_text.setFont(new Font("Calibri Light", Font.PLAIN, 20));
		help_text.setText("");
		help_text.setCaretPosition(0); //scrolled to the top

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

		JScrollPane scroll_pane = new JScrollPane(help_text);
		scroll_pane.setBounds(32, 164, 606, 534);
		content_pane.add(scroll_pane);
	}

	/**
	 * Populates text for main menu
	 * @author Abby S
	 */
	private void populateForMainMenu() {
		help_text.append("Welcome to VOXSPELL!\n");
		help_text.append("Has default spelling list and reward video. Application won't run if they're deleted. Has the ability to import lists, put space if empty sentences when importing lists \n");
	}

	/**
	 * Populates text for stat selection pane
	 * @author Abby S
	 */
	private void populateForStatSelection() {
		help_text.append("There are 4 types of stats. Strike and spare\n");
	}

	/**
	 * Populates text for all stats
	 * @author Abby S
	 */
	private void populateForGeneralStats() {
		help_text.append("These stats are for the current list.\n");
		help_text.append("They are for all words attempted (in any level). Table will be blank if no words have been attempted.\n");
		help_text.append("You can sort by clicking on the column headers.\n");
	}

	/**
	 * Populates text for stats by level
	 * @author Abby S
	 */
	private void populateForLevelStats() {
		help_text.append("These stats are for the level chosen in the current list.\n");
		help_text.append("They are for all words attempted in the chosen level. Table will be blank if no words have been attempted for that level.\n");
		help_text.append("The default drop down option is your current level. You can change it to any level using the dropdown\n");
		help_text.append("You can sort by clicking on the column headers.\n");
	}

	/**
	 * Populates text for quiz and review
	 * @author Abby S
	 */
	private void populateForQuizzing() {
		help_text.append("The default drop down option is your current setting. You can change it using the dropdown\nNZ voice can't say everything, e.g. expelliarmus\nChange voice and speed saves like settings\nCase sensitive no penalty if just case wrong");
	}

	/**
	 * Populates text for quiz complete
	 * @author Abby S
	 */
	private void populateForQuizComplete() {
		help_text.append("Level up will change your current level to the one above. If you are on the highest level, you will be asked to select a level to go to in a pop up.\n");
	}

	/**
	 * Populates text for list builder
	 * @author Abby S
	 */
	private void populateForListBuilder() {
		help_text.append("Build list\n");
	}
}
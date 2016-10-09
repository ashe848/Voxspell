package voxspell;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import voxspell.Voxspell.PanelID;

@SuppressWarnings({ "serial" })

/**
 * Pop up frame displaying help message for current panel (functionality)
 * @author Abby S
 */
public class Help extends JFrame {
	private JPanel content_pane;
	private PanelID from_panel;

	/**
	 * Creates the frame which will pop up over main frame
	 * @author Abby S
	 */
	public Help(PanelID from) {
		setTitle("Help");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(600, 125, 683, 745);

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
		JTextArea help_text = new JTextArea();
		help_text.setEditable(false);
		help_text.setLineWrap(true);
		help_text.setWrapStyleWord(true);
		help_text.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		help_text.setText("");

		switch(from_panel){
		case MainMenu:
			help_text.append("Welcome to VOXSPELL!\n");
			help_text.append("Has default spelling list and reward video. Application won't run if they're deleted \n");
			break;
		case StatSelection:
			help_text.append("There are 4 types of stats.\n");
			break;
		case PersistentAllStats: case SessionAllStats:
			help_text.append("These stats are for the current list.\n");
			help_text.append("They are for all words attempted (in any level).\n");
			help_text.append("You can sort by clicking on the column headers.\n");
			break;
		case PersistentLevelStats: case SessionLevelStats:
			help_text.append("These stats are for the level chosen in the current list.\n");
			help_text.append("They are for all words attempted in the chosen level.\n");
			help_text.append("The default drop down option is your current level. You can change it to any level using the dropdown\n");
			help_text.append("You can sort by clicking on the column headers.\n");
			break;
		case Quiz: case Review:
			help_text.append("The default drop down option is your current setting. You can change it using the dropdown\n");
			break;
		case QuizComplete:
			help_text.append("Level up will change your current level to the one above. If you are on the highest level, you will be asked to select a level to go to in a pop up.\n");
			break;
		default:
			break;
		}

		JScrollPane scroll_pane = new JScrollPane(help_text);
		scroll_pane.setBounds(32, 164, 606, 534);
		content_pane.add(scroll_pane);
	}
}
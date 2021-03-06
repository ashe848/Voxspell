package visiblegui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import vox.VoxMouseAdapter;
import vox.Voxspell;
import vox.Voxspell.PanelID;

@SuppressWarnings({ "static-access", "serial" })

/**
 * JPanel for Main Menu
 * Allows user to either:
 * 		Go do a new quiz
 * 		Review words
 * 		Build their own list
 * 		Enter settings screen
 * 		Show statistics
 * 		Log in
 * 		Quit program
 */
public class MainMenu extends JPanel{
	private Voxspell parent_frame;

	private Image bg_image;

	/**
	 * Constructor, initialise panel properties and GUI elements
	 */
	public MainMenu(Voxspell parent){
		setSize(1366,745);
		setLayout(null);
		setBackground(new Color(235, 235, 235));

		parent_frame = parent;

		setupBackground();
		setupLogInButton();
		setupListBuilderButton();
		setupNewQuizButton();
		setupReviewButton();
		setupSettingsButton();
		setupStatsButton();
		setupExitButton();
		setupHelpButton();
		setupGameSummary();
	}

	/**
	 * Puts the voxspell background image, overriding paintComponent method(below) to ensure functionality
	 * http://stackoverflow.com/questions/1466240/how-to-set-an-image-as-a-background-for-frame-in-swing-gui-of-java
	 */
	private void setupBackground(){
		try {
			bg_image = ImageIO.read(new File(parent_frame.getResourceFileLocation() + "background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setLocation(0,0);
		setSize(1366, 745);
	}

	/**
	 * Overriding the paintComponent method to place Voxspell background on main menu
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bg_image, 0, 0, this);
	}

	/**
	 * Allows user to log in or register on a pop up frame
	 * @author Abby S
	 */
	private void setupLogInButton() {
		ImageIcon login_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "login.png");
		JButton log_in_button= new JButton("",login_button_image);
		log_in_button.setBounds(965, 72, 177, 100);
		log_in_button.setBorderPainted(false);
		log_in_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogIn log_in_frame=new LogIn(parent_frame);
				log_in_frame.setVisible(true);
			}
		});
		log_in_button.addMouseListener(new VoxMouseAdapter(log_in_button,null));
		add(log_in_button);
	}

	/**
	 * Settings button
	 */
	private void setupSettingsButton(){
		ImageIcon setting_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "settings.png");
		JButton settings_button = new JButton("", setting_button_image);
		settings_button.setBorderPainted(false);
		settings_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.Settings);
			}
		});
		settings_button.addMouseListener(new VoxMouseAdapter(settings_button, null));
		add(settings_button);
		settings_button.setBounds(788, 72, 177, 100);
	}

	/**
	 * Statistics button
	 */
	private void setupStatsButton(){
		ImageIcon stats_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "stats.png");
		JButton stats_button = new JButton("", stats_button_image);
		stats_button.setBorderPainted(false);
		stats_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.StatSelection);
			}
		});
		stats_button.addMouseListener(new VoxMouseAdapter(stats_button,null));
		add(stats_button);
		stats_button.setBounds(611, 72, 177, 100);
	}

	/**
	 * New Quiz button
	 */
	private void setupNewQuizButton(){
		ImageIcon newquiz_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "newquiz.png");
		JButton new_quiz_button = new JButton("", newquiz_button_image);
		new_quiz_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.Quiz);
			}
		});
		new_quiz_button.addMouseListener(new VoxMouseAdapter(new_quiz_button,null));
		add(new_quiz_button);
		new_quiz_button.setBounds(306, 598, 177, 100);
	}

	/**
	 * Review button
	 */
	private void setupReviewButton(){
		ImageIcon review_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "review.png");
		JButton review_button = new JButton("", review_button_image);
		review_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.Review);
			}
		});
		review_button.addMouseListener(new VoxMouseAdapter(review_button,null));
		add(review_button);
		review_button.setBounds(598, 598, 177, 100);
	}

	/**
	 * Allows user to build their own list, which will also be saved in spellinglists directory for future use as well
	 * @author Abby S
	 */
	private void setupListBuilderButton(){
		ImageIcon buildlist_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "buildlist.png");
		JButton list_builder_button = new JButton("",buildlist_button_image);
		list_builder_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.ListBuilder);
			}
		});
		list_builder_button.addMouseListener(new VoxMouseAdapter(list_builder_button,null));
		add(list_builder_button);
		list_builder_button.setBounds(890, 598, 177, 100);
	}

	/**
	 * Displays help pop up frame
	 * @author Abby S
	 */
	private void setupHelpButton() {
		ImageIcon help_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "help.png");
		JButton help_button = new JButton("",help_button_image);
		help_button.setBorderPainted(false);
		help_button.setBounds(45, 45, 100, 100);
		help_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Help help_frame=new Help(PanelID.MainMenu);
				help_frame.setVisible(true);
			}
		});
		help_button.addMouseListener(new VoxMouseAdapter(help_button,null));
		add(help_button);
	}

	/**
	 * To display summary for this user
	 * 		user name
	 * 		personal best
	 *		current list
	 *		current level name
	 * @author Abby S
	 */
	private void setupGameSummary() {
		JTextArea game_summary = new JTextArea();
		game_summary.setWrapStyleWord(true);
		game_summary.setEditable(false);
		game_summary.setLineWrap(true);
		game_summary.setFont(new Font("Calibri Light", Font.PLAIN, 18));

		game_summary.setText(parent_frame.getDataHandler().getUser()+" \n\n");
		game_summary.append("Personal Best: \n"+parent_frame.getDataHandler().getPersonalBest()+" \n\n");
		game_summary.append("Current List: \n"+parent_frame.getDataHandler().getSpellingListName()+" \n\n");
		game_summary.append("Level: \n"+parent_frame.getDataHandler().getLevelNames().get(parent_frame.getDataHandler().getCurrentLevel())+" \n\n");

		game_summary.setBounds(30, 289, 120, 400);
		game_summary.setOpaque(false);
		add(game_summary);
	}

	/**
	 * Exit button (asks for confirmation)
	 */
	private void setupExitButton(){
		ImageIcon exit_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "exit.png");
		JButton exit_button = new JButton("", exit_button_image);
		exit_button.setBorderPainted(false);
		exit_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean ask_leave_result = askToLeave();
				if (ask_leave_result){
					//saves to settings before exiting (e.g. the last thing done could've been clicking the level up button)
					parent_frame.getFileWritingHandler().writeToSettingsFiles();
					parent_frame.getFileWritingHandler().writeToProgramFiles();
					System.exit(0);
				}
			}
		});
		exit_button.addMouseListener(new VoxMouseAdapter(exit_button,null));
		add(exit_button);
		exit_button.setBounds(1216, 598, 100, 100);
	}

	/**
	 * Displays window asking user to confirm exiting program
	 * http://stackoverflow.com/questions/8689122/joptionpane-yes-no-options-confirm-dialog-box-issue-java
	 * @return boolean if they want to or not
	 */
	private boolean askToLeave(){
		int ask_leave_prompt = JOptionPane.YES_NO_OPTION;
		int ask_leave_result = JOptionPane.showConfirmDialog(this, "Would you like to quit?", "Quit Voxspell", ask_leave_prompt);
		if (ask_leave_result == JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}
}
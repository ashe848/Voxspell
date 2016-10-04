package voxspell;

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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import voxspell.Voxspell.PanelID;

@SuppressWarnings({ "static-access", "serial" })

/**
 * JPanel for Main Menu
 * Allows user to either:
 * 		Go do a quiz
 * 		Review failed words
 * 		Enter settings screen
 * 		Show statistics
 * 		Quit program
 * 
 * Based on Theo's A2 code
 */
public class MainMenu extends JPanel{
	private Voxspell parent_frame;

	private Image bg_image;

	/**
	 * Constructor, initialise panel properties and GUI elements
	 */
	MainMenu(Voxspell parent){
		super();
		setSize(800, 600);
		setLayout(null);

		parent_frame = parent;

		setupBackground();
		setupLogInButton();
		setupListBuilderButton();
		setupNewQuizButton();
		setupReviewButton();
		setupSettingsButton();
		setupStatsButton();
		setupExitButton();
		setupAccuracyRateLabel();
	}

	/**
	 * Puts the voxspell background image, overriding paintComponent method(below) to ensure functionality
	 * 
	 * http://stackoverflow.com/questions/1466240/how-to-set-an-image-as-a-background-for-frame-in-swing-gui-of-java
	 */
	private void setupBackground(){
		try {
			bg_image = ImageIO.read(new File(parent_frame.getResourceFileLocation() + "voxspell_opaque_bg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setLocation(0,0);
		setSize(800, 600);
	}

	/**
	 * Overriding the paintComponent method to place Voxspell background on main menu
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bg_image, 0, 0, this);
	}

	/**
	 * @author Abby S
	 */
	private void setupLogInButton() {
		JButton log_in_button= new JButton("Log In");
		log_in_button.setBounds(184, 50, 93, 23);
		log_in_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogIn logIn=new LogIn(parent_frame);
				logIn.setVisible(true);
			}
		});
		add(log_in_button);
	}

	/**
	 * @author Abby S
	 */
	private void setupListBuilderButton(){
		JButton list_builder_button = new JButton("Custom List Builder");

		list_builder_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.ListBuilder);
			}
		});

		add(list_builder_button);
		list_builder_button.setSize(200, 50);
		list_builder_button.setLocation(550, 260);
	}

	/**
	 * New Spelling Quiz button creation
	 */
	private void setupNewQuizButton(){
		ImageIcon newquiz_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "quiz_button.png");
		JButton new_quiz_button = new JButton("", newquiz_button_image);

		new_quiz_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.Quiz);
			}
		});

		add(new_quiz_button);
		new_quiz_button.setSize(200, 50);
		new_quiz_button.setLocation(550, 330);
	}

	/**
	 * Review Quiz button creation
	 */
	private void setupReviewButton(){
		ImageIcon review_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "review_button.png");
		JButton review_button = new JButton("", review_button_image);

		review_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.Review);
			}
		});

		add(review_button);
		review_button.setSize(200, 50);
		review_button.setLocation(550, 400);
	}

	/**
	 * Settings button creation
	 */
	private void setupSettingsButton(){
		ImageIcon setting_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "settings_button.png");
		JButton settings_button = new JButton("", setting_button_image);
		settings_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.Settings);
			}
		});

		add(settings_button);
		settings_button.setSize(50, 50);
		settings_button.setLocation(550, 500);
	}

	/**
	 * Statistics button creation
	 */
	private void setupStatsButton(){
		ImageIcon stats_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "stats_button.png");
		JButton stats_button = new JButton("", stats_button_image);
		stats_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.StatSelection);
			}
		});

		add(stats_button);
		stats_button.setSize(100, 100);
		stats_button.setLocation(50, 50);
	}

	/**
	 * Exit button creation
	 */
	private void setupExitButton(){
		ImageIcon exit_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "ext_btn.png");
		JButton exit_button = new JButton("", exit_button_image);
		exit_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean askquit_result = askToLeave();
				if (askquit_result){
					//saves to settings before exiting (e.g. if the last thing done was click the level up button)
					parent_frame.getDataHandler().writeToSettingsFiles();
					parent_frame.getDataHandler().writeToProgramFiles();
					System.exit(0);
				}
			}
		});

		add(exit_button);
		exit_button.setSize(50,50);
		exit_button.setLocation(700,500);
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

	/**
	 * To display accuracy rates for level user is currently on
	 */
	void setupAccuracyRateLabel() {
		JLabel accuracy_rate_label = new JLabel(parent_frame.getDataHandler().getAccuracyRates()); 
		accuracy_rate_label.setFont(new Font("Courier New", Font.BOLD, 12));

		add(accuracy_rate_label);
		accuracy_rate_label.setVisible(true);
		accuracy_rate_label.setLocation(50, 530);
		accuracy_rate_label.setSize(400, 30);
		accuracy_rate_label.setOpaque(true);
	}
}
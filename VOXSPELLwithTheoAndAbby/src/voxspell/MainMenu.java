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
import javax.swing.JTextArea;

import voxspell.Voxspell.PanelID;
import windowbuilder.VoxMouseAdapter;

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
		setSize(1366,747);
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
		
		JButton help = new JButton("Help");
		help.setBounds(45, 72, 100, 100);
		add(help);
	
		setupGameSummary();
	}

	/**
	 * Puts the voxspell background image, overriding paintComponent method(below) to ensure functionality
	 * 
	 * http://stackoverflow.com/questions/1466240/how-to-set-an-image-as-a-background-for-frame-in-swing-gui-of-java
	 */
	private void setupBackground(){
		try {
			bg_image = ImageIO.read(new File(parent_frame.getResourceFileLocation() + "Background.png"));
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
		log_in_button.setBounds(965, 72, 177, 100);
		log_in_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LogIn log_in_frame=new LogIn(parent_frame);
				log_in_frame.setVisible(true);
			}
		});
		log_in_button.addMouseListener(new VoxMouseAdapter(log_in_button,null));
		log_in_button.addMouseListener(new VoxMouseAdapter(log_in_button,null));
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
		list_builder_button.addMouseListener(new VoxMouseAdapter(list_builder_button,null));
		add(list_builder_button);
		list_builder_button.setBounds(890, 598, 177, 100);
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
		new_quiz_button.addMouseListener(new VoxMouseAdapter(new_quiz_button,null));
		add(new_quiz_button);
		new_quiz_button.setBounds(306, 598, 177, 100);
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
		review_button.addMouseListener(new VoxMouseAdapter(review_button,null));
		add(review_button);
		review_button.setBounds(598, 598, 177, 100);
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
		settings_button.addMouseListener(new VoxMouseAdapter(settings_button, null));
		add(settings_button);
		settings_button.setBounds(788, 72, 177, 100);
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
		stats_button.addMouseListener(new VoxMouseAdapter(stats_button,null));
		add(stats_button);
		stats_button.setBounds(611, 72, 177, 100);
	}

	/**
	 * Exit button creation
	 */
	private void setupExitButton(){
		ImageIcon exit_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "ext_btn.png");
		JButton exit_button = new JButton("", exit_button_image);
		exit_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean ask_leave_result = askToLeave();
				if (ask_leave_result){
					//saves to settings before exiting (e.g. if the last thing done was click the level up button)
					parent_frame.getDataHandler().writeToSettingsFiles();
					parent_frame.getDataHandler().writeToProgramFiles();
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

	/**
	 * To display accuracy rates for level user is currently on
	 */
	private void setupGameSummary() {
		JTextArea game_summary = new JTextArea();
		game_summary.setWrapStyleWord(true);
		game_summary.setEditable(false);
		game_summary.setLineWrap(true);
		game_summary.setFont(new Font("Calibri Light", Font.PLAIN, 18));
		game_summary.setText(parent_frame.getDataHandler().user+" \n\n");
		game_summary.append("List Name: \n"+parent_frame.getDataHandler().spelling_list_name+" \n\n");
		game_summary.append("Level: \n"+parent_frame.getDataHandler().level_names.get(parent_frame.getDataHandler().current_level)+" \n\n");
		game_summary.append("Total Words: \n"+parent_frame.getDataHandler().wordlist_words.get(parent_frame.getDataHandler().current_level).size()+"\n\n");
		game_summary.append("Attempted: \n"+parent_frame.getDataHandler().getAttemptedCount()+" \n\n");
		game_summary.append("Didn't Get: \n"+parent_frame.getDataHandler().reviewlist_words.get(parent_frame.getDataHandler().current_level).size()+"\n\n");
		game_summary.setBounds(45, 319, 137, 389);
		game_summary.setOpaque(false);
		add(game_summary);
	}
}
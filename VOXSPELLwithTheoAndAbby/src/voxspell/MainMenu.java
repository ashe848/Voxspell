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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import voxspell.Voxspell.PanelID;

@SuppressWarnings("serial")
/**
 * Literally all of this is Theo's
 * @author theooswanditosw164 not ashe848 XD
 *
 */
public class MainMenu extends JPanel{
	

	private Voxspell parent_frame;
	private static FileIO file_handler;
	private Image bg_image;
	
	/**
	 * Constructor of main method panel, sets parameters and creates GUI elements
	 */
	MainMenu(Voxspell parent){
		super();
		
		setSize(800, 600);
		setLayout(null);
		
		parent_frame  = parent;
		file_handler=FileIO.getInstance(parent_frame);
		
		setupBackground();
		
		setupNewQuizButton();
		setupReviewQuiz();
		setupSettingsButton();
		setupStatsButton();
		setupExitButton();
	}
	
	/**
	 * Puts the voxspell background image, overriding paintComponent method(below) to ensure functionality
	 */
	private void setupBackground(){
		//http://stackoverflow.com/questions/1466240/how-to-set-an-image-as-a-background-for-frame-in-swing-gui-of-java
		try {
			bg_image = ImageIO.read(new File(parent_frame.getResourceFileLocation() + "voxspell_opaque_bg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setLocation(0,0);
		setSize(800, 600);
	}
	
	/**
	 * Overriding the paintComponent method to place voxspell background on main menu
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bg_image, 0, 0, this);
	}
	
	/**
	 * New Spelling Quiz setup and functionality method
	 */
	private void setupNewQuizButton(){
		JButton new_quiz_button = new JButton("DO A QUIZ");
		new_quiz_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				file_handler.writeStats();
				parent_frame.changePanel(PanelID.Quiz);
			}
		});
		
		add(new_quiz_button);
		new_quiz_button.setFont(new Font("Arial", Font.PLAIN, 10));;
		new_quiz_button.setSize(200, 50);
		new_quiz_button.setLocation(550, 300);
	}
	
	/**
	 * Review Quiz setup and functionality method
	 */
	private void setupReviewQuiz(){
		JButton new_quiz_button = new JButton("REVIEW A QUIZ");
		new_quiz_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.Review);
			}
		});
		
		add(new_quiz_button);
		new_quiz_button.setFont(new Font("Arial", Font.PLAIN, 10));;
		new_quiz_button.setSize(200, 50);
		new_quiz_button.setLocation(550, 400);
	}
	
	/**
	 * Settings button setup and functionality method
	 */
	private void setupSettingsButton(){
		ImageIcon setting_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "settings_button.png");

		JButton new_quiz_button = new JButton("", setting_button_image);
		new_quiz_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.Settings);
			}
		});
		
		add(new_quiz_button);
		new_quiz_button.setFont(new Font("Arial", Font.PLAIN, 10));;
		new_quiz_button.setSize(50, 50);
		new_quiz_button.setLocation(550, 500);
	}
	
	/**
	 * Statistics button creation and functionality method
	 */
	private void setupStatsButton(){
		ImageIcon stats_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "stats_button.png");

		JButton new_quiz_button = new JButton("", stats_button_image);
		new_quiz_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.StatSelection);
			}
		});
		
		add(new_quiz_button);
		new_quiz_button.setFont(new Font("Arial", Font.PLAIN, 10));;
		new_quiz_button.setSize(100, 100);
		new_quiz_button.setLocation(50, 50);
	}
	
	/**
	 * Exit button creation and funtionality method
	 */
	private void setupExitButton(){
		ImageIcon exit_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "ext_btn.png");
		JButton exit_button = new JButton("", exit_button_image);
		
		exit_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean askquit_result = askToLeave();
				if (askquit_result){
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
	 * @return boolean if they want to or not
	 */
	private boolean askToLeave(){
		//http://stackoverflow.com/questions/8689122/joptionpane-yes-no-options-confirm-dialog-box-issue-java
		int ask_leave_prompt = JOptionPane.YES_NO_OPTION;
		int ask_leave_result = JOptionPane.showConfirmDialog(this, "Would you like to quit?", "Quit Voxspell", ask_leave_prompt);
		if (ask_leave_result == JOptionPane.YES_OPTION){
			return true;
		}
		return false;
		
	}
}

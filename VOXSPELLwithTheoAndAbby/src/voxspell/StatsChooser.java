package voxspell;

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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import voxspell.Voxspell.PanelID;

@SuppressWarnings("serial")

/**
 * To select type of statistics to view.
 * Has 4 options of stats to be displayed, and a return to main menu button.
 * 	-All stats from all sessions (all levels)
 * 	-All stats from current session (all levels)
 * 	-Stats for user selected level from all sessions
 * 	-Stats for user selected level from current session
 */
public class StatsChooser extends JPanel{
	private Voxspell parent_frame;
	private Image bg_image;

	/**
	 * Constructor. Setting up panel properties and initialise GUI elements
	 */
	public StatsChooser(Voxspell parent){
		super();
		setSize(1366,747);
		setLayout(null);

		parent_frame=parent;

		setupTitle();
		setupPersistentAllButton();
		setupPersistentLevelButton();
		setupSessionAllButton();
		setupSessionLevelButton();
		setupBackButton();
		setupAccuracyRateLabel();
//		setupBackground();
	}

	private void setupTitle() {
		JLabel title = new JLabel("Statistics for Current List");
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setForeground(new Color(254, 157, 79));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(32, 24, 1136, 119);
		add(title);
	}

	/**
	 * Statistics could be just for current session or persistent
	 * Used to differentiate what data to fetch when button clicked
	 */
	public enum StatsType{
		Session, Persistent;
	}

	/**
	 * Creates button that displays statistics data for all levels from all sessions
	 */
	private void setupPersistentAllButton(){
		ImageIcon persistent_all_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "persistent_all_button.png");
		JButton persistent_all_button = new JButton("", persistent_all_button_image);
		persistent_all_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.PersistentAllStats);
			}
		});
		persistent_all_button.addMouseListener(new VoxMouseAdapter(persistent_all_button,null));
		add(persistent_all_button);
		persistent_all_button.setBounds(153, 165, 354, 200);
	}

	/**
	 * Creates button that displays statistics data for user selected level, from all sessions
	 */
	private void setupPersistentLevelButton(){
		ImageIcon persistent_level_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "persistent_level_button.png");
		JButton persistent_level_button = new JButton("", persistent_level_button_image);
		persistent_level_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.PersistentLevelStats);
			}
		});
		persistent_level_button.addMouseListener(new VoxMouseAdapter(persistent_level_button,null));
		add(persistent_level_button);
		persistent_level_button.setBounds(702, 165, 354, 200);
	}

	/**
	 * Creates button that displays statistics data for all levels, only from current session
	 */
	private void setupSessionAllButton(){
		ImageIcon session_all_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "session_all_button.png");
		JButton session_all_button = new JButton("", session_all_button_image);
		session_all_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.SessionAllStats);
			}
		});
		session_all_button.addMouseListener(new VoxMouseAdapter(session_all_button,null));
		add(session_all_button);
		session_all_button.setBounds(153, 395, 354, 200);
	}

	/**
	 * Creates button that displays statistics data for user selected level
	 * but only from current session
	 */
	private void setupSessionLevelButton(){
		ImageIcon session_level_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "session_level_button.png");
		JButton session_level_button = new JButton("", session_level_button_image);
		session_level_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.SessionLevelStats);
			}
		});
		session_level_button.addMouseListener(new VoxMouseAdapter(session_level_button,null));
		add(session_level_button);
		session_level_button.setBounds(702, 395, 354, 200);
	}

	/**
	 * Back button to return to previous panel (main menu)
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.MainMenu);
			}
		});
		back_button.addMouseListener(new VoxMouseAdapter(back_button,null));
		add(back_button);
		back_button.setBounds(1216, 598, 100, 100);
	}

	/**
	 * To display accuracy rates for level user is currently on
	 */
	private void setupAccuracyRateLabel() {
		JLabel accuracy_rate_label = new JLabel(parent_frame.getDataHandler().getAccuracyRates()); 
		accuracy_rate_label.setFont(new Font("Calibri Light", Font.PLAIN, 25));

		add(accuracy_rate_label);
		accuracy_rate_label.setBounds(32, 630, 1136, 68);
		accuracy_rate_label.setOpaque(true);
	}

	/**
	 * Puts the background image, overriding paintComponent method(below) to ensure functionality
	 */
	private void setupBackground(){
		//http://stackoverflow.com/questions/1466240/how-to-set-an-image-as-a-background-for-frame-in-swing-gui-of-java
		try {
			bg_image = ImageIO.read(new File(parent_frame.getResourceFileLocation() + "stats_chooser_bg_underlined.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setLocation(0,0);
		setSize(800, 600);
	}

	/**
	 * Overriding the paintComponent method to place background on panel
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bg_image, 0, 0, this);
	}
}
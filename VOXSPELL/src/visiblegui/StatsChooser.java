package visiblegui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import vox.VoxMouseAdapter;
import vox.Voxspell;
import vox.Voxspell.PanelID;

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

	/**
	 * Constructor. Setting up panel properties and initialise GUI elements
	 */
	public StatsChooser(Voxspell parent){
		setSize(1366,745);
		setLayout(null);
		setBackground(new Color(235, 235, 235));

		parent_frame=parent;

		setupTitle();
		setupHelpButton();
		setupPersistentAllButton();
		setupPersistentLevelButton();
		setupSessionAllButton();
		setupSessionLevelButton();
		setupBackButton();
		setupAccuracyRateLabel();
	}

	/**
	 * Title to tell user to choose which type of stats for the current spelling list
	 * @author Abby S
	 */
	private void setupTitle() {
		JLabel title = new JLabel("Statistics for Current List");
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setForeground(new Color(254, 157, 79));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(32, 24, 1136, 119);
		add(title);
	}

	/**
	 * Displays help pop up to aid user in which button to select for which purpose
	 * @author Abby S
	 */
	private void setupHelpButton() {
		ImageIcon help_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "help.png");
		JButton help_button = new JButton("",help_button_image);
		help_button.setBorderPainted(false);
		help_button.setBounds(1216, 24, 100, 100);
		help_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Help help_frame=new Help(PanelID.StatSelection);
				help_frame.setVisible(true);
			}
		});
		help_button.addMouseListener(new VoxMouseAdapter(help_button,null));
		add(help_button);
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
		ImageIcon persistent_all_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "allalllevels.png");
		JButton persistent_all_button = new JButton("", persistent_all_button_image);
		persistent_all_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.PersistentAllStats);
			}
		});
		persistent_all_button.addMouseListener(new VoxMouseAdapter(persistent_all_button,null));
		add(persistent_all_button);
		persistent_all_button.setBounds(153, 165, 355, 200);
	}

	/**
	 * Creates button that displays statistics data for user selected level, from all sessions
	 */
	private void setupPersistentLevelButton(){
		ImageIcon persistent_level_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "allbylevel.png");
		JButton persistent_level_button = new JButton("", persistent_level_button_image);
		persistent_level_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.PersistentLevelStats);
			}
		});
		persistent_level_button.addMouseListener(new VoxMouseAdapter(persistent_level_button,null));
		add(persistent_level_button);
		persistent_level_button.setBounds(702, 165, 355, 200);
	}

	/**
	 * Creates button that displays statistics data for all levels, only from current session
	 */
	private void setupSessionAllButton(){
		ImageIcon session_all_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "thisalllevels.png");
		JButton session_all_button = new JButton("", session_all_button_image);
		session_all_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.SessionAllStats);
			}
		});
		session_all_button.addMouseListener(new VoxMouseAdapter(session_all_button,null));
		add(session_all_button);
		session_all_button.setBounds(153, 395, 355, 200);
	}

	/**
	 * Creates button that displays statistics data for user selected level
	 * but only from current session
	 */
	private void setupSessionLevelButton(){
		ImageIcon session_level_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "thisbylevel.png");
		JButton session_level_button = new JButton("", session_level_button_image);
		session_level_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.SessionLevelStats);
			}
		});
		session_level_button.addMouseListener(new VoxMouseAdapter(session_level_button,null));
		add(session_level_button);
		session_level_button.setBounds(702, 395, 355, 200);
	}

	/**
	 * Back button to return to previous panel (main menu)
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.setBorderPainted(false);
		back_button.setContentAreaFilled(false);
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
		accuracy_rate_label.setHorizontalAlignment(SwingConstants.CENTER);
		add(accuracy_rate_label);
		accuracy_rate_label.setBounds(32, 630, 1136, 68);
		accuracy_rate_label.setOpaque(true);
	}
}
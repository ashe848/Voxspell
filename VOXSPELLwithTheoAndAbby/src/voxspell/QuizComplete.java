package voxspell;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import voxspell.Voxspell.PanelID;

@SuppressWarnings("serial")

/**
 * JPanel class displayed when user finishes the quiz or review quiz
 * Shows user table of words attempted and if they got it right or not
 * as well as how many attempts taken if they got it correct.
 * 
 * Also allows user to level up and watch reward video
 * if user has completed quiz well.
 * @author theooswanditosw164
 */
public class QuizComplete extends JPanel{
	private Voxspell parent_frame;
	private Image bg_image;

	//list of words from quiz just completed
	private static ArrayList<String> latest_mastered_words;
	private static ArrayList<String> latest_faulted_words;
	private static ArrayList<String> latest_failed_words;

	/**
	 * Constructor, initialise panel parameters and GUI elements
	 */
	public QuizComplete(Voxspell parent){
		setSize(800,600);
		setLayout(null);

		parent_frame = parent;

		latest_mastered_words=parent_frame.getDataHandler().getLatestWordResults().get(0);
		latest_faulted_words=parent_frame.getDataHandler().getLatestWordResults().get(1);
		latest_failed_words=parent_frame.getDataHandler().getLatestWordResults().get(2);

		setupTable();
		determineButtons();
		setupBackButton();
		setupAccuracyRateLabel();
		setupBackground();
	}

	/**
	 * Table with summary of words quizzed
	 * Based on Abby's A2 code
	 */
	private void setupTable() {
		//sets up empty table
		String[] column_names = {};
		int row_count = 0;
		DefaultTableModel model = new DefaultTableModel(column_names, row_count){
			public boolean isCellEditable(int row, int col) {
				return false; //so users can't change their stats
			}

			//types of the columns for correct ordering
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				default:
					return Integer.class;
				}
			}
		};
		JTable table = new JTable(model);

		//add columns to table with count in column names
		Object[] mastered_col=new Object[latest_mastered_words.size()];
		model.addColumn("Strike! ("+latest_mastered_words.size()+")", latest_mastered_words.toArray(mastered_col));
		Object[] faulted_col=new Object[latest_mastered_words.size()];
		model.addColumn("Spare! ("+latest_faulted_words.size()+")", latest_faulted_words.toArray(faulted_col));
		Object[] failed_col=new Object[latest_mastered_words.size()];
		model.addColumn("Didn't get it ("+latest_failed_words.size()+")", latest_failed_words.toArray(failed_col));

		table.setModel(model);

		//Disallow reording of columns
		table.getTableHeader().setReorderingAllowed(false);

		//Alignment for the cells http://stackoverflow.com/a/7433758
		DefaultTableCellRenderer alignment_renderer = new DefaultTableCellRenderer();
		alignment_renderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(String.class, alignment_renderer);
		table.setDefaultRenderer(Integer.class, alignment_renderer);

		//adds scroll pane to table to panel
		JScrollPane scroll_pane = new JScrollPane(table);
		add(scroll_pane);
		scroll_pane.setVisible(true);
		scroll_pane.setBounds(50, 150, 450, 300);
	}

	/**
	 * Determines, based on result of quiz, which buttons to display
	 */
	private void determineButtons(){
		//at most 1 incorrect
		if(latest_failed_words.size()<2) {
			setupVideoButton();
			
			//whether user has already levelled up before playing video
			if(!parent_frame.getDataHandler().getLevelledUp()){
				setupLevelUpButton();//set up when just 1 failed just for assignment 3 purposes to go with specs
			} else {
				setupLevelledUpLabel("");
			}
		}

		/* GOOD IDEA FOR FINAL PROJECT, BUT MAY BE GOING AGAINST A3 SPECS SO COMMENTED OUT ON NASSER'S RECOMMENDATION

		//complete level when 50% attempted with no fails
		if (parent_frame.getDataHandler().halfAttempted() && parent_frame.getgetDataHandler().noReview() && parent_frame.getgetDataHandler().getCurrentLevel()<parent_frame.getgetDataHandler().getNumberOfLevels()){
			setupLevelUpButton();
		}
		 */
	}

	/**
	 * Button to move up a level
	 */
	private void setupLevelUpButton() {
		ImageIcon levelup_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "levelup_button.png");
		final JButton level_up_button = new JButton("", levelup_button_image);

		level_up_button.setBounds(550, 39, 200, 200);
		level_up_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//if not on highest level
				if (parent_frame.getDataHandler().getCurrentLevel()<parent_frame.getDataHandler().getNumberOfLevels()-1){
					parent_frame.getDataHandler().increaseLevel();
					parent_frame.getDataHandler().setLevelledUp(true);
					setupLevelledUpLabel("up ");
				} else {
					//on highest level, so prompt user to choose which level they want to go to
					//if user made a choice, then create label behind button
					if(parent_frame.getDataHandler().chooseLevel("All levels completed!\n", true)){
						parent_frame.getDataHandler().setLevelledUp(true);
						setupLevelledUpLabel("");
					}
				}
				level_up_button.setVisible(false);
			}
		});
		add(level_up_button);
		level_up_button.setVisible(true);
	}

	/**
	 * label created once user clicks level up. Notifies them that they have leveled up
	 * to a new level.
	 */
	private void setupLevelledUpLabel(String direction) {
		JLabel level_up_button = new JLabel("Moved " + direction + "to level "+parent_frame.getDataHandler().getCurrentLevel(), JLabel.CENTER);

		level_up_button.setBounds(550, 39, 200, 200);
		level_up_button.setForeground(Color.YELLOW);
		add(level_up_button);
		level_up_button.setVisible(true);
	}

	/**
	 * Button to play video
	 */
	private void setupVideoButton() {
		ImageIcon videoreward_image = new ImageIcon(parent_frame.getResourceFileLocation() + "video_reward_button.png");
		JButton video_button = new JButton("", videoreward_image);		

		video_button.setBounds(550, 268, 200, 200);
		video_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.Video);
			}
		});
		add(video_button);		
	}

	/**
	 * Back button to return to previous panel
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//resets flag for level up
				parent_frame.getDataHandler().setLevelledUp(false);
				parent_frame.changePanel(PanelID.MainMenu);
			}
		});

		add(back_button);
		back_button.setSize(50,50);
		back_button.setLocation(700,500);
	}

	/**
	 * To display accuracy rates for level user is currently on
	 */
	private void setupAccuracyRateLabel() {
		JLabel accuracy_rate_label = new JLabel(parent_frame.getDataHandler().getAccuracyRates()); 
		accuracy_rate_label.setFont(new Font("Courier New", Font.BOLD, 10));

		add(accuracy_rate_label);
		accuracy_rate_label.setLocation(50, 500);
		accuracy_rate_label.setSize(300, 50);
		accuracy_rate_label.setOpaque(true);
	}

	/**
	 * Puts the background image, overriding paintComponent method(below) to ensure functionality
	 */
	private void setupBackground(){
		//http://stackoverflow.com/questions/1466240/how-to-set-an-image-as-a-background-for-frame-in-swing-gui-of-java
		try {
			bg_image = ImageIO.read(new File(parent_frame.getResourceFileLocation() + "quiz_complete_bg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setLocation(0,0);
		setSize(800, 600);
	}

	/**
	 * Overriding the paintComponent method to place background
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bg_image, 0, 0, this);
	}
}
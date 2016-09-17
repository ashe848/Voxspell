package voxspell;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

public class QuizComplete extends JPanel{
	private Voxspell parent_frame;

	//list of words from quiz just completed
	private static ArrayList<String> latest_mastered_words;
	private static ArrayList<String> latest_faulted_words;
	private static ArrayList<String> latest_failed_words;

	/**
	 * Constructor
	 */
	public QuizComplete(Voxspell parent){
		setSize(800,600);
		setLayout(null);

		parent_frame = parent;

		latest_mastered_words=parent_frame.getDataHandler().getLatestWordResults().get(0);
		latest_faulted_words=parent_frame.getDataHandler().getLatestWordResults().get(1);
		latest_failed_words=parent_frame.getDataHandler().getLatestWordResults().get(2);

		setupTitle();
		setupTable();
		determineButtons();
		setupBackButton();
		//		TODO
		//		parent_frame.component_maker.setupBackButton(this, PanelID.MainMenu);
		setupAccuracyRateLabel();
	}

	/**
	 * Title
	 */
	private void setupTitle() {
		JLabel quiz_complete_label = new JLabel("Quiz Completed");
		quiz_complete_label.setFont(new Font("Courier New", Font.BOLD, 50));
		quiz_complete_label.setBounds(38, 39, 469, 87);
		add(quiz_complete_label);
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
		model.addColumn("Mastered ("+latest_mastered_words.size()+")", latest_mastered_words.toArray(mastered_col));
		Object[] faulted_col=new Object[latest_mastered_words.size()];
		model.addColumn("Faulted ("+latest_faulted_words.size()+")", latest_faulted_words.toArray(faulted_col));
		Object[] failed_col=new Object[latest_mastered_words.size()];
		model.addColumn("Failed ("+latest_failed_words.size()+")", latest_failed_words.toArray(failed_col));

		table.setModel(model);

		//Alignment for the cells http://stackoverflow.com/a/7433758
		DefaultTableCellRenderer alignment_renderer = new DefaultTableCellRenderer();
		alignment_renderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(String.class, alignment_renderer);
		table.setDefaultRenderer(Integer.class, alignment_renderer);

		//adds scroll pane to table to panel
		JScrollPane scroll_pane = new JScrollPane(table);
		add(scroll_pane);
		scroll_pane.setVisible(true);
		scroll_pane.setBounds(38, 168, 469, 382);
	}

	/**
	 * Determines, based on result of quiz, which buttons to display
	 */
	private void determineButtons(){
		//at most 1 incorrect
		if(latest_failed_words.size()<2) {
			setupVideoButton();
			setupLevelUpButton();//just for assignment 3 purposes to go with specs
		}

		/* GOOD IDEA FOR FINAL PROJECT, BUT MAY BE GOING AGAINST A3 SPECS SO COMMENTED OUT

		//complete level when 50% attempted with no fails
		if (parent_frame.getFileIO().halfAttempted() && parent_frame.getFileIO().noReview() && parent_frame.getFileIO().getCurrentLevel()<parent_frame.getFileIO().getNumberOfLevels()){
			setupLevelUpButton();
		}
		 */
	}

	/**
	 * Button to move up a level
	 */
	private void setupLevelUpButton() {
		JButton level_up_button = new JButton("LEVEL UP");
		level_up_button.setBounds(550, 39, 200, 200);
		level_up_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//if not on highest level, increase level
				if (parent_frame.getDataHandler().getCurrentLevel()<parent_frame.getDataHandler().getNumberOfLevels()-1){
					parent_frame.getDataHandler().increaseLevel();
				} else {
					//prompt user to choose which level they want to go to
					parent_frame.getDataHandler().chooseLevel("All levels completed!");
				}
				level_up_button.setVisible(false);
			}
		});
		add(level_up_button);
		level_up_button.setVisible(true);
	}

	/**
	 * Button to play video
	 */
	private void setupVideoButton() {
		JButton video_button = new JButton("PLAY VIDEO");
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
		accuracy_rate_label.setLocation(50, 530);
		accuracy_rate_label.setSize(400, 30);
		accuracy_rate_label.setOpaque(true);
	}
}
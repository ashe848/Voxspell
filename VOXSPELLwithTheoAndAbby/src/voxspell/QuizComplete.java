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
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
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
	private Clip clip;
	

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

		setupAudio();
		setupTable();
		determineDisplay();
		setupBackButton();
		setupAccuracyRateLabel();
		setupBackground();
	}

	/**
	 * @author http://stackoverflow.com/a/11025384
	 */
	private void setupAudio() {
		String audio=parent_frame.getResourceFileLocation()+"11k16bitpcm.wav";
		try {
		    File yourFile=new File(audio);
		    AudioInputStream stream;
		    AudioFormat format;
		    DataLine.Info info;

		    stream = AudioSystem.getAudioInputStream(yourFile);
		    format = stream.getFormat();
		    info = new DataLine.Info(Clip.class, format);
		    clip = (Clip) AudioSystem.getLine(info);
		    clip.open(stream);
		    clip.start();
		}
		catch (Exception e) {
		   //do nothing
		}
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
	private void determineDisplay(){
		//at most 10% incorrect (truncated to whole number)
		if(latest_failed_words.size()<=(int)(0.1*parent_frame.getDataHandler().latest_quiz_length)) {
			setupVideoButton();
			
			//whether user has already levelled up before playing video
			if(parent_frame.getDataHandler().getNumberOfLevels()-1==1) {
				//do nothing. Only 1 level.
			} else if(!parent_frame.getDataHandler().getLevelledUp()){
				setupLevelUpButton();//set up when just 1 failed just for assignment 3 purposes to go with specs
			} else {
				setupLevelledUpLabel("");
			}
		}
		
		//3 points for mastered, 1 point for faulted. None for failed. As a percentage
		double score=(double)(latest_mastered_words.size()*3 + latest_faulted_words.size())/parent_frame.getDataHandler().latest_quiz_length;
		if(score > parent_frame.getDataHandler().personal_best){
			setupNewPB(score);
			parent_frame.getDataHandler().personal_best=score;
			
			double global_top_score=Double.parseDouble(parent_frame.getDataHandler().global_top.split("\\s+")[0]);
			if (score > global_top_score){
				parent_frame.getDataHandler().global_top=score+" "+parent_frame.getDataHandler().user+" "+parent_frame.getDataHandler().spelling_list_name;
				setupNewGlobalTop(score,global_top_score);
			} else {
				setupJustNewPB(score,parent_frame.getDataHandler().global_top.split("\\s+"));
			}
		}
	}

	/**
	 * Button to move up a level
	 */
	private void setupLevelUpButton() {
		ImageIcon levelup_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "levelup_button.png");
		final JButton level_up_button = new JButton("", levelup_button_image);

		level_up_button.setBounds(550, 213, 200, 114);
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
		JLabel level_up_button = new JLabel("Moved " + direction + "to "+parent_frame.getDataHandler().level_names.get(parent_frame.getDataHandler().getCurrentLevel()), JLabel.CENTER);

		level_up_button.setBounds(550, 213, 200, 114);
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

		video_button.setBounds(550, 354, 200, 114);
		video_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clip.close();
				parent_frame.changePanel(PanelID.Video);
			}
		});
		add(video_button);		
	}
	
	/**
	 * @author Abby S
	 */
	private void setupNewPB(double record_score){
		JLabel lblNewPersonalBest = new JLabel("New personal best score: "+record_score+"!");
		lblNewPersonalBest.setBounds(550, 119, 200, 15);
		add(lblNewPersonalBest);
	}
	
	/**
	 * @author Abby S
	 */
	private void setupJustNewPB(double record_score, String[] global){		
		JLabel label = new JLabel("Didn't beat global top of "+global[0]+" by "+global[1]+" in "+global[2]);
		label.setBounds(550, 150, 200, 15);
		add(label);
	}
	
	/**
	 * @author Abby S
	 */
	private void setupNewGlobalTop(double record_score, double global) {
			JLabel label = new JLabel("Bet previous global top by "+(record_score-global)+"!");
			label.setBounds(550, 150, 2000, 15);
			add(label);
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
				clip.close();
				parent_frame.getDataHandler().writeToProgramFiles();
				parent_frame.getDataHandler().writeToSettingsFiles();
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
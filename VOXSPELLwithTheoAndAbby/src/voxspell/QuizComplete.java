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
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import voxspell.Voxspell.PanelID;

@SuppressWarnings({ "static-access", "serial", "unchecked", "rawtypes" })

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

	private Clip clip;

	//list of words from quiz just completed
	private ArrayList<String> latest_mastered_words;
	private ArrayList<String> latest_faulted_words;
	private ArrayList<String> latest_failed_words;

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

		setupTitle();
		setupAudio();
		setupTable();
		determineDisplay();
		setupBackButton();
		setupAccuracyRateLabel();
//		setupBackground();
	}

	private void setupTitle() {
		JLabel title = new JLabel("Quiz Complete");
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setForeground(new Color(254, 157, 79));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(32, 24, 1136, 119);
		add(title);
	}

	/**
	 * Modified from http://stackoverflow.com/a/11025384
	 */
	private void setupAudio() {
		/*
		 * Smile (For A Bit) by The Orchestral Movement of 1932 (c) copyright 2009 Licensed under a Creative Commons Attribution (3.0) license. http://dig.ccmixter.org/files/jacksontorreal/22341 Ft: Fourstones
		 * Free to use in commercial projects.
		 * 
		 * Found from http://beta.ccmixter.org/playlist/browse/40708?offset=10
		 * 
		 * Author comment:
		 * Smile for a bit, because everyone deserves to.
		 * The second mix that I made for submission. This song is more my poppy, upbeat, sound. If you¡¯re wondering, the title comes from a line from a movie wherein one character was urged to smile more. 
		 * I don¡¯t know why but I found that moment stuck with me. In any case, please enjoy.
		 */
		File audio_file=new File(parent_frame.getResourceFileLocation()+"quiz_complete_audio.wav");

		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(audio_file);
			AudioFormat format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
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

		table.setFont(new Font("Calibri Light", Font.PLAIN, 20));
		table.setRowHeight(27);
		
		//adds scroll pane to table to panel
		JScrollPane scroll_pane = new JScrollPane(table);
		add(scroll_pane);
		scroll_pane.setVisible(true);
		scroll_pane.setBounds(32, 169, 585, 435);
	}

	/**
	 * Determines, based on result of quiz, which buttons to display
	 * @author Abby S
	 */
	private void determineDisplay(){
		//at most 10% incorrect (truncated to whole number)
		if(latest_failed_words.size()<=(int)(0.1*parent_frame.getDataHandler().getLatestQuizLength())) {
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

		//3 points for mastered, 1 point for faulted. None for failed. Multiplied by level number. As a percentage
		double score=parent_frame.getDataHandler().getCurrentLevel()*((double)(latest_mastered_words.size()*3 + latest_faulted_words.size()))/parent_frame.getDataHandler().getLatestQuizLength();
		if(score > parent_frame.getDataHandler().getPersonalBest()){
			parent_frame.getDataHandler().setPersonalBest(score);

			double global_top_score=Double.parseDouble(parent_frame.getDataHandler().getGlobalTop().split("\\s+")[0]);
			if (score > global_top_score){
				setupNewPB(score,true);
				parent_frame.getDataHandler().setGlobalTop(score+" "+parent_frame.getDataHandler().getUser()+" "+parent_frame.getDataHandler().getSpellingListName());				
			} else {
				setupNewPB(score,false);
			}
		}
	}
	
	/**
	 * Button to move up a level
	 */
	private void setupLevelUpButton() {
		ImageIcon levelup_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "levelup_button.png");
		final JButton level_up_button = new JButton("", levelup_button_image);

		level_up_button.setBounds(648, 404, 354, 200);
		level_up_button.addActionListener(new ActionListener() {
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
		level_up_button.addMouseListener(new VoxMouseAdapter(level_up_button,null));
		add(level_up_button);
		level_up_button.setVisible(true);
	}

	/**
	 * label created once user clicks level up. Notifies them that they have leveled up
	 * to a new level.
	 */
	private void setupLevelledUpLabel(String direction) {
		JLabel levelled_up_label= new JLabel("Moved " + direction + "to "+parent_frame.getDataHandler().getLevelNames().get(parent_frame.getDataHandler().getCurrentLevel()), JLabel.CENTER);

		levelled_up_label.setBounds(648, 404, 354, 200);
		levelled_up_label.setFont(new Font("Arial", Font.PLAIN, 20));
		levelled_up_label.setForeground(new Color(254, 157, 79));
		add(levelled_up_label);
		levelled_up_label.setVisible(true);
	}

	/**
	 * Button to play video
	 */
	private void setupVideoButton() {
		ImageIcon videoreward_image = new ImageIcon(parent_frame.getResourceFileLocation() + "video_reward_button.png");
		JButton video_button = new JButton("", videoreward_image);		

		video_button.setBounds(648, 169, 354, 200);
		video_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clip.close();
				parent_frame.changePanel(PanelID.Video);
			}
		});
		video_button.addMouseListener(new VoxMouseAdapter(video_button,null));
		add(video_button);		
	}

	/**
	 * @author Abby S
	 */
	private void setupNewPB(double pb_score, boolean bet_global){
		JTextArea score_results = new JTextArea();
		score_results.setLineWrap(true);
		score_results.setEditable(false);
		score_results.setWrapStyleWord(true);
		score_results.setFont(new Font("Arial", Font.PLAIN, 24));
		score_results.setOpaque(false);
		score_results.setText("New personal best score: \n"+pb_score+"\n\n\n");
		score_results.setBounds(1028, 232, 288, 347);
		add(score_results);	
		
		String[] global = parent_frame.getDataHandler().getGlobalTop().split("\\s+");
		if(bet_global){
			score_results.append("Also bet previous global top by "+(pb_score - Double.parseDouble(global[0]))+"!");
		} else {
			score_results.append("Didn't beat global top of "+global[0]+"\n\nby "+global[1]+"\n\nin "+global[2]);
		}
	}

	/**
	 * Back button to return to previous panel
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clip.close();
				parent_frame.getDataHandler().writeToProgramFiles();
				parent_frame.getDataHandler().writeToSettingsFiles();
				//resets flag for level up
				parent_frame.getDataHandler().setLevelledUp(false);
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
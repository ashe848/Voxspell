package voxspell;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import voxspell.Festival.FestivalSpeed;
import voxspell.Festival.FestivalVoice;
import voxspell.Voxspell.PanelID;

@SuppressWarnings({ "static-access", "serial", "unchecked", "rawtypes" })

/**
 * Settings JPanel class
 * Allows user to change festival voice and speed
 * Also allows user to reset all data (settings & stats)
 */

public class Settings extends JPanel {
	private Voxspell parent_frame;
	private Image bg_image;

	//temporary values selected by user. Only saved if user confirms
	private FestivalVoice temp_voice_selection=null;
	private FestivalSpeed temp_speed_selection=null;
	private Integer temp_word_selection=null;
	private String temp_level_selection=null;
	private String temp_list_selection=null;
	private String temp_video_selection=null;

	/**
	 * Constructor, initialise panel properties and GUI elements
	 */
	public Settings(Voxspell parent){
		setSize(800,600);
		setLayout(null);

		parent_frame = parent;
		
		JLabel reset_buttons = new JLabel("Other settings may not be saved:");
		reset_buttons.setBounds(335, 232, 254, 15);
		add(reset_buttons);
		seupResetListStats();
		seupResetUser();
		seupResetToDefaultSettings();

		setupChangeVoice();
		setupChangeSpeed();
		setupWordInQuiz();
		setupChooseLevel();
		setupChooseWordList();
		setupChooseRewardVideo();
		setupBackButton();
		setupBackground();
	}

	/**
	 * Resets all stats to as if it was the user's first launch (prompts user for confirmation)
	 */
	private void seupResetListStats() {
		JButton reset_list_stats_button = new JButton("Reset Stats for Current List");
		reset_list_stats_button.setBounds(335, 257, 254, 23);
		add(reset_list_stats_button);
		reset_list_stats_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean ask_reset_result = askForConfirmation("Are you sure you want to reset Stats for "+parent_frame.getDataHandler().spelling_list_name+"?", "Reset Stats for Current List");
				if (ask_reset_result){
					parent_frame.getDataHandler().resetListStats();
				}
			}
		});
	}

	/**
	 * @author Abby S
	 */
	private void seupResetUser() {
		JButton clear_all_button = new JButton("Clear All My Data");
		clear_all_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean ask_clear_result = askForConfirmation("Are you sure you want to reset all your Data?\nWill be logged into Visitor", "Reset User Stats");
				if (ask_clear_result){
					try {
						parent_frame.getDataHandler().resetUser();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		clear_all_button.setBounds(335, 326, 254, 23);
		add(clear_all_button);
	}

	/**
	 * @author Abby S
	 */
	private void seupResetToDefaultSettings() {
		JButton reset_to_default_button = new JButton("Reset My Settings data Back to Defaults");
		reset_to_default_button.setBounds(335, 293, 254, 23);
		reset_to_default_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean ask_result = askForConfirmation("Are you sure you want to reset your Settings data Back to Defaults?\nList-specific data will be retained.", "Reset Settings Back to Defaults");
				if (ask_result){
					parent_frame.getDataHandler().resetToDefaults();
				}
			}
		});
		add(reset_to_default_button);
	}

	/**
	 * Drop down to change festival voice
	 */
	private void setupChangeVoice() {
		JLabel change_voice_label = new JLabel("Change voice (you can change this during the quiz as well)");
		change_voice_label.setBounds(31, 209, 359, 15);
		change_voice_label.setForeground(Color.YELLOW);
		add(change_voice_label);

		FestivalVoice[] voices={FestivalVoice.American, FestivalVoice.Kiwi};
		final JComboBox voice_chooser = new JComboBox(voices);
		voice_chooser.setForeground(Color.BLACK);
		voice_chooser.setBackground(Color.WHITE);

		//set shown item to be the current voice
		voice_chooser.setSelectedItem(parent_frame.getFestival().getFestivalVoice());
		voice_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				temp_voice_selection=(FestivalVoice)voice_chooser.getSelectedItem();
			}
		});

		voice_chooser.setBounds(31, 234, 166, 40);
		add(voice_chooser);
	}

	/**
	 * Drop down to change festival speed
	 */
	private void setupChangeSpeed() {
		JLabel change_speed_label = new JLabel("Change speed");
		change_speed_label.setBounds(31, 330, 166, 15);
		change_speed_label.setForeground(Color.YELLOW);
		add(change_speed_label);

		FestivalSpeed[] speeds={FestivalSpeed.slow, FestivalSpeed.normal, FestivalSpeed.fast};
		final JComboBox speed_chooser = new JComboBox(speeds);
		speed_chooser.setForeground(Color.BLACK);
		speed_chooser.setBackground(Color.WHITE);

		//set shown item to be the current voice
		speed_chooser.setSelectedItem(parent_frame.getFestival().getFestivalSpeed());
		speed_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				temp_speed_selection=(FestivalSpeed)speed_chooser.getSelectedItem();
			}
		});

		speed_chooser.setBounds(31, 355, 166, 40);
		add(speed_chooser);
	}

	/**
	 * @author Abby S
	 */
	private void setupWordInQuiz(){
		JLabel change_words_in_quiz_label = new JLabel("Change preferred number of words in quiz");
		change_words_in_quiz_label.setForeground(Color.YELLOW);
		change_words_in_quiz_label.setBounds(31, 416, 254, 15);
		add(change_words_in_quiz_label);

		//		TODO: remove 1 after testing
		Integer[] word_numbers={1, 5, 10, 15, 25, 50};
		final JComboBox word_number_chooser = new JComboBox(word_numbers);
		word_number_chooser.setForeground(Color.BLACK);
		word_number_chooser.setBackground(Color.WHITE);

		//set shown item to be the current voice
		word_number_chooser.setSelectedItem(parent_frame.getDataHandler().words_in_quiz);
		word_number_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				temp_word_selection=(Integer) word_number_chooser.getSelectedItem();
			}
		});

		word_number_chooser.setBounds(31, 443, 166, 40);
		add(word_number_chooser);
	}

	/**
	 * @author Abby S
	 */
	private void setupChooseLevel() {
		JLabel choose_level_label = new JLabel("Choose Level (for current list)");
		choose_level_label.setForeground(Color.YELLOW);
		choose_level_label.setBounds(335, 418, 166, 15);
		add(choose_level_label);

		String[] levels = parent_frame.getDataHandler().getLevelArray();
		//only shows levels up to and including current level
		final JComboBox level_chooser = new JComboBox(Arrays.copyOf(levels, parent_frame.getDataHandler().current_level));

		//default to current level
		level_chooser.setSelectedItem(parent_frame.getDataHandler().level_names.get(parent_frame.getDataHandler().current_level));
		level_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				temp_level_selection = (String)level_chooser.getSelectedItem();
			}
		});
		level_chooser.setForeground(Color.BLACK);
		level_chooser.setBackground(Color.WHITE);
		level_chooser.setBounds(335, 443, 166, 40);
		add(level_chooser);
	}

	/**
	 * @author Abby S
	 */
	private void setupChooseWordList(){
		JLabel choose_wordlist_label = new JLabel("Current word list: "+parent_frame.getDataHandler().spelling_list_name);
		choose_wordlist_label.setForeground(Color.YELLOW);
		choose_wordlist_label.setBounds(31, 500, 254, 15);
		add(choose_wordlist_label);

		JLabel will_change_to=new JLabel("");
		add(will_change_to);
		
		JButton list_choose_button = new JButton("Choose another list");
		list_choose_button.setBounds(31, 527, 93, 23);
		list_choose_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				File spelling_lists_folder = new File(System.getProperty("user.dir")+"/spellinglists/");
				FileSystemView fsv = new SingleRootFileSystemView(spelling_lists_folder);

				JFileChooser chooser = new JFileChooser(spelling_lists_folder, fsv);
				//expected format is a .txt file. But if it is another form of text file that can be read to have the correct format, the application will accept it.
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Plain text files", "txt");
				chooser.setFileFilter(filter);
				
				int button_clicked = chooser.showDialog(parent_frame, "Choose this word list");
				if(button_clicked == JFileChooser.APPROVE_OPTION) {
					if (!parent_frame.getDataHandler().errorCheckSelectedFile(chooser.getSelectedFile())){
						JOptionPane.showMessageDialog(null, "Chosen list is not in correct format\nPlease choose another list", "List Format Error", JOptionPane.WARNING_MESSAGE);
					} else {
						temp_list_selection=chooser.getSelectedFile().getName();
						will_change_to.setText("Will change to "+temp_list_selection+" on save.");
						will_change_to.setForeground(Color.YELLOW);
						will_change_to.setBounds(150, 531, 254, 15);
					}
				}
			}
		});
		add(list_choose_button);
	}

	/**
	 * @author Abby S
	 */
	private void setupChooseRewardVideo(){
		JLabel choose_video_label = new JLabel("Current Reward Video: "+parent_frame.getDataHandler().video_name);
		choose_video_label.setForeground(Color.YELLOW);
		choose_video_label.setBounds(31, 171, 254, 15);
		add(choose_video_label);

		JLabel will_change_to=new JLabel("");
		add(will_change_to);
		
		JButton choose_video_button = new JButton("Choose another video");
		choose_video_button.setBounds(327, 167, 93, 23);
		choose_video_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				File reward_videos_folder = new File(System.getProperty("user.dir")+"/rewardvideos/");
				FileSystemView fsv = new SingleRootFileSystemView(reward_videos_folder);

				JFileChooser chooser = new JFileChooser(reward_videos_folder, fsv);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("AVI files", "avi");
				chooser.setFileFilter(filter);
				
				int button_clicked = chooser.showDialog(parent_frame, "Choose this video");
				if(button_clicked == JFileChooser.APPROVE_OPTION) {
					if (chooser.getSelectedFile().getName().contains(" ")){
						JOptionPane.showMessageDialog(null, "Chosen video has space(s) in its name\nPlease rename", "Filename Format Error", JOptionPane.WARNING_MESSAGE);
					} else {
						temp_video_selection=chooser.getSelectedFile().getName();
						will_change_to.setText("Will change to "+temp_video_selection+" on save.");
						will_change_to.setForeground(Color.YELLOW);
						will_change_to.setBounds(446, 171, 254, 15);
					}
				}
			}
		});
		add(choose_video_button);
	}

	/**
	 * Back button to return to previous panel (user prompted to save before actually doing so)
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean ask_save_result = askForConfirmation("Would you like to Save?", "Save Settings");
				if (ask_save_result){
					/* if user didn't touch the drop downs, i.e. they don't want to make a change,
					 * the temporary selection values will be null
					 */
					if(temp_voice_selection!=null){
						parent_frame.getFestival().setFestivalVoice(temp_voice_selection);
					}
					if(temp_speed_selection!=null){
						parent_frame.getFestival().setFestivalSpeed(temp_speed_selection);
					}
					if(temp_word_selection!=null){
						parent_frame.getDataHandler().words_in_quiz=temp_word_selection;
					}
					if(temp_level_selection!=null){
						parent_frame.getDataHandler().current_level=parent_frame.getDataHandler().level_names.indexOf(temp_level_selection);
					}
					if(temp_video_selection!=null){
						parent_frame.getDataHandler().video_name=temp_video_selection;
					}
					parent_frame.getDataHandler().writeToSettingsFiles();

					if(temp_list_selection!=null){
						parent_frame.getDataHandler().spelling_list_name=temp_list_selection;
						parent_frame.getDataHandler().readListSpecificFiles();
						parent_frame.getDataHandler().writeToSettingsFiles();
					}
				}
				parent_frame.changePanel(PanelID.MainMenu); //else doesn't save
			}
		});

		add(back_button);
		back_button.setSize(50,50);
		back_button.setLocation(700,500);
	}

	/**
	 * Pop up to confirm that user wants to commit to their selection
	 * http://stackoverflow.com/questions/8689122/joptionpane-yes-no-options-confirm-dialog-box-issue-java
	 * @param message body of dialog box
	 * @param title	title of dialog box
	 * @return
	 */
	private boolean askForConfirmation(String message, String title){
		int ask_prompt = JOptionPane.YES_NO_OPTION;
		int ask_result = JOptionPane.showConfirmDialog(this, message, title, ask_prompt);
		if (ask_result == JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}

	/**
	 * Puts the background image, overriding paintComponent method(below) to ensure functionality
	 */
	private void setupBackground(){
		//http://stackoverflow.com/questions/1466240/how-to-set-an-image-as-a-background-for-frame-in-swing-gui-of-java
		try {
			bg_image = ImageIO.read(new File(parent_frame.getResourceFileLocation() + "settings_bg.png"));
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

	/**
	 * 	Slightly modified from:	
	 * 	https://tips4java.wordpress.com/2009/01/28/single-root-file-chooser/
	 * 	http://www.camick.com/java/source/SingleRootFileSystemView.java
	 * 
	 * A FileSystemView class that limits the file selections to a single root.
	 *
	 * When used with the JFileChooser component the user will only be able to
	 * traverse the directories contained within the specified root fill.
	 *
	 * The "Look In" combo box will only display the specified root.
	 *
	 * The "Up One Level" button will be disabled
	 * 
	 */
	class SingleRootFileSystemView extends FileSystemView{
		File root;
		File[] roots = new File[1];

		public SingleRootFileSystemView(File path){
			super();

			try{
				root = path.getCanonicalFile();
				roots[0] = root;
			}catch(IOException e){
				throw new IllegalArgumentException( e );
			}

			if ( !root.isDirectory() ) {
				String message = root + " is not a directory";
				throw new IllegalArgumentException( message );
			}
		}

		//Disable creating new folders. The icon will do nothing (as with the home icon)
		public File createNewFolder(File containingDir){
			return null;
		}

		public File getDefaultDirectory(){
			return root;
		}

		public File getHomeDirectory(){
			return root;
		}

		public File[] getRoots(){
			return roots;
		}
	}
}
package visiblegui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import backendio.Festival.FestivalSpeed;
import backendio.Festival.FestivalVoice;
import vox.VoxButton;
import vox.VoxMouseAdapter;
import vox.Voxspell;
import vox.Voxspell.PanelID;

@SuppressWarnings({ "static-access", "serial", "unchecked", "rawtypes" })

/**
 * Settings JPanel class
 * Allows user to modify settings and reset some data
 */
public class Settings extends JPanel {
	private Voxspell parent_frame;

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
		setSize(1366,745);
		setLayout(null);
		setBackground(new Color(235, 235, 235));

		parent_frame = parent;

		setupTitle();
		seupResetListStats();
		seupResetUser();
		seupResetToDefaultSettings();
		setupWarningLabel();

		setupChangeVoice();
		setupChangeSpeed();
		setupWordInQuiz();
		setupChooseLevel();
		setupChooseWordListLabel();
		setupChooseWordList();
		setupChooseRewardVideoLabel();
		setupChooseRewardVideo();
		setupBackButton();
	}

	/**
	 * Displays "Settings" title
	 * @author Abby S
	 */
	private void setupTitle() {
		JLabel title = new JLabel("Settings");
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setForeground(new Color(254, 157, 79));
		title.setBounds(32, 24, 1284, 119);
		add(title);
	}

	/**
	 * Resets all stats to as if it was the user's first time for this list (prompts user for confirmation)
	 * @author Abby S
	 */
	private void seupResetListStats() {
		VoxButton reset_list_stats_button = new VoxButton("Clear stats for current list");
		reset_list_stats_button.setBounds(523, 462, 319, 50);
		add(reset_list_stats_button);
		reset_list_stats_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean ask_reset_result = askForConfirmation("Are you sure you want to reset Stats for "+parent_frame.getDataHandler().getSpellingListName()+"?", "Reset Stats for Current List");
				if (ask_reset_result){
					parent_frame.getDataHandler().resetListStats();
				}
			}
		});
		reset_list_stats_button.setBackground(Color.RED);
		reset_list_stats_button.changeMouseEventColor(Color.BLACK);
	}

	/**
	 * Resets non-list-specific settings for that user back to defaults (prompts for confirmation)
	 * @author Abby S
	 */
	private void seupResetToDefaultSettings() {
		VoxButton reset_to_default_button = new VoxButton("Reset settings data to defaults");
		reset_to_default_button.setBounds(523, 533, 319, 50);
		reset_to_default_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean ask_result = askForConfirmation("Are you sure you want to reset your Settings data Back to Defaults?\nList-specific data will be retained.", "Reset Settings Back to Defaults");
				if (ask_result){
					parent_frame.getDataHandler().resetToDefaults();
				}
			}
		});
		add(reset_to_default_button);
		reset_to_default_button.setBackground(Color.RED);
		reset_to_default_button.changeMouseEventColor(Color.BLACK);
	}

	/**
	 * Resets to as if this user never existed (prompts user for confirmation)
	 * Goes back to the default Visitor login
	 * @author Abby S
	 */
	private void seupResetUser() {
		VoxButton reset_user_button = new VoxButton("Clear all my data");
		reset_user_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean ask_clear_result = askForConfirmation("Are you sure you want to reset all your Data?\nWill be logged into Visitor", "Reset User Stats");
				if (ask_clear_result){
					parent_frame.getDataHandler().resetUser();
				}
			}
		});
		reset_user_button.setBounds(523, 605, 319, 50);
		add(reset_user_button);
		reset_user_button.setBackground(Color.RED);
		reset_user_button.changeMouseEventColor(Color.BLACK);
	}

	/**
	 * warns user that other settings will not be saved due to the nature of the back button prompting for save or discard changes
	 * @author Abby S
	 */
	private void setupWarningLabel() {
		JLabel warning_label = new JLabel("^ other settings will not be saved ^");
		warning_label.setForeground(Color.RED);
		warning_label.setFont(new Font("Arial", Font.PLAIN, 17));
		warning_label.setHorizontalAlignment(SwingConstants.CENTER);
		warning_label.setBounds(523, 668, 319, 30);
		add(warning_label);
	}

	/**
	 * Drop down to change festival voice
	 */
	private void setupChangeVoice() {
		JLabel change_voice_label = new JLabel("Change voice of audio prompt in quiz");
		change_voice_label.setBounds(32, 169, 580, 30);
		change_voice_label.setFont(new Font("Arial", Font.PLAIN, 20));
		change_voice_label.setForeground(Color.BLACK);
		add(change_voice_label);

		//Kiwi, British and American are the available voices
		FestivalVoice[] voices={FestivalVoice.Kiwi, FestivalVoice.British, FestivalVoice.American};
		final JComboBox voice_chooser = new JComboBox(voices);
		voice_chooser.setFont(new Font("Arial", Font.PLAIN, 20));
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
		voice_chooser.setBounds(32, 209, 210, 50);
		add(voice_chooser);
	}

	/**
	 * Drop down to change festival speed
	 */
	private void setupChangeSpeed() {
		JLabel change_speed_label = new JLabel("Change speed of audio prompt in quiz");
		change_speed_label.setBounds(32, 294, 580, 30);
		change_speed_label.setFont(new Font("Arial", Font.PLAIN, 20));
		change_speed_label.setForeground(Color.BLACK);
		add(change_speed_label);

		//Just the simple slow/normal/fast
		FestivalSpeed[] speeds={FestivalSpeed.slow, FestivalSpeed.normal, FestivalSpeed.fast};
		final JComboBox speed_chooser = new JComboBox(speeds);
		speed_chooser.setFont(new Font("Arial", Font.PLAIN, 20));
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
		speed_chooser.setBounds(32, 334, 210, 50);
		add(speed_chooser);
	}

	/**
	 * Drop down to select preferred number of words in each quiz
	 * @author Abby S
	 */
	private void setupWordInQuiz(){
		JLabel change_words_in_quiz_label = new JLabel("Change preferred number of words in quiz");
		change_words_in_quiz_label.setForeground(Color.BLACK);
		change_words_in_quiz_label.setFont(new Font("Arial", Font.PLAIN, 20));
		change_words_in_quiz_label.setBounds(32, 422, 517, 30);
		add(change_words_in_quiz_label);

		//just a few choices for simplicity in use
		Integer[] word_numbers={1, 5, 10, 15, 25, 50};//TODO: remove 1 after testing
		final JComboBox word_number_chooser = new JComboBox(word_numbers);
		word_number_chooser.setFont(new Font("Arial", Font.PLAIN, 20));
		word_number_chooser.setForeground(Color.BLACK);
		word_number_chooser.setBackground(Color.WHITE);

		//set shown item to be the current number of words
		word_number_chooser.setSelectedItem(parent_frame.getDataHandler().getNumWordsInQuiz());
		word_number_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				temp_word_selection=(Integer) word_number_chooser.getSelectedItem();
			}
		});
		word_number_chooser.setBounds(32, 462, 210, 50);
		add(word_number_chooser);
	}

	/**
	 * Choose levels up to and including current level
	 * @author Abby S
	 */
	private void setupChooseLevel() {
		JLabel choose_level_label = new JLabel("Choose Level (for current list)");
		choose_level_label.setForeground(Color.BLACK);
		choose_level_label.setFont(new Font("Arial", Font.PLAIN, 20));
		choose_level_label.setBounds(32, 552, 517, 30);
		add(choose_level_label);

		String[] levels = parent_frame.getDataHandler().getLevelArray();
		//only shows levels up to and including current level
		final JComboBox level_chooser = new JComboBox(Arrays.copyOf(levels, parent_frame.getDataHandler().getCurrentLevel()));

		//default to current level
		level_chooser.setSelectedItem(parent_frame.getDataHandler().getLevelNames().get(parent_frame.getDataHandler().getCurrentLevel()));
		level_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				temp_level_selection = (String)level_chooser.getSelectedItem();
			}
		});
		level_chooser.setForeground(Color.BLACK);
		level_chooser.setBackground(Color.WHITE);
		level_chooser.setFont(new Font("Arial", Font.PLAIN, 20));
		level_chooser.setBounds(32, 592, 210, 50);
		add(level_chooser);
	}

	
	/**
	 * label for file chooser to choose word list from spellinglists directory
	 * @author Abby S
	 */
	private void setupChooseWordListLabel(){
		JLabel choose_wordlist_label = new JLabel("Current word list: "+parent_frame.getDataHandler().getSpellingListName());
		choose_wordlist_label.setForeground(Color.BLACK);
		choose_wordlist_label.setFont(new Font("Arial", Font.PLAIN, 20));
		choose_wordlist_label.setHorizontalAlignment(SwingConstants.RIGHT);
		choose_wordlist_label.setBounds(625, 169, 691, 30);
		add(choose_wordlist_label);
	}
	
	/**
	 * File chooser to choose word list from spellinglists directory
	 * @author Abby S
	 */
	private void setupChooseWordList(){
		//tells user what list they chose
		final JLabel will_change_to=new JLabel("");
		add(will_change_to);

		VoxButton list_choose_button = new VoxButton("Choose another list");
		list_choose_button.setBounds(1038, 200, 278, 46);
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
						//checks if file format is correct
						JOptionPane.showMessageDialog(null, "Chosen list is not in correct format\nPlease choose another list", "List Format Error", JOptionPane.WARNING_MESSAGE);
					} else {
						temp_list_selection=chooser.getSelectedFile().getName();
						will_change_to.setText("Will change to "+temp_list_selection+" on save");
						will_change_to.setForeground(new Color(254, 157, 79));
						will_change_to.setFont(new Font("Arial", Font.PLAIN, 18));
						will_change_to.setHorizontalAlignment(SwingConstants.RIGHT);
						will_change_to.setBounds(625, 246, 691, 30);
					}
				}
			}
		});
		add(list_choose_button);
	}

	/**
	 * Label for file chooser to choose reward video from rewardvideos directory
	 * @author Abby S
	 */
	private void setupChooseRewardVideoLabel(){
		JLabel choose_video_label = new JLabel("Current Reward Video: "+parent_frame.getDataHandler().getVideoName());
		choose_video_label.setForeground(Color.BLACK);
		choose_video_label.setFont(new Font("Arial", Font.PLAIN, 20));
		choose_video_label.setHorizontalAlignment(SwingConstants.RIGHT);
		choose_video_label.setBounds(625, 318, 691, 30);
		add(choose_video_label);
	}
	
	/**
	 * File chooser to choose reward video from rewardvideos directory
	 * @author Abby S
	 */
	private void setupChooseRewardVideo(){
		//tells user what video they chose
		final JLabel will_change_to=new JLabel("");
		add(will_change_to);

		VoxButton choose_video_button = new VoxButton("Choose another video");
		choose_video_button.setBounds(1038, 348, 278, 46);
		choose_video_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				File reward_videos_folder = new File(System.getProperty("user.dir")+"/rewardvideos/");
				FileSystemView fsv = new SingleRootFileSystemView(reward_videos_folder);

				JFileChooser chooser = new JFileChooser(reward_videos_folder, fsv);
				//expected format is a .avi file for VLCJ to be able to play it
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Video files", "avi", "mp4");
				chooser.setFileFilter(filter);

				int button_clicked = chooser.showDialog(parent_frame, "Choose this video");
				if(button_clicked == JFileChooser.APPROVE_OPTION) {
					if (chooser.getSelectedFile().getName().contains(" ")){
						//disallows spaces in file name due to difficulties with dealing with spaces
						JOptionPane.showMessageDialog(null, "Chosen video has space(s) in its name\nPlease rename", "Filename Format Error", JOptionPane.WARNING_MESSAGE);
					} else {
						temp_video_selection=chooser.getSelectedFile().getName();
						will_change_to.setText("Will change to "+temp_video_selection+" on save");
						will_change_to.setForeground(new Color(254, 157, 79));
						will_change_to.setFont(new Font("Arial", Font.PLAIN, 18));
						will_change_to.setHorizontalAlignment(SwingConstants.RIGHT);
						will_change_to.setBounds(625, 395, 691, 30);
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
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.setBorderPainted(false);
		back_button.setContentAreaFilled(false);
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
						parent_frame.getDataHandler().setNumWordsInQuiz(temp_word_selection);
					}
					if(temp_level_selection!=null){
						parent_frame.getDataHandler().setCurrentLevel(parent_frame.getDataHandler().getLevelNames().indexOf(temp_level_selection));
					}
					if(temp_video_selection!=null){
						parent_frame.getDataHandler().setVideoName(temp_video_selection);
					}
					//saves list settings first, before seeing if the list needs to change
					parent_frame.getDataHandler().writeToSettingsFiles();

					if(temp_list_selection!=null){
						parent_frame.getDataHandler().setSpellingListName(temp_list_selection);
						parent_frame.getDataHandler().readListSpecificFiles();
						parent_frame.getDataHandler().writeToSettingsFiles();
					}
				}
				parent_frame.changePanel(PanelID.MainMenu); //else doesn't save
			}
		});
		back_button.addMouseListener(new VoxMouseAdapter(back_button,null));
		add(back_button);
		back_button.setBounds(1216, 598, 100, 100);
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
	 * Means the file chooser is restricted to the directory I specify
	 * So user only allowed to choose lists for the spellinglists folder for example
	 * For ease of use, as users don't want to accidently click something 
	 * and end up changing things in the system file hierarchy
	 * Especially considering this application is intended for use on a healthcare robot
	 * 
	 * Slightly modified from:	
	 * https://tips4java.wordpress.com/2009/01/28/single-root-file-chooser/
	 * http://www.camick.com/java/source/SingleRootFileSystemView.java
	 * 
	 * A FileSystemView class that limits the file selections to a single root.
	 * When used with the JFileChooser component the user will only be able to
	 * traverse the directories contained within the specified root fill.
	 * The "Look In" combo box will only display the specified root
	 * The "Up One Level" button will be disabled
	 */
	private class SingleRootFileSystemView extends FileSystemView{
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
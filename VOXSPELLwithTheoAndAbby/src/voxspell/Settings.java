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
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import voxspell.Festival.FestivalSpeed;
import voxspell.Festival.FestivalVoice;
import voxspell.Voxspell.PanelID;

//import java.io.*;
//import javax.swing.filechooser.*;

@SuppressWarnings("serial")

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
	private boolean changedWordList=false;
	/**
	 * Constructor, initialise panel properties and GUI elements
	 */
	public Settings(Voxspell parent){
		setSize(800,600);
		setLayout(null);

		parent_frame = parent;
		seupReset();
		setupChangeVoice();
		setupChangeSpeed();
		setupWordInQuiz();
		setupChooseLevel();
		setupChooseWordList();
		setupBackButton();
		setupBackground();
	}

	/**
	 * Resets all stats to as if it was the user's first launch (prompts user for confirmation)
	 */
	private void seupReset() {
		ImageIcon clearall_image = new ImageIcon(parent_frame.getResourceFileLocation() + "clear_stats_button_alt.png");
		JButton clear_stats_button = new JButton("", clearall_image);

		clear_stats_button.setBounds(400, 200, 300, 200);
		add(clear_stats_button);
		clear_stats_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean askclear_result = askToConfirm("Are you sure you want to reset all?", "Reset All");
				if (askclear_result){
					parent_frame.getDataHandler().clearFiles();
				}
			}
		});
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
		JLabel lblChangeNumberOf = new JLabel("Change preferred number of words in quiz");
		lblChangeNumberOf.setForeground(Color.YELLOW);
		lblChangeNumberOf.setBounds(31, 416, 254, 15);
		add(lblChangeNumberOf);

		//		TODO: remove 1 after testing
		Integer[] words={1, 5, 10, 15, 25, 50};
		final JComboBox word_number_chooser = new JComboBox(words);
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
		JLabel lblChooseLevel = new JLabel("Choose Level (for current list)");
		lblChooseLevel.setForeground(Color.YELLOW);
		lblChooseLevel.setBounds(335, 418, 166, 15);
		add(lblChooseLevel);


		String[] levels = parent_frame.getDataHandler().getLevelArray();
		//only shows levels up to and including current level
		final JComboBox level_chooser = new JComboBox(Arrays.copyOf(levels, parent_frame.getDataHandler().getCurrentLevel()));

		//default to current level
		level_chooser.setSelectedItem(parent_frame.getDataHandler().level_names.get(parent_frame.getDataHandler().getCurrentLevel()));
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
		JLabel label_1=new JLabel("");
		add(label_1);
		JButton btnNewButton = new JButton("Choose another list");
		btnNewButton.setBounds(31, 527, 93, 23);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				File spelling_lists_folder = new File(System.getProperty("user.dir")+"/spellinglists/");
				FileSystemView fileSystemView = new SingleRootFileSystemView(spelling_lists_folder);

				JFileChooser chooser = new JFileChooser(spelling_lists_folder, fileSystemView);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Plain text files", "txt");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showDialog(parent_frame, "Choose this word list");
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					if (!parent_frame.getDataHandler().errorCheckSelectedFile(chooser.getSelectedFile())){
						JOptionPane.showMessageDialog(null, "Chosen list is not in correct format\nPlease choose another list", "List Format Error", JOptionPane.WARNING_MESSAGE);
					} else {
						temp_list_selection=chooser.getSelectedFile().getName();
						changedWordList=true;
						label_1.setText("Will change to "+temp_list_selection+" on save.");
						label_1.setForeground(Color.YELLOW);
						label_1.setBounds(150, 531, 254, 15);
					}
				}
			}
		});
		add(btnNewButton);

		JLabel label = new JLabel("Current word list: "+parent_frame.getDataHandler().spelling_list_name);
		label.setForeground(Color.YELLOW);
		label.setBounds(31, 500, 254, 15);
		add(label);
	}


	/**
	 * Back button to return to previous panel (user prompted to save before actually doing so)
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent e) {
				boolean asksave_result = askToConfirm("Would you like to Save?", "Save Settings");
				if (asksave_result){
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
					parent_frame.getDataHandler().writeToSettingsFiles();
					if(changedWordList && temp_list_selection!=null){
						parent_frame.getDataHandler().spelling_list_name=temp_list_selection;
						parent_frame.getDataHandler().readListSpecificFiles();
					}
					parent_frame.getDataHandler().writeToSettingsFiles();
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
	 * @param body  body of dialog box
	 * @param title	title of dialog box
	 * @return
	 */
	private boolean askToConfirm(String body, String title){
		int ask_prompt = JOptionPane.YES_NO_OPTION;
		int ask_result = JOptionPane.showConfirmDialog(this, body, title, ask_prompt);
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
	 *  A FileSystemView class that limits the file selections to a single root.
	 *
	 *  When used with the JFileChooser component the user will only be able to
	 *  traverse the directories contained within the specified root fill.
	 *
	 *  The "Look In" combo box will only display the specified root.
	 *
	 *  The "Up One Level" button will be disable when at the root.
	 * https://tips4java.wordpress.com/2009/01/28/single-root-file-chooser/
	 * http://www.camick.com/java/source/SingleRootFileSystemView.java
	 */
	class SingleRootFileSystemView extends FileSystemView
	{
		File root;
		File[] roots = new File[1];

		public SingleRootFileSystemView(File path)
		{
			super();

			try
			{
				root = path.getCanonicalFile();
				roots[0] = root;
			}
			catch(IOException e)
			{
				throw new IllegalArgumentException( e );
			}

			if ( !root.isDirectory() )
			{
				String message = root + " is not a directory";
				throw new IllegalArgumentException( message );
			}
		}

		@Override
		public File createNewFolder(File containingDir)
		{
			return null;
		}

		@Override
		public File getDefaultDirectory()
		{
			return root;
		}

		@Override
		public File getHomeDirectory()
		{
			return root;
		}

		@Override
		public File[] getRoots()
		{
			return roots;
		}
	}
}
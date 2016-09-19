package voxspell;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import voxspell.Festival.FestivalSpeed;
import voxspell.Festival.FestivalVoice;
import voxspell.Voxspell.PanelID;

@SuppressWarnings("serial")

/**
 * Settings
 */

//TODO make dropdown not look terrible
public class Settings extends JPanel {
	private Voxspell parent_frame;
	private Image bg_image;

	//temporary values selected by user. Only saved if user confirms
	private FestivalVoice temp_voice_selection=null;
	private FestivalSpeed temp_speed_selection=null;

	/**
	 * Constructor
	 */
	public Settings(Voxspell parent){
		setSize(800,600);
		setLayout(null);

		parent_frame = parent;
		seupReset();
		setupChangeVoice();
		setupChangeSpeed();
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
		JLabel change_voice_label = new JLabel("Change voice");
		change_voice_label.setBounds(31, 146, 166, 15);
		change_voice_label.setForeground(Color.YELLOW);
		add(change_voice_label);

		//TODO: select from voice.list on user's machine
		//Would introduce changes to DataHandler methods dealing with stats
		//Way out could be to use String instead of enum
		FestivalVoice[] voices={FestivalVoice.American, FestivalVoice.Kiwi};
		JComboBox voice_chooser = new JComboBox(voices);

		//set shown item to be the current voice
		voice_chooser.setSelectedItem(parent_frame.getFestival().getFestivalVoice());
		voice_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				temp_voice_selection=(FestivalVoice)voice_chooser.getSelectedItem();
			}
		});

		voice_chooser.setBounds(31, 193, 166, 72);
		add(voice_chooser);
	}

	/**
	 * Drop down to change festival speed
	 */
	private void setupChangeSpeed() {
		JLabel change_speed_label = new JLabel("Change speed");
		change_speed_label.setBounds(31, 309, 166, 15);
		change_speed_label.setForeground(Color.YELLOW);
		add(change_speed_label);

		FestivalSpeed[] speeds={FestivalSpeed.slow, FestivalSpeed.normal, FestivalSpeed.fast};
		JComboBox speed_chooser = new JComboBox(speeds);

		//set shown item to be the current voice
		speed_chooser.setSelectedItem(parent_frame.getFestival().getFestivalSpeed());
		speed_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				temp_speed_selection=(FestivalSpeed)speed_chooser.getSelectedItem();
			}
		});

		speed_chooser.setBounds(31, 359, 166, 72);
		add(speed_chooser);
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
					parent_frame.getDataHandler().writeToSettings();
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
	 * @param body
	 * @param title
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
}
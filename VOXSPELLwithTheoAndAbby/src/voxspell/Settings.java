package voxspell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class Settings extends JPanel {
	private Voxspell parent_frame;

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
	}

	/**
	 * Resets all stats to as if it was the user's first launch
	 */
	private void seupReset() {
		JButton clear_stats_button = new JButton("Reset All");
		clear_stats_button.setBounds(31, 30, 166, 72);
		add(clear_stats_button);
		clear_stats_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean askclear_result = askToConfirm("Are you sure you want to reset all?", "Reset All");
				if (askclear_result){
					parent_frame.getFileIO().clearFiles();
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
		add(change_voice_label);
//		JLabel lblCurrentVoiceIs = new JLabel("Orginal voice was "+parent_frame.festival.getFestivalVoice().toString());
//		lblCurrentVoiceIs.setBounds(31, 171, 166, 15);
//		add(lblCurrentVoiceIs);

		//TODO: select from voice.list on user's machine
		FestivalVoice[] voices={FestivalVoice.American, FestivalVoice.Kiwi};
		JComboBox voice_chooser = new JComboBox(voices);
		
		//set shown item to be the current voice
		voice_chooser.setSelectedItem(parent_frame.festival.getFestivalVoice());
		voice_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				temp_voice_selection=(FestivalVoice)voice_chooser.getSelectedItem();
//				parent_frame.festival.setFestivalVoice((FestivalVoice)voice_chooser.getSelectedItem());
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
		add(change_speed_label);
//		JLabel lblCurrentSpeedIs = new JLabel("Orginal speed was "+parent_frame.festival.getFestivalSpeed().toString());
//		lblCurrentSpeedIs.setBounds(31, 334, 166, 15);
//		add(lblCurrentSpeedIs);
		
		FestivalSpeed[] speeds={FestivalSpeed.slow, FestivalSpeed.normal, FestivalSpeed.fast};
		JComboBox speed_chooser = new JComboBox(speeds);
		
		//set shown item to be the current voice
		speed_chooser.setSelectedItem(parent_frame.festival.getFestivalSpeed());
		speed_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				temp_speed_selection=(FestivalSpeed)speed_chooser.getSelectedItem();
//				parent_frame.festival.setFestivalSpeed((FestivalSpeed)speed_chooser.getSelectedItem());
			}
		});
		
		speed_chooser.setBounds(31, 359, 166, 72);
		add(speed_chooser);
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
				boolean asksave_result = askToConfirm("Would you like to Save?", "Save Settings");
				if (asksave_result){
					/* if user didn't touch the drop downs, i.e. they don't want to make a change,
					 * the temporary selection values will be null
					 */
					if(temp_voice_selection!=null){
						parent_frame.festival.setFestivalVoice(temp_voice_selection);
					}
					if(temp_speed_selection!=null){
						parent_frame.festival.setFestivalSpeed(temp_speed_selection);
					}
					parent_frame.getFileIO().writeToSettings();
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
}
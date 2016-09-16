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

public class Settings extends JPanel {
	private Voxspell parent_frame;

	public Settings(Voxspell parent){
		setSize(800,600);
		setLayout(null);

		parent_frame = parent;
		seupReset();
		setupChangeVoice();
		setupChangeSpeed();
		setupBackButton();
	}

	private void setupChangeSpeed() {
		JLabel lblChangeSpeed = new JLabel("Change speed");
		lblChangeSpeed.setBounds(31, 309, 166, 15);
		add(lblChangeSpeed);
		JLabel lblCurrentSpeedIs = new JLabel("Orginal speed was "+parent_frame.festival.getFestivalSpeed().toString());
		lblCurrentSpeedIs.setBounds(31, 334, 166, 15);
		add(lblCurrentSpeedIs);
		
		FestivalSpeed[] speeds={FestivalSpeed.slow, FestivalSpeed.normal, FestivalSpeed.fast};
		JComboBox speed_chooser = new JComboBox(speeds);
		speed_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.festival.setFestivalSpeed((FestivalSpeed)speed_chooser.getSelectedItem());
				System.out.print(parent_frame.festival.getFestivalSpeed().toString());
			}
		});
		speed_chooser.setBounds(31, 359, 166, 72);
		add(speed_chooser);
	}

	private void setupChangeVoice() {
		JLabel lblChangeVoice = new JLabel("Change voice");
		lblChangeVoice.setBounds(31, 146, 166, 15);
		add(lblChangeVoice);
		JLabel lblCurrentVoiceIs = new JLabel("Orginal voice was "+parent_frame.festival.getFestivalVoice().toString());
		lblCurrentVoiceIs.setBounds(31, 171, 166, 15);
		add(lblCurrentVoiceIs);

		FestivalVoice[] voices={FestivalVoice.American, FestivalVoice.Kiwi};
		JComboBox voice_chooser = new JComboBox(voices);
		voice_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.festival.setFestivalVoice((FestivalVoice)voice_chooser.getSelectedItem());
				System.out.print(parent_frame.festival.getFestivalVoice().toString());
			}
		});
		voice_chooser.setBounds(31, 193, 166, 72);
		add(voice_chooser);
	}



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

	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);

		back_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean asksave_result = askToConfirm("Would you like to Save?", "Save Settings");
				if (asksave_result){
					parent_frame.getFileIO().writeToSettings();
				}
				parent_frame.changePanel(PanelID.MainMenu);
			}
		});

		add(back_button);
		back_button.setSize(50,50);
		back_button.setLocation(700,500);
	}
	
	private boolean askToConfirm(String body, String title){
		//http://stackoverflow.com/questions/8689122/joptionpane-yes-no-options-confirm-dialog-box-issue-java
		int ask_prompt = JOptionPane.YES_NO_OPTION;
		int ask_result = JOptionPane.showConfirmDialog(this, body, title, ask_prompt);
		if (ask_result == JOptionPane.YES_OPTION){
			return true;
		}
		return false;
		
	}
}

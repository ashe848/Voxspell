package voxspell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import voxspell.Festival.FestivalSpeed;
import voxspell.Voxspell.PanelID;

public class Settings extends JPanel {
	private Voxspell parent_frame;
	private Festival festival;
	
	public Settings(Voxspell parent){
		setSize(800,600);
		setLayout(null);

		parent_frame = parent;
		seupClearStats();
		setupChangeVoice();
		setupChangeSpeed();
		setupBackButton();
	}
	
	
	
	private void setupChangeSpeed() {
		JLabel lblChangeSpeed = new JLabel("Change speed");
		lblChangeSpeed.setBounds(31, 309, 166, 15);
		add(lblChangeSpeed);
		JLabel lblCurrentSpeedIs = new JLabel("Current speed is _");
		lblCurrentSpeedIs.setBounds(31, 334, 166, 15);
		add(lblCurrentSpeedIs);
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(31, 359, 166, 72);
		add(comboBox_1);
	}



	private void setupChangeVoice() {
		JLabel lblChangeVoice = new JLabel("Change voice");
		lblChangeVoice.setBounds(31, 146, 166, 15);
		add(lblChangeVoice);
		JLabel lblCurrentVoiceIs = new JLabel("Current voice is _");
		lblCurrentVoiceIs.setBounds(31, 171, 166, 15);
		add(lblCurrentVoiceIs);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(31, 193, 166, 72);
		add(comboBox);
	}



	private void seupClearStats() {
		JButton btnClearStats = new JButton("Clear Stats");
		btnClearStats.setBounds(31, 30, 166, 72);
		add(btnClearStats);
	}



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
}

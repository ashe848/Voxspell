package voxspell;

import javax.swing.JPanel;



	import java.awt.Font;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;

	import javax.swing.ImageIcon;
	import javax.swing.JButton;
	import javax.swing.JLabel;
	import javax.swing.JComboBox;

	public class GuiBuilder extends JPanel {

		/**
		 * Create the panel.
		 */
		public GuiBuilder() {
			setLayout(null);
			setSize(800,600);
			setupBackButton();
		}
		
		private void setupBackButton(){
			
			JButton btnClearStats = new JButton("Clear Stats");
			btnClearStats.setBounds(31, 30, 166, 72);
			add(btnClearStats);
			
			JComboBox comboBox = new JComboBox();
			comboBox.setBounds(31, 193, 166, 72);
			add(comboBox);
			
			JComboBox comboBox_1 = new JComboBox();
			comboBox_1.setBounds(31, 359, 166, 72);
			add(comboBox_1);
			
			JLabel lblChangeVoice = new JLabel("Change voice");
			lblChangeVoice.setBounds(31, 146, 166, 15);
			add(lblChangeVoice);
			
			JLabel lblChangeSpeed = new JLabel("Change speed");
			lblChangeSpeed.setBounds(31, 309, 166, 15);
			add(lblChangeSpeed);
			
			JLabel lblCurrentVoiceIs = new JLabel("Current voice is _");
			lblCurrentVoiceIs.setBounds(31, 171, 166, 15);
			add(lblCurrentVoiceIs);
			
			JLabel lblCurrentSpeedIs = new JLabel("Current speed is _");
			lblCurrentSpeedIs.setBounds(31, 334, 166, 15);
			add(lblCurrentSpeedIs);
		}
	}

	


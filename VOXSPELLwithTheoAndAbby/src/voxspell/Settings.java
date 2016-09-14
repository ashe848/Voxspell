package voxspell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import voxspell.Voxspell.PanelID;

public class Settings extends JPanel {
	private Voxspell parent_frame;
	private double festival_speed;
	private String festival_voice;
	
	public Settings(Voxspell parent){
		setSize(800,600);
		setLayout(null);

		parent_frame = parent;
		setupBackButton();
		festival_speed=2.2;
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

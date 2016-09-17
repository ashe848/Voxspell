package voxspell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import voxspell.Voxspell.PanelID;

/**
 * For reusing components
 */
public class ReusableComponents {
	private static Voxspell parent_frame;

	/**
	 * Constructor
	 */
	public ReusableComponents(Voxspell parent){
		super();
		parent_frame=parent;
	}
	
	/**
	 * Back button to return to previous panel
	 */
	public void setupBackButton(JPanel panel, PanelID return_to){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(return_to);
			}
		});

		panel.add(back_button);
		back_button.setSize(50,50);
		back_button.setLocation(700,500);
	}
}

package voxspell;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import voxspell.Voxspell.PanelID;

@SuppressWarnings({ "static-access", "serial" })

public class Help extends JPanel{
	private Voxspell parent_frame; //link to parent frame
	private PanelID from_panel;

	public Help(Voxspell parent, PanelID from) {
		setSize(1366,745);
		setLayout(null);
		setBackground(new Color(235, 235, 235));

		parent_frame=parent;
		from_panel=from;
		
		setupTitle();
		setupTextArea();
		setupBackButton();
	}

	private void setupTitle() {
		JLabel title = new JLabel(parent_frame.getDataHandler().getVideoName());
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setForeground(new Color(254, 157, 79));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(32, 24, 1284, 119);
		add(title);
	}

	/**
	 * Sets up the
	 */
	private void setupTextArea() {
		
	}


	/**
	 * Back button to return to previous panel
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.setBorderPainted(false);
		back_button.setContentAreaFilled(false);
		back_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(from_panel, PanelID.Help);
			}
		});
		back_button.addMouseListener(new VoxMouseAdapter(back_button,null));
		add(back_button);
		back_button.setBounds(1216, 598, 100, 100);
	}
}
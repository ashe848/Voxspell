package voxspell;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

@SuppressWarnings("serial")

/**
 * Text (not image) buttons used in VOXSPELL
 * 
 * @author Abby S
 */
public class VoxButton extends JButton {
	private VoxMouseAdapter adapter;

	/**
	 * Constructor to set the defaults
	 * @param text the text to be displayed on the button
	 * @author Abby S
	 */
	public VoxButton(String text){
		super(text);
		setForeground(Color.WHITE);
		setBackground(new Color(254, 157, 79));
		setFont(new Font("Calibri Light", Font.PLAIN, 17));
		setContentAreaFilled(false);
		setOpaque(true);
		setFocusPainted(false);
		setBorderPainted(false);
		setupMouseListener();
	}

	/**
	 * Adds a default Mouse Listeners to all these buttons
	 * @author Abby S
	 */
	private void setupMouseListener() {
		adapter=new VoxMouseAdapter(this,Color.WHITE);
		addMouseListener(adapter);
	}

	/**
	 * If don't want the default white border hover effect
	 * e.g. the red buttons in settings have black
	 * @param special_color
	 * @author Abby S
	 */
	public void changeMouseEventColor(Color special_color){
		removeMouseListener(adapter);
		addMouseListener(new VoxMouseAdapter(this,special_color));
	}
}

package vox;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Custom implementation for a Mouse Listener
 * Used by all the buttons in VOXSPELL
 * 
 * @author Abby S
 */
public class VoxMouseAdapter extends MouseAdapter{
	private JButton action_button;
	private Color border_color;

	public VoxMouseAdapter(JButton button, Color special_color) {
		action_button=button;
		if(special_color!=null){
			border_color=special_color;
		} else {
			border_color = new Color(254, 157, 79); //defaults to the orange theme colour
		}
	}

	/**
	 * sets back to hover effect on mouse release
	 * @author Abby S
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		action_button.setBorderPainted(true);
		action_button.setBorder(BorderFactory.createLineBorder(border_color,4));
	}

	/**
	 * thicker border for mouse pressed
	 * @author Abby S
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		action_button.setBorderPainted(true);
		action_button.setBorder(BorderFactory.createLineBorder(border_color,7));
	}

	/**
	 * nothing special. Action listener handles the mouse clicks
	 * @author Abby S
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		//do nothing
	}

	/**
	 * border effect for "hovering" over the button
	 * @author Abby S
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		action_button.setBorderPainted(true);
		action_button.setBorder(BorderFactory.createLineBorder(border_color,4));
	}

	/**
	 * resets back to default no border
	 * @author Abby S
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		action_button.setBorderPainted(false);
	}
}

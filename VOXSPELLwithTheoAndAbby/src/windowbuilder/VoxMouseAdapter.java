package windowbuilder;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * 
 * @author Abby S
 *
 */
public class VoxMouseAdapter extends MouseAdapter{
	private JButton action_button;
	private Color border_color;
	
	public VoxMouseAdapter(JButton button, Color special_color) {
		action_button=button;
		if(special_color!=null){
			border_color=special_color;
		} else {
			border_color = new Color(254, 157, 79);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		action_button.setBorderPainted(true);
		action_button.setBorder(BorderFactory.createLineBorder(border_color,4));
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		action_button.setBorderPainted(true);
		action_button.setBorder(BorderFactory.createLineBorder(border_color,6));
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//do nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		action_button.setBorderPainted(true);
		action_button.setBorder(BorderFactory.createLineBorder(border_color,4));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		action_button.setBorderPainted(false);
	}
}

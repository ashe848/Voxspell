package windowbuilder;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class MyMouseAdaptor extends MouseAdapter{
	JButton button_2;
	
	public MyMouseAdaptor(JButton button) {
		// TODO Auto-generated constructor stub
	
		button_2=button;
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {	
		button_2.setBorderPainted(true);
		button_2.setBorder(BorderFactory.createLineBorder(Color.GREEN,5));
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		button_2.setBorderPainted(true);
		button_2.setBorder(BorderFactory.createLineBorder(Color.GREEN,10));
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		button_2.setBorderPainted(true);
		button_2.setBorder(BorderFactory.createLineBorder(Color.GREEN,5));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		button_2.setBorderPainted(false);
	}
}

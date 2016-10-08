package voxspell;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

@SuppressWarnings("serial")

/**
 * 
 * @author Abby S
 *
 */
public class VoxButton extends JButton {
	private VoxMouseAdapter adapter;
	
	public VoxButton(String text){
		super(text);
		setForeground(Color.WHITE);
		setBackground(new Color(254, 157, 79));
		setFont(new Font("Calibri Light", Font.PLAIN, 20));
		setContentAreaFilled(false);
		setOpaque(true);
		setFocusPainted(false);
		setBorderPainted(false);
		setupMouseListener();
	}

	private void setupMouseListener() {
		adapter=new VoxMouseAdapter(this,Color.WHITE);
		addMouseListener(adapter);
	}
	
	public void changeMouseEventColor(Color special_color){
		removeMouseListener(adapter);
		addMouseListener(new VoxMouseAdapter(this,special_color));
	}
}

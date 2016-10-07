package windowbuilder;

import java.awt.Color;

import javax.swing.JButton;

@SuppressWarnings("serial")

/**
 * 
 * @author Abby S
 *
 */
public class VoxButton extends JButton {
	private VoxMouseAdapter adapter;
	
	public VoxButton(String s){
		super(s);
		setForeground(Color.WHITE);
		setBackground(new Color(254, 157, 79));
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
	
	public void changeMouseEventColor(Color c){
		removeMouseListener(adapter);
		addMouseListener(new VoxMouseAdapter(this,c));
	}
}

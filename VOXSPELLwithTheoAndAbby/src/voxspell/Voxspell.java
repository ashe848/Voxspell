package voxspell;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Voxspell extends JFrame{
	
	
	public Voxspell(){
		super();
		setTitle("VoxSpell version 0.0000000001 Post-PreAlpha (but still in Alpha)");
		setSize(800,600);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		revalidate();
	}
	
	public static void main(String[] args){
		Festival festival = new Festival();
		System.out.println("Program Starting...");
		festival.speak("HEllo");
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				Voxspell instance = new Voxspell();
				instance.setVisible(true);
			}
		});
	}
}

package voxspell;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Voxspell extends JFrame{
	JPanel contentPane=new JPanel();
	private static String RESOURCE_FILE_LOCATION = System.getProperty("user.dir")+"/resources/";
	private static FileIO file_handler;
	
	public Voxspell(){
		//Below setup code for frame from Theo's code
		super();
		setTitle("VoxSpell version 0.0000000001 Post-PreAlpha (but still in Alpha)");
		setSize(800,600);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		revalidate();
		
		file_handler=FileIO.getInstance(this);
		
		add(new GeneralStats(this));
		revalidate();
	}
	
	public static void main(String[] args){
		Festival festival = new Festival();
		
		//Testing swingworker capabilities
		System.out.println("Program Starting...");
		festival.speak("I should be going on after line below printed");
		System.out.println("This should be printed before line above finishes being said");
//		TestClass tc = new TestClass();
//		tc.setVisible(true);
		
		//Initialising Swing program
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				Voxspell instance = new Voxspell();
				instance.setVisible(true);
			}
		});
	}
	
	public static String getResourceFileLocation(){
		return RESOURCE_FILE_LOCATION;
	}
	
	public static FileIO getFileIO(){
		return file_handler;
	}
}

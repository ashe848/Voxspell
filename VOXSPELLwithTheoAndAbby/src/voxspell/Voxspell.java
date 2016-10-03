package voxspell;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import voxspell.StatsChooser.StatsType;

@SuppressWarnings("serial")

/*
parent_frame.setEnabled(false);
set it back to true on X of dialog
 */


/**
 * The class containing the main method 
 * Is the frame for the Voxspell program
 */
public class Voxspell extends JFrame{
	//TODO make this folder hidden
	private static final String RESOURCE_FILE_LOCATION = System.getProperty("user.dir")+"/resources/";
	static MainMenu main_menu;
	
	//Contains the DataHandler and Festival instances which panels get
	private static DataHandler data_handler;
	private static Festival festival;

	/**
	 * Constructor. Initialise frame, create data structure & set original panel to main menu
	 */
	public Voxspell(){
		super();
		
		//setup code from Theo's A2 code
		setTitle("Voxspell");
		//TODO 570 or 600?
		if (System.getProperty("os.name").equals("Linux")) {
			setSize(800,570);
		} else {
			setSize(800,600);
		}
		setLocationRelativeTo(null);
		setResizable(false);

		//TODO back to DO_NOTHING_ON_CLOSE
		//Make the power button on main menu the only way to exit the application
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				JOptionPane.showMessageDialog(null, "Closing the window will result in loss of data\nPlease exit using the power button on main menu", "Error", JOptionPane.WARNING_MESSAGE);
//			}
//		});
		
		festival = Festival.getInstance(this);
		data_handler=DataHandler.getInstance(this);
		
		//make MainMenu the panel to display
		main_menu=new MainMenu(this);
		add(main_menu);
		revalidate();
	}

	public static void main(String[] args){
		//Initialise Swing program
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Voxspell instance = new Voxspell();
				instance.setVisible(true);
			}
		});
	}

	/**
	 * Method to return location of resources folder
	 * @return
	 */
	public String getResourceFileLocation(){
		return RESOURCE_FILE_LOCATION;
	}

	/**
	 * Method to return object responsible for handling data
	 * @return
	 */
	public DataHandler getDataHandler(){
		return data_handler;
	}

	/**
	 * Method to return object responsible for making Festival calls
	 * @return
	 */
	public Festival getFestival(){
		return festival;
	}

	/**
	 * @param id	passed into method to change panel shown
	 */
	public void changePanel(PanelID id){
		//Removes the current panel from the frame, ready for new one to take its place.
		//http://stackoverflow.com/questions/9347076/how-to-remove-all-components-from-a-jframe-in-java
		this.getContentPane().removeAll();
		this.repaint();

		//Change panel pased on ID passed into method. Initialise extra constructor parameters if needed.
		switch (id) {
		case MainMenu:
			main_menu=new MainMenu(this);
			this.getContentPane().add(main_menu);
			main_menu.setupAccuracyRateLabel();
			break;
		case Settings:
			this.getContentPane().add(new Settings(this));
			break;
		case StatSelection:
			this.getContentPane().add(new StatsChooser(this));
			break;
		case PersistentAllStats:
			this.getContentPane().add(new GeneralStats(this, StatsType.Persistent));
			break;
		case PersistentLevelStats:
			this.getContentPane().add(new LevelStats(this, StatsType.Persistent));
			break;
		case SessionAllStats:
			this.getContentPane().add(new GeneralStats(this, StatsType.Session));
			break;
		case SessionLevelStats:
			this.getContentPane().add(new LevelStats(this, StatsType.Session));
			break;
		case Quiz:
			this.getContentPane().add(new Quiz(this, PanelID.Quiz));
			break;
		case Review:
			this.getContentPane().add(new Quiz(this, PanelID.Review));
			break;
		case QuizComplete:
			this.getContentPane().add(new QuizComplete(this));
			break;
		case Video:
			//Setting up various libraries needed to show video reward after quiz is complete
			/*NativeLibrary.addSearchPath(
					RuntimeUtil.getLibVlcLibraryName(), "/Applications/vlc-2.0.0/VLC.app/Contents/MacOS/lib"
					);
			Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);*/
			this.getContentPane().add(new Video(this));
			break;
		}
		
		//Repaints the panel to the changed one
		this.revalidate();
	}

	/**
	 * The different panels IDs used to differentiate different panels
	 */
	public enum PanelID{
		MainMenu, Settings, StatSelection, PersistentAllStats, PersistentLevelStats, SessionAllStats, SessionLevelStats, Quiz, Review, QuizComplete, Video;
	}
}
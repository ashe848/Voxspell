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

/**
 * The class containing the main method 
 * Is the frame for the Voxspell program
 */
public class Voxspell extends JFrame{
	private static final String RESOURCE_FILE_LOCATION = System.getProperty("user.dir")+"/.resources/";

	//Contains the DataHandler and Festival instances which panels get
	private static DataHandler data_handler;
	private static Festival festival;

	/**
	 * Constructor. Initialise frame, create data structure & set original panel to main menu
	 */
	public Voxspell(){
		super();
		festival = Festival.getInstance(this);
		data_handler=DataHandler.getInstance(this);

		//setup code from Theo's A2 code
		setTitle("Voxspell version 0.0000000001 Post-PreAlpha (but still in Alpha)");

		setSize(800,570);

		setLocationRelativeTo(null);
		setResizable(false);

		//Make the power button on main menu the only way to exit the application
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JOptionPane.showMessageDialog(null, "Closing the window will result in loss of data\nPlease exit using the power button on main menu", "Error", JOptionPane.WARNING_MESSAGE);
			}
		});

		//make MainMenu the panel to display
		add(new MainMenu(this));
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
			this.getContentPane().add(new MainMenu(this));
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
			NativeLibrary.addSearchPath(
					RuntimeUtil.getLibVlcLibraryName(), "/Applications/vlc-2.0.0/VLC.app/Contents/MacOS/lib"
					);
			Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
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
package voxspell;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import backendio.DataHandler;
import backendio.Festival;
import guiview.GeneralStats;
import guiview.LevelStats;
import guiview.ListBuilder;
import guiview.MainMenu;
import guiview.Quiz;
import guiview.QuizComplete;
import guiview.Settings;
import guiview.StatsChooser;
import guiview.Video;
import guiview.StatsChooser.StatsType;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

@SuppressWarnings("serial")

/**
 * The class containing the main method 
 * Is the frame for the Voxspell program
 */
public class Voxspell extends JFrame{
	private final String RESOURCE_FILE_LOCATION = System.getProperty("user.dir")+"/resources/";//TODO make this folder hidden

	//Contains the singleton DataHandler and Festival instances which panels get
	private DataHandler data_handler;
	private Festival festival;


	//To be used when refreshing the main menu on panel change
	private MainMenu main_menu;

	/**
	 * Constructor. Initialise frame, create data structure & set content panel to main menu
	 */
	public Voxspell(){
		setTitle("VOXSPELL");
		setSize(1366,772);//TODO: 745 height
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

		setGraphicalEnhancements();

		festival = Festival.getInstance(this);
		data_handler=DataHandler.getInstance(this);

		//make MainMenu the panel to display
		main_menu=new MainMenu(this);
		add(main_menu);
		revalidate();
	}

	/**
	 * Main method. Starts the Swing program
	 */
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Voxspell instance = new Voxspell();
				instance.setVisible(true);
			}
		});
	}

	/**
	 * Defaults for better looks
	 * Not every key for put() seems to be working, but the below work. Might be a Java version thing
	 * @author Abby S
	 */
	private void setGraphicalEnhancements() {
		UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 25));
		UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 25));
		UIManager.put("OptionPane.background", new Color(235, 235, 235));
		UIManager.put("ComboBox.selectionBackground", new Color(254, 157, 79));
		UIManager.put("ComboBox.background", Color.WHITE);
		UIManager.put("ScrollBar.background", new Color(254, 157, 79));
		UIManager.put("ScrollPane.background", new Color(254, 157, 79));
		UIManager.put("TableHeader.font", new Font("Arial", Font.PLAIN, 23));
		UIManager.put("TableHeader.foreground", Color.WHITE);
		UIManager.put("TableHeader.background", new Color(254, 157, 79));
		UIManager.put("ProgressBar.selectionBackground", Color.BLACK); //not covered by bar
		UIManager.put("ProgressBar.selectionForeground", Color.BLACK); //covered by bar
		UIManager.put("ProgressBar.font", new Font("Arial", Font.PLAIN, 20));
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
	 * @param id	passed into method to change to that panel
	 */
	public void changePanel(PanelID id){
		//Removes the current panel from the frame, ready for new one to take its place.
		//http://stackoverflow.com/questions/9347076/how-to-remove-all-components-from-a-jframe-in-java
		this.getContentPane().removeAll();
		this.repaint();

		//Change panel based on ID passed into method. Initialise extra constructor parameters if needed.
		switch (id) {
		case MainMenu:
			main_menu=new MainMenu(this);
			this.getContentPane().add(main_menu);
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
		case ListBuilder:
			this.getContentPane().add(new ListBuilder(this));
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
			//Setting up libraries needed to show video reward after quiz is complete
			//Based on Nasser's ACP code
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
		MainMenu, Settings, StatSelection, PersistentAllStats, PersistentLevelStats, SessionAllStats, SessionLevelStats, ListBuilder, Quiz, Review, QuizComplete, Video;
	}
}
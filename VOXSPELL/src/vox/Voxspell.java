package vox;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import audio.Festival;
import backendio.DataHandler;
import backendio.FileReadingHandler;
import backendio.FileWritingHandler;
import backendio.PostQuizHandler;
import backendio.PreQuizHandler;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import visiblegui.GeneralStats;
import visiblegui.LevelStats;
import visiblegui.ListBuilder;
import visiblegui.MainMenu;
import visiblegui.Quiz;
import visiblegui.QuizComplete;
import visiblegui.Settings;
import visiblegui.StatsChooser;
import visiblegui.StatsChooser.StatsType;
import visiblegui.Video;

@SuppressWarnings("serial")

/**
 * The class containing the main method 
 * Is the frame for the Voxspell program
 */
public class Voxspell extends JFrame{
	private final String RESOURCE_FILE_LOCATION = System.getProperty("user.dir")+"/.resources/";

	//Contains the singleton backend I/O handler and Festival instances which panels get
	private FileReadingHandler file_reading_handler;
	private FileWritingHandler file_writing_handler;
	private PostQuizHandler post_quiz_handler;
	private PreQuizHandler pre_quiz_handler;
	private DataHandler data_handler;
	private Festival festival;

	//To be used when refreshing the main menu on panel change
	private MainMenu main_menu;

	/**
	 * Constructor. Initialise frame, create data structure & set content panel to main menu
	 */
	public Voxspell(){
		setTitle("VOXSPELL");
		setSize(1366,745);
		setLocationRelativeTo(null);
		setResizable(false);

		//Make the power button on main menu the only way to exit the application
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				JOptionPane.showMessageDialog(null, "Closing the window will result in loss of data\nPlease exit using the power button on main menu", "Error", JOptionPane.WARNING_MESSAGE);
			}
		});

		//Initialisations
		VoxDefaultUI.getInstance();
		festival = Festival.getInstance(this);
		file_reading_handler=FileReadingHandler.getInstance(this);
		file_writing_handler=FileWritingHandler.getInstance(this);
		post_quiz_handler=PostQuizHandler.getInstance(this);
		pre_quiz_handler=PreQuizHandler.getInstance(this);
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
	 * Method to return location of resources folder
	 */
	public String getResourceFileLocation(){
		return RESOURCE_FILE_LOCATION;
	}

	/**
	 * Method to return responsible for handling data structures
	 */
	public DataHandler getDataHandler(){
		return data_handler;
	}

	/**
	 * Method to return object responsible for handling file reading
	 */
	public FileReadingHandler getFileReadingHandler(){
		return file_reading_handler;
	}

	/**
	 * Method to return object responsible for handling file writing
	 */
	public FileWritingHandler getFileWritingHandler(){
		return file_writing_handler;
	}

	/**
	 * Method to return object responsible for handling post quiz data manipulation
	 */
	public PostQuizHandler getPostQuizHandler(){
		return post_quiz_handler;
	}

	/**
	 * Method to return object responsible for handling pre quiz data manipulation
	 */
	public PreQuizHandler getPreQuizHandler(){
		return pre_quiz_handler;
	}

	/**
	 * Method to return object responsible for making Festival calls
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

		//Changes panel based on ID passed into method. Initialise extra constructor parameters if needed.
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
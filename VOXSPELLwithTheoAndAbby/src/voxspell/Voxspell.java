package voxspell;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

//import com.sun.jna.Native;
//import com.sun.jna.NativeLibrary;
//
//import uk.co.caprica.vlcj.binding.LibVlc;
//import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import voxspell.Festival.FestivalWorker;
import voxspell.StatsChooser.StatsType;

@SuppressWarnings("serial")

/**
 * The class containing the main method 
 * Is the frame for the Voxspell program
 */
public class Voxspell extends JFrame{
	//////////////JPanel content_pane=new JPanel();

	private static String RESOURCE_FILE_LOCATION = System.getProperty("user.dir")+"/resources/";

	//Contains the FileIO and Festival instances which panels get
	private static FileIO file_handler;
	public static Festival festival;

	/**
	 * Constructor
	 */
	public Voxspell(){
		//Below setup code for frame from Theo's A2 code
		super();
		festival = Festival.getInstance(this);
		setTitle("VoxSpell version 0.0000000001 Post-PreAlpha (but still in Alpha)");
		setSize(800,600);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		revalidate();

		file_handler=FileIO.getInstance(this);

		add(new MainMenu(this));
		revalidate();
	}

	public static void main(String[] args){
		//Initialising Swing program
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
	 * Method to return object responsible for handling file I/O
	 * @return
	 */
	public static FileIO getFileIO(){
		return file_handler;
	}

	public void changePanel(PanelID id){
		//http://stackoverflow.com/questions/9347076/how-to-remove-all-components-from-a-jframe-in-java
		this.getContentPane().removeAll();
		this.repaint();

		switch (id) {
		case MainMenu:
			this.getContentPane().add(new MainMenu(this));
			break;
		case Settings:
			this.getContentPane().add(new Settings(this));
			break;
		case Help:
			///////////////
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
			/*
			NativeLibrary.addSearchPath(
					RuntimeUtil.getLibVlcLibraryName(), "/Applications/vlc-2.0.0/VLC.app/Contents/MacOS/lib"
					);
			Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
			this.getContentPane().add(new Video(this));
			*/
			System.out.println("Video");
			break;
		}
		this.revalidate();
	}

	/**
	 * The different panels
	 */
	public enum PanelID{
		MainMenu, Settings, Help, StatSelection, PersistentAllStats, PersistentLevelStats, SessionAllStats, SessionLevelStats, Quiz, Review, QuizComplete, Video;
	}
}
package backendio;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingWorker;

import vox.Voxspell;

@SuppressWarnings({ "static-access" })

/**
 * Singleton class responsible for making the festival calls
 * Based on Abby's A2 code
 */
public class Festival {
	private Voxspell parent_frame;

	private static Festival instance=null; //since singleton class

	//initialised by readfiles in DataHandler
	private static FestivalSpeed festival_speed;
	private static FestivalVoice festival_voice;

	//queuing of SwingWorkers to avoid voice overlapping
	//inspired by http://stackoverflow.com/questions/22412544/swingworker-queue-and-single-using
	private ArrayList<FestivalWorker> worker_queue=new ArrayList<FestivalWorker>();
	//flag to indicate if there is already a worker executing Festival calls
	private boolean locked=false;

	/**
	 * Private constructor
	 */
	private Festival(Voxspell parent){
		parent_frame = parent;
	}

	/**
	 * Method to return the single instance responsible for making the Festival calls
	 * @param parent
	 * @return
	 */
	public static Festival getInstance(Voxspell parent){
		if (instance==null){
			instance=new Festival(parent);
		}
		return instance;
	}

	/**
	 * The different options for speed
	 */
	public enum FestivalSpeed {
		slow, normal, fast;

		double getSpeedValue(){
			switch(this){
			case slow:
				return 2.0;
			case fast:
				return 1.0;
			default: //normal
				return 1.5;
			}
		}
	}

	/**
	 * The different options for voice
	 */
	public enum FestivalVoice {
		Kiwi, American, British;

		String getVoiceValue(){
			switch(this){
			case American:
				return "kal_diphone";
			case British:
				return "rab_diphone";
			default: //Kiwi
				return "akl_nz_jdt_diphone";
			}
		}
	}

	/**
	 * Getter for whether festival is executing a voice prompt
	 */
	public boolean isLocked() {
		return locked;
	}
	
	/**
	 * Getter for speed
	 */
	public FestivalSpeed getFestivalSpeed() {
		return festival_speed;
	}

	/**
	 * setters for speed
	 */
	public FestivalVoice getFestivalVoice() {
		return festival_voice;
	}

	/**
	 * Getters for voice
	 */
	public void setFestivalSpeed(FestivalSpeed speed) {
		festival_speed = speed;
	}

	/**
	 * setter for voice
	 */
	public void setFestivalVoice(FestivalVoice voice) {
		festival_voice = voice;
	}

	/**
	 * Method that "speaks" the word or phrase
	 * Only executes the background worker thread if there is no other one being executed
	 * Else adds to a queue to avoid the overlapping of voices
	 * @param speech
	 */
	public void speak(String speech, boolean say_again){
		/*		TODO
		Only makes Festival calls on Linux to avoid issues on other OS
		For development purposes to speed up testing
		 */
		if (System.getProperty("os.name").equals("Linux")) {
			FestivalWorker worker = new FestivalWorker(speech, say_again);
			worker_queue.add(worker);
			if (!locked){
				locked=true;
				worker_queue.get(0).execute();
				worker_queue.remove(0);
			}
		} else {
			if (say_again){
				parent_frame.getDataHandler().writeToScheme(speech, FestivalSpeed.slow, festival_voice);
			} else {
				parent_frame.getDataHandler().writeToScheme(speech, festival_speed, festival_voice);
			}
			System.out.println(speech + " " + festival_speed.getSpeedValue() + " " + festival_voice.getVoiceValue());
		}
	}

	/**
	 * Empties not yet said things
	 * Called when user has already completed quiz or had quit the game
	 * so no point continuing speaking
	 */
	public void emptyWorkerQueue(){
		worker_queue=new ArrayList<FestivalWorker>();
	}

	/**
	 * Subclass of SwingWorker responsible for making Festival calls in the background
	 * Ensures this time consuming task doesn't hold up the ED thread,
	 * and so ensures the GUI doesn't freeze
	 */
	private class FestivalWorker extends SwingWorker<Void, Void> {
		private String speech;//what to speak
		private boolean say_again;

		/**
		 * Constructor
		 * @param speech
		 */
		private FestivalWorker(String speech, boolean say_again){
			this.speech=speech;
			this.say_again=say_again;
		}

		/**
		 * The time-consuming task done on a background worker thread
		 */
		protected Void doInBackground(){
			//writes to .scm file, uses slow speed if say again @author Abby S
			if (say_again){
				parent_frame.getDataHandler().writeToScheme(speech, festival_speed.slow, festival_voice);
			} else {
				parent_frame.getDataHandler().writeToScheme(speech, festival_speed, festival_voice);
			}

			//makes call to festival to execute the scm file in batch mode
			String command = "festival -b "+"\""+parent_frame.getResourceFileLocation()+"festival.scm"+"\"";
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			try {
				Process p = pb.start();
				p.waitFor(); //waits for the festival call to finish before proceeding so as to avoid the speaking overlapping
			} catch (IOException e){
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * When the background task completes
		 */
		protected void done(){
			locked=false;
//			System.out.println("unlocked");
			if (!worker_queue.isEmpty()){
				locked=true;
				//executes the next worker (i.e. Festival call) in the queue
				worker_queue.get(0).execute();
				worker_queue.remove(0);
			}
		}
	}
}
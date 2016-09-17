package voxspell;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingWorker;

/**
 * Singleton class responsible for making the festival calls
 * Based on Abby's A2 code
 *
 */
public class Festival {
	private static Voxspell parent_frame;

	private static Festival instance=null;

	//initialised by readfiles in FileIO
	private static FestivalSpeed festival_speed;
	private static FestivalVoice festival_voice;

	//queuing of SwingWorkers to avoid voice overlapping
	//inspired by http://stackoverflow.com/questions/22412544/swingworker-queue-and-single-using
	private static ArrayList<FestivalWorker> worker_queue=new ArrayList<FestivalWorker>();
	//flag to indicate if there is already a worker excuting Festival calls
	private static boolean locked=false;

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

		public double getSpeedValue(){
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
		Kiwi, American;

		public String getVoiceValue(){
			switch(this){
			case Kiwi:
				return "akl_nz_jdt_diphone";
			default: //American
				return "kal_diphone";
			}
		}
	}

	/**
	 * Getters and setters for speed and voice
	 * @return
	 */
	public FestivalSpeed getFestivalSpeed() {
		return festival_speed;
	}

	public FestivalVoice getFestivalVoice() {
		return festival_voice;
	}

	public void setFestivalSpeed(FestivalSpeed speed) {
		festival_speed = speed;
	}

	public void setFestivalVoice(FestivalVoice voice) {
		festival_voice = voice;
	}

	/**
	 * Method that "speaks" the word or phrase
	 * Only executes the background worker thread if there is no other one being executed
	 * Else adds to a queue to avoid the overlapping of voices
	 * @param speech
	 */
	public void speak(String speech){
		//Only makes Festival calls if the OS is Linux to avoid issues when using Windows
		//Just for development purposes to speed up testing
		if (System.getProperty("os.name").equals("Linux")) {
			FestivalWorker worker = new FestivalWorker(speech);
			worker_queue.add(worker);
			if (!locked){
				locked=true;
				worker_queue.get(0).execute();
				worker_queue.remove(0);
			}
		} else {
			System.out.println(speech);
		}
	}

	/**
	 * Subclass of SwingWorker responsible for making Festival calls in the background
	 * Ensures this time consuming task doesn't hold up the ED thread,
	 * and so ensures the GUI doesn't freeze
	 */
	static class FestivalWorker extends SwingWorker<Void, Void> {
		//what the speak
		private String speech;

		/**
		 * Constructor
		 * @param speech
		 */
		private FestivalWorker(String speech){
			this.speech=speech;
		}

		/**
		 * The time-consuming task done on a background worker thread
		 */
		protected Void doInBackground(){
			//writes to scm file
			parent_frame.getFileIO().writeToScheme(speech, festival_speed, festival_voice);

			//makes call to festival to execute the scm file in batch mode
			String command = "festival -b "+parent_frame.getResourceFileLocation()+"festival.scm";
			ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
			try {
				Process p = pb.start();
				p.waitFor(); //waits for the festival call to finish before proceeding as to avoid the speaking overlapping
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
			if (!worker_queue.isEmpty()){
				locked=true;
				//executes the next worker (i.e. Festival call) in the queue
				worker_queue.get(0).execute();
				worker_queue.remove(0);
			}
		}
	}
}
package voxspell;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingWorker;

//class responsible for making the festival calls
//Based on Abby's code.
public class Festival {
	private static FestivalSpeed festival_speed=FestivalSpeed.slow;
	private static FestivalVoice festival_voice=FestivalVoice.Kiwi;
	private static Voxspell parent_frame;
	private static Festival instance=null;
	private static ArrayList<FestivalWorker> worker_queue=new ArrayList<FestivalWorker>();
	private static boolean locked=false;

	public static Festival getInstance(Voxspell parent){
		if (instance==null){
			instance=new Festival(parent);
		}
		return instance;
	}

	private Festival(Voxspell parent){
		parent_frame = parent;
	}

	public void speak(String speech){
		if (System.getProperty("os.name").equals("Linux")) {
			FestivalWorker worker = new FestivalWorker(speech);
			worker_queue.add(worker);
			if (!locked){
				locked=true;
				worker_queue.get(0).execute();
				worker_queue.remove(0);
			}
		}else {
			System.out.println(speech);
		}
	}

	public void setFestivalSpeed(FestivalSpeed speed) {
		festival_speed = speed;
	}

	public void setFestivalVoice(FestivalVoice voice) {
		festival_voice = voice;
	}

	public enum FestivalSpeed {
		slow, normal, fast;

		public double getSpeedValue(){
			switch(this){
			case slow:
				return 2.5;
			case fast:
				return 0.75;
			default:
				return 1.0;
			}
		}
	}

	public enum FestivalVoice {
		Kiwi, American;

		public String getVoiceValue(){
			switch(this){
			case Kiwi:
				return "akl_nz_jdt_diphone";
			default:
				return "kal_diphone";
			}
		}
	}

	static class FestivalWorker extends SwingWorker<Void, Void>{
		private String speech;

		private FestivalWorker(String speech){
			this.speech=speech;
		}

		protected Void doInBackground(){
			//String command = "echo " + speech + "| festival --tts";
			parent_frame.getFileIO().writeToScheme(speech, festival_speed, festival_voice);
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

		protected void done(){
			locked=false;
			if (!worker_queue.isEmpty()){
				locked=true;
				worker_queue.get(0).execute();
				worker_queue.remove(0);
			}
		}
	}
}

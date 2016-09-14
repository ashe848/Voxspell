package voxspell;

import java.io.IOException;
import java.nio.DoubleBuffer;

import javax.swing.SwingWorker;

public class Festival {
	private static FestivalSpeed festival_speed=FestivalSpeed.normal;
	private static FestivalVoice festival_voice=FestivalVoice.American;
	private static Voxspell parent_frame;

	public Festival(Voxspell parent){
		parent_frame = parent;
//		System.out.println("(Parameter.set 'Duration_Stretch " + festival_speed.getSpeedValue() +")");
	}

	//class responsible for making the festival calls
	public void speak(String speech){
		if (System.getProperty("os.name").equals("Lsinux")) {
			//Was Abby's code.
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				protected Void doInBackground(){
					//String command = "echo " + speech + "| festival --tts";
					parent_frame.getFileIO().writeToScheme(speech, festival_speed, festival_voice);

					String command = "festival -b festival.scm";
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
			};
			worker.execute();
		}
		else {
			System.out.println(speech);
			parent_frame.getFileIO().writeToScheme(speech, festival_speed, festival_voice);
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
				return 1.5;
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
}

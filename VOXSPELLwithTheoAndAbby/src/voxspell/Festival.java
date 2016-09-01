package voxspell;

import java.io.IOException;

import javax.swing.SwingWorker;

public class Festival {
	
	//class responsible for making the festival calls
	ProcessBuilder pb;
	public void speak(String speech){
		if (System.getProperty("os.name").equals("Linux")) {
			String command = "echo " + speech + "| festival --tts";
			pb = new ProcessBuilder("bash", "-c", command);
			Worker worker = new Worker();
			worker.execute();
		}
		else {
			System.out.println(speech);
		}
	}
	
	class Worker extends SwingWorker<Void,Void>{
		protected Void doInBackground(){
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
	}
}

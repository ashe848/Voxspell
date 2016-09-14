package voxspell;

import java.io.IOException;

import javax.swing.SwingWorker;

public class Festival {
	
	//class responsible for making the festival calls
	public void speak(String speech){
		if (System.getProperty("os.name").equals("Lsinux")) {
			//Was Abby's code.
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				protected Void doInBackground(){
					String command = "echo " + speech + "| festival --tts";
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
		}
	}
	

}

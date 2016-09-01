package voxspell;

import java.io.IOException;

public class Festival {
	//class responsible for making the festival calls

	public void speak(String speech){
		if (System.getProperty("os.name").equals("Linux")) {
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
		}
		else {
			System.out.println(speech);
		}
	}
}

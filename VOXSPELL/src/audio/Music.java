package audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Singleton class responsible for controlling Music
 * 
 * @author Abby S
 */
public class Music {
	private static Clip clip;

	/**
	 * Sets up background audio if it isn't already playing
	 * Audio chosen for background celebratory music on quiz completion. 
	 * Smile (For A Bit) by The Orchestral Movement of 1932 (c) copyright 2009 Licensed under a Creative Commons Attribution (3.0) license. http://dig.ccmixter.org/files/jacksontorreal/22341 Ft: Fourstones
	 * Free to use in commercial projects.
	 * 
	 * Found from http://beta.ccmixter.org/playlist/browse/40708?offset=10
	 * 
	 * Original author comment:
	 * Smile for a bit, because everyone deserves to.
	 * The second mix that I made for submission. This song is more my poppy, upbeat, sound. If you're wondering, the title comes from a line from a movie wherein one character was urged to smile more. 
	 * I don't know why but I found that moment stuck with me. In any case, please enjoy.
	 * 
	 * @author Abby S
	 */
	public static void setupBackgroundMusic(String audio_name) {
		if(clip==null){
			File audio_file=new File(audio_name);

			//Modified from http://stackoverflow.com/a/11025384
			try {
				AudioInputStream stream = AudioSystem.getAudioInputStream(audio_file);
				AudioFormat format = stream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				clip = (Clip) AudioSystem.getLine(info);
				clip.open(stream);
				clip.start();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Stops the audio clip
	 * Resets clip to null, should different music be played next time
	 * @author Abby S
	 */
	public static void closeClip() {
		clip.close();
		clip = null;		
	}
}
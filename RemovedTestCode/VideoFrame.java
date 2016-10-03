package voxspell;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import voxspell.Voxspell.PanelID;

@SuppressWarnings("serial")

/**
 * Video reward JPanel class
 * Contains video panel, stop+play+play/pause button
 * and return to quiz complete screen button.
 * Based on Nasser's ACP VLCJ code
 */
public class VideoFrame extends JFrame{
	static String video;
	//Component on panel used to show video
	private EmbeddedMediaPlayerComponent media_player_component;
	//Panel that video is shown on
	private JPanel panel;
	//Video player contained in the component
	private EmbeddedMediaPlayer player;
	
	private int duration;
	private Timer timer;
	private JProgressBar progressBar_1;

	/**
	 * Constructor initialising the size and layout of jpanel
	 * Also sets up buttons used to manipulate playback.
	 */
	public VideoFrame(Voxspell parent){
		super();
		setSize(800,600);

		if (System.getProperty("os.name").equals("Linux")) {
			video = System.getProperty("user.dir")+"/rewardvideos/"+parent.getDataHandler().video_name;
			setupPlayer();	
		} else {
			
			System.out.println(video);
		}	
	}

	/**
	 * Sets up the video to be played at the top of the panel, in its own JPanel
	 */
	private void setupPlayer() {
		JFrame frame = this; //just use parent_frame wasn't working?

		media_player_component = new EmbeddedMediaPlayerComponent();
		player = media_player_component.getMediaPlayer();

		panel = new JPanel(null);
		panel.add(media_player_component);
		media_player_component.setSize(800,439);
		media_player_component.setLocation(0,0);

		frame.setContentPane(panel); //sticking new panel on the frame that program is on

		player.playMedia(video);
	}

}
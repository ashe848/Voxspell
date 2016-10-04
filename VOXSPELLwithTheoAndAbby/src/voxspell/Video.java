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

@SuppressWarnings({ "static-access", "serial" })

/**
 * Video reward JPanel class
 * Contains video panel, stop+play+play/pause button
 * and return to quiz complete screen button.
 * Based on Nasser's ACP VLCJ code
 */
public class Video extends JPanel{
	private Voxspell parent_frame; //link to parent frame

	private String video;
	//Panel that video is shown on
	private JPanel panel;

	//Component on panel used to show video
	private EmbeddedMediaPlayerComponent media_player_component;
	//Video player contained in the component
	private EmbeddedMediaPlayer player;

	private int duration;
	private Timer timer;
	private JProgressBar progressbar;

	/**
	 * Constructor initialising the size and layout of jpanel
	 * Also sets up buttons used to manipulate playback.
	 */
	public Video(Voxspell parent){
		super();
		setSize(800,600);
		setLayout(null);

		parent_frame=parent;

		video = System.getProperty("user.dir")+"/rewardvideos/"+parent_frame.getDataHandler().video_name;
		setupPlayer();
		setupTimer();
		setupProgressBar();
		setupStartButton();
		setupPauseButton();
		setupStopButton();
		setupBackButton();
	}

	/**
	 * Sets up the video to be played at the top of the panel, in its own JPanel
	 */
	private void setupPlayer() {
		JFrame frame = parent_frame; //just use parent_frame wasn't working?

		media_player_component = new EmbeddedMediaPlayerComponent();
		player = media_player_component.getMediaPlayer();

		panel = new JPanel(null);
		panel.add(media_player_component);
		media_player_component.setSize(800,439);
		media_player_component.setLocation(0,0);

		frame.setContentPane(panel); //sticking new panel on the frame that program is on

		player.playMedia(video);
	}

	private void setupProgressBar(){
		player.parseMedia();

		duration=(int)player.getLength()/1000;
		//Set max to duration in seconds
		progressbar = new JProgressBar(0,duration);
		progressbar.setForeground(Color.ORANGE);
		progressbar.setBackground(Color.WHITE);
		progressbar.setBounds(50, 458, 700, 14);
		progressbar.setValue(0);
		panel.add(progressbar);
	}

	/**
	 * From Nasser
	 */
	private void setupTimer(){
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int time = (int)(player.getTime()/1000);
				progressbar.setValue(time);
				System.out.println(time);
			}
		});
		timer.start();
	}

	/**
	 * Creates start button for video on panel and sets its functionality
	 */
	private void setupStartButton() {
		ImageIcon start_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "play_button.png");
		JButton start_button = new JButton("", start_button_image);
		start_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.start();
			}
		});

		panel.add(start_button);
		start_button.setSize(50,50);
		start_button.setLocation(50,500);
	}

	/**
	 * Creates stop button for video panel and creates functionality
	 */
	private void setupStopButton() {
		ImageIcon stop_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "stop_button.png");
		JButton stop_button = new JButton("", stop_button_image);
		stop_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.stop();
			}
		});

		panel.add(stop_button);
		stop_button.setSize(50,50);
		stop_button.setLocation(110,500);
	}

	/**
	 * Creates pause/play button for video on panel
	 */
	private void setupPauseButton() {
		ImageIcon pause_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "pause_button.png");
		JButton pause_button = new JButton("", pause_button_image);
		pause_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.pause();
			}
		});

		panel.add(pause_button);
		pause_button.setSize(50,50);
		pause_button.setLocation(180,500);
	}

	/**
	 * Back button to return to previous panel
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.stop();
				timer.stop();
				parent_frame.changePanel(PanelID.QuizComplete);
			}
		});

		panel.add(back_button);
		back_button.setSize(50,50);
		back_button.setLocation(700,500);
	}
}
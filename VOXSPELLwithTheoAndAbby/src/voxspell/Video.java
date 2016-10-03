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
public class Video extends JPanel{
	private static Voxspell parent_frame; //link to parent frame
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
	public Video(Voxspell parent){
		super();
		setSize(800,600);
		setLayout(null);

		parent_frame=parent;

		if (System.getProperty("os.name").equals("Linux")) {
			video = System.getProperty("user.dir")+"/rewardvideos/"+parent_frame.getDataHandler().video_name;
			setupPlayer();
			setupTimer();
			setupProgressBar();
			setupStartButton();
			setupPauseButton();
			setupStopButton();
			setupBackButton();
		} else {
			System.out.println(video);
		}	
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

		/*ffmpeg -i reward_video.avi  \ 
		 * -vf vflip,hflip,negate,reverse,drawtext="text='CONGRATULATIONS ON GOOD QUIZ': \ 
		 * fontsize=40: box=1: boxcolor=white@0.8: x=(w-text_w)/2: y=(h-text_h)/2"  \ 
		 * -af aecho,areverse,ashowinfo ffmpeg_reward_video.avi
		 * 
		 * Skeleton from Nasser's Lecture 9 slides
		 * Filters from `ffmpeg -filters`
		 * drawtext code from http://stackoverflow.com/questions/17623676/text-on-video-ffmpeg
		 * video = parent_frame.getResourceFileLocation()+"ffmpeg_reward_video.avi";
		 */
		player.playMedia(video);
	}

	private void setupProgressBar(){
		player.parseMedia();

		duration=(int)player.getLength()/1000;
		//Set max to duration in seconds
		progressBar_1 = new JProgressBar(0,duration);
		progressBar_1.setForeground(Color.ORANGE);
		progressBar_1.setBackground(Color.WHITE);
		progressBar_1.setBounds(50, 458, 700, 14);
		progressBar_1.setValue(0);
		panel.add(progressBar_1);
	}

	/**
	 * From Nasser
	 */
	private void setupTimer(){
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int time = (int)(player.getTime()/1000);
				progressBar_1.setValue(time);
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
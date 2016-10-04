package voxspell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import voxspell.Voxspell.PanelID;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import java.awt.Color;

public class WindowBuilderVideo extends JPanel {

	private static Voxspell parent_frame; //link to parent frame
	static String video;
	//Component on panel used to show video
	private EmbeddedMediaPlayerComponent media_player_component;
	//Panel that video is shown on
	private JPanel panel;
	//Video player contained in the component
	private EmbeddedMediaPlayer player;
	private final JProgressBar progressBar = new JProgressBar();

	/**
	 * Constructor initialising the size and layout of jpanel
	 * Also sets up buttons used to manipulate playback.
	 */
	public WindowBuilderVideo(Voxspell parent){
		super();
		setSize(800,600);
		setLayout(null);

		parent_frame=parent;

		
			video = System.getProperty("user.dir")+"/rewardvideos/"+parent_frame.getDataHandler().video_name;
			setupPlayer();
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

		panel = this;
		panel.add(media_player_component);
		media_player_component.setSize(800,439);
		media_player_component.setLocation(0,0);

		frame.setContentPane(panel); //sticking new panel on the frame that program is on

		//Getting .avi video file from the resources folder
		//        String video;

		//Different video displayed if they get all right, then if they get 1 wrong
		/*boolean perfect_quiz = parent_frame.getDataHandler().getLatestWordResults().get(2).size()==0;
		if(perfect_quiz){
			video = parent_frame.getResourceFileLocation()+"reward_video.avi";
		} else { //1 wrong
			 
			 ffmpeg -i reward_video.avi  \ 
			 * -vf vflip,hflip,negate,reverse,drawtext="text='CONGRATULATIONS ON GOOD QUIZ': \ 
			 * fontsize=40: box=1: boxcolor=white@0.8: x=(w-text_w)/2: y=(h-text_h)/2"  \ 
			 * -af aecho,areverse,ashowinfo ffmpeg_reward_video.avi
			 * 
			 * Skeleton from Nasser's Lecture 9 slides
			 * Filters from `ffmpeg -filters`
			 * drawtext code from http://stackoverflow.com/questions/17623676/text-on-video-ffmpeg
			
			 
			video = parent_frame.getResourceFileLocation()+"ffmpeg_reward_video.avi";
		}*/
		player.playMedia(video);
	}
	
	private void setupProgressBar(){
		
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
		progressBar.setBounds(0, 0, 0, 0);
		panel.add(progressBar);

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
				parent_frame.changePanel(PanelID.QuizComplete);
			}
		});

		panel.add(back_button);
		back_button.setSize(50,50);
		back_button.setLocation(700,500);
		
		JProgressBar progressBar_1 = new JProgressBar();
		progressBar_1.setForeground(Color.ORANGE);
		progressBar_1.setBounds(50, 458, 700, 14);
		add(progressBar_1);
	}
}

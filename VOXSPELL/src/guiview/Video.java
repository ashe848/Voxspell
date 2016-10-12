package guiview;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import voxspell.VoxMouseAdapter;
import voxspell.Voxspell;
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

	//Panel that video is shown on. Must be a separate panel rather than "this"
	private JPanel panel;

	private String video;
	//Component on panel used to show video
	private EmbeddedMediaPlayerComponent media_player_component;
	//Video player contained in the component
	private EmbeddedMediaPlayer player;

	private int duration;
	private Timer timer;
	private JProgressBar progress_bar;

	/**
	 * Constructor initialising the size and layout of jpanel
	 * Also sets up buttons used to manipulate playback.
	 */
	public Video(Voxspell parent){
		setSize(1366,745);
		setLayout(null);
		setBackground(new Color(235, 235, 235));

		parent_frame=parent;
		panel = new JPanel(null);

		video = System.getProperty("user.dir")+"/rewardvideos/"+parent_frame.getDataHandler().getVideoName();

		setupTitle();
		setupPlayer();
		setupTimer();
		setupProgressBar();
		setupStartButton();
		setupPauseButton();
		setupStopButton();
		setupBackButton();
	}

	/**
	 * Sets up the title to be the name of the video
	 * @author Abby S
	 */
	private void setupTitle() {
		JLabel title = new JLabel(parent_frame.getDataHandler().getVideoName());
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setForeground(new Color(254, 157, 79));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(32, 24, 1284, 119);
		panel.add(title);
	}

	/**
	 * Sets up the video to be played at the top of the panel, in its own JPanel
	 */
	private void setupPlayer() {
		JFrame frame = parent_frame; //just using parent_frame doesn't work

		media_player_component = new EmbeddedMediaPlayerComponent();
		player = media_player_component.getMediaPlayer();

		panel.add(media_player_component);
		media_player_component.setBounds(32, 169, 1140, 496);

		frame.setContentPane(panel); //sticking new panel on the frame that program is on

		player.playMedia(video);
	}

	/**
	 * Displays progress through the video
	 * Updates once per second as it's just for indicative purposes
	 * @author Abby S
	 */
	private void setupProgressBar(){
		player.parseMedia();

		//get length of video
		duration=(int)player.getLength()/1000;
		//Set max to duration in seconds
		progress_bar = new JProgressBar(0,duration);
		progress_bar.setForeground(new Color(254, 157, 79));
		progress_bar.setBackground(Color.WHITE);
		progress_bar.setBorderPainted(false);
		progress_bar.setBounds(32, 675, 1140, 23);
		progress_bar.setValue(0); //start at beginning
		panel.add(progress_bar);
	}

	/**
	 * Based on Nasser's code
	 */
	private void setupTimer(){
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int time = (int)(player.getTime()/1000); //get time in seconds
				progress_bar.setValue(time);
			}
		});
		timer.start();
	}

	/**
	 * Creates start button for video on panel and sets its functionality
	 */
	private void setupStartButton() {
		ImageIcon start_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "play.png");
		JButton start_button = new JButton("", start_button_image);
		start_button.setBorderPainted(false);
		start_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.start();
			}
		});
		start_button.addMouseListener(new VoxMouseAdapter(start_button,null));
		panel.add(start_button);
		start_button.setBounds(1216, 169, 100, 100);
	}

	/**
	 * Creates pause button for video on panel and sets its functionality
	 */
	private void setupPauseButton() {
		ImageIcon pause_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "pause.png");
		JButton pause_button = new JButton("", pause_button_image);
		pause_button.setBorderPainted(false);
		pause_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.pause();
			}
		});
		pause_button.addMouseListener(new VoxMouseAdapter(pause_button,null));
		panel.add(pause_button);
		pause_button.setBounds(1216, 279, 100, 100);
	}

	/**
	 * Creates stop button for video panel and sets its functionality
	 */
	private void setupStopButton() {
		ImageIcon stop_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "stop.png");
		JButton stop_button = new JButton("", stop_button_image);
		stop_button.setBorderPainted(false);
		stop_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.stop();
			}
		});
		stop_button.addMouseListener(new VoxMouseAdapter(stop_button,null));
		panel.add(stop_button);
		stop_button.setBounds(1216, 389, 100, 100);
	}

	/**
	 * Back button to return to quiz complete panel
	 * Also stops the timer and video
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.setBorderPainted(false);
		back_button.setContentAreaFilled(false);
		back_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.stop();
				timer.stop();
				parent_frame.changePanel(PanelID.QuizComplete);
			}
		});
		back_button.addMouseListener(new VoxMouseAdapter(back_button,null));
		panel.add(back_button);
		back_button.setBounds(1216, 598, 100, 100);
	}
}
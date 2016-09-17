package voxspell;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import voxspell.Voxspell.PanelID;

@SuppressWarnings("serial")

/**
 * Video reward
 * Based on Nasser's ACP VLCJ code
 */
public class Video extends JPanel{
	private static Voxspell parent_frame;
	
	private EmbeddedMediaPlayerComponent media_player_component;
	private JPanel panel;
	
	/**
	 * Constructor
	 */
	public Video(Voxspell parent){
		super();
		setSize(800,600);
		setLayout(null);

		parent_frame=parent;
		
		setupPlayer();
		setupBackButton();
	}

	private void setupPlayer() {
		JFrame frame = parent_frame; //don't need this? just use parent_frame?
		
        media_player_component = new EmbeddedMediaPlayerComponent();
        final EmbeddedMediaPlayer player = media_player_component.getMediaPlayer();

        panel = new JPanel(null);
        panel.add(media_player_component);
        media_player_component.setSize(400,400);
        media_player_component.setLocation(0,0);
        
        frame.setContentPane(panel);

        String video = parent_frame.getResourceFileLocation()+"big_buck_bunny_1_minute.avi";
        player.playMedia(video);
        
        setupStartButton(player);
        setupPauseButton(player);
		setupStopButton(player);
		
		
		/*
		 *         JButton btnMute = new JButton("Shh....");
		        panel.add(btnMute, BorderLayout.NORTH);
		        btnMute.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						video.mute();
					}
				});
		        
		        JButton btnSkip = new JButton("Hurry up!");
		        btnSkip.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						video.skip(5000);
					}
				});
		        panel.add(btnSkip, BorderLayout.EAST);

		        JButton btnSkipBack = new JButton("Say what!?!?");
		        btnSkipBack.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						video.skip(-5000);
					}
				});
		        panel.add(btnSkipBack, BorderLayout.WEST);
		        
		        JLabel labelTime = new JLabel("0 seconds");
		        panel.add(labelTime, BorderLayout.SOUTH);

		        Timer timer = new Timer(50, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						long time = (long)(video.getTime()/1000.0);
						labelTime.setText(String.valueOf(time));
					}
				});
		        timer.start();
		        
		        frame.setLocation(100, 100);
		        frame.setSize(1050, 600);
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.setVisible(true);
		 */
	}

	private void setupStartButton(final EmbeddedMediaPlayer player) {
		ImageIcon start_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton start_button = new JButton("", start_button_image);
		start_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.start();
			}
		});
		
		panel.add(start_button);
		start_button.setSize(50,50);
		start_button.setLocation(500,50);
	}
	
	private void setupPauseButton(final EmbeddedMediaPlayer player) {
		ImageIcon pause_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton pause_button = new JButton("", pause_button_image);
		pause_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.pause();
			}
		});
		
		panel.add(pause_button);
		pause_button.setSize(50,50);
		pause_button.setLocation(600,0);
	}

	private void setupStopButton(final EmbeddedMediaPlayer player) {
		ImageIcon stop_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton stop_button = new JButton("", stop_button_image);
		stop_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				player.stop();
			}
		});
		
		panel.add(stop_button);
		stop_button.setSize(50,50);
		stop_button.setLocation(550,50);
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
				parent_frame.changePanel(PanelID.QuizComplete);
			}
		});
		
		panel.add(back_button);
		back_button.setSize(50,50);
		back_button.setLocation(700,500);
	}
}
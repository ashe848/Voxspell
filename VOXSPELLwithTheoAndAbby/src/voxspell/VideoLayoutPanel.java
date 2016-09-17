package voxspell;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

//import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
//import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import voxspell.Voxspell.PanelID;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")

/**
 * Video reward
 * Based on Nasser's ACP VLCJ code
 */
public class VideoLayoutPanel extends JPanel{
	private static Voxspell parent_frame;
	
	private JTextArea media_player_component;
	private JPanel panel;
	
	/**
	 * Constructor
	 */
	public VideoLayoutPanel(Voxspell parent){
		super();
		setSize(800,600);
		setLayout(null);

		parent_frame=parent;
		
		setupPlayer();
		setupBackButton();
	}

	private void setupPlayer() {
		JFrame frame = parent_frame; //don't need this? just use parent_frame?
		
        media_player_component = new JTextArea();
//        final EmbeddedMediaPlayer player = media_player_component.getMediaPlayer();

        panel = this;
        panel.add(media_player_component);
        media_player_component.setSize(800,439);
        media_player_component.setLocation(0,0);
        
        frame.setContentPane(panel);

//        String video = parent_frame.getResourceFileLocation()+"big_buck_bunny_1_minute.avi";
//        player.playMedia(video);
        
        setupStartButton();
        setupPauseButton();
		setupStopButton();
		setupAccuracyRateLabel();
		
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
		 */
	}

	private void setupStartButton() {
		ImageIcon start_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton start_button = new JButton("", start_button_image);
		start_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				player.start();
			}
		});
		
		panel.add(start_button);
		start_button.setSize(50,50);
		start_button.setLocation(21,500);
	}
	
	private void setupPauseButton() {
		ImageIcon pause_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton pause_button = new JButton("", pause_button_image);
		pause_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				player.pause();
			}
		});
		
		panel.add(pause_button);
		pause_button.setSize(50,50);
		pause_button.setLocation(82,500);
	}

	private void setupStopButton() {
		ImageIcon stop_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton stop_button = new JButton("", stop_button_image);
		stop_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				player.stop();
			}
		});
		
		panel.add(stop_button);
		stop_button.setSize(50,50);
		stop_button.setLocation(177,500);
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
		
		JLabel lblTimeTotal = new JLabel("Time / Total");
		lblTimeTotal.setBounds(711, 449, 79, 15);
		add(lblTimeTotal);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(20, 449, 681, 14);
		add(progressBar);
		
		JButton button = new JButton("<<", null);
		button.setBounds(269, 500, 50, 50);
		add(button);
		
		JButton button_1 = new JButton(">>", null);
		button_1.setBounds(330, 500, 50, 50);
		add(button_1);
		
		JButton btnMute = new JButton("Mute", null);
		btnMute.setBounds(415, 500, 50, 50);
		add(btnMute);
	}
	
	/**
	 * To display accuracy rates for level user is currently on
	 */
	private void setupAccuracyRateLabel() {
		JLabel accuracy_rate_label = new JLabel(parent_frame.getDataHandler().getAccuracyRates()); 
		accuracy_rate_label.setFont(new Font("Courier New", Font.BOLD, 10));

		add(accuracy_rate_label);
		accuracy_rate_label.setLocation(50, 530);
		accuracy_rate_label.setSize(400, 30);
		accuracy_rate_label.setOpaque(true);	
	}
}
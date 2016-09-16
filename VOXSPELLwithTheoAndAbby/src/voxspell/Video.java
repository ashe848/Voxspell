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

public class Video extends JPanel{
	private static Voxspell parent_frame;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private JPanel panel;
	
	public Video(Voxspell parent){
		super();
		setSize(800,600);
		setLayout(null);

		parent_frame=parent;
		setupPlayer();
		setupBackButton();
	}

	private void setupPlayer() {
		JFrame frame = parent_frame;
		

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        final EmbeddedMediaPlayer video = mediaPlayerComponent.getMediaPlayer();
        
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.add(mediaPlayerComponent, BorderLayout.CENTER);
        panel = new JPanel(null);
        panel.add(mediaPlayerComponent);
        mediaPlayerComponent.setSize(400,400);
        mediaPlayerComponent.setLocation(0,0);
        
        frame.setContentPane(panel);
        

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

        String filename = parent_frame.getResourceFileLocation()+"big_buck_bunny_1_minute.avi";
        video.playMedia(filename);		
        
        ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);
		
		back_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.pause();
			}
		});
		
		panel.add(back_button);
		back_button.setSize(50,50);
		back_button.setLocation(600,0);
		
		ImageIcon asdfg = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton asdf = new JButton("", asdfg);
		
		asdf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.stop();
			}
		});
		
		panel.add(asdf);
		asdf.setSize(50,50);
		asdf.setLocation(550,50);
		
		ImageIcon as = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton a = new JButton("", as);
		
		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.start();
			}
		});
		
		panel.add(a);
		a.setSize(50,50);
		a.setLocation(500,50);
		
	}
	
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

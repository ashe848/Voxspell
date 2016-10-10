package windowbuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class Video extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Video frame = new Video();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Video() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1366, 787);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(Color.WHITE);

		JTextPane txtpnBottom = new JTextPane();
		txtpnBottom.setText("bottom");
		txtpnBottom.setBackground(Color.GREEN);
		txtpnBottom.setBounds(0, 708, 1350, 40);
		contentPane.add(txtpnBottom);

		JButton Quit = new JButton("Quit");
		Quit.setBounds(1216, 598, 100, 100);
		contentPane.add(Quit);
		
		JButton Title = new JButton("Video Name");
		Title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		Title.setBounds(32, 24, 1284, 119);
		contentPane.add(Title);
		
		JButton btnVideo = new JButton("Video");
		btnVideo.setBounds(32, 169, 1140, 496);
		contentPane.add(btnVideo);
		
		JButton btnProgressBar = new JButton("Progress Bar");
		btnProgressBar.setBounds(32, 675, 1140, 23);
		contentPane.add(btnProgressBar);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(1216, 169, 100, 100);
		contentPane.add(btnStart);
		
		JButton btnPause = new JButton("Pause");
		btnPause.setBounds(1216, 279, 100, 100);
		contentPane.add(btnPause);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setBounds(1216, 389, 100, 100);
		contentPane.add(btnStop);
	}

}

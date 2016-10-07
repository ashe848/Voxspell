package windowbuilder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class MainMenuPanel extends JPanel {

	Image bg_image;
	JPanel contentPane=this;
	/**
	 * Create the panel.
	 */
	public MainMenuPanel() {
		super();
		setSize(800,747);
		setLayout(null);
		setupBackground();

		//new JButton("", new ImageIcon("C:/Users/Abby Shen/Desktop/206/Graphics/Final/Button Bold.png"));


		
		JButton btnQuiz = new JButton("", new ImageIcon("C:/Users/Abby Shen/Desktop/206/Graphics/Final/Button Bold.png"));
		btnQuiz.setBounds(306, 598, 177, 100);
		contentPane.add(btnQuiz);
		
		JButton button = new JButton("", new ImageIcon("C:/Users/Abby Shen/Desktop/206/Graphics/Final/Button Bold.png"));
		button.setBounds(598, 598, 177, 100);
		contentPane.add(button);
		
		JButton button_1 = new JButton("", new ImageIcon("C:/Users/Abby Shen/Desktop/206/Graphics/Final/Button Bold.png"));
		button_1.setBounds(890, 598, 177, 100);
		contentPane.add(button_1);
		
		JTextPane txtpnBottom = new JTextPane();
		txtpnBottom.setText("bottom");
		txtpnBottom.setBackground(Color.GREEN);
		txtpnBottom.setBounds(0, 708, 1350, 40);
		contentPane.add(txtpnBottom);
		
		
		JButton button_2 = new JButton("", new ImageIcon("C:/Users/Abby Shen/Desktop/206/Graphics/Final/Button Bold.png"));
		button_2.setBounds(617, 72, 177, 100);
		contentPane.add(button_2);
		
		JButton button_3 = new JButton("", new ImageIcon("C:/Users/Abby Shen/Desktop/206/Graphics/Final/Button Bold.png"));
		button_3.setBounds(792, 72, 177, 100);
		contentPane.add(button_3);
		
		JButton button_4 = new JButton("", new ImageIcon("C:/Users/Abby Shen/Desktop/206/Graphics/Final/Button Bold.png"));
		button_4.setBounds(965, 72, 177, 100);
		contentPane.add(button_4);
		
		
		JButton button_5 = new JButton("Quiz");
		button_5.setBounds(1216, 598, 100, 100);
		contentPane.add(button_5);
		
		JButton button_6 = new JButton("Quiz");
		button_6.setBounds(45, 72, 100, 100);
		contentPane.add(button_6);

	}

	
	private void setupBackground(){
		try {
			bg_image = ImageIO.read(new File("C:/Users/Abby Shen/Desktop/206/Graphics/Final/Background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setLocation(0,0);
		setSize(800, 600);
	}

	/**
	 * Overriding the paintComponent method to place Voxspell background on main menu
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bg_image, 0, 0, this);
	}
}

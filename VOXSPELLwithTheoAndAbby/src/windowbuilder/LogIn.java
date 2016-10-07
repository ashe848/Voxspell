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
import javax.swing.JTextArea;

public class LogIn extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogIn frame = new LogIn();
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
	public LogIn() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(441, 100, 683, 787);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(235, 235, 235));
		
		JTextPane txtpnBottom = new JTextPane();
		txtpnBottom.setText("bottom");
		txtpnBottom.setBackground(Color.GREEN);
		txtpnBottom.setBounds(0, 708, 683, 40);
		contentPane.add(txtpnBottom);

		JButton Quit = new JButton("Quit");
		Quit.addMouseListener(new VoxMouseAdapter(Quit, null));
		Quit.setBounds(247, 598, 177, 100);
		contentPane.add(Quit);
		
		JButton Title = new JButton("Log In");
		Title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		Title.setBounds(32, 24, 606, 119);
		contentPane.add(Title);
		
		JButton btnYourName = new JButton("Your Name:");
		btnYourName.setFont(new Font("Arial", Font.PLAIN, 25));
		btnYourName.setBounds(32, 527, 172, 52);
		contentPane.add(btnYourName);
		
		JButton btnNameField = new JButton("name field");
		btnNameField.setBounds(214, 527, 424, 52);
		contentPane.add(btnNameField);
		
		JTextArea txtrTextArea = new JTextArea();
		txtrTextArea.setText("text area");
		txtrTextArea.setBounds(32, 164, 606, 353);
		contentPane.add(txtrTextArea);
	}
}

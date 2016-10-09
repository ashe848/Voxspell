package windowbuilder;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class Help extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Help frame = new Help();
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
	public Help() {
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
		
		JButton Title = new JButton("Help");
		Title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		Title.setBounds(32, 24, 606, 119);
		contentPane.add(Title);
		
		JTextArea txtrTextArea = new JTextArea();
		txtrTextArea.setText("text area");
		txtrTextArea.setBounds(32, 164, 606, 534);
		contentPane.add(txtrTextArea);
	}

}

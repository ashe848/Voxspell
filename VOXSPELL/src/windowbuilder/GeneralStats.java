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

public class GeneralStats extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GeneralStats frame = new GeneralStats();
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
	public GeneralStats() {
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
		
		JButton Title = new JButton("All Stats for All/Cur Session");
		Title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		Title.setBounds(32, 24, 1136, 119);
		contentPane.add(Title);
		
		JButton btnHelp = new JButton("Help");
		btnHelp.setBounds(1216, 24, 100, 100);
		contentPane.add(btnHelp);
		
		
		
		
		
		
		JButton btnAccuracyRates = new JButton("AbbyShen [Level: This level is for tomatoes] [Attempted: 12/300 words] [Didn't get: 402]");
		btnAccuracyRates.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		btnAccuracyRates.setBounds(32, 630, 1136, 68);
		contentPane.add(btnAccuracyRates);
		
		JButton btnTable = new JButton("Table");
		btnTable.setBounds(32, 165, 1136, 443);
		contentPane.add(btnTable);
	}

}

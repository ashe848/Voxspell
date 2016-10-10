package windowbuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import java.awt.Font;

public class Quiz extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Quiz frame = new Quiz();
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
	public Quiz() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1366, 787);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JTextPane txtpnBottom = new JTextPane();
		txtpnBottom.setText("bottom");
		txtpnBottom.setBackground(Color.GREEN);
		txtpnBottom.setBounds(0, 708, 1350, 40);
		contentPane.add(txtpnBottom);

		JButton Quit = new JButton("Quit");
		Quit.setBounds(1216, 598, 100, 100);
		contentPane.add(Quit);
		
		JButton Title = new JButton("Quiz: Level n");
		Title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		Title.setBounds(32, 24, 1136, 119);
		contentPane.add(Title);
		
		JButton btnHelp = new JButton("Help");
		btnHelp.setBounds(1216, 24, 100, 100);
		contentPane.add(btnHelp);
		
		
		
		
		
		
		
		
		
		JButton btnTextArea = new JButton("Text Area");
		btnTextArea.setBounds(32, 222, 1284, 252);
		contentPane.add(btnTextArea);
		
		JButton btnprogressBar = new JButton("Progress Bar");
		btnprogressBar.setBounds(32, 170, 1284, 34);
		contentPane.add(btnprogressBar);
		
		JButton btnSpellHere = new JButton("Spell Here");
		btnSpellHere.setBounds(32, 484, 332, 74);
		contentPane.add(btnSpellHere);
		
		JButton btnAttemptField = new JButton("Attempt Field");
		btnAttemptField.setBounds(374, 484, 942, 74);
		contentPane.add(btnAttemptField);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(32, 598, 177, 100);
		contentPane.add(btnSubmit);
		
		JButton btnSayAgagin = new JButton("Say Again");
		btnSayAgagin.setBounds(667, 598, 177, 100);
		contentPane.add(btnSayAgagin);
		
		JButton btnAddToReview = new JButton("Add To Review");
		btnAddToReview.setBounds(374, 598, 177, 100);
		contentPane.add(btnAddToReview);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(919, 600, 154, 40);
		contentPane.add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(919, 658, 154, 40);
		contentPane.add(comboBox_1);	
	}
}

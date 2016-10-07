package windowbuilder;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

/*
 * 
 * 
 * CHANGE COLOUR ON CLICK
 * 
 * 
 */
public class Settings extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Settings frame = new Settings();
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
	public Settings() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1366, 787);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(235, 235, 235));

		JTextPane txtpnBottom = new JTextPane();
		txtpnBottom.setText("bottom");
		txtpnBottom.setBackground(Color.GREEN);
		txtpnBottom.setBounds(0, 708, 1350, 40);
		contentPane.add(txtpnBottom);

		JButton Quit = new JButton("Quit");
		Quit.addMouseListener(new VoxMouseAdapter(Quit, null));
		Quit.setBounds(1216, 598, 100, 100);
		contentPane.add(Quit);
		
		JButton Title = new JButton("Settings");
		Title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		Title.setBounds(32, 24, 1284, 119);
		contentPane.add(Title);
		
		JLabel lblChangeVoice = new JLabel("Change Voice (can also change during quizzes)");
		lblChangeVoice.setFont(new Font("Arial", Font.PLAIN, 20));
		lblChangeVoice.setBounds(32, 169, 517, 30);
		contentPane.add(lblChangeVoice);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(32, 209, 210, 50);
		contentPane.add(comboBox);
		
		JLabel lblChangeSpeakingSpeed = new JLabel("Change Speaking Speed (can also change during quizzes)");
		lblChangeSpeakingSpeed.setFont(new Font("Arial", Font.PLAIN, 20));
		lblChangeSpeakingSpeed.setBounds(32, 294, 517, 30);
		contentPane.add(lblChangeSpeakingSpeed);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(32, 334, 210, 50);
		contentPane.add(comboBox_1);
		
		JLabel lblChangePreferredNumber = new JLabel("Change Preferred Number of Words in a Quiz");
		lblChangePreferredNumber.setFont(new Font("Arial", Font.PLAIN, 20));
		lblChangePreferredNumber.setBounds(32, 422, 517, 30);
		contentPane.add(lblChangePreferredNumber);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(32, 462, 210, 50);
		contentPane.add(comboBox_2);
		
		JLabel lblChangeLevelFor = new JLabel("Change Level (for Current List)");
		lblChangeLevelFor.setFont(new Font("Arial", Font.PLAIN, 20));
		lblChangeLevelFor.setBounds(32, 552, 517, 30);
		contentPane.add(lblChangeLevelFor);
		
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setBounds(32, 592, 210, 50);
		contentPane.add(comboBox_3);
		
		JLabel lblOtherSettingsMay = new JLabel("^ other settings will not be saved ^");
		lblOtherSettingsMay.setForeground(Color.RED);
		lblOtherSettingsMay.setFont(new Font("Arial", Font.PLAIN, 20));
		lblOtherSettingsMay.setBounds(543, 668, 299, 30);
		contentPane.add(lblOtherSettingsMay);
		
		VoxButton btnClearStatsFor = new VoxButton("Clear stats for current list");
		btnClearStatsFor.setBackground(Color.RED);
		btnClearStatsFor.changeMouseEventColor(Color.BLACK);
		btnClearStatsFor.setFont(new Font("Calibri Light", Font.PLAIN, 20));
		btnClearStatsFor.setBounds(543, 462, 299, 50);
		contentPane.add(btnClearStatsFor);
		
		VoxButton btnResetSettingsData = new VoxButton("Reset settings data to default");
		btnResetSettingsData.setFont(new Font("Calibri Light", Font.PLAIN, 20));
		btnResetSettingsData.setBackground(Color.RED);
		btnResetSettingsData.changeMouseEventColor(Color.BLACK);
		btnResetSettingsData.setBounds(543, 533, 299, 50);
		contentPane.add(btnResetSettingsData);
		
		VoxButton btnResetAllMy = new VoxButton("Reset all my stats");
		btnResetAllMy.setBackground(Color.RED);
		btnResetAllMy.setForeground(Color.WHITE);
		btnResetAllMy.changeMouseEventColor(Color.BLACK);
		btnResetAllMy.setFont(new Font("Calibri Light", Font.BOLD, 20));
		btnResetAllMy.setBounds(543, 605, 299, 50);
		contentPane.add(btnResetAllMy);
		
		JLabel lblCurrentListIs = new JLabel("Current list: NZCER-spelling-lists.txt");
		lblCurrentListIs.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCurrentListIs.setFont(new Font("Arial", Font.PLAIN, 20));
		lblCurrentListIs.setBounds(655, 169, 661, 30);
		contentPane.add(lblCurrentListIs);
		
		JLabel lblWillChangeTo = new JLabel("Will change to NZCER-spelling-lists.txt on save");
		lblWillChangeTo.setForeground(new Color(254, 157, 79));
		lblWillChangeTo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWillChangeTo.setFont(new Font("Arial", Font.PLAIN, 18));
		lblWillChangeTo.setBounds(655, 246, 661, 30);
		contentPane.add(lblWillChangeTo);
		
		JLabel label = new JLabel("Will change to NZCER-spelling-lists.txt on save");
		label.setForeground(new Color(254, 157, 79));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		label.setBounds(655, 395, 661, 30);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Current list: NZCER-spelling-lists.txt");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setFont(new Font("Arial", Font.PLAIN, 20));
		label_1.setBounds(655, 318, 661, 30);
		contentPane.add(label_1);
		
		VoxButton btnChooseSpellingList = new VoxButton("Choose Spelling List");
		btnChooseSpellingList.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		btnChooseSpellingList.setBounds(1038, 200, 278, 46);
		contentPane.add(btnChooseSpellingList);
		
		VoxButton btnChooseRewardVideo = new VoxButton("Choose Reward Video");
		btnChooseRewardVideo.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		btnChooseRewardVideo.setBounds(1038, 348, 278, 46);
		contentPane.add(btnChooseRewardVideo);
	}
}

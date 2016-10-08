package windowbuilder;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class MainMenu extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu frame = new MainMenu();
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
	public MainMenu() {
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














		JButton Help = new JButton("Help");
		Help.setBounds(45, 72, 100, 100);
		contentPane.add(Help);

		
		JTextArea txtrAbbys = new JTextArea();
		txtrAbbys.setWrapStyleWord(true);
		txtrAbbys.setEditable(false);
		txtrAbbys.setLineWrap(true);;
		txtrAbbys.setText("AbbyS\n\n");
		txtrAbbys.append("List Name:\nSome List\n\n");
		txtrAbbys.append("Level:\nMy Level Name\n\n");
		txtrAbbys.append("SLKdgasopgihsodgawoejgsdldgjsdgearystrhrtshffhdffg\n\n");
		txtrAbbys.append("Total Words:\n412 Name\n\n");
		txtrAbbys.append("Attempted:\n303\n\n");
		txtrAbbys.append("Didn't Get:\n303\n\n");
		txtrAbbys.setBounds(45, 319, 137, 389);
		txtrAbbys.setOpaque(false);
		contentPane.add(txtrAbbys);
		
		JTextPane txtpnLeftBounds = new JTextPane();
		txtpnLeftBounds.setBackground(Color.GREEN);
		txtpnLeftBounds.setText("Left Bounds");
		txtpnLeftBounds.setBounds(0, 0, 198, 708);
		contentPane.add(txtpnLeftBounds);

		JTextPane txtpnTopBounds = new JTextPane();
		txtpnTopBounds.setBackground(Color.GREEN);
		txtpnTopBounds.setText("top bounds");
		txtpnTopBounds.setBounds(192, 0, 984, 47);
		contentPane.add(txtpnTopBounds);

		JButton btnQuiz = new JButton("Quiz");
		btnQuiz.setBounds(306, 598, 177, 100);
		contentPane.add(btnQuiz);

		JButton Review = new JButton("Quiz");
		Review.setBounds(598, 598, 177, 100);
		contentPane.add(Review);

		JButton Builder = new JButton("Quiz");
		Builder.setBounds(890, 598, 177, 100);
		contentPane.add(Builder);

		JButton Stats = new JButton("Quiz");
		Stats.setBounds(611, 72, 177, 100);
		contentPane.add(Stats);

		JButton Settings = new JButton("Quiz");
		Settings.setBounds(788, 72, 177, 100);
		contentPane.add(Settings);

		JButton login = new JButton("Quiz");
		login.setBounds(965, 72, 177, 100);
		contentPane.add(login);

		JTextPane txtpnRightBounds = new JTextPane();
		txtpnRightBounds.setBackground(Color.GREEN);
		txtpnRightBounds.setText("Right Bounds");
		txtpnRightBounds.setBounds(1171, 0, 179, 708);
		contentPane.add(txtpnRightBounds);
		
		
	}
}

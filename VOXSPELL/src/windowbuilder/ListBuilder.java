package windowbuilder;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class ListBuilder extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListBuilder frame = new ListBuilder();
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
	public ListBuilder() {
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
		
		
		
		JTextArea txtrWordsAdded = new JTextArea();
		txtrWordsAdded.setText("");
		txtrWordsAdded.setEditable(false);
		txtrWordsAdded.setLineWrap(true);
		txtrWordsAdded.setWrapStyleWord(true);
		
		JScrollPane sPane = new JScrollPane(txtrWordsAdded);
		sPane.setBounds(32, 235, 337, 463);
		contentPane.add(sPane);
		
		JLabel lblWordsAdded = new JLabel("Words Added:");
		lblWordsAdded.setFont(new Font("Arial", Font.PLAIN, 25));
		lblWordsAdded.setBounds(32, 180, 223, 45);
		contentPane.add(lblWordsAdded);
		
		JLabel lblNameOfList = new JLabel("Name of List:");
		lblNameOfList.setFont(new Font("Arial", Font.PLAIN, 25));
		lblNameOfList.setBounds(420, 180, 223, 45);
		contentPane.add(lblNameOfList);
		
		JTextField textField = new JTextField();
		textField.setBounds(688, 180, 383, 45);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblWord = new JLabel("Word:");
		lblWord.setFont(new Font("Arial", Font.PLAIN, 25));
		lblWord.setBounds(420, 269, 223, 45);
		contentPane.add(lblWord);
		
		JTextField textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(688, 269, 383, 45);
		contentPane.add(textField_1);
		
		JLabel lblSampleSentenceoptional = new JLabel("Sample sentence (optional):");
		lblSampleSentenceoptional.setFont(new Font("Arial", Font.PLAIN, 25));
		lblSampleSentenceoptional.setBounds(420, 399, 369, 45);
		contentPane.add(lblSampleSentenceoptional);
		
		JTextArea textArea = new JTextArea();
		textArea.setText("");
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		JScrollPane sp = new JScrollPane(textArea);
		sp.setBounds(420, 461, 651, 237);
		contentPane.add(sp);
		
		JButton btnAddThisWord = new JButton("Add This Word");
		btnAddThisWord.setBounds(1139, 180, 177, 100);
		contentPane.add(btnAddThisWord);

		JButton btnSave = new JButton("Save List");
		btnSave.setBounds(1139, 461, 177, 100);
		contentPane.add(btnSave);
		
		JButton btnDiscard = new JButton("Discard");
		btnDiscard.setBounds(1139, 598, 177, 100);
		contentPane.add(btnDiscard);
		
		JButton button = new JButton("Build Your Own List");
		button.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		button.setBounds(32, 24, 1284, 119);
		contentPane.add(button);
		
		
		
		
	}

}

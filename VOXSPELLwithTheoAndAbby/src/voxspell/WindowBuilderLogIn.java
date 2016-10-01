package voxspell;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import voxspell.WindowBuilderInputError.InputError;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;

public class WindowBuilderLogIn extends JFrame {
	private static Voxspell parent_frame;
	private JPanel contentPane;
	private JTextField textField;
	private WindowBuilderLogIn logInFrame=this;

	/**
	 * Create the frame.
	 */
	public WindowBuilderLogIn(Voxspell parent) {
		parent_frame=parent;
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(800, 300, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLogIn = new JLabel("Log In");
		lblLogIn.setBounds(174, 10, 54, 15);
		contentPane.add(lblLogIn);
		
		JLabel lblIfYourName = new JLabel("If your name is in the list, please enter your name to log in");
		lblIfYourName.setBounds(10, 34, 414, 15);
		contentPane.add(lblIfYourName);
		
		JLabel lblElseEnterYour = new JLabel("If not, WELCOME! Enter your name and you will be registered.");
		lblElseEnterYour.setBounds(10, 59, 414, 15);
		contentPane.add(lblElseEnterYour);
		
		
		
		JLabel lblYourName = new JLabel("Your Name:");
		lblYourName.setBounds(10, 395, 77, 15);
		contentPane.add(lblYourName);
		
		textField = new JTextField();
		textField.setBounds(84, 392, 340, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(162, 438, 93, 23);
		contentPane.add(btnOk);
		
		JTextArea txtrRegisteredUsers = new JTextArea();
		JScrollPane scrollBar = new JScrollPane(txtrRegisteredUsers);
		scrollBar.setBounds(10, 84, 414, 284);
		contentPane.add(scrollBar);
		txtrRegisteredUsers.setEditable(false);
		txtrRegisteredUsers.setText("Registered Users (Case Sensitive):\n");
		for (String u:parent_frame.getDataHandler().users){
			txtrRegisteredUsers.append(u+"\n");
		}
		
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username=textField.getText();
				if(!username.matches("[a-zA-Z]+")){
					WindowBuilderInputError input_error_poup = new WindowBuilderInputError(InputError.Alpha);
					input_error_poup.setVisible(true);
					input_error_poup.setAlwaysOnTop(true);
				} else {
					parent_frame.getDataHandler().user=username;
					if(!parent_frame.getDataHandler().users.contains(username)){
						parent_frame.getDataHandler().users.add(username);
					}
					parent_frame.getDataHandler().readUserFiles();
					parent_frame.getDataHandler().readListSpecificFiles();
//					parent_frame.setEnabled(true);
					parent_frame.main_menu.setupAccuracyRateLabel();
					logInFrame.dispose();
				}
			}
		});
	}
}
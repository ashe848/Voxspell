package voxspell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import voxspell.Voxspell.PanelID;

@SuppressWarnings({ "static-access", "serial" })

public class LogIn extends JFrame {
	private static Voxspell parent_frame;
	private JPanel contentPane;
	private JTextField textField;
	private LogIn logInFrame=this;

	/**
	 * Create the frame.
	 */
	public LogIn(Voxspell parent) {
		parent_frame=parent;
		setTitle("Log In");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(800, 300, 450, 500);
		
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setupLabels();
		setupRegisteredUsers();
		setupEnterName();
		setupOKButton();
	}

	private void setupLabels() {
		JLabel lblIfYourName = new JLabel("If your name is in the list, please enter your name to log in");
		lblIfYourName.setBounds(10, 34, 414, 15);
		contentPane.add(lblIfYourName);
		
		JLabel lblElseEnterYour = new JLabel("If not, WELCOME! Enter your name and you will be registered.");
		lblElseEnterYour.setBounds(10, 59, 414, 15);
		contentPane.add(lblElseEnterYour);
		
		JLabel label = new JLabel("Registered Users (Case Sensitive):");
		label.setBounds(10, 84, 414, 15);
		contentPane.add(label);
	}

	private void setupRegisteredUsers() {
		JTextArea txtrRegisteredUsers = new JTextArea();
		JScrollPane scrollBar = new JScrollPane(txtrRegisteredUsers);
		scrollBar.setBounds(10, 110, 414, 258);
		contentPane.add(scrollBar);
		txtrRegisteredUsers.setEditable(false);
		txtrRegisteredUsers.setLineWrap(true);
		txtrRegisteredUsers.setWrapStyleWord(true);
		txtrRegisteredUsers.setText("Visitor\n");
		for (String u:parent_frame.getDataHandler().users){
			if (!u.equals("Visitor")){
				txtrRegisteredUsers.append(u+"\n");
			}
		}
	}

	private void setupEnterName() {
		JLabel lblYourName = new JLabel("Your name:");
		lblYourName.setBounds(10, 381, 77, 15);
		contentPane.add(lblYourName);
		
		textField = new JTextField();
		textField.setBounds(84, 378, 340, 21);
		textField.setColumns(10);
		contentPane.add(textField);
	}

	private void setupOKButton() {
		JLabel lblOnlyAlphabeticalCharacters = new JLabel("");
		contentPane.add(lblOnlyAlphabeticalCharacters);
		
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(162, 438, 93, 23);
		contentPane.add(btnOk);
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username=textField.getText();
				if(!username.matches("[a-zA-Z]+")){
					lblOnlyAlphabeticalCharacters.setText("Only alphabetical characters allowed!");
					lblOnlyAlphabeticalCharacters.setBounds(10, 413, 414, 15);
				} else {
					parent_frame.getDataHandler().user=username;
					if(!parent_frame.getDataHandler().users.contains(username)){
						parent_frame.getDataHandler().users.add(username);
						parent_frame.getDataHandler().writeToProgramFiles();
					}
					parent_frame.getDataHandler().readUserFiles();
					parent_frame.getDataHandler().readListSpecificFiles();
					parent_frame.changePanel(PanelID.MainMenu);
					logInFrame.dispose();
				}
			}
		});
	}
}
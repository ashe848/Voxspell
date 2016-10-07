package voxspell;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import voxspell.Voxspell.PanelID;

@SuppressWarnings({ "static-access", "serial" })

public class LogIn extends JFrame {
	private Voxspell parent_frame;
	private JPanel contentPane;
	
	private LogIn logInFrame=this;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public LogIn(Voxspell parent) {
		parent_frame=parent;
		setTitle("Log In");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(560, 100, 683, 787);

		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel title = new JLabel("Log In");
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(32, 24, 606, 119);
		contentPane.add(title);
//		setupLabels();
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
		scrollBar.setBounds(32, 164, 606, 310);
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
		lblYourName.setBounds(32, 484, 172, 52);
		contentPane.add(lblYourName);

		textField = new JTextField();
		textField.setBounds(214, 484, 424, 52);
		textField.setColumns(10);
		contentPane.add(textField);
	}

	private void setupOKButton() {
		final JLabel lblOnlyAlphabeticalCharacters = new JLabel("");
		contentPane.add(lblOnlyAlphabeticalCharacters);

		JButton btnOk = new JButton("OK");
		btnOk.setBounds(247, 598, 177, 100);
		contentPane.add(btnOk);
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username=textField.getText();
				if(!username.matches("[a-zA-Z]+")){
					lblOnlyAlphabeticalCharacters.setText("Only alphabetical characters allowed!");
					lblOnlyAlphabeticalCharacters.setBounds(32, 546, 606, 40);
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
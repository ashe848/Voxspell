package voxspell;

import java.awt.Color;
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
	private JPanel content_pane;
	
	private LogIn log_in_frame=this;
	private JTextField enter_name_field;

	/**
	 * Create the frame.
	 */
	public LogIn(Voxspell parent) {
		parent_frame=parent;
		setTitle("Log In");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(560, 100, 683, 787);

		content_pane = new JPanel();
		setContentPane(content_pane);
		content_pane.setLayout(null);

		setupTitle();
//		setupLabels();
		setupRegisteredUsers();
		setupEnterName();
		setupOKButton();
	}

	private void setupTitle() {
		JLabel title = new JLabel("Log In");
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setForeground(new Color(254, 157, 79));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(32, 24, 606, 119);
		content_pane.add(title);
	}

	private void setupRegisteredUsers() {
		JTextArea registered_users = new JTextArea();
		registered_users.setEditable(false);
		registered_users.setLineWrap(true);
		registered_users.setWrapStyleWord(true);
		registered_users.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		
		registered_users.setText("If your name is in the list, please enter your name to log in\n\nIf not, WELCOME! Enter your name and you will be registered.\n\nRegistered Users (Case Sensitive):\n");
		registered_users.append("Visitor\n");
		for (String u:parent_frame.getDataHandler().users){
			if (!u.equals("Visitor")){
				registered_users.append(u+"\n");
			}
		}
		
		JScrollPane scroll_pane = new JScrollPane(registered_users);
		scroll_pane.setBounds(32, 164, 606, 310);
		content_pane.add(scroll_pane);
	}

	private void setupEnterName() {
		JLabel enter_name_label = new JLabel("Your name:");
		enter_name_label.setBounds(32, 484, 172, 52);
		enter_name_label.setFont(new Font("Arial", Font.PLAIN, 25));
		content_pane.add(enter_name_label);

		enter_name_field = new JTextField();
		enter_name_field.setBounds(214, 484, 424, 52);
		enter_name_field.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		enter_name_field.setColumns(10);
		content_pane.add(enter_name_field);
	}

	private void setupOKButton() {
		final JLabel warning_label = new JLabel("");
		warning_label.setFont(new Font("Arial", Font.PLAIN, 25));
		warning_label.setHorizontalAlignment(SwingConstants.CENTER);
		warning_label.setForeground(new Color(254, 157, 79));
		content_pane.add(warning_label);

		JButton ok_button = new JButton("OK");
		ok_button.setBounds(247, 598, 177, 100);
		content_pane.add(ok_button);
		ok_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username=enter_name_field.getText();
				if(!username.matches("[a-zA-Z]+")){
					warning_label.setText("Only alphabetical characters allowed!");
					warning_label.setBounds(32, 546, 606, 40);
				} else {
					parent_frame.getDataHandler().user=username;
					if(!parent_frame.getDataHandler().users.contains(username)){
						parent_frame.getDataHandler().users.add(username);
						parent_frame.getDataHandler().writeToProgramFiles();
					}
					parent_frame.getDataHandler().readUserFiles();
					parent_frame.getDataHandler().readListSpecificFiles();
					parent_frame.changePanel(PanelID.MainMenu);
					log_in_frame.dispose();
				}
			}
		});
		ok_button.addMouseListener(new VoxMouseAdapter(ok_button,null));
	}
}
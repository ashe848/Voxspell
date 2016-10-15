package visiblegui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import vox.VoxMouseAdapter;
import vox.Voxspell;
import vox.Voxspell.PanelID;

@SuppressWarnings({ "static-access", "serial" })

/**
 * Frame showing registered users
 * And allows user to log in or register
 * 
 * @author Abby S
 */
public class LogIn extends JFrame {
	private Voxspell parent_frame;
	private JPanel content_pane;

	private LogIn log_in_frame=this;
	private JTextField enter_name_field;

	/**
	 * Creates the frame and lays out components
	 * @author Abby S
	 */
	public LogIn(Voxspell parent) {
		parent_frame=parent;
		setTitle("Log In");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(600, 165, 683, 745);

		content_pane = new JPanel();
		setContentPane(content_pane);
		content_pane.setLayout(null);
		content_pane.setBackground(new Color(235, 235, 235));

		setupTitle();
		setupRegisteredUsers();
		setupEnterName();
		setupOKButton();
	}

	/**
	 * Login title
	 * @author Abby S
	 */
	private void setupTitle() {
		JLabel title = new JLabel("Log In");
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setForeground(new Color(254, 157, 79));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(32, 24, 606, 119);
		content_pane.add(title);
	}

	/**
	 * Text area to list registered users
	 * @author Abby S
	 */
	private void setupRegisteredUsers() {
		JTextArea registered_users = new JTextArea();
		registered_users.setEditable(false);
		registered_users.setLineWrap(true);
		registered_users.setWrapStyleWord(true);
		registered_users.setFont(new Font("Calibri Light", Font.PLAIN, 25));

		registered_users.setText("If your name is in the list, please enter your name to log in\n\nIf not, WELCOME! Enter your name and you will be registered.\n\nRegistered Users (Case Sensitive):\n");
		registered_users.append("Visitor\n");
		for (String u:parent_frame.getDataHandler().getUsers()){
			if (!u.equals("Visitor")){
				registered_users.append(u+"\n");
			}
		}

		JScrollPane scroll_pane = new JScrollPane(registered_users);
		scroll_pane.setBounds(32, 164, 606, 310);
		content_pane.add(scroll_pane);
	}

	/**
	 * Field for user to enter their name
	 * @author Abby S
	 */
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

	/**
	 * OK button
	 * @author Abby S
	 */
	private void setupOKButton() {
		//for error in entering name
		final JLabel error_label = new JLabel("");
		error_label.setFont(new Font("Arial", Font.PLAIN, 25));
		error_label.setHorizontalAlignment(SwingConstants.CENTER);
		error_label.setForeground(new Color(254, 157, 79));
		content_pane.add(error_label);

		ImageIcon ok_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "ok.png");
		JButton ok_button = new JButton("",ok_button_image);
		ok_button.setBounds(247, 598, 177, 100);
		content_pane.add(ok_button);
		ok_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username=enter_name_field.getText();
				if(!username.matches("[a-zA-Z]+")){
					error_label.setText("Only alphabetical characters allowed!");
					error_label.setBounds(32, 546, 606, 40);
				} else {
					parent_frame.getDataHandler().setUser(username);
					//registers user if they're new. Saves them having to go to a separate register screen
					if(!parent_frame.getDataHandler().getUsers().contains(username)){
						parent_frame.getDataHandler().getUsers().add(username);
						parent_frame.getFileWritingHandler().writeToProgramFiles();
					}
					parent_frame.getFileReadingHandler().readUserFiles();
					parent_frame.getFileReadingHandler().readListSpecificFiles();
					parent_frame.changePanel(PanelID.MainMenu);
					log_in_frame.dispose();
				}
			}
		});
		ok_button.addMouseListener(new VoxMouseAdapter(ok_button,null));
	}
}
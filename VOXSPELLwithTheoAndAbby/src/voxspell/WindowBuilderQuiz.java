package voxspell;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import voxspell.Voxspell.PanelID;
import javax.swing.JComboBox;

public class WindowBuilderQuiz extends JPanel {


	private Voxspell parent_frame;
	private Image bg_image;
	private PanelID quiz_type; //Distinguishes if quiz is normal or review quiz

	private JTextArea display_to_user; //progress text area to show previous information
	private JTextField input_from_user; //what user puts as guess for spelling quiz

	private ArrayList<String> words_to_spell; //list of words to spell in quiz
	private int current_word_number; //indicates which word the user is up to in quiz
	private int current_attempt_number; //indicates which attempt user is up to when spelling
	private boolean attempted_once; //flag indicating which attempt user is up to
	//	TODO: change back to 10 after testing
	private int words_in_quiz=3; //number of words in each quiz

	private ArrayList<String> words_mastered; //list of words user got first try in quiz
	private ArrayList<String> words_faulted; //list of words user got second try in quiz
	private ArrayList<String> words_failed; //list of words user didn't get right in quiz


	public WindowBuilderQuiz(){
		setSize(800,600);
		setLayout(null);
		
		JLabel title_to_display = new JLabel(quiz_type.toString()+" (Level: "+parent_frame.getDataHandler().getCurrentLevel()+")"); 
		title_to_display.setFont(new Font("Courier New", Font.BOLD, 50));

		add(title_to_display);
		title_to_display.setLocation(50, 20);
		title_to_display.setSize(700, 50);
		title_to_display.setOpaque(false);

		display_to_user = new JTextArea();
		display_to_user.setFont(new Font("Courier New", Font.BOLD, 18));
		display_to_user.setEditable(false);
		display_to_user.setOpaque(true);

		JScrollPane scrolling_pane = new JScrollPane(display_to_user);
		add(scrolling_pane);
		scrolling_pane.setSize(700, 250);
		scrolling_pane.setLocation(50, 80);
		scrolling_pane.setBackground(Color.WHITE);

		JLabel spell_here_text = new JLabel("SPELL HERE");
		spell_here_text.setFont(new Font("Courier New", Font.BOLD, 30));

		add(spell_here_text);
		spell_here_text.setLocation(50, 340);
		spell_here_text.setSize(300, 50);
		spell_here_text.setOpaque(false);

		input_from_user = new JTextField();
		input_from_user.setFont(new Font("Courier New", Font.BOLD, 25));
		input_from_user.setEditable(true);
		input_from_user.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				input_from_user.requestFocusInWindow();
			}
		});

		add(input_from_user);
		input_from_user.setSize(400, 40);
		input_from_user.setLocation(280, 340);

		ImageIcon submit_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "submit_button.png");
		JButton submit_button = new JButton("", submit_button_image);
		submit_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				input_from_user.requestFocusInWindow();
			}
		});

		add(submit_button);
		submit_button.setSize(300,150);
		submit_button.setLocation(50,400);

		ImageIcon sayagain_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "sayagain_button_alt.png");
		JButton sayagain_button = new JButton("", sayagain_button_image);
		sayagain_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.getFestival().speak(words_to_spell.get(current_word_number));
			}
		});

		add(sayagain_button);
		sayagain_button.setSize(150,150);
		sayagain_button.setLocation(450,400);


		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);

		add(back_button);
		back_button.setSize(50,50);
		back_button.setLocation(700,500);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(622, 400, 140, 40);
		add(comboBox);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(622, 450, 140, 40);
		add(comboBox_1);


	}	
}

package voxspell;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import voxspell.Voxspell.PanelID;

@SuppressWarnings("serial")
public class StatsChooser extends JPanel{
	private static Voxspell parent_frame;
	private static FileIO file_handler;
	private static JTable table;


	public StatsChooser(Voxspell parent){
		super();
		setSize(800,600);
		setLayout(null);

		parent_frame=parent;
		file_handler=FileIO.getInstance(parent_frame);
		
		setupTitle();
		setupPersistentAllButton();
		setupPersistentLevelButton();
		setupSessionAllButton();
		setupSessionLevelButton();
		setupBackButton();
		setupAccuracyRateLabel();
	}
	
	private void setupAccuracyRateLabel() {
		JLabel accuracy_rate_label = new JLabel(parent_frame.getFileIO().getAccuracyRates()); 
		accuracy_rate_label.setFont(new Font("Courier New", Font.BOLD, 10));

		add(accuracy_rate_label);
		accuracy_rate_label.setLocation(50, 550);
		accuracy_rate_label.setSize(700, 50);
		accuracy_rate_label.setOpaque(true);
		
	}
	private void setupTitle() {
		JLabel title = new JLabel("Choose type of statistics");
		title.setFont(new Font("Courier New", Font.BOLD, 50));
		title.setBounds(20, 39, 760, 87);
		add(title);
	}
	
	private void setupPersistentAllButton(){
		ImageIcon persistent_all_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "persistent_all_button.png");

		JButton persistent_all_button = new JButton("", persistent_all_button_image);
		persistent_all_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.PersistentAllStats);
//				parent_frame.changePanel(PanelID.Video);
			}
		});
		
		add(persistent_all_button);
		persistent_all_button.setFont(new Font("Arial", Font.PLAIN, 10));;
		persistent_all_button.setSize(200,100);
		persistent_all_button.setLocation(50, 200);
	}
	
	private void setupPersistentLevelButton(){
		ImageIcon persistent_level_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "persistent_level_button.png");

		JButton persistent_level_button = new JButton("", persistent_level_button_image);
		persistent_level_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.PersistentLevelStats);
			}
		});
		
		add(persistent_level_button);
		persistent_level_button.setSize(200,100);
		persistent_level_button.setLocation(350, 200);
	}
	
	private void setupSessionAllButton(){
		ImageIcon session_all_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "session_all_button.png");

		JButton session_all_button = new JButton("", session_all_button_image);
		session_all_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.SessionAllStats);
			}
		});
		
		add(session_all_button);
		session_all_button.setSize(200,100);
		session_all_button.setLocation(50, 350);
	}
	
	private void setupSessionLevelButton(){
		ImageIcon session_level_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "session_level_button.png");

		JButton session_level_button = new JButton("", session_level_button_image);
		session_level_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.SessionLevelStats);
			}
		});
		
		add(session_level_button);
		session_level_button.setSize(200,100);
		session_level_button.setLocation(350, 350);
	}
	
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);
		
		back_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.MainMenu);
			}
		});
		
		add(back_button);
		back_button.setSize(50,50);
		back_button.setLocation(700,500);
	}
	
	public enum StatsType{
		Session, Persistent;
	}
}

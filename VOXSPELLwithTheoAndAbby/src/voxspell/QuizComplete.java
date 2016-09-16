package voxspell;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import voxspell.Voxspell.PanelID;

public class QuizComplete extends JPanel{
	private Voxspell parent_frame;
	private static ArrayList<String> latest_mastered_words;
	private static ArrayList<String> latest_faulted_words;
	private static ArrayList<String> latest_failed_words;
	
	public QuizComplete(Voxspell parent){
		setSize(800,600);
		setLayout(null);

		parent_frame = parent;
		latest_mastered_words=parent_frame.getFileIO().getLatestWordResults().get(0);
		latest_faulted_words=parent_frame.getFileIO().getLatestWordResults().get(1);
		latest_failed_words=parent_frame.getFileIO().getLatestWordResults().get(2);
		setupTable();
		setupTitle();
		determineButtons();
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
		JLabel lblQuizCompleted = new JLabel("Quiz completed");
		lblQuizCompleted.setFont(new Font("Courier New", Font.BOLD, 50));
		lblQuizCompleted.setBounds(38, 39, 469, 87);
		add(lblQuizCompleted);
	}

	private void setupTable() {
		String[] columnNames = {};
		int rowCount = 0;

		DefaultTableModel model = new DefaultTableModel(columnNames, rowCount){
			public boolean isCellEditable(int row, int col) {
				return false; //so users can't change their stats
			}

			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				default:
					return Integer.class;
				}
			}
		};
		JTable table = new JTable(model);
		Object[] mastered_col=new Object[latest_mastered_words.size()];
		model.addColumn("Mastered ("+latest_mastered_words.size()+")", latest_mastered_words.toArray(mastered_col));
		Object[] faulted_col=new Object[latest_mastered_words.size()];
		model.addColumn("Faulted ("+latest_faulted_words.size()+")", latest_faulted_words.toArray(faulted_col));
		Object[] failed_col=new Object[latest_mastered_words.size()];
		model.addColumn("Failed ("+latest_failed_words.size()+")", latest_failed_words.toArray(failed_col));
		table.setModel(model);

		//http://stackoverflow.com/a/7433758
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(String.class, centerRenderer);
		table.setDefaultRenderer(Integer.class, centerRenderer);


		//adds scroll pane to table
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
		scrollPane.setVisible(true);
		scrollPane.setBounds(38, 168, 469, 382);
	}

	private void determineButtons(){
		//complete level when 50% attempted with no fails
		if (parent_frame.getFileIO().halfAttempted() && parent_frame.getFileIO().noReview() && parent_frame.getFileIO().getCurrentLevel()<parent_frame.getFileIO().getNumberOfLevels()){
			setupLevelUpButton();
		}
		
		//at most 1 incorrect
		if(latest_failed_words.size()<2) {
			setupVideoButton();
		}
	}

	private void setupLevelUpButton() {
		JButton btnUpLvl = new JButton("UP LVL");
		btnUpLvl.setBounds(550, 39, 200, 200);
		btnUpLvl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.getFileIO().increaseLevel();
				System.out.println("Level: "+parent_frame.getFileIO().getCurrentLevel());
				btnUpLvl.setVisible(false);
			}
		});
		add(btnUpLvl);
		btnUpLvl.setVisible(true);
	}
	
	
	private void setupVideoButton() {
		JButton button = new JButton("VIDEO");
		button.setBounds(550, 268, 200, 200);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("video");
			}
		});
		add(button);		
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
	
	
}

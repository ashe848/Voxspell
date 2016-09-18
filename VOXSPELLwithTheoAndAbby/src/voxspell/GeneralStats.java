package voxspell;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import voxspell.StatsChooser.StatsType;
import voxspell.Voxspell.PanelID;

@SuppressWarnings("serial")

/**
 * Statistics for all levels
 */
public class GeneralStats extends JPanel {
	private static Voxspell parent_frame;
	private Image bg_image;

	private static JTable table;

	/**
	 * Constructor
	 */
	public GeneralStats(Voxspell parent, StatsType type){
		super();
		setSize(800,600);
		setLayout(null);

		parent_frame=parent;

		setupTable(type);		
		setupBackButton();
		setupAccuracyRateLabel();
		setupBackground(type);
	}

	/**
	 * Creates the JTable for stats
	 * Based on Abby's A2 code
	 * @param type
	 */
	private void setupTable(StatsType type) {
		//creates table model with said column names, currently no rows, and disallowing the editing of cells
		String[] column_names = {"Level","Word","Mastered","Faulted","Failed"};
		int row_count = 0;
		DefaultTableModel model = new DefaultTableModel(column_names, row_count){
			public boolean isCellEditable(int row, int col) {
				return false; //so users can't change their stats
			}

			//types of the columns for correct ordering
			public Class getColumnClass(int column) {
				switch (column) {
				case 1:
					return String.class;
				default:
					return Integer.class;
				}
			}
		};
		table = new JTable(model);

		//adds row for word into table if it has been attempted
		for (int i=0; i<parent_frame.getDataHandler().getNumberOfLevels(); i++){
			for (Object[] o:parent_frame.getDataHandler().returnWordDataForLevel(i, type)){
				if(!(o[2].equals(0)&&o[3].equals(0)&&o[4].equals(0))){
					model.addRow(o);
				}
			}
		}
		table.setModel(model);

		//For ordering
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		table.setRowSorter(sorter);
		ArrayList<RowSorter.SortKey> key = new ArrayList<RowSorter.SortKey>();
		key.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//default is ascending order of first column i.e. by level
		sorter.setSortKeys(key);
		sorter.sort();

		//Disallow reordering of columns
		table.getTableHeader().setReorderingAllowed(false);
		
		//Alignment for the cells http://stackoverflow.com/a/7433758
		DefaultTableCellRenderer alignment_renderer = new DefaultTableCellRenderer();
		alignment_renderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(String.class, alignment_renderer);
		table.setDefaultRenderer(Integer.class, alignment_renderer);

		//adds scroll pane to table to panel
		JScrollPane scroll_pane = new JScrollPane(table);
		add(scroll_pane);
		scroll_pane.setVisible(true);
		scroll_pane.setLocation(50,150);
		scroll_pane.setSize(700,300);
	}

	/**
	 * Back button to return to previous panel
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.StatSelection);
			}
		});

		add(back_button);
		back_button.setSize(50,50);
		back_button.setLocation(700,500);
	}

	/**
	 * To display accuracy rates for level user is currently on
	 */
	private void setupAccuracyRateLabel() {
		JLabel accuracy_rate_label = new JLabel(parent_frame.getDataHandler().getAccuracyRates()); 
		accuracy_rate_label.setFont(new Font("Courier New", Font.BOLD, 10));

		add(accuracy_rate_label);
		accuracy_rate_label.setLocation(50, 500);
		accuracy_rate_label.setSize(300, 50);
		accuracy_rate_label.setVisible(true);
		accuracy_rate_label.setOpaque(true);
	}
	
	/**
	 * Puts the background image, overriding paintComponent method(below) to ensure functionality
	 * @param type 
	 */
	private void setupBackground(StatsType type){
		//http://stackoverflow.com/questions/1466240/how-to-set-an-image-as-a-background-for-frame-in-swing-gui-of-java
		try {
			String background_filename="";
			if (type.equals(StatsType.Persistent)){
				background_filename="general_stats_bg.png";
			} else {
				background_filename="general_stats_current_bg.png";
			}
			bg_image = ImageIO.read(new File(parent_frame.getResourceFileLocation() + background_filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setLocation(0,0);
		setSize(800, 600);
	}
	
	/**
	 * Overriding the paintComponent method to place background
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bg_image, 0, 0, this);
	}
}
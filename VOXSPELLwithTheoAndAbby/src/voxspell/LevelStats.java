package voxspell;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
 * Statistics by levels
 */
public class LevelStats extends JPanel{
	private static Voxspell parent_frame;

	private static JTable table;
	private static TableRowSorter<TableModel> sorter;
	private static JScrollPane scroll_pane;

	/**
	 * Constructor
	 */
	public LevelStats(Voxspell parent, StatsType type){
		super();
		setSize(800,600);
		setLayout(null);

		parent_frame=parent;

		//defaults to user's current level
		refreshTable(parent_frame.getDataHandler().getCurrentLevel(), type);

		setupLevelChooser(type);
		setupBackButton();
		//		TODO
		//		parent_frame.component_maker.setupBackButton(this, PanelID.MainMenu);
		setupAccuracyRateLabel();
	}

	/**
	 * Refreshes table to match the selected level
	 * Unlike the table in GeneralStats, this table doesn't have a level column
	 * Based on Abby's A2 code
	 * @param level
	 * @param type
	 */
	private void refreshTable(int level, StatsType type){
		//creates table model with said column names, currently no rows, and disallowing the editing of cells
		String[] column_names = {"Word","Mastered","Faulted","Failed"};
		int row_count = 0;

		DefaultTableModel model = new DefaultTableModel(column_names, row_count){
			public boolean isCellEditable(int row, int col) {
				return false; //so users can't change their stats
			}

			//types of the columns for correct ordering
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				default:
					return Integer.class;
				}
			}
		};
		table = new JTable(model);

		//adds row for word into table if it has been attempted
		for (Object[] o:parent_frame.getDataHandler().returnWordDataForLevel(level, type)){
			if(!(o[2].equals(0)&&o[3].equals(0)&&o[4].equals(0))){
				model.addRow(new Object[] {o[1], o[2], o[3], o[4]});
			}
		}
		table.setModel(model);

		//For ordering
		sorter = new TableRowSorter<TableModel>(model);
		table.setRowSorter(sorter);
		ArrayList<RowSorter.SortKey> key = new ArrayList<RowSorter.SortKey>();
		key.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//default is ascending order of first column i.e. alphabetical order of words
		sorter.setSortKeys(key);
		sorter.sort();

		//Alignment for the cells http://stackoverflow.com/a/7433758
		DefaultTableCellRenderer alignment_renderer = new DefaultTableCellRenderer();
		alignment_renderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(String.class, alignment_renderer);
		table.setDefaultRenderer(Integer.class, alignment_renderer);

		//adds scroll pane to table to panel
		scroll_pane = new JScrollPane(table);
		add(scroll_pane);
		scroll_pane.setVisible(true);
		scroll_pane.setLocation(50,50);
		scroll_pane.setSize(700,400);
	}

	/**
	 * Removes current table from panel to allow refreshing
	 */
	private void removeTableFromPanel(){
		this.remove(scroll_pane); //removes scroll pane as it contains the table
	}

	/**
	 * Set up level chooser for the number of levels in this word list
	 * @param type
	 */
	private void setupLevelChooser(StatsType type) {
		Integer[] levels = parent_frame.getDataHandler().getLevelArray();
		JComboBox level_chooser = new JComboBox(levels);

		//default to current level
		level_chooser.setSelectedItem(parent_frame.getDataHandler().getCurrentLevel());
		level_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int level=(int)level_chooser.getSelectedItem();
				removeTableFromPanel();
				refreshTable(level,type);			
			}
		});

		add(level_chooser);
		level_chooser.setLocation(50,500);
		level_chooser.setSize(100,50);
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
		accuracy_rate_label.setLocation(200, 530);
		accuracy_rate_label.setSize(400, 30);
		accuracy_rate_label.setOpaque(true);	
	}
}
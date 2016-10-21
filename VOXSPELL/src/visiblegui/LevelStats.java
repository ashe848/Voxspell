package visiblegui;

import java.awt.Color;
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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import visiblegui.StatsChooser.StatsType;
import vox.VoxMouseAdapter;
import vox.Voxspell;
import vox.Voxspell.PanelID;

@SuppressWarnings({ "static-access", "serial", "unchecked", "rawtypes" })

/**
 * JPanel class displaying statistics by level selected
 * by a combo box dropdown, for either all stats or for
 * current session
 */
public class LevelStats extends JPanel{
	private Voxspell parent_frame;

	private JTable table;
	private TableRowSorter<TableModel> sorter;
	private JScrollPane scroll_pane;

	/**
	 * Constructor, initialise panel properties and adding GUI elements
	 */
	public LevelStats(Voxspell parent, StatsType type){
		setSize(1366,745);
		setLayout(null);
		setBackground(new Color(235, 235, 235));

		parent_frame=parent;

		setupTitle(type);

		//defaults to user's current level
		refreshTable(parent_frame.getDataHandler().getCurrentLevel(), type);

		setupLevelChooser(type);
		setupHelpButton();
		setupBackButton();
		setupAccuracyRateLabel();
	}

	/**
	 * Title based on type (all or just current session)
	 * @param type
	 */
	private void setupTitle(StatsType type) {
		JLabel title = new JLabel("");
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setForeground(new Color(254, 157, 79));
		title.setBounds(32, 24, 1136, 119);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		add(title);
		if (type.equals(StatsType.Persistent)){
			title.setText("By Level for All Sessions");
		} else {
			title.setText("By Level for Current Session");
		}
	}

	/**
	 * Refreshes table to match the selected level
	 * Unlike the table in GeneralStats, this table doesn't have a level column
	 * 
	 * Based on Abby's A2 code
	 * 
	 * @param level		word data for level based on combobox
	 * @param type		type of data to fetch (Persistent or Session)
	 */
	private void refreshTable(int level, StatsType type){
		//creates table model with said column names, currently no rows, and disallowing the editing of cells
		String[] column_names = {"Word","Strike!","Spare!","Didn't get it"};
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
			if(!(o[5].equals(0)&&o[3].equals(0)&&o[4].equals(0))){
				model.addRow(new Object[] {o[2], o[3], o[4], o[5]});
			}
		}
		table.setModel(model);

		enhanceTableAppearance(model);
	}

	/**
	 * Essentially adds a visual appearance to the table
	 * Also sets some restrictions on manipulation of the table
	 */
	private void enhanceTableAppearance(DefaultTableModel model) {
		//For ordering
		sorter = new TableRowSorter<TableModel>(model);
		table.setRowSorter(sorter);
		ArrayList<RowSorter.SortKey> key = new ArrayList<RowSorter.SortKey>();
		key.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//default is ascending order of first column i.e. alphabetical order of words
		sorter.setSortKeys(key);
		sorter.sort();

		//Disallow reording of columns
		table.getTableHeader().setReorderingAllowed(false);

		//Alignment for the cells http://stackoverflow.com/a/7433758
		DefaultTableCellRenderer alignment_renderer = new DefaultTableCellRenderer();
		alignment_renderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(String.class, alignment_renderer);
		table.setDefaultRenderer(Integer.class, alignment_renderer);

		table.setFont(new Font("Calibri Light", Font.PLAIN, 20));
		table.setRowHeight(27);

		//adds scroll pane to table to panel
		scroll_pane = new JScrollPane(table);
		add(scroll_pane);
		scroll_pane.setVisible(true);
		scroll_pane.setBounds(32, 165, 1136, 443);
	}

	/**
	 * Removes current table from panel to allow refreshing once user
	 * selects different level to display
	 */
	private void removeTableFromPanel(){
		this.remove(scroll_pane); //removes the scroll pane as it contains the table
	}

	/**
	 * Set up level chooser for the number of levels in this word list
	 * @param type		type of data to fetch for when combobox changes
	 */
	private void setupLevelChooser(final StatsType type) {
		String[] levels = parent_frame.getDataHandler().getLevelArray();
		final JComboBox level_chooser = new JComboBox(levels);
		level_chooser.setFont(new Font("Arial", Font.PLAIN, 20));
		//default to current level
		level_chooser.setSelectedItem(parent_frame.getDataHandler().getLevelNames().get(parent_frame.getDataHandler().getCurrentLevel()));
		level_chooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String choice = (String)level_chooser.getSelectedItem();
				int level=parent_frame.getDataHandler().getLevelNames().indexOf(choice);
				removeTableFromPanel();
				refreshTable(level,type);			
			}
		});
		add(level_chooser);
		level_chooser.setBounds(1189, 293, 127, 50);
		level_chooser.setBackground(Color.WHITE);
	}

	/**
	 * displays popup help menu
	 * @author Abby S
	 */
	private void setupHelpButton() {
		ImageIcon help_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "help.png");
		JButton help_button = new JButton("",help_button_image);
		help_button.setBorderPainted(false);
		help_button.setBounds(1216, 24, 100, 100);
		help_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Help help_frame=new Help(PanelID.PersistentLevelStats);
				help_frame.setVisible(true);
			}
		});
		help_button.addMouseListener(new VoxMouseAdapter(help_button,null));
		add(help_button);
	}

	/**
	 * Back button to return to previous panel
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.setBorderPainted(false);
		back_button.setContentAreaFilled(false);
		back_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.StatSelection);
			}
		});
		back_button.addMouseListener(new VoxMouseAdapter(back_button,null));
		add(back_button);
		back_button.setBounds(1216, 598, 100, 100);
	}

	/**
	 * To display accuracy rates for level user is currently on
	 */
	private void setupAccuracyRateLabel() {
		JLabel accuracy_rate_label = new JLabel(parent_frame.getDataHandler().getAccuracyRates()); 
		accuracy_rate_label.setFont(new Font("Calibri Light", Font.PLAIN, 25));
		accuracy_rate_label.setHorizontalAlignment(SwingConstants.CENTER);
		add(accuracy_rate_label);
		accuracy_rate_label.setBounds(32, 630, 1136, 68);
		accuracy_rate_label.setOpaque(true);	
	}
}
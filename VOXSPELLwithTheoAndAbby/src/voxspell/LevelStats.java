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

import voxspell.StatsChooser.StatsType;
import voxspell.Voxspell.PanelID;

@SuppressWarnings({ "static-access", "serial", "unchecked", "rawtypes" })

/**
 * JPanel class displaying statistics by level selected
 * by a combo box dropdown, for either all stats or for
 * current session
 */
public class LevelStats extends JPanel{
	private Voxspell parent_frame;
	private Image bg_image;

	private JTable table;
	private TableRowSorter<TableModel> sorter;
	private JScrollPane scroll_pane;

	/**
	 * Constructor, initialise panel properties and adding GUI elements
	 */
	public LevelStats(Voxspell parent, StatsType type){
		super();
		setSize(800,600);
		setLayout(null);

		parent_frame=parent;
		
		JLabel title = new JLabel("");
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setBounds(32, 24, 1136, 119);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		add(title);
		if (type.equals(StatsType.Persistent)){
			title.setText("By Level for All Sessions");
		} else {
			title.setText("By Level for Current Session");
		}

		//defaults to user's current level
		refreshTable(parent_frame.getDataHandler().current_level, type);

		setupLevelChooser(type);
		setupBackButton();
		setupAccuracyRateLabel();
//		setupBackground(type);
	}

	/**
	 * Refreshes table to match the selected level
	 * Unlike the table in GeneralStats, this table doesn't have a level column
	 * Based on Abby's A2 code
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
		this.remove(scroll_pane); //removes scroll pane as it contains the table
	}

	/**
	 * Set up level chooser for the number of levels in this word list
	 * @param type		type of data to fetch for when combobox changes
	 */
	private void setupLevelChooser(final StatsType type) {
		String[] levels = parent_frame.getDataHandler().getLevelArray();
		final JComboBox level_chooser = new JComboBox(levels);

		//default to current level
		level_chooser.setSelectedItem(parent_frame.getDataHandler().level_names.get(parent_frame.getDataHandler().current_level));
		level_chooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String choice = (String)level_chooser.getSelectedItem();
				int level=parent_frame.getDataHandler().level_names.indexOf(choice);
				removeTableFromPanel();
				refreshTable(level,type);			
			}
		});

		add(level_chooser);
		level_chooser.setBounds(1189, 293, 127, 50);
		level_chooser.setBackground(Color.WHITE);
	}

	/**
	 * Back button to return to previous panel
	 */
	private void setupBackButton(){
		ImageIcon back_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "back_button.png");
		JButton back_button = new JButton("", back_button_image);
		back_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.StatSelection);
			}
		});

		add(back_button);
		back_button.setBounds(1216, 598, 100, 100);
	}

	/**
	 * To display accuracy rates for level user is currently on
	 */
	private void setupAccuracyRateLabel() {
		JLabel accuracy_rate_label = new JLabel(parent_frame.getDataHandler().getAccuracyRates()); 
		accuracy_rate_label.setFont(new Font("Courier New", Font.BOLD, 10));

		add(accuracy_rate_label);
		accuracy_rate_label.setBounds(32, 630, 1136, 68);
		accuracy_rate_label.setOpaque(true);	
	}

	/**
	 * Puts the background image, overriding paintComponent method(below) to ensure functionality
	 * @param type 	determines which background image is displayed
	 */
	private void setupBackground(StatsType type){
		//http://stackoverflow.com/questions/1466240/how-to-set-an-image-as-a-background-for-frame-in-swing-gui-of-java
		try {
			String background_filename="";
			if (type.equals(StatsType.Persistent)){
				background_filename="level_stats_bg_alt.png";
			} else {
				background_filename="level_stats_current_bg_alt.png";
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
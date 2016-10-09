package voxspell;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import windowbuilder.VoxMouseAdapter;

@SuppressWarnings({ "static-access", "serial", "unchecked", "rawtypes" })

/**
 * JPanel that displays statistics for all levels
 * based on given parameter (persistent, or current session)
 * Shows a table of all the words attempted and allows user
 * to sort by the column headers
 */
public class GeneralStats extends JPanel {
	private Voxspell parent_frame;
	private Image bg_image;

	private JTable table;

	/**
	 * Constructor, initialise panel properties and set up GUI elements
	 */
	public GeneralStats(Voxspell parent, StatsType type){
		setSize(1366,745);
		setLayout(null);
		setBackground(new Color(235, 235, 235));

		parent_frame=parent;

		setupTitle(type);
		setupTable(type);
		setupHelpButton();
		setupBackButton();
		setupAccuracyRateLabel();
	}

	private void setupTitle(StatsType type) {
		JLabel title = new JLabel("");
		title.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 65));
		title.setForeground(new Color(254, 157, 79));
		title.setBounds(32, 24, 1136, 119);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		add(title);
		if (type.equals(StatsType.Persistent)){
			title.setText("All Stats for All Sessions");
		} else {
			title.setText("All Stats for Current Session");
		}
	}

	/**
	 * Creates the JTable for stats
	 * Based on Abby's A2 code
	 * @param type		type of data requested to be displayed
	 */
	private void setupTable(StatsType type) {
		//creates table model with said column names, currently no rows, and disallowing the editing of cells
		String[] column_names = {"Level","Level Name","Word","Strike!","Spare!","Didn't get it"};
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
				case 2:
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
				if(!(o[5].equals(0)&&o[3].equals(0)&&o[4].equals(0))){
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

		table.setFont(new Font("Calibri Light", Font.PLAIN, 20));
		table.setRowHeight(27);
		
		//adds scroll pane to table to panel
		JScrollPane scroll_pane = new JScrollPane(table);
		add(scroll_pane);
		scroll_pane.setVisible(true);
		scroll_pane.setBounds(32, 165, 1136, 443);
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
			@Override
			public void actionPerformed(ActionEvent e) {
				parent_frame.changePanel(PanelID.StatSelection);
			}
		});

		add(back_button);
		back_button.setBounds(1216, 598, 100, 100);
	}
	
	private void setupHelpButton() {
		ImageIcon help_button_image = new ImageIcon(parent_frame.getResourceFileLocation() + "help.png");
		JButton help_button = new JButton("",help_button_image);
		help_button.setBorderPainted(false);
		help_button.setBounds(1216, 24, 100, 100);
		help_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Help help_frame=new Help(PanelID.PersistentAllStats);
				help_frame.setVisible(true);
			}
		});
		help_button.addMouseListener(new VoxMouseAdapter(help_button,null));
		add(help_button);
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
		accuracy_rate_label.setVisible(true);
		accuracy_rate_label.setOpaque(true);
	}

	/**
	 * Overriding the paintComponent method to place background
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(bg_image, 0, 0, this);
	}
}
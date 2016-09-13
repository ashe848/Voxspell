package voxspell;

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
public class LevelStats extends JPanel{
	private static Voxspell parent_frame;
	private static FileIO file_handler;
	private static JTable table;
	private static TableRowSorter<TableModel> sorter;
	private static JScrollPane scrollPane;

	public LevelStats(Voxspell parent, StatsType type){
		super();
		setSize(800,600);
		setLayout(null);

		parent_frame=parent;
		file_handler=FileIO.getInstance(parent_frame);

		refreshTable(1, type);

		String[] levels = {"1","2","3","4","5","6","7","8","9","10","11"};
		JComboBox level_chooser = new JComboBox(levels);
		level_chooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int level=Integer.parseInt((String)level_chooser.getSelectedItem());
				removeTableFromPanel();
				refreshTable(level,type);			}
		});

		add(level_chooser);
		level_chooser.setLocation(50,500);
		level_chooser.setSize(100,50);
		setupBackButton();
	}

	private void removeTableFromPanel(){
		this.remove(scrollPane);
	}

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

	private void refreshTable(int level, StatsType type){
		//creates table model with said column names, currently no rows, and disallowing the editing of cells
		String[] columnNames = {"Word","Mastered","Faulted","Failed"};
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
		table = new JTable(model);

		for (Object[] o:file_handler.returnWordDataForLevel(level, type)){
			if(!(o[2].equals(0)&&o[3].equals(0)&&o[4].equals(0))){
				model.addRow(new Object[] {o[1], o[2], o[3], o[4]});
			}
		}
		table.setModel(model);

		//For ordering
		sorter = new TableRowSorter<TableModel>(model);
		table.setRowSorter(sorter);
		ArrayList<RowSorter.SortKey> key = new ArrayList<RowSorter.SortKey>();
		key.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//default is ascending order of first column ie alphabetical order of words
		sorter.setSortKeys(key);
		sorter.sort();

		//http://stackoverflow.com/a/7433758
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(String.class, centerRenderer);
		table.setDefaultRenderer(Integer.class, centerRenderer);


		//adds scroll pane to table
		scrollPane = new JScrollPane(table);
		add(scrollPane);
		scrollPane.setVisible(true);

		scrollPane.setLocation(50,50);
		scrollPane.setSize(700,400);
	}
}

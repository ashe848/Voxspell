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
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import voxspell.StatsChooser.StatsType;
import voxspell.Voxspell.PanelID;

@SuppressWarnings("serial")
public class GeneralStats extends JPanel {
	private static Voxspell parent_frame;
	private static FileIO file_handler;
	private static JTable table;

	public GeneralStats(Voxspell parent, StatsType type){
		super();
		setSize(800,600);
		setLayout(null);

		parent_frame=parent;
		file_handler=FileIO.getInstance(parent_frame);

		//creates table model with said column names, currently no rows, and disallowing the editing of cells
		String[] columnNames = {"Level","Word","Mastered","Faulted","Failed"};
		int rowCount = 0;
		DefaultTableModel model = new DefaultTableModel(columnNames, rowCount){
			public boolean isCellEditable(int row, int col) {
				return false; //so users can't change their stats
			}
			
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

		for (int i=0; i<file_handler.getNumberOfLevels(); i++){
			for (Object[] o:file_handler.returnWordDataForLevel(i, type)){
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
		key.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));//default is ascending order of first column ie alphabetical order of words
		sorter.setSortKeys(key);
		sorter.sort();
		
		//http://stackoverflow.com/a/7433758
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(String.class, centerRenderer);
		table.setDefaultRenderer(Integer.class, centerRenderer);


		//adds scroll pane to table
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
		scrollPane.setVisible(true);

		scrollPane.setLocation(50,50);
		scrollPane.setSize(700,400);
		
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
}

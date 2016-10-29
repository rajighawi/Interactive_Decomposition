package gui.utilities;


import java.awt.Color;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import renderer.MyHeaderRenderer;
import renderer.MyTableCellRenderer;
import utilities.SwingUtility;

public class MyTable extends JTable {

	public MyTable(){
		super();
		init();
	}
	
	public MyTable(AbstractTableModel model){
		super(model);
		init();
	}
	
	public void init(){
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setFont(SwingUtility.fnt_label2);
		getTableHeader().setForeground(Color.red);
		getTableHeader().setDefaultRenderer(new MyHeaderRenderer());
		setOpaque(true);
		setRowHeight(28);
		setFont(SwingUtility.fnt_data);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setDefaultRenderer(Object.class, new MyTableCellRenderer());
		setColumnSelectionAllowed(false);
		setRowSelectionAllowed(true);		
	}
	
}

package interactive;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import fd.FDSet;
import fd.Relation;
 

public class DescrTableModel extends AbstractTableModel {
	
	private String[] columnNames;
	private Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
 	
	public DescrTableModel(){
		this.columnNames = new String[] {"Relation", "Attributes", "FDs", "2NF", "3NF", "BCNF"};//;columnNames;		
		this.rows = new Vector<Vector<Object>>();
	}
	
	public void describe(Relation r, FDSet fdSet, boolean _2nf, boolean _3nf, boolean bcnf){
	//	rows.removeAllElements();	
		
		Vector<Object> newRow = new Vector<Object>();
		newRow.addElement(r.getName());
		newRow.addElement(r.getAttributes().toString2());
		newRow.addElement(fdSet.toString());
		newRow.addElement(_2nf);
		newRow.addElement(_3nf);
		newRow.addElement(bcnf);
		
		rows.addElement(newRow);
		
		fireTableChanged(null);
	}
	
	//--------------------------------------------------------------------
		
	public Object getValueAt(int rowIndex, int columnIndex) {
		Vector<Object> row = (Vector<Object>) rows.elementAt(rowIndex);
		return row.elementAt(columnIndex);				
	}
	
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public Class<?> getColumnClass(int column) {
		return String.class;
	}

	public Object getSelectedObject(int line) {
		return rows.get(line);
	}

	public void setData(Vector<Vector<Object>> newData) {
		rows = newData;
	}
	
	public String getColumnName(int columnIndex) {
		if (columnNames[columnIndex] != null)
			return columnNames[columnIndex];
		else
			return "";
	}

	public int getRowCount() {
		return rows.size();
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}

	public void clear(){
		rows = new Vector<Vector<Object>>();
		fireTableChanged(null);
	}
	
	
}

package gui.decomposition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import fd.Attribute;
 

public class ArmstrongTableModel extends AbstractTableModel {
	
	private String[] columnNames = { };
	private Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
 	
	public ArmstrongTableModel(String[] columnNames){
		this.columnNames = columnNames;		
	}
	
	public ArmstrongTableModel(){
		this.columnNames = new String[]{};		
	}

	public void load(ArrayList<Attribute> atts, ArrayList<? extends Object[]> tuples){
		rows.removeAllElements();	
		Collections.sort(atts);
		columnNames = new String[atts.size()];		
		for (int i = 0; i < atts.size(); i++) {
			columnNames[i] = atts.get(i).getName();					
		}
		for (int i = 0; i < tuples.size(); i++) {
			Object[] tuple = tuples.get(i);
			Vector<Object> newRow = new Vector<Object>();
						
			for (int j = 0; j < tuple.length; j++) {
				Object value = tuple[j];				
				newRow.addElement(value);
			}
			rows.addElement(newRow);
		}		
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

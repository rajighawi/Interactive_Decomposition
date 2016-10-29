package gui.decomposition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.google.common.collect.HashBasedTable;

import fd.Attribute;
import fd.Relation;
import fd.Symbol;
 

public class TableauTableModel extends AbstractTableModel {
	
	private String[] columnNames = { };
	private Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
 	
	public TableauTableModel(String[] columnNames){
		this.columnNames = columnNames;		
	}
	
	public TableauTableModel(){
		this.columnNames = new String[]{};		
	}

	public void loadTableau(HashBasedTable<Relation, Attribute, Symbol> tableau){
		rows.removeAllElements();	
		ArrayList<Relation> rels = new ArrayList<Relation>(tableau.rowKeySet());
		Collections.sort(rels);
		ArrayList<Attribute> atts = new ArrayList<Attribute>(tableau.columnKeySet());
		Collections.sort(atts);
		columnNames = new String[atts.size()+1];
		columnNames[0] = "";
		for (int i = 0; i < atts.size(); i++) {
			columnNames[i+1] = atts.get(i).getName();					
		}
		for (int i = 0; i < rels.size(); i++) {
			Relation relation = rels.get(i);
			Vector<Object> newRow = new Vector<Object>();
			newRow.addElement(relation.getName());
			
			for (int j = 0; j < atts.size(); j++) {
				Attribute a = atts.get(j);
				Symbol sy = tableau.get(relation, a);
				newRow.addElement(sy);
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

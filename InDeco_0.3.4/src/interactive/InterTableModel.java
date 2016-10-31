package interactive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import utilities.IconUtility;

import com.google.common.collect.HashBasedTable;

import fd.Attribute;
import fd.Relation;
 

public class InterTableModel extends AbstractTableModel {
	
	private String[] columnNames = { };
	private Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
 	
	HashBasedTable<Relation, Attribute, Boolean> tableau;
	ArrayList<Relation> rels;
	ArrayList<Attribute> atts;
	
	public InterTableModel(String[] columnNames){
		this.columnNames = columnNames;		
		tableau = HashBasedTable.create();
		rels = new ArrayList<Relation>();
		atts = new ArrayList<Attribute>();
	}
	
	public InterTableModel(){
		this(new String[]{});		
	}
	
	public void loadTableau(HashBasedTable<Relation, Attribute, Boolean> tableau){
		this.tableau = tableau;
		this.rels = new ArrayList<Relation>(tableau.rowKeySet());
		this.atts = new ArrayList<Attribute>(tableau.columnKeySet());		
		Collections.sort(rels);
		Collections.sort(atts);
		
		columnNames = new String[atts.size()+2];
		columnNames[0] = ""; columnNames[1] = ""; 
		for (int i = 0; i < atts.size(); i++) {
			columnNames[i+2] = atts.get(i).getName();					
		}
		update_();
	}
	
	public void update_(){
		rows.removeAllElements();
		for (int i = 0; i < rels.size(); i++) {
			Relation relation = rels.get(i);
			Vector<Object> newRow = new Vector<Object>();
			newRow.addElement(IconUtility.DELETE);
			newRow.addElement(relation.getName());
			for (int j = 0; j < atts.size(); j++) {
				Attribute a = atts.get(j);
				boolean sy = tableau.get(relation, a);
				newRow.addElement(sy);
			}
			rows.addElement(newRow);
		}		
		fireTableChanged(null);
	}
	
	//--------------------------------------------------------------------
		
	public Boolean getValue(int rowIndex, int columnIndex) {
		if(columnIndex>1 && rowIndex>-1){
			Attribute a = atts.get(columnIndex-2);
			Relation r = rels.get(rowIndex);
			return tableau.get(r, a);
		}
		return null;				
	}
	
	public void setValue(Relation r, Attribute a, boolean v) {
		tableau.put(r, a, v);	
		update_();
	}
	
	public Relation getRelationAt(int rowIndex) {
		if(rowIndex>-1){
			return rels.get(rowIndex);
		}
		return null;				
	}
	
	public Attribute getAttributeAt(int columnIndex) {
		if(columnIndex>1){
			return atts.get(columnIndex-2);
		}
		return null;				
	}
	
	public void deleteRelation(Relation r){
		for(Attribute a:atts){
			tableau.remove(r, a);
		}
		rels.remove(r);
		update_();
	}
	
	public HashBasedTable<Relation, Attribute, Boolean> getTableau() {
		return tableau;
	}
	
	// -------------------------------------------------------------
	
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

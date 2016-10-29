package interactive;

import fd.Attribute;
import fd.Relation;
import gui.utilities.MyTable;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.table.TableColumnModel;

import main.Global;
import renderer.MyTableCellRenderer;
import utilities.SwingUtility;

import com.google.common.collect.HashBasedTable;

public class InterTable extends MyTable {

	//	HashBasedTable<Relation, Attribute, Boolean> tableau;

	InterTableModel interTableModel;

	public InterTable(){
		//tableau = HashBasedTable.create();
		interTableModel = new InterTableModel();
		setModel(interTableModel);
		setDefaultRenderer(Object.class, new MyTableCellRenderer());
		setFont(SwingUtility.fnt_label2);
		//	setAutoCreateRowSorter(true);		
		setColumnSelectionAllowed(false);
		setRowSelectionAllowed(true);
		addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent arg0) {
				Point p = arg0.getPoint();
				boolean dblClick = arg0.getClickCount()==2;
				int col  = InterTable.this.columnAtPoint(p);
				int row = InterTable.this.rowAtPoint(p);

				if (row > -1){ 
					if(dblClick){
						if(col == 0) {		
							Relation r  = interTableModel.getRelationAt(row);
							int opt = JOptionPane.showConfirmDialog(null, "Delete schema `"+r.getName()+"`");
							if(opt==JOptionPane.OK_OPTION){
								interTableModel.deleteRelation(r);
								setModel(interTableModel);	
								updateColumnWidth();
								Global.getInstance().getMainFrame().go();	
							}							
						} else if(col > 1) {
							System.out.println("col="+col+"\trow="+row);
							Relation r  = interTableModel.getRelationAt(row);
							Attribute a = interTableModel.getAttributeAt(col);						
							Boolean oldValue = interTableModel.getValue(row, col);
							System.out.println(r.getName()+"\t"+a.getName()+"\t"+oldValue);

							boolean newValue = !oldValue;						
							interTableModel.setValue(r, a, newValue);
							setModel(interTableModel);	
							updateColumnWidth();
							Global.getInstance().getMainFrame().go();
						}					
					}
				}
			}
		});
	}

	public InterTableModel getInterTableModel() {
		return interTableModel;
	}


	public HashBasedTable<Relation, Attribute, Boolean> getTableau() {
		return interTableModel.getTableau();
	}

	public void load(HashBasedTable<Relation, Attribute, Boolean> tableau){
		interTableModel.loadTableau(tableau);
		setModel(interTableModel);
		updateColumnWidth();
	}

	public void clear(){
		interTableModel.clear();
		setModel(interTableModel);
	}

	public void updateColumnWidth(){
		TableColumnModel tcm = getColumnModel();
		tcm.getColumn(0).setMinWidth(32);
		tcm.getColumn(0).setMaxWidth(32);
	}

}

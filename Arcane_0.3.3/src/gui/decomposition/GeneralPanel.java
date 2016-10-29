package gui.decomposition;

import fd.Decomposition;
import fd.FDSet;
import fd.Relation;
import gui.utilities.MyTable;
import interactive.DescrTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumnModel;

import normalization.BCNF;
import normalization.SecondNF;
import normalization.ThirdNF;
import utilities.SwingUtility;

public class GeneralPanel extends JPanel {
	DescrTableModel descrTableModel = new DescrTableModel();
	MyTable descTable = new MyTable(descrTableModel);
	
	
	Decomposition decomposition;
	
	
	public GeneralPanel(){
		super(new BorderLayout());
		add(bottomPanel(),  BorderLayout.CENTER);	
	}
	
	public JPanel bottomPanel(){
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(SwingUtility.myBorder2());
		JScrollPane sp1 = new JScrollPane(descTable);
		sp1.setPreferredSize(new Dimension(200, 200));		
		
		//p.add(midPanel(), BorderLayout.PAGE_START);
		p.add(sp1, 		  BorderLayout.CENTER);		
		
		return p;
	}
	
	public Decomposition getDecomposition() {
		return decomposition;
	}

	public void setDecomposition(Decomposition decomposition) {
		this.decomposition = decomposition;
		load();
	}
	
	public void load(){
		ArrayList<Relation> rls = decomposition.getSubrelations();
		Collections.sort(rls);		
		descrTableModel = new DescrTableModel();
		for (int i = 0; i < rls.size(); i++) {
			Relation sub = rls.get(i);
			FDSet f_ = sub.getFdSets().get(0);
			SecondNF _2nf = new SecondNF(sub, f_);
			ThirdNF  _3nf = new ThirdNF(sub, f_);
			BCNF     bcnf = new BCNF(sub, f_);
			boolean in2NF = _2nf.check();
			boolean in3NF = _3nf.check();
			boolean inBCNF= bcnf.check();						
			descrTableModel.describe(sub, f_, in2NF, in3NF, inBCNF);
		}		
		descTable.setModel(descrTableModel);
		updateColWidth();
	}

	public void clear() {
		decomposition = null;
		descrTableModel.clear();
		descTable.setModel(descrTableModel);
		updateColWidth();
		
	}
	
	public void updateColWidth(){
		TableColumnModel tcm = descTable.getColumnModel();
		int[] w = {80, 120, -1, 56, 56, 56};
		for (int i = 0; i < w.length; i++) {
			if(w[i]!=-1){
				tcm.getColumn(i).setMinWidth(w[i]);
				tcm.getColumn(i).setMaxWidth(w[i]);
			}
		}
	}
}

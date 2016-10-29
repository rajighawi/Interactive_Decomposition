package gui.tasks;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import renderer.MyListRenderer;
import utilities.SwingUtility;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;
import gui.general.FDSetPanel2;

public class EquivalencePanel extends JPanel implements ActionListener {

		
	JComboBox<FDSet> cmbFDSet1 = new JComboBox<FDSet>();	
	JComboBox<FDSet> cmbFDSet2 = new JComboBox<FDSet>();	
		
	FDSetPanel2 fdSetResultPanel1 = new FDSetPanel2();
	FDSetPanel2 fdSetResultPanel2 = new FDSetPanel2();
	
	JButton btnOk1;
	
	Relation relation;

	FDSet fdSet1, fdSet2;
	
	public EquivalencePanel(){
		this.relation = null;
		this.fdSet1 = null;
		this.fdSet2 = null;
		setLayout(new BorderLayout());

		cmbFDSet1.setFont(SwingUtility.fnt_label1);
		cmbFDSet1.addActionListener(this);	
		cmbFDSet1.setRenderer(new MyListRenderer());
		cmbFDSet1.setPreferredSize(new Dimension(160, 32));
				
		cmbFDSet2.setFont(SwingUtility.fnt_label1);
		cmbFDSet2.addActionListener(this);	
		cmbFDSet2.setRenderer(new MyListRenderer());
		cmbFDSet2.setPreferredSize(new Dimension(160, 32));
				
		JLabel lblFDSet1 = new JLabel("FD Set 1:");
		lblFDSet1.setFont(SwingUtility.fnt_label1);		
		JLabel lblFDSet2 = new JLabel("FD Set 2:");
		lblFDSet2.setFont(SwingUtility.fnt_label1);		
		
		btnOk1 = new JButton("Check Equivalence");
		btnOk1.setFont(SwingUtility.fnt_label1);
		btnOk1.addActionListener(this);
		
		JPanel pp = new JPanel(new GridBagLayout());
		pp.add(btnOk1, SwingUtility.myC(0, 0));
				
		JPanel topPanel1 = new JPanel(new GridBagLayout());		
		topPanel1.add(lblFDSet1, SwingUtility.myC(0, 1));
		topPanel1.add(cmbFDSet1, SwingUtility.myC(1, 1));
		
		JPanel topPanel2 = new JPanel(new GridBagLayout());		
		topPanel2.add(lblFDSet2, SwingUtility.myC(0, 1));
		topPanel2.add(cmbFDSet2, SwingUtility.myC(1, 1));
		
		JPanel p1 = new JPanel(new GridLayout(1, 3));
		p1.add(topPanel1); 
		p1.add(pp); 
		p1.add(topPanel2); 
		
		fdSetResultPanel1.setBorder(SwingUtility.myBorder("Validity of FDSet-1 given FDSet-2"));
		fdSetResultPanel2.setBorder(SwingUtility.myBorder("Validity of FDSet-2 given FDSet-1"));		
		
		JPanel midPanel = new JPanel(new GridLayout(1, 2));
		midPanel.add(fdSetResultPanel1); 
		midPanel.add(fdSetResultPanel2);
		
		add(p1, BorderLayout.PAGE_START); 
		add(midPanel, BorderLayout.CENTER); 		
		
	}
	
	public Relation getRelation(){
		return relation;
	}
	
	public void clear(){
		this.relation = null;
		this.fdSet1 = null;
		this.fdSet2 = null;
		fdSetResultPanel1.clear();
		fdSetResultPanel2.clear();
	}
	
	public void loadFDSets(ArrayList<FDSet> fdSets){
		cmbFDSet1.removeAllItems();
		cmbFDSet1.addItem(null);
		cmbFDSet2.removeAllItems();
		cmbFDSet2.addItem(null);
		for (int i = 0; i < fdSets.size(); i++) {
			cmbFDSet1.addItem(fdSets.get(i));
			cmbFDSet2.addItem(fdSets.get(i));
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if(src == cmbFDSet1){
			Object o = cmbFDSet1.getSelectedItem();
			if(o==null){
				fdSetResultPanel1.clear();
				fdSet1 = null;
			} else if(o instanceof FDSet){
				fdSet1 = (FDSet) o;
				Map<FD, Boolean> map1 = FDSetPanel2.makeNullMap(fdSet1);
				fdSetResultPanel1.load(map1);
				if(fdSet2!=null){
					Map<FD, Boolean> map2 = FDSetPanel2.makeNullMap(fdSet2);
					fdSetResultPanel2.load(map2);
				} else {
					fdSetResultPanel2.clear();
				}
			}
		} else if(src == cmbFDSet2){
			Object o = cmbFDSet2.getSelectedItem();
			if(o==null){
				fdSetResultPanel2.clear();
				fdSet2 = null;
			} else if(o instanceof FDSet){
				fdSet2 = (FDSet) o;
				Map<FD, Boolean> map2 = FDSetPanel2.makeNullMap(fdSet2);
				fdSetResultPanel2.load(map2);
				if(fdSet1!=null){
					Map<FD, Boolean> map1 = FDSetPanel2.makeNullMap(fdSet1);
					fdSetResultPanel1.load(map1);					
				} else {
					fdSetResultPanel1.clear();
				}
			}
		} else if(src == btnOk1){
			if(fdSet1 != null && fdSet2 != null){
				Map<FD, Boolean> map1_2 = FDUtility.checkImplication(fdSet1, fdSet2);
				Map<FD, Boolean> map2_1 = FDUtility.checkImplication(fdSet2, fdSet1);
				
				fdSetResultPanel1.load(map1_2);
				fdSetResultPanel2.load(map2_1);
			}
		}
	}
	
	public void load(Relation r, FDSet fdSet){
		clear();
		this.relation = r;
		this.fdSet1 = fdSet;
		if(relation!=null){
			loadFDSets(relation.getFdSets());
			if(fdSet1!=null){
				cmbFDSet1.setSelectedItem(fdSet1);
			}
		}
		
	}
	
	
	
}

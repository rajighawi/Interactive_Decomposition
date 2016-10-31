package gui.decomposition;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import renderer.MyListRenderer;
import renderer.RelationListRenderer;
import utilities.SwingUtility;
import fd.Decomposition;
import fd.FDSet;
import fd.Relation;
import gui.general.FDSetPanel;

public class DecomposPanel extends JPanel implements ActionListener{

	DefaultListModel<Decomposition> decoListModel = new DefaultListModel<Decomposition>();
	JList<Decomposition> decoList = new JList<Decomposition>(decoListModel);
	
	DefaultListModel<Relation> relListModel = new DefaultListModel<Relation>();
	JList<Relation> relList = new JList<Relation>(relListModel);
	
	FDSetPanel subFDSetPanel = new FDSetPanel();
	FDSetPanel originFDSetPanel = new FDSetPanel();
	
	GeneralPanel generalPanel = new GeneralPanel();	
	
	ChasePanel chasePanel = new ChasePanel();
	PreservationPanel preservePanel = new PreservationPanel();
		
	Relation relation;
		
	public DecomposPanel(){
		super(new BorderLayout());		
		relation = null;		
		subFDSetPanel.setBorder(SwingUtility.myBorder("SubRelation FD Set"));
		originFDSetPanel.setBorder(SwingUtility.myBorder("Original FD Set"));
		
		JPanel mainPanel = new JPanel(new GridLayout(1, 4));
		mainPanel.add(decosListPanel());
		mainPanel.add(originFDSetPanel);
		mainPanel.add(midPanel());
		mainPanel.add(subFDSetPanel);
		
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.setFont(SwingUtility.fnt_label1);
		tabPane.addTab("General", generalPanel);
		tabPane.addTab("Join Losslessness (Chase)", chasePanel);
		tabPane.addTab("Dependency Preservation",   preservePanel);
		
		add(mainPanel, BorderLayout.PAGE_START);
		add(tabPane,   BorderLayout.CENTER);
	}	
	
	public JPanel decosListPanel(){
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(SwingUtility.myBorder("Decompositions"));
		decoList.setFont(SwingUtility.fnt_data2);
		decoList.setCellRenderer(new MyListRenderer());
		decoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel lsm = decoList.getSelectionModel();
		lsm.addListSelectionListener(new DecomLSL());
		
		JScrollPane sp = new JScrollPane(decoList);
		sp.setPreferredSize(new Dimension(100, 100));		
		
		p.add(sp, BorderLayout.CENTER);		
		return p;
	}
	
	public JPanel midPanel(){
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(SwingUtility.myBorder("Sub-Relations"));
		relList.setFont(SwingUtility.fnt_data2);		
		relList.setCellRenderer(new RelationListRenderer());
		relList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel lsm = relList.getSelectionModel();
		lsm.addListSelectionListener(new SubsLSL());
		
		JScrollPane sp = new JScrollPane(relList);
		sp.setPreferredSize(new Dimension(100, 100));
		
	//	lblFDSet.setFont(SwingUtility.fnt_label1);
		
		p.add(sp, 		BorderLayout.CENTER);
	//	p.add(lblFDSet, BorderLayout.PAGE_END);
		return p;
	}
	
	public void loadRelation(Relation relation){
		//if(this.relation!=null && this.relation.equals(relation)) return;
		this.relation = relation;
		clear();
		decoListModel.removeAllElements();
		ArrayList<Decomposition> decos = relation.getDecompositions();
		for (int i = 0; i < decos.size(); i++) {
			decoListModel.addElement(decos.get(i));
		}
	}
	
	public void clear() {
		clearDecoList();
		chasePanel.clear();
		preservePanel.clear();
		generalPanel.clear();
	}
	
	public void clearRelList(){
		relListModel.removeAllElements();
		relList.setModel(relListModel);
		subFDSetPanel.clear();
		originFDSetPanel.clear();
	}
	
	public void clearDecoList(){
		decoListModel.removeAllElements();
		decoList.setModel(decoListModel);
		clearRelList();
	}
	
	public void loadSubs(Decomposition deco){
		relListModel.removeAllElements();
		ArrayList<Relation> subs = deco.getSubrelations();
		for (int i = 0; i < subs.size(); i++) {
			relListModel.addElement(subs.get(i));
		}
		relList.setModel(relListModel);
	}
	
	public void selectDecomposition(Decomposition d){
		decoList.setSelectedValue(d, true);
	}
	
	class DecomLSL implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			if(lsm.isSelectionEmpty()){
				clearRelList();			
			} else {
				int idx = lsm.getMinSelectionIndex();
				Decomposition deco = decoListModel.get(idx);
				loadSubs(deco);
				FDSet fdSet = deco.getFDSet();
				originFDSetPanel.load(fdSet);
				chasePanel.clear();
				chasePanel.setDecomposition(deco);
				preservePanel.clear();
				preservePanel.setDecomposition(deco);
				generalPanel.clear();
				generalPanel.setDecomposition(deco);
			}
		}		
	}
	
	class SubsLSL implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			if(lsm.isSelectionEmpty()){
				subFDSetPanel.clear();
			} else {
				int idx = lsm.getMinSelectionIndex();
				Relation sub = relListModel.get(idx);
				FDSet f = sub.getFdSets().get(0);
				subFDSetPanel.load(f);
			}
		}		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Object src = e.getSource();			
	}
	
}

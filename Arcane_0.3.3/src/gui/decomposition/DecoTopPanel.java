package gui.decomposition;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.Global;
import main.Listener;
import renderer.MyListRenderer;
import renderer.RelationListRenderer;
import utilities.SwingUtility;
import fd.Decomposition;
import fd.FDSet;
import fd.Relation;
import gui.general.FDSetPanel;

public class DecoTopPanel extends JPanel implements ActionListener {

	JComboBox<Relation> cmbRelation = new JComboBox<Relation>();
	JComboBox<Decomposition> cmbDeco= new JComboBox<Decomposition>();
	// JTextField txtFDset    = new JTextField();
	// JTextArea txtSubRelations = new JTextArea();
	
	FDSetPanel fdSetPanel = new FDSetPanel();
	FDSetPanel fdSetPanel2 = new FDSetPanel();
	
	DefaultListModel<Relation> relListModel = new DefaultListModel<Relation>();
	JList<Relation> relList = new JList<Relation>(relListModel);
	
	Decomposition decomposition;
	Listener listener;
	
	public DecoTopPanel(Listener listener){
		super(new BorderLayout());
		this.listener = listener;
		
		setBorder(SwingUtility.myBorder2());
		
		JLabel lblRelation = new JLabel("Relation", SwingConstants.CENTER);     
		lblRelation.setFont(SwingUtility.fnt_label1);
		JLabel lblDeco = new JLabel("Decomposition", SwingConstants.CENTER); 
		lblDeco.setFont(SwingUtility.fnt_label1);
		
		cmbRelation.setFont(SwingUtility.fnt_data2);
		cmbDeco.setFont(SwingUtility.fnt_data2);

		fdSetPanel.setBorder(SwingUtility.myBorder("Original FD Set:"));
		fdSetPanel2.setBorder(SwingUtility.myBorder("SubRelation FD Set:"));
		
		relList.setFont(SwingUtility.fnt_data2);
		relList.setCellRenderer(new RelationListRenderer());
		relList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel lsm = relList.getSelectionModel();
		lsm.addListSelectionListener(new SubsLSL());
		
		cmbRelation.addActionListener(this);
		cmbRelation.setRenderer(new MyListRenderer());
		cmbDeco.addActionListener(this);
		cmbDeco.setRenderer(new MyListRenderer());
		cmbRelation.setPreferredSize(new Dimension(240, 32));
		cmbDeco.setPreferredSize(new Dimension(240, 32));
		
		JPanel p1 = new JPanel(new GridBagLayout());
		p1.setBorder(SwingUtility.myBorder2());		
		p1.add(lblRelation, SwingUtility.myC(0, 0));	
		p1.add(cmbRelation, SwingUtility.myC(1, 0));		
		p1.add(lblDeco, 	SwingUtility.myC(0, 1));	
		p1.add(cmbDeco, 	SwingUtility.myC(1, 1));
		
		JScrollPane sp = new JScrollPane(relList);
		sp.setPreferredSize(new Dimension(300, 200));
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(SwingUtility.myBorder("Sub-Relations"));
		p.add(sp, BorderLayout.CENTER);
		
		
		JPanel pp2 = new JPanel(new GridLayout(1, 3));		
		pp2.add(fdSetPanel);
		pp2.add(p);
		pp2.add(fdSetPanel2);
		
		
		add(p1, 		BorderLayout.PAGE_START);
		add(pp2, 		BorderLayout.CENTER);
		//add(btnStart, 	BorderLayout.PAGE_END);
		
	}
	
	
	public void load(){
		ArrayList<Relation> relations = Global.getInstance().getRelationList();
		cmbRelation.removeAllItems();
		cmbRelation.addItem(null);		
		for (int i = 0; i < relations.size(); i++) {
			cmbRelation.addItem(relations.get(i));	
		}
		clearOthers();
	}
	
	public void selectRelation(Relation r){
		cmbRelation.setSelectedItem(r);
	}
	
	public void loadDecos(ArrayList<Decomposition> decos){
		// clear2();
		cmbDeco.removeAllItems();
		cmbDeco.addItem(null);
		for (int i = 0; i < decos.size(); i++) {
			cmbDeco.addItem(decos.get(i));
		}		
	}
	
	public void loadDecomposition(Decomposition deco){
		this.decomposition = deco;
		FDSet fdset = deco.getFDSet();
		cmbDeco.setSelectedItem(deco);
		fdSetPanel.load(fdset);
		fdSetPanel2.clear();
		loadSubs(decomposition);		
	}
	
	private void loadSubs(Decomposition deco){
		relListModel.removeAllElements();
		ArrayList<Relation> subs = deco.getSubrelations();
		for (int i = 0; i < subs.size(); i++) {
			relListModel.addElement(subs.get(i));
		}
		relList.setModel(relListModel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if(src==cmbRelation){
			Object o = cmbRelation.getSelectedItem();
			if(o!=null && o instanceof Relation){
				Relation r = (Relation) o;
				ArrayList<Decomposition> decos = r.getDecompositions();
				loadDecos(decos);
				clear2();
			} else {
				clearOthers();
			}
		} else if(src==cmbDeco){
			Object o = cmbDeco.getSelectedItem();
			if(o!=null && o instanceof Decomposition){
				decomposition = (Decomposition) o;				
				loadDecomposition(decomposition);
				listener.go("clear2");
			} else {
				clear2();
			}
		}
	}

	public Decomposition getDecomposition() {
		return decomposition;
	}
	
	public void clearAll() {
		cmbRelation.removeAllItems();
		clearOthers();
	}
	
	public void clearOthers() {
		cmbDeco.removeAllItems();
		clear2();
	}
	
	public void clear2() {
		this.decomposition = null;
		cmbDeco.setSelectedItem(null);
		fdSetPanel.clear();		
		relListModel.removeAllElements();
		relList.setModel(relListModel);
		fdSetPanel2.clear();
		listener.go("clear2");
	}
	
	class SubsLSL implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			if(lsm.isSelectionEmpty()){
				fdSetPanel2.clear();
			} else {
				int idx = lsm.getMinSelectionIndex();
				Relation sub = relListModel.get(idx);
				FDSet f = sub.getFdSets().get(0);
				fdSetPanel2.load(f);
			}
		}		
	}
	
}

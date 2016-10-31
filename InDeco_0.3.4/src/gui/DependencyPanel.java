package gui;

import fd.FDSet;
import fd.Relation;
import gui.general.CreateFDSetFrame;
import gui.general.FDSetPanel;
import gui.tasks.ArmstrongPanel;
import gui.tasks.EquivalencePanel;
import gui.tasks.FDsProjectionPanel;
import gui.tasks.FindClosurePanel;
import gui.tasks.FindKeysPanel;
import gui.tasks.FindMinCoverPanel;
import gui.tasks.NormalizePanel;
import gui.utilities.MyButton1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import normalization.Normalizer;
import renderer.MyListRenderer;
import utilities.SwingUtility;

public class DependencyPanel extends JPanel implements ActionListener {

	JButton btnNew    = new MyButton1("New");
	JButton btnRename = new MyButton1("Rename");
	JButton btnRemove = new MyButton1("Remove");
	
	DefaultListModel<FDSet> fdsListModel = new DefaultListModel<FDSet>();
	JList<FDSet> fdsList = new JList<FDSet>(fdsListModel);
	
	FDSetPanel fdSetPanel = new FDSetPanel();
	 
	JTabbedPane tabPane = new JTabbedPane();
	
	Relation relation;
	
	FindClosurePanel 	closurePanel 	 = new FindClosurePanel();
	FindKeysPanel 		keysPanel 		 = new FindKeysPanel();
	FindMinCoverPanel 	minCoverPanel 	 = new FindMinCoverPanel();
	FDsProjectionPanel 	projectionPanel  = new FDsProjectionPanel();
	EquivalencePanel    equivalencePanel = new EquivalencePanel();
	ArmstrongPanel 	    armstrongPanel   = new ArmstrongPanel();
	
	NormalizePanel normalize2NFPanel  = new NormalizePanel(Normalizer._2NF);
	NormalizePanel normalize3NFPanel  = new NormalizePanel(Normalizer._3NF);
	NormalizePanel normalizeBCNFPanel = new NormalizePanel(Normalizer.BCNF);
	
	public DependencyPanel(){
		super(new BorderLayout());
		this.relation = null;
		fdSetPanel.setBorder(SwingUtility.myBorder("Functional Dependencies"));
		
		fdsList.setCellRenderer(new MyListRenderer());
		fdsList.setFont(SwingUtility.fnt_data2);
		fdsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel lsm = fdsList.getSelectionModel();
		lsm.addListSelectionListener(new FDSListSelectionListener());
		
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(SwingUtility.myBorder("FD Sets"));		
		JScrollPane sp = new JScrollPane(fdsList);
		sp.setPreferredSize(new Dimension(200, 200));			
		p.add(sp, 		   BorderLayout.CENTER);		
		p.add(btnsPanel(), BorderLayout.PAGE_END);
		
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(p, 			BorderLayout.LINE_START);
		topPanel.add(fdSetPanel, BorderLayout.CENTER);
		
		add(topPanel, 		BorderLayout.PAGE_START);
		add(bottomPanel(),  BorderLayout.CENTER);
	}
	
	public JPanel bottomPanel(){
		JPanel panel = new JPanel(new BorderLayout()); 
		tabPane.setFont(SwingUtility.fnt_label1);
		
		tabPane.addTab("Closure", closurePanel);
		tabPane.addTab("Keys",	  keysPanel);
		tabPane.addTab("Minimal Cover",	  minCoverPanel);
		tabPane.addTab("Projection",	  projectionPanel);		
		tabPane.addTab("Equivalence",	  equivalencePanel);	
		
		tabPane.addTab("2NF",	  normalize2NFPanel);
		tabPane.addTab("3NF",	  normalize3NFPanel);
		tabPane.addTab("BCNF",	  normalizeBCNFPanel);		
		tabPane.addTab("Armstrong", armstrongPanel);		
		
		panel.add(tabPane, BorderLayout.CENTER);
		return panel;
	}
	
	public JPanel btnsPanel(){
		JPanel p = new JPanel(new GridBagLayout());
		
		btnNew.addActionListener(this);
		btnRename.addActionListener(this);
		btnRemove.addActionListener(this);
		enableButtons(false);
		p.add(btnNew,    SwingUtility.myC(0, 0));
		p.add(btnRename, SwingUtility.myC(1, 0));
		p.add(btnRemove, SwingUtility.myC(2, 0));
		
		return p;
	}
	
	// ====================================================
	
	public void loadRelation(Relation relation){
		//if(this.relation!=null && this.relation.equals(relation)) return;
		this.relation = relation;
		ArrayList<FDSet> fdsets = relation.getFdSets();
		fdsListModel.removeAllElements();
		for (int i = 0; i < fdsets.size(); i++) {
			fdsListModel.addElement(fdsets.get(i));
		}
		fdsList.setModel(fdsListModel);
		fdSetPanel.clear();
		
		if(fdsets.size()>0){
			fdsList.setSelectedIndex(0);
			FDSet F = fdsets.get(0);
			fdSetPanel.load(F);
		}
		
		enableButtons(false);
		btnNew.setEnabled(true);
	}
	
	public void clear(){
		fdsListModel.removeAllElements();
		fdsList.setModel(fdsListModel);
		fdSetPanel.clear();
		relation = null;
		enableButtons(false);
		
		closurePanel.clear();
		keysPanel.clear();
		minCoverPanel.clear();
		projectionPanel.clear();
		equivalencePanel.clear();
		
		normalize2NFPanel.clear();
		normalize3NFPanel.clear();
		normalizeBCNFPanel.clear();
		// armstrongPanel.clear();
	}

	private void enableButtons(boolean b){
		btnNew.setEnabled(b);
		btnRemove.setEnabled(b);
		btnRename.setEnabled(b);
	}
	
	public void selectFDSet(FDSet fdSet){
		fdsList.setSelectedValue(fdSet, true);
		fdSetPanel.load(fdSet);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if(src == btnNew){
			CreateFDSetFrame.showMessageDialog(relation);
		} else if(src == btnRemove){
			
		} else if(src == btnRename){
			int index = fdsList.getSelectedIndex();
	        if(index>-1){	        	
	        	FDSet f = fdsListModel.get(index);
	        	
	        	String name = JOptionPane.showInputDialog("New Name: ", f.getName());
	        	if(name!=null){
	        		f.setName(name);
	        		loadRelation(relation);
	        		fdsList.setSelectedIndex(index);
	        	}
	        	
	        }
		}
	}

	class FDSListSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			if(lsm.isSelectionEmpty()){
				btnRemove.setEnabled(false);
				btnRename.setEnabled(false);
				load(relation, null);
			} else {
				int idx = lsm.getMinSelectionIndex();
				//Relation r = Global.getInstance().getRelationList().get(index);
	        	FDSet fdSet = fdsListModel.get(idx);
	        	fdSetPanel.load(fdSet);	
	        	btnRemove.setEnabled(true);
				btnRename.setEnabled(true);
				load(relation, fdSet);
			}
		}
		
	}

	public void load(Relation relation, FDSet fdSet){
		keysPanel.load(relation, fdSet);
		closurePanel.load(relation, fdSet);
		minCoverPanel.load(relation, fdSet);
		projectionPanel.load(relation, fdSet);
		equivalencePanel.load(relation, fdSet);
		normalize2NFPanel.load(relation, fdSet);
		normalizeBCNFPanel.load(relation, fdSet);
		normalize3NFPanel.load(relation, fdSet);
		armstrongPanel.load(relation, fdSet);
	}
	
}


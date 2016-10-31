package gui.decomposition;

import fd.ChasePack;
import fd.Decomposition;
import gui.utilities.MyButton;
import gui.utilities.MyTable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import renderer.MyListRenderer;
import renderer.MyTableCellRenderer;
import utilities.SwingUtility;

public class ChasePanel extends JPanel implements ActionListener {
	
	JButton btnStart = new MyButton("Start Test", null);
	
	DefaultListModel<String> stepsListModel = new DefaultListModel<String>();
	JList<String> stepsList = new JList<String>(stepsListModel);
	
	TableauTableModel tableauTableModel = new TableauTableModel();
	MyTable tableauTable = new MyTable();
	
	JTextArea lblMsg1 = new JTextArea();
	JTextArea lblMsg2 = new JTextArea();
	
	ArrayList<ChasePack> packs;
		
	Decomposition decomposition;
	
	public ChasePanel(){
		setLayout(new BorderLayout());
		
		decomposition = null;
		packs = null;
				
		add(topPanel(), BorderLayout.PAGE_START);
		add(mainPanel(), BorderLayout.CENTER);
	}
	

	public JPanel mainPanel(){
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		stepsList.setFont(SwingUtility.fnt_label1);
		stepsList.setCellRenderer(new MyListRenderer());
		ListSelectionModel lsm = stepsList.getSelectionModel();
		lsm.addListSelectionListener(new MyStepListListener());
		
		JScrollPane sp1 = new JScrollPane(stepsList);
		sp1.setPreferredSize(new Dimension(200, 200));
		
		tableauTable.setDefaultRenderer(Object.class, new MyTableCellRenderer(true));
				
		JPanel p1 = new JPanel(new BorderLayout());
		JScrollPane sp2 = new JScrollPane(tableauTable);
		sp2.setPreferredSize(new Dimension(200, 200));
		lblMsg1.setFont(SwingUtility.fnt_label1);
		lblMsg2.setFont(SwingUtility.fnt_label1);
		p1.setBorder(SwingUtility.myBorder2());
		
		JScrollPane sp3 = new JScrollPane(lblMsg1);
		sp3.setPreferredSize(new Dimension(200, 60));
		JScrollPane sp4 = new JScrollPane(lblMsg2);
		sp4.setPreferredSize(new Dimension(200, 60));
		
		p1.add(sp3, BorderLayout.PAGE_START);
		p1.add(sp2, 	BorderLayout.CENTER);
		p1.add(sp4, BorderLayout.PAGE_END);
		
		mainPanel.add(sp1, BorderLayout.LINE_START);
		mainPanel.add(p1,  BorderLayout.CENTER);
		
		return mainPanel;
	}
	
	public JPanel topPanel(){
		JPanel topPanel = new JPanel(new GridBagLayout());
		topPanel.setBorder(SwingUtility.myBorder2());
		btnStart.addActionListener(this);
		topPanel.add(btnStart, SwingUtility.myC(0, 0));
		return topPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src= e.getSource();
		if(src==btnStart){
			if(decomposition!=null){
				packs = decomposition.chase();
				stepsListModel.removeAllElements();
				for (int i = 0; i < packs.size(); i++) {
					String ttl = packs.get(i).getTitle();
					stepsListModel.addElement("  "+i+":  "+ttl);
				}
				stepsList.setModel(stepsListModel);
				stepsList.setSelectedIndex(0);
			}			
		}
	}
	
	class MyStepListListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			if(lsm.isSelectionEmpty()){
				
			} else {
				int idx = lsm.getMinSelectionIndex();
				if(packs != null){
					ChasePack pack = packs.get(idx);
					lblMsg1.setText(pack.getMsg1());
					tableauTableModel.loadTableau(pack.getTableau());
					tableauTable.setModel(tableauTableModel);					
					lblMsg2.setText(pack.getMsg2());
				}				
			}
		}		
	}

	public void clear() {
		this.packs = null;		
		this.decomposition = null;
		stepsListModel.removeAllElements();
		stepsList.setModel(stepsListModel);
		lblMsg1.setText("");
		lblMsg2.setText("");
		
		tableauTableModel.clear();
		tableauTable.setModel(tableauTableModel);
	}


	public Decomposition getDecomposition() {
		return decomposition;
	}


	public void setDecomposition(Decomposition decomposition) {
		this.decomposition = decomposition;
	}


	public ArrayList<ChasePack> getPacks() {
		return packs;
	}
	
	

}

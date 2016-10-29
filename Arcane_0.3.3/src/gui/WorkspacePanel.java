package gui;

import interactive.InteractivePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import jdbcTest.MyConnection;
import main.Global;
import utilities.IconUtility;
import utilities.SwingUtility;
import xml.FDXModel;
import fd.Decomposition;
import fd.FDSet;
import fd.Relation;
import gui.decomposition.DecomposPanel;
import gui.utilities.MyButton;
import gui.utilities.MyButton1;

public class WorkspacePanel extends JPanel implements ActionListener {

	Relation relation;
	JTextField txtRelName = new JTextField();
	JButton btnRename = new MyButton1("Rename");
	JButton btnConn = new JButton(IconUtility.db24); 
	
	MyButton btnSave = new MyButton("Save", IconUtility.save);	
	//TasksPanel tasksPanel = new TasksPanel();
	
	//UpdateAttributeSetPanel attributesPanel = new UpdateAttributeSetPanel();
	
	DependencyPanel dependencyPanel = new DependencyPanel();
	DecomposPanel   decomposPanel   = new DecomposPanel();
	DiscoveryPanel  discoveryPanel  = new DiscoveryPanel();
	InteractivePanel interPanel = new InteractivePanel();
	
	JTabbedPane tabPane = new JTabbedPane();
	
	
	public WorkspacePanel(){
		super(new BorderLayout());
		setBorder(SwingUtility.myBorder2());		
		
		add(mainPanel(), BorderLayout.CENTER);
	}
	
	public JPanel mainPanel(){
		tabPane.setFont(SwingUtility.fnt_label1);
		tabPane.addTab("Dependencies",   dependencyPanel);
		tabPane.addTab("Decompositions", decomposPanel);
		tabPane.addTab("Discovery",		 discoveryPanel);
		tabPane.addTab("Interactive Decomposition",		 interPanel); 
		
		JPanel p = new JPanel(new BorderLayout()); 	
		p.add(topPanel(), 	   BorderLayout.PAGE_START);
	//	p.add(attributesPanel, BorderLayout.LINE_START);
		p.add(tabPane,   	   BorderLayout.CENTER);
		
		return p;
	}
		
	public JPanel topPanel(){
		JPanel p = new JPanel(new GridBagLayout());
		JLabel lblName = new JLabel("Relation Name: ");
		lblName.setFont(SwingUtility.fnt_label1);
		txtRelName.setPreferredSize(new Dimension(200, 36));
		txtRelName.setFont(SwingUtility.fnt_label2);
		txtRelName.getDocument().addDocumentListener(new MyDocumentListener());

		btnRename.addActionListener(this);
		btnRename.setEnabled(false);
		
		p.add(lblName,    SwingUtility.myC(0, 0));
		p.add(txtRelName, SwingUtility.myC(1, 0));
		
		p.add(new JSeparator(), SwingUtility.myC(2, 0));
		p.add(btnRename, SwingUtility.myC(3, 0));
		
		btnConn.setEnabled(false);
		btnConn.addActionListener(this);
		
		JPanel ps = new JPanel(new GridBagLayout());		
		btnSave.addActionListener(this);
		//btnSave.setPreferredSize(new Dimension(140, 40));
		ps.add(btnSave, SwingUtility.myC(0, 0));
		ps.add(btnConn, SwingUtility.myC(1, 0));
		
		JPanel pp = new JPanel(new BorderLayout());
		pp.setBorder(SwingUtility.myBorder2());
		pp.add(p,  BorderLayout.CENTER);
		pp.add(ps, BorderLayout.LINE_END);
		
		
		return pp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if(src == btnSave){
			FDXModel fdxModel = new FDXModel(relation);
			JFileChooser fileChooser = Global.getInstance().getFileChooser();
			int returnVal = fileChooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				fdxModel.toFile(file);	
				//file.clo
			}
		} else if(src == btnRename){
			if(relation == null){
				JOptionPane.showMessageDialog(null, "No relation is selected");
				return;
			}
			String oldname = relation.getName();
			String newname = txtRelName.getText();
			for(String rr : Global.getInstance().getRelations().keySet()){
				if(rr.equalsIgnoreCase(newname)){
					JOptionPane.showMessageDialog(null, "Relation `"+rr+"` already exists");
					return;
				}
			}
			relation.rename(newname);
			
			Global.getInstance().getRelations().remove(oldname);
			Global.getInstance().getRelations().put(newname, relation);
			
			loadRelation(relation);
			Global.getInstance().getMainFrame().updateRelationsList();
			
		} else if(src == btnConn){
			MyConnection cc = relation.getConn();
			ViewConnectDialog.showMessageDialog(cc);
		} 
	}
	// --------------------------------------------------
	
	public void loadRelation(Relation relation){
		this.relation = relation;
		txtRelName.setText(relation.getName());
		btnRename.setEnabled(false);
		MyConnection cc = relation.getConn();
		btnConn.setEnabled(cc!=null);
	//	attributesPanel.setRelation(relation);		
		
		dependencyPanel.loadRelation(relation);
		discoveryPanel.loadRelation(relation);
		decomposPanel.loadRelation(relation);		
		interPanel.loadRelation(relation);
	}
	
	
	public void loadRelationF(Relation relation, FDSet fdset){
		loadRelation(relation);
		dependencyPanel.selectFDSet(fdset);
		tabPane.setSelectedComponent(dependencyPanel);
	}
	
	public void loadRelationD(Relation relation, Decomposition d){
		loadRelation(relation);
		decomposPanel.selectDecomposition(d);
		tabPane.setSelectedComponent(decomposPanel);
	}
	
	public void clear(){
		txtRelName.setText("");
		btnRename.setEnabled(false);
	//	attributesPanel.clear();		
		
		dependencyPanel.clear();
		decomposPanel.clear();	
		discoveryPanel.clear();
		interPanel.clear();
	}
	
	public void go(){
		interPanel.go();
	}
	
	class MyDocumentListener implements DocumentListener {
	    
	    public void insertUpdate(DocumentEvent e) {
	        btnRename.setEnabled(true);
	    }
	    public void removeUpdate(DocumentEvent e) {
	    	btnRename.setEnabled(true);
	    }
	    public void changedUpdate(DocumentEvent e) {
	    	btnRename.setEnabled(true);
	    }
	}
	
}

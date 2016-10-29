package gui;

import fd.Decomposition;
import fd.FDSet;
import fd.Relation;
import gui.general.UpdateAttributeSetPanel;
import interactive.InterSettings;
import interactive.Interactive;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import main.Global;
import utilities.IconUtility;
import utilities.SwingUtility;

public class MainFrame extends JFrame{
	
	public static final String version = "0.3.3";
	   
	WorkspacePanel workspacePanel = new WorkspacePanel();
	
	JTabbedPane tabPane = new JTabbedPane();
	
	RelationsPanel relationsPanel = new RelationsPanel();
	
	UpdateAttributeSetPanel attributesPanel = new UpdateAttributeSetPanel();
	
	MainToolBar mainToolBar = new MainToolBar();
	
	public MainFrame() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	private void createAndShowGUI() {
		Global global = Global.getInstance();
		global.setMainFrame(this);	
		global.setRelations(new HashMap<String, Relation>());
		global.setFileChooser(new JFileChooser("data/"));
		InterSettings settings = new InterSettings(true, Interactive.defaultWeights);
		global.setSettings(settings);
		SwingUtilities.updateComponentTreeUI(this);
		validate();
		
		JFrame frame = new JFrame("Arcane " + version);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(IconUtility.logo.getImage());			
		
		Container pane = frame.getContentPane();
		pane.setLayout(new BorderLayout());
						 
		pane.add(centerPanel(), BorderLayout.CENTER); 
				 
		frame.pack();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		SwingUtility.center(frame);
		frame.setVisible(true);		
		 
	}
	
	public JPanel centerPanel(){
		JPanel panel = new JPanel(new BorderLayout());		
		
		JPanel p1 = new JPanel(new BorderLayout());			
		p1.add(mainToolBar,    	BorderLayout.PAGE_START);
		p1.add(relationsPanel, 	BorderLayout.CENTER);
		
		JPanel p2 = new JPanel(new BorderLayout());			
		p2.add(p1,    	BorderLayout.PAGE_START);	
		p2.add(attributesPanel,	BorderLayout.CENTER);
				 
		panel.add(p2, BorderLayout.LINE_START);
		panel.add(workspacePanel, BorderLayout.CENTER);
		
		return panel;
	}
	
	// --------------------------------------------------------------------------
	
	public void clear(){
		relationsPanel.clear();
		workspacePanel.clear();
		attributesPanel.clear();
	}
	
	public void addRelation(Relation r){
		System.out.println("Relation " + r + " has been added to the workspace");			
		relationsPanel.updateRelationList();
	}
	
	public void selectRelation(Relation r, FDSet fdSet){
		relationsPanel.selectRelation(r);
		//loadRelation(r);
		workspacePanel.loadRelationF(r, fdSet);
		attributesPanel.setRelation(r);
	}
	
	public void selectRelation(Relation r, Decomposition deco){
		relationsPanel.selectRelation(r);
		//loadRelation(r);
		workspacePanel.loadRelationD(r, deco);
		attributesPanel.setRelation(r);
	}
	
	public void updateRelationsList(){
		relationsPanel.updateRelationList();
	}
	
	public void removeRelation(Relation r){
		System.out.println("Relation " + r.getName() + " has been removed from the workspace");			
		relationsPanel.updateRelationList();
		workspacePanel.clear();
		attributesPanel.clear();
	}

	public void loadRelation(Relation r){
		if(r==null){
			workspacePanel.clear();
		} else {
			workspacePanel.loadRelation(r);		
			attributesPanel.setRelation(r);	
		}		
	}
	
	public void go(){
		workspacePanel.go();
	}
	
}

package main;

import fd.Relation;
import gui.MainFrame;
import interactive.InterSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Global {
	
	private static Global global;
	
	private MainFrame mainFrame;

	private JFileChooser fileChooser;
	
	private HashMap<String, String> properties;
	 
	private Map<String, Relation> relations;
		
	private String report;
	
	private InterSettings settings;
		
	private Global() {
		global = this;
	}

	public static Global getInstance() {
		if (global == null)
			return new Global();
		return global;
	}

	public void close() {
		global = null;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	 
	public HashMap<String, String> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}

	public JFileChooser getFileChooser() {
		return fileChooser;
	}

	public void setFileChooser(JFileChooser fileChooser) {
		this.fileChooser = fileChooser;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}
	
	public void appendToReport(String msg) {
		this.report += msg;
	}

	public Map<String, Relation> getRelations() {
		return relations;
	}
	
	public void clearRelations() {
		relations = new HashMap<String, Relation>();
	}
	
	public void removeRelation(Relation r) {
		relations.remove(r.getName(), r);
	}
	
	public ArrayList<Relation> getRelationList() {
		ArrayList<Relation> list = new ArrayList<Relation>(relations.values());
		Collections.sort(list);
		return list;
	}
	
	public void setRelations(Map<String, Relation> relations) {
		this.relations = relations;
	}
	
	public Relation getRelation(String name){
		return relations.get(name);
	}
	
	public void addRelation(Relation r) throws Exception{
		if(!relations.containsKey(r.getName())){
			relations.put(r.getName(), r);
		} else {			
			String msg = "Relation named `"+r.getName()+"` already exists!";
			JOptionPane.showMessageDialog(null, msg);
			throw new Exception(msg);			
		}
	}

	public InterSettings getSettings() {
		return settings;
	}

	public void setSettings(InterSettings settings) {
		this.settings = settings;
	}
	
	
	
}

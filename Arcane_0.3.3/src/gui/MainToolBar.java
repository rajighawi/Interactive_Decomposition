package gui;

import fd.Relation;
import gui.decomposition.CreateDecompositionFrame;
import gui.general.CreateFDSetFrame;
import gui.general.CreateRelationFrame;
import gui.utilities.MyButton1;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.Global;
import utilities.SwingUtility;
import xml.FDXParser;

public class MainToolBar extends JPanel implements ActionListener{

	protected JButton btnCreateRel   = new MyButton1("New Relation");	
	protected JButton btnCreateFDSet = new MyButton1("Create FD Set");	
	protected JButton btnDBConnect   = new MyButton1("Connect");
	protected JButton btnCreateDeco  = new MyButton1("Create Decomposition");
	protected JButton btnLoad        = new MyButton1("Load");
	protected JButton btnClear		 = new MyButton1("Clear");
	
	public MainToolBar(){
		super(new GridLayout(2, 2, 4, 4));
		
		setBorder(SwingUtility.myBorder2());
		btnCreateRel.addActionListener(this);		
		btnDBConnect.addActionListener(this);		
		btnCreateFDSet.addActionListener(this);
		btnCreateDeco.addActionListener(this);
		btnLoad.addActionListener(this);
		btnClear.addActionListener(this);
				 
		add(btnCreateRel);
		add(btnDBConnect);
		add(btnLoad);
		add(btnClear);			
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == btnLoad){
			JFileChooser fileChooser = Global.getInstance().getFileChooser();
			int returnVal = fileChooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				Relation r = FDXParser.parse(file);
				if(r != null){
					try {
						Global.getInstance().addRelation(r);
						Global.getInstance().getMainFrame().addRelation(r);
						Global.getInstance().getMainFrame().loadRelation(r);
					} catch (Exception e1) {
						e1.printStackTrace();
					}										
				}
			}			
		} else if(source == btnCreateRel){
			CreateRelationFrame.showMessageDialog();
		} else if(source == btnCreateFDSet){
			CreateFDSetFrame.showMessageDialog();
		} else if(source == btnDBConnect){
			ConnectDialog.showMessageDialog();
		} else if(source == btnCreateDeco){
			CreateDecompositionFrame.showMessageDialog();
		} else if(source == btnClear){ 
			int ans = JOptionPane.showConfirmDialog(null, "This will clear the workspace and remove all relations!\nAre you sure?");
			if(ans==JOptionPane.OK_OPTION){
				Global.getInstance().clearRelations();
				Global.getInstance().getMainFrame().clear();
			}
		}
	}
}

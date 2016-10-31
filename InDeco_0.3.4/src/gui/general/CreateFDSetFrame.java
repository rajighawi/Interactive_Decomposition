package gui.general;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import main.Global;
import utilities.SwingUtility;
import fd.FD;
import fd.FDSet;
import fd.Relation;
import gui.utilities.MyButton1;

public class CreateFDSetFrame extends JDialog implements ActionListener{

	private static CreateFDSetFrame currentDialog;
	
	CreateFDSetPanel fdSetPanel;
	
	JButton btnOk     = new MyButton1("OK");
	JButton btnClear  = new MyButton1("Clear");
	JButton btnCancel = new MyButton1("Cancel");
	
	CreateFDSetFrame(){
		super();
		setTitle("Create New FD Set "+FD.FD_ARROW);
		setModal(true);
		fdSetPanel = new CreateFDSetPanel();
				
		JDialog.setDefaultLookAndFeelDecorated(true);
		addWindowListener(new WindowCloseListener());	
		setPreferredSize(new Dimension(640, 560));
		
		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(fdSetPanel, BorderLayout.CENTER);
		getContentPane().add(buttonsPanel(), BorderLayout.PAGE_END);
		
		pack();
		requestFocus();		
		SwingUtility.center(this);
		setResizable(false);
	}

	
	public JPanel buttonsPanel(){
		JPanel p2 = new JPanel(new GridBagLayout());
				
		btnOk.addActionListener(this);
		btnClear.addActionListener(this);
		btnCancel.addActionListener(this);
		
		p2.add(btnClear,  SwingUtility.myC(0, 0));
		p2.add(btnOk,     SwingUtility.myC(1, 0));
		p2.add(btnCancel, SwingUtility.myC(2, 0));
		return p2;
	}
	
	private class WindowCloseListener extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			setVisible(false);
		}
	}	
	
	public static CreateFDSetFrame getCurrentDialog() {
		if(currentDialog == null){
			currentDialog = new CreateFDSetFrame();
		}
		return currentDialog;
	}
	
	public void close() {
		currentDialog = null;
	}
	
	public static void showMessageDialog() {
		CreateFDSetFrame cf = getCurrentDialog();
		cf.fdSetPanel.clear();
		cf.fdSetPanel.loadRelations();
		cf.setVisible(true);		
	}
	
	public static void showMessageDialog(Relation r) {
		CreateFDSetFrame cf = getCurrentDialog();
		cf.fdSetPanel.clear();
		cf.fdSetPanel.loadRelations();
		if(r!=null){
			cf.fdSetPanel.selectRelation(r);
		}
		cf.setVisible(true);		
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object src = ae.getSource();
		if(src==btnOk){		
			FDSet fdSet = fdSetPanel.getFDSet();
			Relation r  = fdSetPanel.getRelation();
			if(r!=null && fdSet != null){
				Global.getInstance().getRelation(r.getName()).addFDSet(fdSet);
				Global.getInstance().getMainFrame().selectRelation(r, fdSet);				
			}
			currentDialog.dispose();
		} else if(src==btnCancel){
			currentDialog.dispose();
		} else if(src==btnClear){
			fdSetPanel.clear();
		}
	}
	
	

}

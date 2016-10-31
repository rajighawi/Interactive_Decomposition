package gui.decomposition;

import fd.Decomposition;
import fd.Relation;
import gui.utilities.MyButton1;

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

public class CreateDecompositionFrame extends JDialog implements ActionListener{

	private static CreateDecompositionFrame currentDialog;
	
	CreateDecompositionPanel fdSetPanel;
	
	JButton btnOk     = new MyButton1("OK");
	JButton btnClear  = new MyButton1("Clear");
	JButton btnCancel = new MyButton1("Cancel");
	
	CreateDecompositionFrame(){
		super();
		setTitle("Create New Decomposition");
		setModal(true);
		fdSetPanel = new CreateDecompositionPanel();
				
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
	
	public static CreateDecompositionFrame getCurrentDialog() {
		if(currentDialog == null){
			currentDialog = new CreateDecompositionFrame();
		}
		return currentDialog;
	}
	
	public void close() {
		currentDialog = null;
	}
	
	public static void showMessageDialog() {
		CreateDecompositionFrame cf = getCurrentDialog();
		cf.fdSetPanel.clear();
		cf.fdSetPanel.load();//Relations();
		cf.setVisible(true);		
	}
	
	public static void showMessageDialog(Relation r) {
		CreateDecompositionFrame cf = getCurrentDialog();
		cf.fdSetPanel.clear();
		cf.fdSetPanel.load();
		if(r!=null){
			cf.fdSetPanel.selectRelation(r);
		}
		cf.setVisible(true);		
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object src = ae.getSource();
		if(src==btnOk){		
			Decomposition decomposition = fdSetPanel.getResult();
			Relation r  = fdSetPanel.getRelation();
			if(r!=null && decomposition != null){
				Global.getInstance().getRelation(r.getName()).addDecomposition(decomposition);
				Global.getInstance().getMainFrame().selectRelation(r, decomposition);				
			}
			currentDialog.dispose();
		} else if(src==btnCancel){
			currentDialog.dispose();
		} else if(src==btnClear){
			fdSetPanel.clear();
		}
	}
	
	

}

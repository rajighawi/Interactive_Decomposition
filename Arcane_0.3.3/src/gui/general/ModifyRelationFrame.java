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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.Global;
import fd.Relation;
import utilities.SwingUtility;

public class ModifyRelationFrame extends JDialog implements ActionListener{

	private static ModifyRelationFrame currentDialog;
	
	RelationPanel relationPanel;
	
	JButton btnOk     = new JButton();
	JButton btnClear  = new JButton();
	JButton btnCancel = new JButton();
	
	ModifyRelationFrame(){
		super();
		setTitle("Modfiy New Relation");
		setModal(true);
		relationPanel = new RelationPanel();
				
		JDialog.setDefaultLookAndFeelDecorated(true);
		addWindowListener(new WindowCloseListener());	
		setPreferredSize(new Dimension(360, 480));
		
		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(relationPanel, BorderLayout.CENTER);
		getContentPane().add(buttonsPanel(), BorderLayout.PAGE_END);
		
		pack();
		requestFocus();		
		SwingUtility.center(this);
		setResizable(false);
	}
	
	public JPanel buttonsPanel(){
		JPanel p2 = new JPanel(new GridBagLayout());
		
		btnOk.setText("OK");
		btnClear.setText("Clear");
		btnCancel.setText("Cancel");
		
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
	
	public static ModifyRelationFrame getCurrentDialog() {
		if(currentDialog == null){
			currentDialog = new ModifyRelationFrame();
		}
		return currentDialog;
	}
	
	public void close() {
		currentDialog = null;
	}
	
	public static void showMessageDialog() {
		ModifyRelationFrame cr = getCurrentDialog();
		cr.relationPanel.setEditable(true);
		cr.relationPanel.clear();
		cr.setVisible(true);		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object src = ae.getSource();
		if(src==btnOk){			
			Relation relation = relationPanel.getRelation();
			try {
				Global.getInstance().addRelation(relation);
				Global.getInstance().getMainFrame().addRelation(relation);
				currentDialog.dispose();
			} catch (Exception e) {
				//e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage());
			}			
		} else if(src==btnCancel){
			currentDialog.dispose();
		} else if(src==btnClear){
			relationPanel.clear();
		}
	}
	
	

}

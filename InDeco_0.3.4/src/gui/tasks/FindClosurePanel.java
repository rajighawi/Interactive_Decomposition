package gui.tasks;

import fd.AttributeSet;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;
import gui.general.AttributeSetPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import utilities.SwingUtility;

public class FindClosurePanel extends JPanel implements ActionListener {

	AttributeSetPanel attPanel       = new AttributeSetPanel();
	AttributeSetPanel resultAttPanel = new AttributeSetPanel();
	
	JButton btnOk = new JButton("Find Closure");
	
	//FDSetPanel fdSetPanel = new FDSetPanel();
	
	JLabel lblResult;
		
	Relation relation;
	FDSet fdSet;
	
	public FindClosurePanel(){
		setLayout(new GridLayout(1, 2));		
		this.relation = null;
		this.fdSet = null;
		attPanel.setBorder(SwingUtility.myBorder("Select Attributes:")); 		
		
		//JPanel topPanel = new JPanel(new GridLayout(1, 2));
		add(attPanel); 
		add(rightPanel());
	//	add(topPanel,      BorderLayout.CENTER);
	//	add(rightPanel(), BorderLayout.PAGE_END);
	}

	
	private JPanel rightPanel(){
		JPanel p = new JPanel(new BorderLayout());		
		btnOk.setFont(SwingUtility.fnt_label1);
		btnOk.addActionListener(this);
		
		lblResult = new JLabel("{ }", SwingConstants.CENTER);
		lblResult.setFont(SwingUtility.fnt_label2);
		
		
		p.add(btnOk, 		  BorderLayout.PAGE_START);	
		p.add(resultAttPanel, BorderLayout.CENTER);			
		p.add(lblResult, 	  BorderLayout.PAGE_END);
		
		return p;
	}
	
	public void clear(){
		attPanel.clear(); 
		lblResult.setText(" ");
		resultAttPanel.clear();
		this.relation = null;
		this.fdSet = null;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if(src == btnOk){
			AttributeSet atts = attPanel.getAttributeSet();
			if(fdSet!=null ){
				if(atts.isEmpty()){
					JOptionPane.showMessageDialog(null, "No attributes selected! Select some.");
				} else {
					AttributeSet closure = FDUtility.closure(atts, fdSet);
					lblResult.setText(""+closure.toString());
					resultAttPanel.load(closure);	
				}				
			}
		}
	}
	
	public void load(Relation relation, FDSet fdSet){
		clear();
		this.relation = relation;
		this.fdSet = fdSet;
		if(relation!=null){
			attPanel.load(relation.getAttributes());
		}
	}
	
	 
	
}

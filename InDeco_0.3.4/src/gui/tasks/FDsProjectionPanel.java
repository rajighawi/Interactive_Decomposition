package gui.tasks;

import fd.AttributeSet;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;
import gui.general.AttributeSetPanel;
import gui.general.FDSetPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import utilities.SwingUtility;

public class FDsProjectionPanel extends JPanel implements ActionListener {

	AttributeSetPanel attPanel = new AttributeSetPanel();	
	
	FDSetPanel resultPanel;
	
	JButton btnOk = new JButton("Find Projection");	
	
	JLabel lblResult = new JLabel("{ }", SwingConstants.CENTER);
	
	Relation relation;
	FDSet fdSet;
		
	public FDsProjectionPanel(){
		setLayout(new BorderLayout());		

		attPanel.setBorder(SwingUtility.myBorder("Select SubRelation Attributes:")); 
		add(attPanel,     BorderLayout.LINE_START);
		add(rightPanel(), BorderLayout.CENTER);
	}
	
	private JPanel rightPanel(){
		JPanel p = new JPanel(new BorderLayout());
		
		
		btnOk.setFont(SwingUtility.fnt_label1);
		btnOk.addActionListener(this);
		
		
		lblResult.setFont(SwingUtility.fnt_label2);
		resultPanel = new FDSetPanel();
		
		p.add(btnOk, 	   BorderLayout.PAGE_START);	
		p.add(resultPanel, BorderLayout.CENTER);			
		p.add(lblResult,   BorderLayout.PAGE_END);
		
		return p;
	}
	
	public Relation getRelation(){
		return relation;
	}
	
	public void clear(){
		relation = null;
		fdSet = null;
		attPanel.clear(); 
		lblResult.setText(" ");
		resultPanel.clear();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if(src == btnOk){
			AttributeSet atts = attPanel.getAttributeSet();
			if(fdSet != null && relation != null){				
				if(atts.isEmpty()){
					JOptionPane.showMessageDialog(null, "No attributes selected! Select some.");
				} else {
					Relation subR = new Relation("", atts);
					FDSet f_ = FDUtility.project(fdSet, relation, subR);
					
					lblResult.setText(""+f_.toString());
					resultPanel.load(f_);	
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

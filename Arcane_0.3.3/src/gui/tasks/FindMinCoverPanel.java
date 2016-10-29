package gui.tasks;

import fd.FDSet;
import fd.MinCoverExplain;
import fd.Relation;
import gui.general.FDSetPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import main.Global;
import utilities.SwingUtility;

public class FindMinCoverPanel extends JPanel implements ActionListener {

	FDSetPanel resultPanel;
	JButton btnOk1, btnAppend; 	
	JLabel lblResult; 
	JTextArea txtLog;
	
	Relation relation;
	FDSet fdSet;
	FDSet minCover;
	
	public FindMinCoverPanel(){
		this.relation = null;
		this.fdSet 	  = null;
		this.minCover = null;
		setLayout(new BorderLayout());
		add(bottomPanel(), BorderLayout.CENTER);
	}
	
	private JPanel bottomPanel(){
		JPanel p = new JPanel(new BorderLayout());
		
		btnOk1 = new JButton("Find Minimal Cover");
		btnOk1.setFont(SwingUtility.fnt_label1);
		btnOk1.addActionListener(this);
		
		btnAppend = new JButton("Append to Relation");
		btnAppend.setFont(SwingUtility.fnt_label1);
		btnAppend.addActionListener(this);
		
		JPanel pp = new JPanel(new GridBagLayout());
		pp.add(btnOk1, SwingUtility.myC(0, 0));
	//	pp.add(btnOk2, SwingUtility.myC(1, 0));
		
		lblResult = new JLabel("{ }", SwingConstants.CENTER);
		lblResult.setFont(SwingUtility.fnt_label1);
		resultPanel = new FDSetPanel();
		resultPanel.setBorder(SwingUtility.myBorder("Minimal Cover"));

		JPanel p2 = new JPanel(new BorderLayout());
		p2.add(lblResult,  BorderLayout.CENTER);
		p2.add(btnAppend,  BorderLayout.LINE_END);
		
		txtLog = new JTextArea();
		txtLog.setFont(SwingUtility.fnt_label1);
		
		JPanel p3 = new JPanel(new BorderLayout());
		JScrollPane sp = new JScrollPane(txtLog);
		sp.setBorder(SwingUtility.myBorder("Explaination"));
		sp.setPreferredSize(new Dimension(300, 200));		
		p3.add(resultPanel,  BorderLayout.LINE_START);
		p3.add(sp,  		 BorderLayout.CENTER);
			
		p.add(pp, BorderLayout.PAGE_START);	
		p.add(p3, BorderLayout.CENTER);			
		p.add(p2, BorderLayout.PAGE_END);
		
		return p;
	}
	
	public Relation getRelation(){		
		return relation;
	}
	
	public void clear(){
		lblResult.setText(" ");
		resultPanel.clear();
		btnAppend.setEnabled(false);
		txtLog.setText("");
		
		this.relation = null;
		this.minCover = null;
		this.fdSet 	  = null;		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if(src == btnOk1){
			if(fdSet != null){				
				// minCover = FDUtility.minimalCover(fdSet);
				MinCoverExplain mce = new MinCoverExplain(fdSet);
				minCover = mce.getOutput();
				txtLog.setText(mce.getLog());
				
				String fdsn = "fds_"+relation.getName()+"_minCover";
				minCover.setName(fdsn);
				
				resultPanel.load(minCover);
				lblResult.setText("| Minimal Cover | = " + minCover.size());	
				btnAppend.setEnabled(true);
			}
		} else if(src == btnAppend){
			if(relation!=null && minCover != null){
				try {
					Global.getInstance().getRelation(relation.getName()).addFDSet(minCover);
					Global.getInstance().getMainFrame().selectRelation(relation, minCover);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
		}
	}
	
	public void load(Relation relation, FDSet fdSet){
		clear();
		this.relation = relation;
		this.fdSet 	  = fdSet;	
	}
	
}

package gui.tasks;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import utilities.SwingUtility;
import fd.AttributeSet;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class FindKeysPanel extends JPanel implements ActionListener {

	JTextArea txtResult;
	
	JButton btnOk1, btnOk2;
		
	JLabel lblResult;
	LMRPanel lmrPanel;
	PNPPanel pnpPanel;
	
	Relation relation;
	FDSet fdSet;
	
	public FindKeysPanel(){
		setLayout(new BorderLayout());
		
		add(bottomPanel(), BorderLayout.CENTER);
	}
	
	private JPanel bottomPanel(){
		JPanel p = new JPanel(new BorderLayout());
		
		btnOk1 = new JButton("Find Keys (L.M.R)");
		btnOk1.setFont(SwingUtility.fnt_label1);
		btnOk1.addActionListener(this);
		
		btnOk2 = new JButton("Find Keys (Greedy)");
		btnOk2.setFont(SwingUtility.fnt_label1);
		btnOk2.addActionListener(this);
		
		JPanel pp = new JPanel(new GridBagLayout());
		pp.add(btnOk1, SwingUtility.myC(0, 0));
		pp.add(btnOk2, SwingUtility.myC(1, 0));
		
		lblResult = new JLabel("{ }", SwingConstants.CENTER);
		lblResult.setFont(SwingUtility.fnt_label2);
		txtResult = new JTextArea();
		txtResult.setFont(SwingUtility.fnt_data2);
		JScrollPane sp = new JScrollPane(txtResult);
		sp.setPreferredSize(new Dimension(200, 200));
		
		JPanel midp = new JPanel(new GridLayout(1, 3));
		lmrPanel = new LMRPanel();
		lmrPanel.setBorder(SwingUtility.myBorder("L.M.R"));
		pnpPanel = new PNPPanel();
		pnpPanel.setBorder(SwingUtility.myBorder("P. NP"));
		
		midp.add(lmrPanel);
		midp.add(sp);
		midp.add(pnpPanel);
				
		p.add(pp, 	     BorderLayout.PAGE_START);	
		p.add(midp,		 BorderLayout.CENTER);			
		p.add(lblResult, BorderLayout.PAGE_END);
		
		return p;
	}
	
	/*public Relation getRelation(){
		return relation;
	}*/
	
	public void clear(){
		this.relation = null;
		this.fdSet 	  = null;		
		txtResult.setText("");
		lmrPanel.clear();
		lblResult.setText(" ");
		pnpPanel.clear();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if(src == btnOk1 || src == btnOk2){
			if(relation !=null && fdSet != null){
				long start = System.currentTimeMillis();
				
				Set<AttributeSet> keys = new HashSet<AttributeSet>();
				
				if(src == btnOk1){
					keys = FDUtility.findAllKeys_LMR(relation, fdSet);
					lmrPanel.go(relation, fdSet);
				} else if(src == btnOk2)
					keys = FDUtility.findAllKeys_Greedy(relation, fdSet);
				
				pnpPanel.go(relation, keys);
				long ms = System.currentTimeMillis() - start;
				String s = "";
				for(AttributeSet as:keys){
					s += as.toString()+"\n";
				}
				txtResult.setText(s);
				lblResult.setText("Time: "+ms+" ms");				
			}
		}
	}
	
	
	public void load(Relation relation, FDSet fdSet){
		clear();
		this.relation = relation;
		this.fdSet = fdSet;	
	}
	
}

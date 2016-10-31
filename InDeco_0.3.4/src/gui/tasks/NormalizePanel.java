package gui.tasks;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import main.Global;
import normalization.BCNF;
import normalization.Normalizer;
import normalization.SecondNF;
import normalization.ThirdNF;
import utilities.ColorUtility;
import utilities.FileUtility;
import utilities.IconUtility;
import utilities.SwingUtility;
import fd.Decomposition;
import fd.FDSet;
import fd.Relation;
import gui.utilities.MyButton;

public class NormalizePanel extends JPanel implements ActionListener {

	JButton btnCheck, btnNormalize, btnAppend1, btnAppend2, btnSave, btnRunSQL; 	
	
	JLabel lblResult; 	
	
	JTextArea txtExplain;
	JTextArea txtResult;
	JTextArea txtSQL;
	JTabbedPane tabPane;
	
	String type;
	Relation relation;
	FDSet    fdSet;
	
	Set<Relation> result;
	
	Normalizer normalForm;
	
	public NormalizePanel(String type){
		this.type     = type;
		this.relation = null;
		this.fdSet    = null;
		this.result   = null;
		setLayout(new BorderLayout());
		
		add(bottomPanel(), BorderLayout.CENTER);
	}
	
	private JPanel bottomPanel(){
		JPanel p = new JPanel(new BorderLayout());
		
		btnCheck = new JButton("Check "+type);
		btnCheck.setFont(SwingUtility.fnt_label1);
		btnCheck.addActionListener(this);
		btnNormalize = new JButton("Normalize "+type);
		btnNormalize.setFont(SwingUtility.fnt_label1);
		btnNormalize.addActionListener(this);
		btnNormalize.setEnabled(false);
				
		JPanel pp = new JPanel(new GridBagLayout());
		pp.add(btnCheck,     SwingUtility.myC(0, 0));
		pp.add(btnNormalize, SwingUtility.myC(1, 0));
		
		lblResult = new JLabel("{ }", SwingConstants.CENTER);
					
		p.add(pp, BorderLayout.PAGE_START);	
		p.add(resPanel(), BorderLayout.CENTER);
		
		return p;
	}
	
	public JPanel resPanel(){
		JPanel p   = new JPanel(new BorderLayout());
		txtExplain = new JTextArea();
		txtResult  = new JTextArea();
		txtSQL     = new JTextArea();
		txtExplain.setFont(SwingUtility.fnt_label1);
		txtResult.setFont(SwingUtility.fnt_data2);
		txtSQL.setFont(SwingUtility.fnt_data2);
		
		btnSave = new JButton("Save"); 
		btnSave.setFont(SwingUtility.fnt_label1);
		btnSave.addActionListener(this);
		btnAppend1= new MyButton("Append to Workspace", IconUtility.append); 
		btnAppend1.setFont(SwingUtility.fnt_label1);
		btnAppend1.addActionListener(this);
		
		btnAppend2= new MyButton("Asscoiate Decomposition to Relation", IconUtility.add24); 
		btnAppend2.setFont(SwingUtility.fnt_label1);
		btnAppend2.addActionListener(this);
		
		btnRunSQL = new JButton("Save"); 
		btnRunSQL.setFont(SwingUtility.fnt_label1);
		btnRunSQL.addActionListener(this);
		
		JPanel p1 = new JPanel(new BorderLayout());
		JScrollPane sp1 = new JScrollPane(txtExplain);
		p1.add(sp1, 	BorderLayout.CENTER);
		p1.add(btnSave, BorderLayout.PAGE_END);
		
		JPanel p2 = new JPanel(new BorderLayout());
		JScrollPane sp2 = new JScrollPane(txtResult);
		JPanel pp2 = new JPanel(new GridLayout(1, 2));
		pp2.add(btnAppend1);
		pp2.add(btnAppend2);
		p2.add(sp2, BorderLayout.CENTER);
		p2.add(pp2, BorderLayout.PAGE_END);
		
		
		JPanel p3 = new JPanel(new BorderLayout());
		JScrollPane sp3 = new JScrollPane(txtSQL);
		p3.add(sp3, 	BorderLayout.CENTER);
		p3.add(btnRunSQL, BorderLayout.PAGE_END);
		
		tabPane = new JTabbedPane();
		tabPane.setFont(SwingUtility.fnt_label1);
		tabPane.addTab("Explain",	p1);
		tabPane.addTab("Result", 	p2);
		tabPane.addTab("SQL",  		p3);
		
		p.add(tabPane, BorderLayout.CENTER);
		return p;
	}
	
	public Relation getRelation(){
		return relation;
	}
	
	public void clear(){
		lblResult.setText(" ");
		txtExplain.setText("");
		txtResult.setText("");
		txtSQL.setText("");
				
		btnCheck.setEnabled(false);
		btnNormalize.setEnabled(false);
		
		this.relation 	= null;
		this.fdSet 		= null;
		this.result     = null;
		this.normalForm = null;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if(src == btnCheck){			
			if(relation!=null && fdSet!=null){
				normalForm = makeNormalForm();
				String log = normalForm.explain();
				txtExplain.setText(log);
				boolean b = normalForm.check();
				txtExplain.setBackground(b?ColorUtility.lightgreen:ColorUtility.lightpink);
				
				lblResult.setIcon(b?IconUtility.yes24:IconUtility.no24);
				
				btnNormalize.setEnabled(!b); // if not in bcnf, enable btnNormalize
			 
			}
		} else if(src == btnNormalize){
			if(relation!=null && fdSet!=null){
				normalForm = makeNormalForm();
				try {
					result = normalForm.decompose();
					String resultS = "";
					for(Relation f:result){
						resultS += f.toString()+"\n"+f.getFdSets().get(0)+"\n\n";
					}
					txtResult.setText(resultS);
					String[] sql = Normalizer.sql(relation, result);
					for (int i = 0; i < sql.length; i++) {
						txtSQL.append(sql[i]+"\n");	
					}
					
					tabPane.setSelectedIndex(1); // go to Result tab
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
		} else if(src == btnSave){
			FileUtility.save(txtExplain.getText());
		} else if(src == btnAppend2){
			if(relation!=null&&fdSet!=null&&result!=null){
				Decomposition deco = new Decomposition(relation, fdSet, "deco_"+relation.getName()+"_"+type);			
				for(Relation r:result){
					deco.add(r);		
				}
				relation.addDecomposition(deco);
				System.out.println("Decomposition "+deco.getName()+" is added. FDSet = "+fdSet.getName()+".");
				Global.getInstance().getMainFrame().selectRelation(relation, deco);
			}			
		} else if(src == btnAppend1){
			for(Relation r:result){				
				try {					
					Global.getInstance().addRelation(r);
					Global.getInstance().getMainFrame().addRelation(r);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();
					continue;
				}		
			}
		} else if(src == btnRunSQL){
			FileUtility.save(txtSQL.getText());
		}
	}
	
	public Normalizer makeNormalForm(){
		if(relation!=null && fdSet!=null){
			if(type==Normalizer._2NF){
				return new SecondNF(relation, fdSet);
			} else if(type==Normalizer._3NF){
				return new ThirdNF(relation, fdSet);
			} else if(type==Normalizer.BCNF){
				return new BCNF(relation, fdSet);
			} 
		}
		return null;
	}
	
	public void load(Relation r, FDSet fdSet){
		clear();
		this.relation = r;
		this.fdSet = fdSet;
		if(fdSet!=null){
			btnCheck.setEnabled(true);
		}
		
	}
	
}

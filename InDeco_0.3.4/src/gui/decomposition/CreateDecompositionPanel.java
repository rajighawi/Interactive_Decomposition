package gui.decomposition;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.Global;
import renderer.MyListRenderer;
import utilities.SwingUtility;
import fd.AttributeSet;
import fd.Decomposition;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;
import gui.general.AttributeSetPanel;
import gui.general.FDSetPanel;

public class CreateDecompositionPanel extends JPanel implements ActionListener {

	JComboBox<Relation> cmbRelName;
	JComboBox<FDSet> cmbFDSet;	
	AttributeSetPanel attPanel;
	FDSetPanel fdSetPanel;

	//JButton btnCheck, btnNormalize, btnAppend, btnSave, btnRunSQL; 	
	JButton btnAdd;
	
	JTextField txtDecoName = new JTextField();
	
	//JTextArea txtExplain;
	JTextArea txtResult;
	//JTextArea txtSQL;
	JTabbedPane tabPane;
	 
	Relation relation;
	FDSet    fdSet;
	
	//ArrayList<Relation> result;
	Decomposition result;
	
	public CreateDecompositionPanel(){
		this.relation = null;
		this.fdSet    = null;
		this.result   = null;//new ArrayList<Relation>();
		setLayout(new BorderLayout());
		JPanel midPanel = new JPanel(new GridLayout(1, 2));
		
		attPanel = new AttributeSetPanel();		
		attPanel.setBorder(SwingUtility.myBorder("Attributes:"));		
		midPanel.add(attPanel); 
		midPanel.add(fdsPanel());
		
		JPanel mainPanel = new JPanel(new GridLayout(2, 1));
		mainPanel.add(midPanel);		
		mainPanel.add(bottomPanel());
		
		add(topPanel(), BorderLayout.PAGE_START);
		add(mainPanel,  BorderLayout.CENTER);
	}
	
	private JPanel topPanel(){
		JPanel topPanel = new JPanel(new GridLayout());
		
		JLabel lblRelName = new JLabel("Relation Name :", SwingConstants.CENTER);
		lblRelName.setFont(SwingUtility.fnt_label1);
		
		cmbRelName = new JComboBox<Relation>();
		cmbRelName.setFont(SwingUtility.fnt_label1);
		cmbRelName.addActionListener(this);
		cmbRelName.setRenderer(new MyListRenderer());
		cmbRelName.setPreferredSize(new Dimension(160, 32));
		
		JLabel lblDecoId = new JLabel("Decomposition ID :", SwingConstants.CENTER);
		lblDecoId.setFont(SwingUtility.fnt_label1);
		
		txtDecoName.setFont(SwingUtility.fnt_label1);
		
		topPanel.add(lblRelName);
		topPanel.add(cmbRelName);
		topPanel.add(lblDecoId);
		topPanel.add(txtDecoName);
		
		return topPanel;
	}
	
	private JPanel fdsPanel(){	
		cmbFDSet = new JComboBox<FDSet>();
		cmbFDSet.setFont(SwingUtility.fnt_label1);
		cmbFDSet.addActionListener(this);	
		cmbFDSet.setRenderer(new MyListRenderer());
		cmbFDSet.setPreferredSize(new Dimension(160, 32));
		fdSetPanel = new FDSetPanel();
						 
		JLabel lblFDSet = new JLabel("FD Set:");
		lblFDSet.setFont(SwingUtility.fnt_label1);		
		JPanel topPanel = new JPanel(new GridBagLayout());		
		topPanel.add(lblFDSet, SwingUtility.myC(0, 1));
		topPanel.add(cmbFDSet, SwingUtility.myC(1, 1));
		fdSetPanel.setBorder(SwingUtility.myBorder("Functional Dependencies:")); 
		
		JPanel p = new JPanel(new BorderLayout());
		p.add(topPanel, BorderLayout.PAGE_START); 
		p.add(fdSetPanel, BorderLayout.CENTER);
		
		return p;
	}
	
	private JPanel bottomPanel(){
		JPanel p = new JPanel(new BorderLayout());
		
		btnAdd= new JButton("Make Relation");
		btnAdd.setFont(SwingUtility.fnt_label1);
		btnAdd.addActionListener(this);
		btnAdd.setEnabled(false);
		
		/*
		btnCheck = new JButton("Check "+type);
		btnCheck.setFont(SwingUtility.fnt_label2);
		btnCheck.addActionListener(this);
		btnNormalize = new JButton("Normalize "+type);
		btnNormalize.setFont(SwingUtility.fnt_label2);
		btnNormalize.addActionListener(this);
		btnNormalize.setEnabled(false);
			*/	
		JPanel pp = new JPanel(new GridBagLayout());
		pp.add(btnAdd,     SwingUtility.myC(0, 0));
	//	pp.add(btnCheck,     SwingUtility.myC(0, 0));
	//	pp.add(btnNormalize, SwingUtility.myC(1, 0));
							
		p.add(pp, BorderLayout.PAGE_START);	
		p.add(resPanel(), BorderLayout.CENTER);
		
		return p;
	}
	
	public JPanel resPanel(){
		JPanel p   = new JPanel(new BorderLayout());
	//	txtExplain = new JTextArea();
		txtResult  = new JTextArea();
	//	txtSQL     = new JTextArea();
	//	txtExplain.setFont(SwingUtility.fnt_label1);
		txtResult.setFont(SwingUtility.fnt_data2);
	//	txtSQL.setFont(SwingUtility.fnt_data2);
		/*
		btnSave = new JButton("Save"); 
		btnSave.setFont(SwingUtility.fnt_label1);
		btnSave.addActionListener(this);
		btnAppend= new MyButton("Append to Workspace", IconUtility.append); 
		btnAppend.setFont(SwingUtility.fnt_label1);
		btnAppend.addActionListener(this);
		btnRunSQL = new JButton("Save"); 
		btnRunSQL.setFont(SwingUtility.fnt_label1);
		btnRunSQL.addActionListener(this);
		
		JPanel p1 = new JPanel(new BorderLayout());
		JScrollPane sp1 = new JScrollPane(txtExplain);
		p1.add(sp1, 	BorderLayout.CENTER);
		p1.add(btnSave, BorderLayout.PAGE_END);
		*/
		
		JPanel p2 = new JPanel(new BorderLayout());
		JScrollPane sp2 = new JScrollPane(txtResult);
		p2.add(sp2, 	BorderLayout.CENTER);
	//	p2.add(btnAppend, BorderLayout.PAGE_END);
		
		/*
		JPanel p3 = new JPanel(new BorderLayout());
		JScrollPane sp3 = new JScrollPane(txtSQL);
		p3.add(sp3, 	BorderLayout.CENTER);
		p3.add(btnRunSQL, BorderLayout.PAGE_END);
		*/
		tabPane = new JTabbedPane();
		tabPane.setFont(SwingUtility.fnt_label1);
	//	tabPane.addTab("Explain",	p1);
		tabPane.addTab("Sub-Relations", 	p2);
	//	tabPane.addTab("SQL",  		p3);
		
		p.add(tabPane, BorderLayout.CENTER);
		return p;
	}
	
	public Relation getRelation(){
		Object o = cmbRelName.getSelectedItem();
		return o==null?null:(Relation)o;
	}
	
	public void selectRelation(Relation r){
		this.relation = r;
		cmbRelName.setSelectedItem(relation);
	}
	
	public void clear(){
		//cmbRelName.setSelectedItem(null);
		attPanel.clear();
		fdSetPanel.clear();
	//	resultPanel.clear();
	//	btnAppend.setEnabled(false);
	//	txtExplain.setText("");
		txtResult.setText("");
	//	txtSQL.setText("");
		
	//	btnCheck.setEnabled(false);
	//	btnNormalize.setEnabled(false);
		
		btnAdd.setEnabled(false);
		
		this.relation 	= null;
		this.fdSet 		= null;
		this.result     = null; // new ArrayList<Relation>();
	}
	
	public void load(){
		clear();
		cmbRelName.removeAllItems();
		cmbRelName.addItem(null);
		ArrayList<Relation> relations = Global.getInstance().getRelationList();
		for (int i = 0; i < relations.size(); i++) {
			cmbRelName.addItem(relations.get(i));
		}
		btnAdd.setEnabled(false);
	}
	
	public void loadFDSets(ArrayList<FDSet> fdSets){
		cmbFDSet.removeAllItems();
		cmbFDSet.addItem(null);
		for (int i = 0; i < fdSets.size(); i++) {
			cmbFDSet.addItem(fdSets.get(i));
		}	
		btnAdd.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if(src == cmbRelName){
			Object o = cmbRelName.getSelectedItem();
			if(o==null){
				clear();
				cmbFDSet.removeAllItems();
			} else if(o instanceof Relation){
				relation = (Relation) o;
				attPanel.load(relation.getAttributes()); 
				loadFDSets(relation.getFdSets());				
			}
			btnAdd.setEnabled(false);
		} else if(src == cmbFDSet){
			Object o = cmbFDSet.getSelectedItem();
			if(o==null){
				fdSetPanel.clear();
				btnAdd.setEnabled(false);
			} else if(o instanceof FDSet){
				fdSet = (FDSet) o;
				fdSetPanel.load(fdSet);
				String name = "deco_"+relation.getName()+"_"+(1+relation.getDecompositions().size());
				txtDecoName.setText(name);
				result   = new Decomposition(relation, fdSet, name);
				btnAdd.setEnabled(true);				
			}
		} else if(src==btnAdd){
			if(relation==null || result==null) return;
			AttributeSet atts = attPanel.getAttributeSet();
			int n = result.size();
			Relation r_ = new Relation(relation.getName()+(n+1), atts);
			if(fdSet!=null){
				FDSet f_ = FDUtility.project(fdSet, relation, r_);
				r_.addFDSet(f_);
			}
			result.add(r_);			
			updateSubsList();
		}
		/*
		else if(src == btnCheck){			
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
					String[] sql = NormalForm.sql(relation, result);
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
		} else if(src == btnAppend){
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
		*/
	}
	
	public void updateSubsList(){
		txtResult.setText("");
		ArrayList<Relation> subs = result.getSubrelations();
		for (int i = 0; i < subs.size(); i++) {
			Relation sub = subs.get(i);
			txtResult.append(sub.toString()+"\n");
			txtResult.append(sub.getFdSets().get(0)+"\n\n");
		}		 
	}

	public Decomposition getResult() {
		String name = txtDecoName.getText();
		result.setName(name);
		return result;
	}
	
	
	
}

package gui.general;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.Global;
import renderer.MyListRenderer;
import utilities.SwingUtility;
import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.Relation;

public class CreateFDSetPanel extends JPanel implements ActionListener {

	JComboBox<Relation> cmbRelName;
	JTextField txtFdsName;
	
	AttributeSetPanel lhsPanel, rhsPanel;
	
	JButton btnAdd;
//	DefaultListModel<FD> fdListModel;
//	JList<FD> fdList;
	
	FDSetPanel fdSetPanel;
	
	Relation relation;	
	
	public CreateFDSetPanel(){
		setLayout(new BorderLayout());
		relation = null;
		
		lhsPanel = new AttributeSetPanel();
		rhsPanel = new AttributeSetPanel();
		fdSetPanel = new FDSetPanel();
		
		//lhs = new AttributeSet();
		//rhs = new AttributeSet();
		
		cmbRelName = new JComboBox<Relation>();
		cmbRelName.setFont(SwingUtility.fnt_label1);
		cmbRelName.setRenderer(new MyListRenderer());		
		cmbRelName.addActionListener(this);
		cmbRelName.setPreferredSize(new Dimension(160, 32));
		
		JLabel lblRelName = new JLabel("Relation Name :", SwingConstants.CENTER);
		lblRelName.setFont(SwingUtility.fnt_label1);
		
		txtFdsName = new JTextField();
		txtFdsName.setFont(SwingUtility.fnt_label1);
		JLabel lblFdsName = new JLabel("FD Set ID :", SwingConstants.CENTER);
		lblFdsName.setFont(SwingUtility.fnt_label1);
		
		
		JPanel topPanel = new JPanel(new GridLayout(1, 4));
		topPanel.add(lblRelName);
		topPanel.add(cmbRelName);		
		topPanel.add(lblFdsName);
		topPanel.add(txtFdsName);		
		 
		JLabel lblArrow1 = new JLabel(FD.FD_ARROW, SwingConstants.CENTER);
		lblArrow1.setFont(SwingUtility.fnt_label3);
		JLabel lblArrow2 = new JLabel(FD.FD_ARROW, SwingConstants.CENTER);
		lblArrow2.setFont(SwingUtility.fnt_title);
		 
		lhsPanel.setBorder(SwingUtility.myBorder("Left hand side:"));
		rhsPanel.setBorder(SwingUtility.myBorder("Right hand side:"));
		
		JPanel midPanel = new JPanel(new BorderLayout());
		midPanel.add(lblArrow2, BorderLayout.CENTER);
		midPanel.add(lblArrow1, BorderLayout.PAGE_END);
		
		add(topPanel, BorderLayout.PAGE_START);
		add(midPanel, BorderLayout.CENTER);
		add(lhsPanel, BorderLayout.LINE_START);
		add(rhsPanel, BorderLayout.LINE_END);
		add(bottomPanel(), BorderLayout.PAGE_END);
	}
	
	private JPanel bottomPanel(){
		JPanel p = new JPanel(new BorderLayout());
		
		btnAdd = new JButton("Add");
		btnAdd.setFont(SwingUtility.fnt_label1);
		btnAdd.addActionListener(this);
		p.add(btnAdd, BorderLayout.PAGE_START);
		
		p.add(fdSetPanel, BorderLayout.CENTER);
		
		return p;
	}
	
	public Relation getRelation(){
		Object o = cmbRelName.getSelectedItem();
		return ((o!=null && o instanceof Relation)?(Relation) o:null);
	}
	
	public void clear(){
		//cmbRelName.setSelectedItem(null);
		txtFdsName.setText("");
		lhsPanel.clear();
		rhsPanel.clear();
		fdSetPanel.clear();
	}
	
	public void selectRelation(Relation r){
		this.relation = r;
		cmbRelName.setSelectedItem(relation);
	}
	
	public void loadRelations(){
		cmbRelName.removeAllItems();
		cmbRelName.addItem(null);
		System.out.println("--------------------");
		ArrayList<Relation> relations = Global.getInstance().getRelationList();
		for (int i = 0; i < relations.size(); i++) {
			cmbRelName.addItem(relations.get(i));
			System.out.println(relations.get(i));
		}
		System.out.println("--------------------");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if(src == cmbRelName){
			Object o = cmbRelName.getSelectedItem();
			if(o==null){
				clear();
			} else if(o instanceof Relation){
				this.relation = (Relation) o;
			//	selectRelation(relation);		
				lhsPanel.load(relation.getAttributes());
				rhsPanel.load(relation.getAttributes());
				int n = relation.getFdSets().size();
				txtFdsName.setText("fds_"+relation.getName()+"_"+n);
			}
		} else if(src == btnAdd){
			AttributeSet lhs = lhsPanel.getAttributeSet();
			AttributeSet rhs = rhsPanel.getAttributeSet();
			FD fd = new FD(lhs, rhs);
			fdSetPanel.addFD(fd);
		}
	}

	public FDSet getFDSet(){
		if(relation==null)
			return null;
		String fdsName = txtFdsName.getText();
		return fdSetPanel.makeFDSet(fdsName);
	}
	
}

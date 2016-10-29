package gui.tasks;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import fd.Attribute;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;
import gui.decomposition.ArmstrongTableModel;
import gui.utilities.MyButton1;
import gui.utilities.MyTable;
import gui.utilities.TextPanel;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import utilities.SwingUtility;

public class ArmstrongPanel extends JPanel implements ActionListener {

	ArmstrongTableModel armstrongTableModel = new ArmstrongTableModel();
	MyTable table = new MyTable(armstrongTableModel);
	
	TextPanel textPanel = new TextPanel("", TextPanel.HORIZONTAL_LAYOUT);
	
	JButton btnStart = new MyButton1("Start");
	
	Relation relation;
	FDSet fdSet;
	
	public ArmstrongPanel() {
		super(new BorderLayout());
		
		relation = null;
		fdSet = null;
		
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.setFont(SwingUtility.fnt_label1);
		
		JScrollPane sp = new JScrollPane(table);
		sp.setPreferredSize(new Dimension(200,200));
		
		tabPane.addTab("Table", sp);
		tabPane.addTab("SQL", textPanel);
		
		add(topPanel(), BorderLayout.PAGE_START);
		add(tabPane, 	BorderLayout.CENTER);
	}
	
	public JPanel topPanel(){
		JPanel topPanel = new JPanel(new GridBagLayout());
		topPanel.setBorder(SwingUtility.myBorder2());
		btnStart.addActionListener(this);
		topPanel.add(btnStart, SwingUtility.myC(0, 0));
		return topPanel;
	}
	
	public void go(Relation r, FDSet f){
		ArrayList<String[]> data = FDUtility.armstrong(r, f);
		ArrayList<Attribute> atts = new ArrayList<Attribute>(r.getAttributes());
		Collections.sort(atts);
		armstrongTableModel.load(atts, data);
		table.setModel(armstrongTableModel);
		
		
		String sql = "";
		sql += "CREATE TABLE "+r.getName()+" (";
		for (int i = 0; i < atts.size(); i++) {
			sql += (i>0?", ":"")+atts.get(i).getName()+" VARCHAR";
		}
		sql += ");\n";
		sql += "INSERT INTO "+r.getName()+" VALUES \n";
		for (int i = 0; i < data.size(); i++) {
			String[] tuple = data.get(i);
			sql += (i>0?",\n":"")+ "(";
			for (int j = 0; j < tuple.length; j++) {
				String v = tuple[j];
				sql += (j>0?", ":"")+"'"+v+"'";
			}
			sql += ")";			
		}
		sql += ";\n";
		
		textPanel.setText(sql);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if(src == btnStart){
			if(relation!=null && fdSet != null){
				go(relation, fdSet);
			}
		}
	}
	
	public void load(Relation r, FDSet fdSet){
		this.relation = r;
		this.fdSet = fdSet;
	}
	
}

package gui.general;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import utilities.SwingUtility;
import fd.Attribute;
import fd.AttributeSet;
import fd.Relation;

public class RelationPanel extends JPanel{

	JTextField txtRelName = new JTextField();
	JTextArea txtColumns = new JTextArea();
	
	public RelationPanel(){
		setLayout(new GridBagLayout());
		
		JLabel lblRelName = new JLabel("Relation Name :");
		JLabel lblColumns = new JLabel("Columns:");
		
		add(lblRelName, SwingUtility.myC(0, 0));
		add(txtRelName, SwingUtility.myC(0, 1, 2, 1));
		add(lblColumns, SwingUtility.myC(0, 2));
		txtColumns.setFont(SwingUtility.fnt_data);
		txtRelName.setFont(SwingUtility.fnt_data2);
		JScrollPane sp = new JScrollPane(txtColumns);
		sp.setPreferredSize(new Dimension(300, 300));
		add(sp, SwingUtility.myC(0, 3, 2, 1));
	}
	
	public Relation getRelation(){
		String rname = txtRelName.getText();
		String columns = txtColumns.getText();
		String[] x = columns.split("\n");
		AttributeSet aa = new AttributeSet();
		for (int i = 0; i < x.length; i++) {
			Attribute a = new Attribute(x[i]);
			aa.add(a);
		}
		return new Relation(rname, aa);
	}
	
	public void load(Relation r){
		txtRelName.setText(r.getName());
		txtColumns.setText("");
		AttributeSet as = r.getAttributes();
		for (Attribute a:as) {
			txtColumns.append(a.getName()+"\n");
		}
	}
	
	public void clear(){
		txtRelName.setText("");
		txtColumns.setText("");
	}
	
	public void setEditable(boolean f){
		txtRelName.setEditable(f);
		txtColumns.setEditable(f);
	}
	
	
}

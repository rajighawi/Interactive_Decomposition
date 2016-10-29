package gui.general;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import renderer.MyListRenderer;
import utilities.SwingUtility;
import fd.Attribute;
import fd.FD;
import fd.FDSet;
import fd.Relation;
import gui.utilities.MyButton1;

public class UpdateAttributeSetPanel extends JPanel implements ActionListener{
	
	DefaultListModel<Attribute> attListModel;
	JList<Attribute> attList;
	
	JButton btnNew    = new MyButton1("New");
	JButton btnRename = new MyButton1("Rename");
	JButton btnRemove = new MyButton1("Remove");
		
	Relation relation;
	//AttributeSet attributeSet;
	
	public UpdateAttributeSetPanel(){
		super(new BorderLayout());
		setBorder(SwingUtility.myBorder("Attributes"));
		relation = null;
				
		attListModel = new DefaultListModel<Attribute>();
		attList = new JList<Attribute>(attListModel);
		attList.setFont(SwingUtility.fnt_data2);
		attList.setCellRenderer(new MyListRenderer());
		attList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		ListSelectionModel lsm = attList.getSelectionModel();
		lsm.addListSelectionListener(new ListSelectionListener() {			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (lsm.isSelectionEmpty()) {
					btnRemove.setEnabled(false);
					btnRename.setEnabled(false);
				} else { // Find out which indexes are selected.
					btnRemove.setEnabled(true);
					btnRename.setEnabled(true);
				}
			}
		});
		
		JScrollPane sp1 = new JScrollPane(attList);
		sp1.setPreferredSize(new Dimension(200, 200));

		add(sp1, BorderLayout.CENTER);
		add(btnsPanel(), BorderLayout.PAGE_END);		
	}
	
	public void clear(){
		attListModel.removeAllElements();
		attList.setModel(attListModel);
		enableButtons(false);
	}
	
	
	public JPanel btnsPanel(){
		JPanel p = new JPanel(new GridLayout(1, 3, 4, 4));
		p.setBorder(SwingUtility.myBorder2());
		btnNew.addActionListener(this);
		btnRename.addActionListener(this);
		btnRemove.addActionListener(this);
		btnNew.setPreferredSize(new Dimension(80, 32));
		btnRename.setPreferredSize(new Dimension(80, 32));
		btnRemove.setPreferredSize(new Dimension(80, 32));
		enableButtons(false);
		p.add(btnNew);
		p.add(btnRename);
		p.add(btnRemove);
		return p;
	}
	
	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
		updateAttList();
		enableButtons(false);
		btnNew.setEnabled(true);
	}
	
	public void updateAttList(){
		attListModel.removeAllElements();
		for(Attribute att : relation.getAttributes()){
			attListModel.addElement(att);
		}		
		attList.setModel(attListModel);	
	}
	
	private Attribute getSelectedAttribute(){
		int idx = attList.getSelectedIndex();
		if(idx>=0){
			Attribute a = attList.getSelectedValue();
			return a;
		} else {
			return null;
		}
	}
	
	public void enableButtons(boolean b){
		btnNew.setEnabled(b);
		btnRemove.setEnabled(b);
		btnRename.setEnabled(b);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if(src==btnNew){	
			if(relation == null){
				JOptionPane.showMessageDialog(null, "No relation is selected");
				return;
			}
			String attName = JOptionPane.showInputDialog("Attribute Name: ");
			if(attName==null) return;
			for(Attribute a:relation.getAttributes()){
				if(a.getName().equalsIgnoreCase(attName)){
					JOptionPane.showMessageDialog(null, "Attribute `"+attName+"` already exists in relation `"+relation.getName()+"`");
					return;
				}
			}			
			Attribute a = new Attribute(attName);
			relation.addAttribute(a);
			updateAttList();
			//Global.getInstance().getRelation(relation.getName()).addAttribute(a);
		} else if(src==btnRename){
			if(relation == null){
				JOptionPane.showMessageDialog(null, "No relation is selected");
				return;
			}
			Attribute att = getSelectedAttribute();
			if(att==null) return;
			Attribute old_ = new Attribute(att.getName());
			if(att!=null){
				String attName = JOptionPane.showInputDialog("Rename `"+att.getName()+"` to: ");
				if(attName==null) return;
				for(Attribute a:relation.getAttributes().minus(att)){
					if(a.getName().equalsIgnoreCase(attName)){
						JOptionPane.showMessageDialog(null, "Attribute `"+attName+"` already exists in relation `"+relation.getName()+"`");
						return;
					}
				}				
				att.setName(attName);				
				for(FDSet f:relation.getFdSets()){
					for(FD fd:f){
						if(fd.getLHS().contains(old_)){
							fd.getLHS().remove(old_);
							fd.getLHS().add(att);
						}
						if(fd.getRHS().contains(old_)){
							fd.getRHS().remove(old_);
							fd.getRHS().add(att);
						}
					}
				}				
				setRelation(relation);
			}			
		} else if(src==btnRemove){
			if(relation == null){
				JOptionPane.showMessageDialog(null, "No relation is selected");
				return;
			}
			Attribute att = getSelectedAttribute();
			if(att!=null){
				int res = JOptionPane.showConfirmDialog(null, "This will remove `"+att.getName()+"`\nAre you sure?");
				if(res!=JOptionPane.OK_OPTION) return;				
				Set<FD> affected = new HashSet<FD>();
				String s = "";
				for(FDSet f:relation.getFdSets()){
					s += f.getName() + "\n";
					for(FD fd:f){
						if(fd.getLHS().contains(att) || fd.getRHS().contains(att)){
							s += "    " + fd.toString() + "\n";
							affected.add(fd);
						}
					}
				}
				if(affected.isEmpty()){
					relation.getAttributes().remove(att);
				} else {
					String msg = "Attribute `"+att.getName()+"` can not be removed\n";
					msg += "The following FDs are affected:\n" + s + "\n";
					JOptionPane.showMessageDialog(null, msg);
				}
				setRelation(relation);
			}
		}
	}

}

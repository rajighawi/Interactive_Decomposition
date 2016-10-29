package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import main.Global;
import renderer.MyListRenderer;
import utilities.SwingUtility;
import fd.Relation;
import gui.utilities.MyButton1;

public class RelationsPanel extends JPanel implements ActionListener{
	
	DefaultListModel<Relation> lmodRelations = new DefaultListModel<Relation>();
	JList<Relation> lstRelations = new JList<Relation>(lmodRelations);
	
	JButton btnDuplicate= new MyButton1("Duplicate");
	JButton btnDelete   = new MyButton1("Remove");
	
	public RelationsPanel(){
		super(new BorderLayout());
		setBorder(SwingUtility.myBorder("Relations"));
		
		lstRelations.setFont(SwingUtility.fnt_data2);
		lstRelations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
		lstRelations.setCellRenderer(new MyListRenderer());
		lstRelations.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        JList<?> list = (JList<?>) evt.getSource();
		        int index = list.locationToIndex(evt.getPoint());
		        if(index>-1){		        	
		        	Relation r = lmodRelations.get(index);
		        	Global.getInstance().getMainFrame().loadRelation(r);
		        	enableButtons(true);
		        } else {
		        	Global.getInstance().getMainFrame().loadRelation(null);
		        	enableButtons(false);
		        }
		    }
		});
		JScrollPane sp = new JScrollPane(lstRelations);
		sp.setPreferredSize(new Dimension(210, 300));
				
		add(sp, BorderLayout.CENTER);
		add(btnsPanel(), BorderLayout.PAGE_END);
	}
	
	public JPanel btnsPanel(){
		JPanel p = new JPanel(new GridLayout(1, 2, 4, 4));
		btnDuplicate.addActionListener(this);
	//	btnDuplicate.setPreferredSize(new Dimension(100, 32));
		btnDelete.addActionListener(this);
	//	btnDelete.setPreferredSize(new Dimension(100, 32));
		enableButtons(false);
		p.add(btnDuplicate);
		p.add(btnDelete);
		return p;
	}
	
	public void updateRelationList(){
		lmodRelations.removeAllElements();
		ArrayList<Relation> relations = new ArrayList<Relation>(Global.getInstance().getRelations().values());
		for (int i = 0; i < relations.size(); i++) {
			lmodRelations.addElement(relations.get(i));
		}
		lstRelations.setModel(lmodRelations);		
	}
	
	public void selectRelation(Relation r){
		lstRelations.setSelectedValue(r, true);
	}
	
	public void clear(){
		lmodRelations.removeAllElements();
		lstRelations.setModel(lmodRelations);
		enableButtons(false);
	}
	
	public void enableButtons(boolean b){
		btnDuplicate.setEnabled(b);
		btnDelete.setEnabled(b);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if(src==btnDelete){
			int idx = lstRelations.getSelectedIndex();
			if(idx>-1){
				Relation r = lmodRelations.getElementAt(idx);
				int res = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the relation `"+r.getName()+"` ?");
				if(res==JOptionPane.OK_OPTION){
					Global.getInstance().removeRelation(r);
					Global.getInstance().getMainFrame().removeRelation(r);
				}
			}
		} else if(src==btnDuplicate){
			int idx = lstRelations.getSelectedIndex();
			if(idx>-1){
				Relation r = lmodRelations.getElementAt(idx);
				Relation r_ = r.duplicate();
				try {
					Global.getInstance().addRelation(r_);
					Global.getInstance().getMainFrame().addRelation(r_);
				} catch (Exception e1) {
					e1.printStackTrace();
				}				
			}
		}
	}
}

package gui.general;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import renderer.MyListRenderer;
import utilities.SwingUtility;
import fd.Attribute;
import fd.AttributeSet;

public class AttributeSetPanel extends JPanel {
	
	DefaultListModel<Attribute> attListModel;
	JList<Attribute> attList;
	JLabel lblAtts;
	AttributeSet attributeSet;
	
	public AttributeSetPanel(){
		super(new BorderLayout());
		 
		lblAtts = new JLabel("{ }", SwingConstants.CENTER);
		lblAtts.setFont(SwingUtility.fnt_label1);
		attributeSet = new AttributeSet();
		
		attListModel = new DefaultListModel<Attribute>();
		attList = new JList<Attribute>(attListModel);
		attList.setFont(SwingUtility.fnt_data2);
		attList.setCellRenderer(new MyListRenderer());
		ListSelectionModel lsm1 = attList.getSelectionModel();
		lsm1.addListSelectionListener(new AttListSelectionHandler());
		
		
		JScrollPane sp1 = new JScrollPane(attList);
		sp1.setPreferredSize(new Dimension(200, 200));

		add(sp1, BorderLayout.CENTER);
		add(lblAtts, BorderLayout.PAGE_END);		
	}
	
	public void clear(){
		attListModel.removeAllElements();
		attList.setModel(attListModel);
		lblAtts.setText(" ");
	}
	
	public void load(AttributeSet attributeSet){
		attListModel.removeAllElements();
		for(Attribute att : attributeSet){
			attListModel.addElement(att);
		}		
		attList.setModel(attListModel);		
	}
	
	public AttributeSet getAttributeSet() {
		return attributeSet;
	}

	public void setAttributeSet(AttributeSet attributeSet) {
		this.attributeSet = attributeSet;
	}

	class AttListSelectionHandler implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			if (lsm.isSelectionEmpty()) {
				lblAtts.setText("{ }");
			} else { // Find out which indexes are selected.
				int minIndex = lsm.getMinSelectionIndex();
				int maxIndex = lsm.getMaxSelectionIndex();
				attributeSet = new AttributeSet();
				for (int i = minIndex; i <= maxIndex; i++) {
					if (lsm.isSelectedIndex(i)) {
						Attribute a = attListModel.get(i);
						attributeSet.add(a);
					}
				}
				lblAtts.setText(attributeSet.toString());
			}
		}

	}

}

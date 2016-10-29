package renderer;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import utilities.ColorUtility;
import utilities.IconUtility;
import fd.Relation;

public class RelationListRenderer extends JLabel implements ListCellRenderer<Object> {

	public RelationListRenderer() {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}


	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		setHorizontalAlignment(SwingConstants.LEFT);
		setPreferredSize(new Dimension(100, 24));
		
		if (isSelected) {
			setForeground(ColorUtility.brown);
			setBackground(ColorUtility.antiquewhite);
		} else {
			setBackground(ColorUtility.white);
			setForeground(list.getForeground());
		}
		
		setFont(list.getFont());
		
		if(value instanceof Relation){
			Relation relation = (Relation) value;	
			setIcon(IconUtility.TABLE); 
			setText(relation.toString());
		} else if(value == null){
			setIcon(null); 
			setText(" ");
		} else {
			setIcon(null); 
			setText(value.toString());
		} 
		
		return this;
	}

	 

}

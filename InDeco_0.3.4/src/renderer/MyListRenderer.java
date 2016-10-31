package renderer;

import interactive.Recommendation;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import utilities.ColorUtility;
import utilities.IconUtility;
import fd.Attribute;
import fd.Decomposition;
import fd.FD;
import fd.FDSet;
import fd.Relation;

public class MyListRenderer extends JLabel implements ListCellRenderer<Object> {

	public MyListRenderer() {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}

	/*
	 * This method finds the image and text corresponding to the selected value
	 * and returns the label, set up to display the text and image.
	 */
	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		// Get the selected index. (The index param isn't always valid, so just use the value.)
		//int selectedIndex = index;

		setHorizontalAlignment(SwingConstants.LEFT);
		setPreferredSize(new Dimension(100, 24));
		// setIconTextGap(8);
		
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
		//	setText(relation.toString());						
			setText(relation.getName());
		} else if(value instanceof FDSet){
			FDSet fdSet = (FDSet) value;	
			setIcon(IconUtility.FDSET); 
			setText(fdSet.getName());			
		} else if(value instanceof FD){
			FD fd = (FD) value; 
			setIcon(IconUtility.FD); 
			setText(fd.toString());			
		} else if(value instanceof Attribute){
			Attribute a = (Attribute) value;
			setIcon(IconUtility.COLUMN); 
			setText(a.toString());			
		} else if(value instanceof Decomposition){
			Decomposition a = (Decomposition) value;
			setIcon(IconUtility.DECO); 
			setText(a.getName());			
		} else if(value instanceof Recommendation){
			Recommendation a = (Recommendation) value;
			byte cm = a.getComp();
			switch (cm) {
			case Recommendation._Better:setIcon(IconUtility.UP); break;
			case Recommendation._Equiv:	setIcon(IconUtility.EQ); break;
			case Recommendation._Worse:	setIcon(IconUtility.DOWN); break;
			default: setIcon(null); break;
			}			
			setText(a.toString());			
		} else if(value instanceof Entry<?, ?>){
			Entry<?, ?> v = (Entry<?, ?>) value;
			if(v.getKey() instanceof FD){
				FD fd = (FD) v.getKey();
				setText(fd.toString());
				Object o = v.getValue();
				if(o == null){
					setIcon(IconUtility.FD);
				} else if(o instanceof Boolean){
					boolean b = (Boolean) v.getValue();				 
					setIcon(b?IconUtility.yes24:IconUtility.no24);
				}								
			}						
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

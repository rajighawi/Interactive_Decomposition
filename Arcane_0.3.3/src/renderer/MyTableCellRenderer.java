package renderer;

import java.awt.Color;
import java.awt.Component;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;

import fd.Symbol;
import utilities.ColorUtility;
import utilities.DateUtility;
import utilities.IconUtility;
import utilities.SwingUtility;
import utilities.Utility;

public class MyTableCellRenderer extends DefaultTableCellRenderer {
	
	Border selectedBorder   = new MatteBorder( 1,  1, 1, -1, Color.RED);
	Border unselectedBorder = new MatteBorder(-1, -1, 1,  1, Color.BLUE);
	
	boolean fix1col;
	
	public MyTableCellRenderer(){
		setOpaque(true); 
		this.fix1col = false;
	}
	
	public MyTableCellRenderer(boolean fix1col){
		setOpaque(true); 
		this.fix1col = fix1col;
	}
	
	public Component getTableCellRendererComponent( JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {			
		setHorizontalAlignment(JLabel.CENTER);	
				
		if(fix1col && column == 0){
			setFont(SwingUtility.fnt_label2);
			setBackground(new Color(210,240,120));
			setBorder(BorderFactory.createRaisedBevelBorder());		
		} else {
			setFont(SwingUtility.fnt_label2); 
			/*if (isSelected) {
				setBackground(SwingUtility.lightCyan3);
				setBorder(selectedBorder);
			} else {
				setBackground(row%2==0?SwingUtility.lightGrey:Color.WHITE);
				setBorder(unselectedBorder);
			}*/
		//	setBackground(row%2==0?SwingUtility.lightGrey:Color.WHITE);
			setBackground(Color.WHITE);
			setBorder(unselectedBorder);
		}		
		 
		if(value instanceof Symbol) {
			Symbol symbol = (Symbol) value;
			setText(symbol.getStr());
			setIcon(null);
			if(symbol.isSubscriped()){
				setFont(SwingUtility.fnt_data); 
				setBackground(Color.WHITE);				
			} else {
				setFont(SwingUtility.fnt_data2); 
				setBackground(SwingUtility.lightCyan3);
			}
			
		} else if(value instanceof String) {
			setText("  "+value.toString());	
			setIcon(null);
		} else if(value instanceof Integer) {
			Integer integer = (Integer) value;
			setText("  "+Utility.formatINT(integer));	
			setIcon(null);
		} else if(value instanceof Double) {
			Double d = (Double) value;
			setText(d==0?"0":String.format("%-7.4f", d));	
			setIcon(null);
		} else if(value instanceof Boolean) {
			boolean b = (Boolean) value;
			setText("");	
			setIcon(b?IconUtility.ok:null);
			setBackground(b?ColorUtility.lightcyan:Color.WHITE);
		} else if(value instanceof Date) {
			setText("  "+DateUtility.formatDate((Date)value));	
			setIcon(null);
		} else if(value instanceof ImageIcon) {
			ImageIcon uri = (ImageIcon) value;
			setText("");	
			setIcon(uri);	
		} else {
			if(value==null){
				setText("");
			} else {
				setText(value.toString());
			}
			setIcon(null);
		}				
		return this;
	}
	
	

}

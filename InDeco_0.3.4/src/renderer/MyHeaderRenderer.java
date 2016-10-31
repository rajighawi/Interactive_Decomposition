package renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import utilities.SwingUtility;

public class MyHeaderRenderer extends JLabel implements TableCellRenderer {
	static final long serialVersionUID = 0l;
	
	public MyHeaderRenderer() {
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object object,
			boolean isSelected, boolean hasFocus, int row, int column) {
		String text = (String) object;
		setText(text);
		setHorizontalAlignment(JLabel.CENTER);
		
		setPreferredSize(new Dimension(100, 40));
		setFont(SwingUtility.fnt_label2);
		setBackground(new Color(210,240,120));
		setBorder(BorderFactory.createRaisedBevelBorder());		
		setToolTipText(null); // Discussed in the following section
		return this;
	}
}

package gui.utilities;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import utilities.SwingUtility;

public class MyButton extends JButton {

	public MyButton(String text, ImageIcon icon){
		super(text); //super(text, icon);		//   
		setFont(SwingUtility.fnt_label1);
		setHorizontalAlignment(SwingConstants.CENTER);
		setPreferredSize(new Dimension(140, 36));		
		//setIcon(icon);			
	}
	
}

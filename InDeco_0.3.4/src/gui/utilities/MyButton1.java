package gui.utilities;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import utilities.SwingUtility;

public class MyButton1 extends JButton {

	public MyButton1(String text){
		super(text);   
		setFont(SwingUtility.fnt_label1);
		setHorizontalAlignment(SwingConstants.CENTER);
		setPreferredSize(new Dimension(100, 32));		
		//setIcon(icon);			
	}
	
}

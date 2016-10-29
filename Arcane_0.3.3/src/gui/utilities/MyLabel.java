package gui.utilities;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import utilities.SwingUtility;

public class MyLabel extends JLabel {
	
	public MyLabel(String title){
		super(title, SwingConstants.CENTER);
		setBorder(SwingUtility.myBorder2());
		setFont(SwingUtility.fnt_label2);		
	}
	
	public MyLabel(String title, Color c){
		this(title);
		setBackground(c);
	}
}

package utilities;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class SwingUtility {
 
	
			
	public static final Font myFont = new Font("Courier New", Font.PLAIN, 14);
	
	public static final int OPTION_OK = 2;
	public static final int OPTION_CANCEL = 3;
	public static final int MODE_OK_CANCEL = 13;
	public static final int RESULT_ERROR = 6;
	
	public static final Font fnt_data  = new Font("Courier", Font.PLAIN, 16);
	public static final Font fnt_data2 = new Font("Courier", Font.BOLD, 16);
	public static final Font fnt_data3 = new Font("Verdana", Font.PLAIN, 16);
	
	public static final Font fnt_title = new Font("Serif", Font.PLAIN, 36);
	
	public static final Font fnt_label0  = new Font("Consolas", Font.PLAIN, 14);
	public static final Font fnt_label1  = new Font("Serif", Font.PLAIN, 16);
	public static final Font fnt_label1B = new Font("Serif", Font.BOLD, 16);
	public static final Font fnt_label2  = new Font("Serif", Font.PLAIN, 20);
	public static final Font fnt_label2B = new Font("Serif", Font.BOLD,  20);
	public static final Font fnt_label3  = new Font("Serif", Font.PLAIN, 24);
		
	public static final Dimension dim_txt1 = new Dimension(400, 36);
	public static final Dimension dim_txt2 = new Dimension(300, 36);
	public static final Dimension dim_lbl1 = new Dimension(200, 36);
	public static final Dimension dim_table1 = new Dimension(700, 160);
	public static final Dimension dim_table2 = new Dimension(700, 360);
	public static final Dimension dim_btn1 = new Dimension(180, 40);
	public static final Dimension dim_btn2 = new Dimension(120, 36);
	public static final Dimension dim_area = new Dimension(640, 300);
	public static final Dimension dim_area2 = new Dimension(480, 240);
	public static final Dimension dim_cmb = new Dimension(80, 36);
	
	
	public static final Color CLR_TOOLBAR = ColorUtility.powderblue;
	public static final Color CLR_HEADER1 = ColorUtility.lightsalmon;
	public static final Color CLR_HEADER2 = ColorUtility.springgreen;
	
	
	public static final Color lightGreen = new Color(204,255,204);
	public static final Color lightPaige = new Color(255,236,170);
	public static final Color paige  	 = new Color(255,226,128);
	public static final Color lightCyan1 = new Color(255,255,183);
	public static final Color lightCyan2 = new Color(206,253,253);
	public static final Color lightCyan3 = new Color(200,240,240);
	public static final Color lightPink  = new Color(255,225,235);
	public static final Color lightGrey  = new Color(242,242,242);
	
	public static final Color roseColor = new Color(255, 157, 157);
	public static final Color headerColor  = new Color(233, 228, 176);
	
	
	public static Border myBorder(String title){
		return BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), 
				title,
				TitledBorder.LEFT, 
				TitledBorder.LEFT, 
				fnt_label1, 
				new Color(25, 25, 250));
	}
	
	public static Border myBorder2(){
		return BorderFactory.createEtchedBorder();
	}
	
	public static GridBagConstraints myC(int gX, int gY) {
		Insets in = new Insets(4, 4, 4, 4);
		return new GridBagConstraints(gX, gY, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, in, 0, 0);
	}

	public static GridBagConstraints myC(int gX, int gY, int w, int h) {
		Insets in = new Insets(4, 4, 4, 4);
		return new GridBagConstraints(gX, gY, w, h, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, in, 0, 0);
	}

	public static void center(Component c) {
		Dimension screenSize = c.getToolkit().getScreenSize();
		Dimension componentSize = c.getSize();
		int xPos = (screenSize.width - componentSize.width) / 2;
		xPos = Math.max(xPos, 0);
		int yPos = (screenSize.height - componentSize.height) / 2;
		yPos = Math.max(yPos, 0);
		c.setLocation(new Point(xPos, yPos));
	}
	
	public static JPanel logoPanel(ImageIcon icon) {
		JPanel panel1 = new JPanel(new GridBagLayout());
		panel1.setBackground(SwingUtility.CLR_TOOLBAR);
		panel1.setBorder(myBorder2());
		panel1.setPreferredSize(new Dimension(100,100));
		panel1.add(new JLabel(icon), SwingUtility.myC(0, 0));		
		return panel1;
	}
	
	
}

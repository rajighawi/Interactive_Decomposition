package main;

import gui.MainFrame;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;


public class Main {
	
	public static void main(String[] args) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			e.printStackTrace();
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
		new MainFrame();
	}

}

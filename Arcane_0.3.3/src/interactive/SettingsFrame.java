package interactive;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import main.Global;
import utilities.SwingUtility;

public class SettingsFrame extends JDialog implements ActionListener{

	private static SettingsFrame currentDialog;
	
	SettingsPanel settingsPanel;
	
	JButton btnOk     = new JButton();
	JButton btnCancel = new JButton();
	
	SettingsFrame(){
		super();
		setTitle("Settings (for Interactive Decomposition)");
		setModal(true);
		settingsPanel = new SettingsPanel();
				
		JDialog.setDefaultLookAndFeelDecorated(true);
		addWindowListener(new WindowCloseListener());	
		setPreferredSize(new Dimension(560, 400));
		
		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(settingsPanel, BorderLayout.CENTER);
		getContentPane().add(buttonsPanel(), BorderLayout.PAGE_END);
		
		pack();
		requestFocus();		
		SwingUtility.center(this);
		setResizable(false);
	}

	
	public JPanel buttonsPanel(){
		JPanel p2 = new JPanel(new GridBagLayout());
		
		btnOk.setText("OK");
		btnCancel.setText("Cancel");
		
		btnOk.addActionListener(this);
		btnCancel.addActionListener(this);
		
		p2.add(btnOk,     SwingUtility.myC(1, 0));
		p2.add(btnCancel, SwingUtility.myC(2, 0));
		return p2;
	}
	
	private class WindowCloseListener extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			setVisible(false);
		}
	}	
	
	public static SettingsFrame getCurrentDialog() {
		if(currentDialog == null){
			currentDialog = new SettingsFrame();
		}
		return currentDialog;
	}
	
	public void close() {
		currentDialog = null;
	}
	
	public static void showMessageDialog() {
		SettingsFrame cr = getCurrentDialog();
		cr.settingsPanel.loadSettings(Global.getInstance().getSettings());
		cr.setVisible(true);		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object src = ae.getSource();
		if(src==btnOk){			
			InterSettings settings = settingsPanel.getSettings();
			Global.getInstance().setSettings(settings);
			currentDialog.dispose();			
		} else if(src==btnCancel){
			currentDialog.dispose();
		}
	}
	
	

}

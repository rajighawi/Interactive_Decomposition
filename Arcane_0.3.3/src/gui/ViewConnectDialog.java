package gui;

import gui.utilities.MyButton1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import jdbcTest.MyConnection;
import utilities.ColorUtility;
import utilities.SwingUtility;

public class ViewConnectDialog extends JDialog implements ActionListener{

	private static ViewConnectDialog currentDialog;
		
	JButton btnCancel  = new MyButton1("Close");
	
	JComboBox<String> cmbDBMS = new JComboBox<String>();
	JTextField txtHost     = new JTextField(40);
	JTextField txtDBName   = new JTextField(40);
	JTextField txtUser     = new JTextField(40);
	JPasswordField txtPass = new JPasswordField(40);
	
	ViewConnectDialog(){
		super();
		setTitle("Database Connection Info.");
		setModal(true); 
				
		JDialog.setDefaultLookAndFeelDecorated(true);
		addWindowListener(new WindowCloseListener());	
		setPreferredSize(new Dimension(560, 340));
		
		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(mainPanel(),    BorderLayout.CENTER);
		getContentPane().add(buttonsPanel(), BorderLayout.PAGE_END);
		
		pack();
		requestFocus();		
		SwingUtility.center(this);
		setResizable(false);
	}

	public JPanel mainPanel(){
		JPanel p2 = new JPanel(new GridBagLayout());
		p2.setBackground(ColorUtility.darkcyan);
		
		JLabel lblDBMS = new JLabel("DBMS");
		lblDBMS.setFont(SwingUtility.fnt_label1);
		JLabel lblHost = new JLabel("Host");
		lblHost.setFont(SwingUtility.fnt_label1);
		JLabel lblDB = new JLabel("Database");
		lblDB.setFont(SwingUtility.fnt_label1);
		JLabel lblUser = new JLabel("User Name");
		lblUser.setFont(SwingUtility.fnt_label1);
		JLabel lblPass = new JLabel("Password");
		lblPass.setFont(SwingUtility.fnt_label1);
		
		cmbDBMS.setFont(SwingUtility.fnt_label1);
		txtHost.setFont(SwingUtility.fnt_data);
		txtDBName.setFont(SwingUtility.fnt_data);
		txtUser.setFont(SwingUtility.fnt_data);
		txtPass.setFont(SwingUtility.fnt_data);	 
		 
		p2.add(lblDBMS, SwingUtility.myC(0, 0));
		p2.add(cmbDBMS, SwingUtility.myC(1, 0));
		p2.add(lblHost, SwingUtility.myC(0, 1));
		p2.add(txtHost, SwingUtility.myC(1, 1));
		p2.add(lblDB,   SwingUtility.myC(0, 2));
		p2.add(txtDBName,SwingUtility.myC(1, 2));
		p2.add(lblUser, SwingUtility.myC(0, 3));
		p2.add(txtUser, SwingUtility.myC(1, 3));
		p2.add(lblPass, SwingUtility.myC(0, 4));
		p2.add(txtPass, SwingUtility.myC(1, 4));		
		txtHost.setText("localhost");
		 
		cmbDBMS.addItem(null);
		cmbDBMS.addItem(MyConnection.POSTGRE);
		cmbDBMS.addItem(MyConnection.ORACLE);
		cmbDBMS.addItem(MyConnection.MYSQL);
		
		cmbDBMS.setEnabled(false);
		txtHost.setEnabled(false);
		txtDBName.setEnabled(false);
		txtUser.setEnabled(false);
		txtPass.setEnabled(false);	 
		
		
		return p2;
	}
	
	
	public JPanel buttonsPanel(){
		JPanel p2 = new JPanel(new GridBagLayout());
		
		btnCancel.addActionListener(this); 
		
		p2.add(btnCancel,  SwingUtility.myC(2, 0));
		
		return p2;
	}
	
	private class WindowCloseListener extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			setVisible(false);
		}
	}	
	
	public static ViewConnectDialog getCurrentDialog() {
		if(currentDialog == null){
			currentDialog = new ViewConnectDialog();
		}
		return currentDialog;
	}
	
	public void close() {
		currentDialog = null;
	}
	
	public static void showMessageDialog(MyConnection c) {
		ViewConnectDialog cf = getCurrentDialog();
		cf.clear();
		cf.fill(c);
		cf.setVisible(true);		
	}	
	
	public void clear(){
		cmbDBMS.setSelectedItem(null);
		txtHost.setText("");
		txtDBName.setText("");
		txtUser.setText("");
		txtPass.setText("");
	}
	
	public void fill(MyConnection conn){
		if(conn==null) return;
		cmbDBMS.setSelectedItem(conn.getDbms());
		txtHost.setText(conn.getHost());
		txtDBName.setText(conn.getDatabase());
		txtUser.setText(conn.getUser());
		txtPass.setText(conn.getPassword());
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object src = ae.getSource();
		if(src==btnCancel){			 
			currentDialog.dispose();
		}
	}
	
	

}

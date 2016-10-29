package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.Global;
import fd.Relation;
import gui.utilities.MyButton1;
import jdbcTest.MyConnection;
import utilities.ColorUtility;
import utilities.SwingUtility;

public class ConnectDialog extends JDialog implements ActionListener{

	private static ConnectDialog currentDialog;
		
	JButton btnConnect= new MyButton1("Connect");
	JButton btnCancel = new MyButton1("Cancel");
	JButton btnClear  = new MyButton1("Clear");
	
	JComboBox<String> cmbDBMS = new JComboBox<String>();
	JTextField txtHost     = new JTextField(40);
	JTextField txtDBName   = new JTextField(40);
	JTextField txtUser     = new JTextField(40);
	JPasswordField txtPass = new JPasswordField(40);
	
	ConnectDialog(){
		super();
		setTitle("Connect to a Database");
		setModal(true); 
				
		JDialog.setDefaultLookAndFeelDecorated(true);
		addWindowListener(new WindowCloseListener());	
		setPreferredSize(new Dimension(560, 400));
		
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
		
		return p2;
	}
	
	
	public JPanel buttonsPanel(){
		JPanel p2 = new JPanel(new GridBagLayout());
		
		btnConnect.addActionListener(this); 
		btnCancel.addActionListener(this); 
		btnClear.addActionListener(this); 
		 
		p2.add(btnConnect, SwingUtility.myC(0, 0));
		p2.add(btnClear,   SwingUtility.myC(1, 0)); 
		p2.add(btnCancel,  SwingUtility.myC(2, 0));
		
		return p2;
	}
	
	private class WindowCloseListener extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			setVisible(false);
		}
	}	
	
	public static ConnectDialog getCurrentDialog() {
		if(currentDialog == null){
			currentDialog = new ConnectDialog();
		}
		return currentDialog;
	}
	
	public void close() {
		currentDialog = null;
	}
	
	public static void showMessageDialog() {
		ConnectDialog cf = getCurrentDialog();
		cf.clear();
		cf.fill();
		cf.setVisible(true);		
	}	
	
	public void clear(){
		txtHost.setText("");
		txtDBName.setText("");
		txtUser.setText("");
		txtPass.setText("");
	}
	
	public void fill(){
		cmbDBMS.setSelectedItem(MyConnection.POSTGRE);
		txtHost.setText("127.0.0.1");
		//txtDBName.setText("FDW");
		txtDBName.setText("FD_reverse");
		txtUser.setText("postgres");
		txtPass.setText("postgres");
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object src = ae.getSource();
		if(src==btnCancel){			 
			currentDialog.dispose();
		} else if(src==btnClear){			 
			currentDialog.clear();
		} else if(src==btnConnect){			 
			Object o = cmbDBMS.getSelectedItem();
			if(o!=null){
				String dbms = (String) o;
				String host = txtHost.getText();
				String db   = txtDBName.getText();
				String user = txtUser.getText();
				String pass = new String(txtPass.getPassword());
				MyConnection connection = new MyConnection(db, dbms, host, db, user, pass);
				connection.connect();
				ArrayList<Relation> relations = connection.getRelations();
				for (int i = 0; i < relations.size(); i++) {
					try {
						Relation r = relations.get(i);
						r.setConn(connection);
						Global.getInstance().addRelation(r);
						Global.getInstance().getMainFrame().addRelation(r);
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, e.getMessage());
						continue;
					}						
				}
				currentDialog.dispose();
			}
		}  
	}
	
	

}

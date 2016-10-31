package gui.utilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import utilities.FileUtility;
import utilities.IconUtility;
import utilities.SwingUtility;

public class TextPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	public static final byte HORIZONTAL_LAYOUT = 1;
	public static final byte VERTICAL_LAYOUT   = 2;
	
	private JTextArea txtSource = new JTextArea();
	 
	protected JButton btnRun  = new JButton("Analyze");
	protected JButton btnLoad = new JButton("Load");
	protected JButton btnSave = new JButton("Save");
	 
	byte layout;
	
	public TextPanel(String title, byte layout){
		super(new BorderLayout());
		this.layout = layout;
		add(queryPanel(title), BorderLayout.CENTER);
	}		
	
	public JPanel queryPanel(String title){
		JPanel p1 = new JPanel(new BorderLayout());		
		p1.setBorder(SwingUtility.myBorder(title));
		txtSource.setFont(SwingUtility.fnt_data);
		JScrollPane sp = new JScrollPane(txtSource);
		sp.setPreferredSize(new Dimension(300, 240));
		
		p1.add(sp,        BorderLayout.CENTER); 
		if(layout==HORIZONTAL_LAYOUT){
			p1.add(toolBar(), BorderLayout.PAGE_START);
		} else if(layout==VERTICAL_LAYOUT){
			p1.add(toolBar(), BorderLayout.LINE_END);
		}
		
		
		return p1;
	}
	
	public JPanel toolBar(){
		JPanel p1 = new JPanel(new GridBagLayout());		
		p1.setBorder(SwingUtility.myBorder2());
		 
		btnRun.addActionListener(this);
		btnRun.setFont(SwingUtility.fnt_label1);
		btnRun.setIcon(IconUtility.run);
		btnLoad.addActionListener(this);
		btnLoad.setFont(SwingUtility.fnt_label1);
		btnLoad.setIcon(IconUtility.load);
		btnSave.addActionListener(this);
		btnSave.setFont(SwingUtility.fnt_label1);
		btnSave.setIcon(IconUtility.save);		
		
		if(layout==HORIZONTAL_LAYOUT){
			p1.add(btnLoad, SwingUtility.myC(0, 0));
			p1.add(btnRun,  SwingUtility.myC(1, 0));
			p1.add(btnSave, SwingUtility.myC(2, 0));			
		} else if(layout==VERTICAL_LAYOUT){
			p1.add(btnLoad, SwingUtility.myC(0, 0));
			p1.add(btnRun,  SwingUtility.myC(0, 1));
			p1.add(btnSave, SwingUtility.myC(0, 2));			
		}
		
		return p1;
	}
 
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == btnRun){
			execute();			
		} else if(source == btnLoad){
			String text = FileUtility.load();
			txtSource.setText(text);			
		} else if(source == btnSave){
			String query = txtSource.getText();
			FileUtility.save(query);	
		} 
	}
	
	
	public void execute(){
		//Global.getInstance().getMainFrame().analyze();
	}
	
	public void setText(String text1){
		txtSource.setText(text1);
	}
	
	public String getText(){
		return txtSource.getText();
	}
	
	public void clearText(){
		setText("");
	}
	
}

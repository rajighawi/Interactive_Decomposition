package gui.decomposition;

import fd.Decomposition;
import fd.FD;
import gui.general.FDSetPanel2;
import gui.utilities.MyButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import utilities.ColorUtility;
import utilities.SwingUtility;

public class PreservationPanel extends JPanel implements ActionListener {

	FDSetPanel2 resultPanel;
	JButton btnStart;
	JTextArea lblMsg2;

	Decomposition decomposition;
	
	public PreservationPanel(){
		setLayout(new BorderLayout());
		resultPanel = new FDSetPanel2();
		btnStart = new MyButton("Start Test", null);
		lblMsg2 = new JTextArea();
		
		add(topPanel(), BorderLayout.PAGE_START);
		add(mainPanel(), BorderLayout.CENTER);
	}
	

	public JPanel mainPanel(){
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(SwingUtility.myBorder2());
				 
		lblMsg2.setFont(SwingUtility.fnt_label1);
		lblMsg2.setBackground(Color.WHITE);
		JScrollPane sp = new JScrollPane(lblMsg2);
		sp.setPreferredSize(new Dimension(200, 60));
			
		mainPanel.add(resultPanel, BorderLayout.CENTER);
		mainPanel.add(sp, 		   BorderLayout.PAGE_END);
		
		return mainPanel;
	}

	public JPanel topPanel(){
		JPanel topPanel = new JPanel(new GridBagLayout());
		topPanel.setBorder(SwingUtility.myBorder2());
		btnStart.addActionListener(this);
		topPanel.add(btnStart, SwingUtility.myC(0, 0));
		return topPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src= e.getSource();
		if(src==btnStart){
			if(decomposition != null){
				Map<FD, Boolean> result = decomposition.checkFDsPreservation();
				resultPanel.load(result);
				boolean all = true;
				for(boolean b:result.values()){
					if(!b){
						all = false; 
						break;
					}
				}
				lblMsg2.setText("Decomposition DO "+(all?"":"NOT ")+"Preserve Functional Dependencies");
				lblMsg2.setBackground(all?ColorUtility.lightgreen:ColorUtility.lightpink);
			} else {
				JOptionPane.showMessageDialog(null, "No Decomposition is Selected!");
			}
		} 
	}
	
	public Decomposition getDecomposition() {
		return decomposition;
	}

	public void setDecomposition(Decomposition decomposition) {
		this.decomposition = decomposition;
	}

	public void clear() {
		decomposition = null;
		lblMsg2.setText("");		
		lblMsg2.setBackground(Color.WHITE);
		resultPanel.clear();
	}

}

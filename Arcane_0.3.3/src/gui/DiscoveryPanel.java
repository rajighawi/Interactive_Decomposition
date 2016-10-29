package gui;

import fd.FD;
import fd.FDSet;
import fd.Relation;
import gui.general.FDSetPanel;
import gui.utilities.MyButton;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import jdbcTest.MyConnection;
import main.Global;
import utilities.IconUtility;
import utilities.SwingUtility;
import xml.FDXModel;
import discovery.Algo_TANE_FastCheck;
import discovery.Algo_TANE_Original;
import discovery.FDDiscovery;

public class DiscoveryPanel extends JPanel implements ActionListener {

	FDSetPanel prepFDSetPanel = new FDSetPanel();
	FDSetPanel holdFDSetPanel = new FDSetPanel();
	JTextArea txtLog = new JTextArea();
	
	MyButton btnPrepare = new MyButton("Prepare",   IconUtility.calendar);
	MyButton btnCheck   = new MyButton("Check",     IconUtility.browse);
	MyButton btnCheckAll= new MyButton("Check All", IconUtility.run);
	
	MyButton btnTANE_origin  = new MyButton("TANE (Origin)", IconUtility.TABLE);
	MyButton btnTANE_fast    = new MyButton("TANE (Fast)", 	 IconUtility.TABLE);
	
	MyButton btnAppend  = new MyButton("Append FDSet", IconUtility.append);
	
	MyButton btnSave	= new MyButton("Save", 		IconUtility.save);
	
	
	Relation relation;
	FDSet prepFDset;
	FDSet holdFDset;
	
	public DiscoveryPanel(){
		super(new BorderLayout());
		setBorder(SwingUtility.myBorder2());
		
		
		add(mainPanel(), BorderLayout.CENTER);
		add(toolBar(),  BorderLayout.LINE_END);
	}
	
	public JPanel mainPanel(){
		JPanel p = new JPanel(new BorderLayout()); 	
		
		txtLog.setFont(SwingUtility.fnt_data);
		JScrollPane sp = new JScrollPane(txtLog);
		sp.setBorder(SwingUtility.myBorder("Log"));
		sp.setPreferredSize(new Dimension(200,200));
		
		prepFDSetPanel.setBorder(SwingUtility.myBorder("Possible Functional Dependencies"));
		holdFDSetPanel.setBorder(SwingUtility.myBorder("Valid Functional Dependencies"));
		
		JPanel pp = new JPanel(new GridLayout(1, 2));
		pp.add(prepFDSetPanel);
		pp.add(holdFDSetPanel);
		
		p.add(pp, BorderLayout.CENTER);		
		p.add(sp, BorderLayout.PAGE_END);
		return p;
	}
	 
	public JPanel toolBar(){
		JPanel pp = new JPanel(new BorderLayout());	
		pp.setBorder(SwingUtility.myBorder2());		
		
		JPanel p = new JPanel(new GridBagLayout());		
		btnSave.addActionListener(this);
		btnPrepare.addActionListener(this);
		btnCheck.addActionListener(this);
		btnCheckAll.addActionListener(this);
		btnAppend.addActionListener(this);
		btnTANE_origin.addActionListener(this);
		btnTANE_fast.addActionListener(this);
		
		btnCheck.setEnabled(false);
		btnCheckAll.setEnabled(false);
		btnAppend.setEnabled(false);
		
		int  i = 0, j = 0;
		p.add(btnPrepare, 		SwingUtility.myC(i, j++));
		p.add(btnCheck, 		SwingUtility.myC(i, j++));
		p.add(btnCheckAll, 		SwingUtility.myC(i, j++));
		p.add(new JSeparator(), SwingUtility.myC(i, j++));
		p.add(btnTANE_origin, 	SwingUtility.myC(i, j++));
		p.add(btnTANE_fast, 	SwingUtility.myC(i, j++));
		p.add(new JSeparator(), SwingUtility.myC(i, j++));
		p.add(btnAppend, 		SwingUtility.myC(i, j++));
		
		//p.add(btnSave,  SwingUtility.myC(0, 1));
		
		pp.add(p, BorderLayout.PAGE_START);
		return pp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == btnPrepare){
		//	prepFDset = FDDiscovery.prepareList(relation);
			prepFDset = FDDiscovery.prepareList1(relation);
			prepFDSetPanel.load(prepFDset);
			
			btnCheck.setEnabled(true);
			btnCheckAll.setEnabled(true);
			btnAppend.setEnabled(false);
						
		} else if(source == btnSave){
			FDXModel fdxModel = new FDXModel(relation);
			JFileChooser fileChooser = Global.getInstance().getFileChooser();
			int returnVal = fileChooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				fdxModel.toFile(file);	
				//file.clo
			}
		} else if(source == btnCheck){
			MyConnection cc = relation.getConn();
			Set<FD> set = prepFDSetPanel.getSelectedFDs();
			for (FD fd : set) {
				boolean b = FDDiscovery.checkFD(cc, relation.getName(), fd);
				System.out.println("FD: "+fd+" is " + (b?"":"Not ") +"Valid");
			}
			
		} else if(source == btnCheckAll){
			MyConnection cc = relation.getConn();
			holdFDset = new FDSet("fds_"+relation.getName()+"_disco");
			for (FD fd : prepFDset) {
				boolean b = FDDiscovery.checkFD(cc, relation.getName(), fd);
				if(b) holdFDset.add(fd);
				System.out.println("FD: "+fd+" is " + (b?"":"Not ") +"Valid");
			}
			holdFDSetPanel.load(holdFDset);
			btnAppend.setEnabled(true);
		} else if(source == btnAppend){
			if(holdFDset!=null){
				try {
					Global.getInstance().getRelation(relation.getName()).addFDSet(holdFDset);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else if(source == btnTANE_origin){
			MyConnection cc = relation.getConn();
			if(cc==null) return;
			holdFDset = new FDSet("fds_"+relation.getName()+"_TANE_O");
			Algo_TANE_Original algo = new Algo_TANE_Original(relation);
			FDSet ffs = algo.getValidFDs();
			holdFDset.addAll(ffs);
			String log = algo.report();
			txtLog.append(log+"\n");
			holdFDSetPanel.load(holdFDset);
			btnAppend.setEnabled(true);
		} else if(source == btnTANE_fast){
			MyConnection cc = relation.getConn();
			if(cc==null) return;
			holdFDset = new FDSet("fds_"+relation.getName()+"_TANE_F");
			Algo_TANE_FastCheck algo = new Algo_TANE_FastCheck(relation);
			FDSet ffs = algo.getValidFDs();
			holdFDset.addAll(ffs);
			String log = algo.report();
			txtLog.append(log+"\n");
			holdFDSetPanel.load(holdFDset);
			btnAppend.setEnabled(true);
		}
	}
	// --------------------------------------------------
	
	public void loadRelation(Relation relation){
		this.relation = relation;		
		holdFDSetPanel.clear();
		prepFDSetPanel.clear();
		
		btnCheck.setEnabled(false);
		btnCheckAll.setEnabled(false);
		btnAppend.setEnabled(false);		
	}
	
	public void clear(){
		holdFDSetPanel.clear();
		prepFDSetPanel.clear();
		btnCheck.setEnabled(false);
		btnCheckAll.setEnabled(false);
		btnAppend.setEnabled(false);		
	}
	
}

package interactive;

import fd.Attribute;
import fd.AttributeSet;
import fd.Decomposition;
import fd.FD;
import fd.FDSet;
import fd.Relation;
import gui.general.FDSetPanel2;
import gui.utilities.MyButton1;
import gui.utilities.MyTable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.TableColumnModel;

import main.Global;
import normalization.BCNF;
import normalization.SecondNF;
import normalization.ThirdNF;
import renderer.MyListRenderer;
import utilities.SwingUtility;

import com.google.common.collect.HashBasedTable;

public class InteractivePanel extends JPanel implements ActionListener{

	InterTable table = new InterTable();

	DescrTableModel descrTableModel = new DescrTableModel();
	MyTable descTable = new MyTable(descrTableModel);
	
	JComboBox<FDSet> cmbFDsets = new JComboBox<FDSet>();
	FDSetPanel2 fdSetPanel = new FDSetPanel2();
	
	JButton btnAddRelation = new MyButton1("Add New Relation");
	JButton btnAssociate   = new MyButton1("Save");
	JButton btnSettings    = new MyButton1("Settings");
	
	HashBasedTable<Relation, Attribute, Boolean> tableau;
	
	JTextArea txtLog    = new JTextArea();
	JTextArea txtIssues = new JTextArea();
	ScoresPanel scoresPanel = new ScoresPanel();
		
	DefaultListModel<Recommendation> recomListModel = new DefaultListModel<Recommendation>();
	JList<Recommendation> recomList = new JList<Recommendation>(recomListModel);
	
	Relation motherRelation;
	FDSet motherFDSet;
	
	ArrayList<Recommendation> recommendations;
	
	public InteractivePanel(){
		super(new BorderLayout());
		this.motherRelation = null;
		this.motherFDSet = null;
		this.recommendations = new ArrayList<Recommendation>();
		this.tableau = HashBasedTable.create();
		this.descrTableModel = new DescrTableModel();
		this.descTable = new MyTable(descrTableModel);		
		
		updateColWidth();
		
		add(toolbar(), 		BorderLayout.PAGE_START);
		add(centerPanel(), 	BorderLayout.CENTER);
		add(rightPanel(),  	BorderLayout.LINE_END);
		add(bottomPanel(), 	BorderLayout.PAGE_END);
	}
	
	public JPanel centerPanel(){
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(SwingUtility.myBorder2());
		JScrollPane sp = new JScrollPane(table);
		
		p.add(leftPanel(),   	BorderLayout.LINE_START);
		p.add(sp, 			BorderLayout.CENTER);
		p.add(midPanel(), 	BorderLayout.PAGE_END);
		
		return p;
	}
	
	public JPanel toolbar(){
		JPanel p = new JPanel(new GridBagLayout());
		p.setBorder(SwingUtility.myBorder2());
		btnAddRelation.addActionListener(this);
		btnAssociate.addActionListener(this);
		btnSettings.addActionListener(this);
		btnAddRelation.setPreferredSize(new Dimension(160, 32));		
		btnAssociate.setPreferredSize(new Dimension(100, 32));
		btnSettings.setPreferredSize(new Dimension(80, 32));
		
		p.add(btnAddRelation, SwingUtility.myC(0, 0));
		p.add(btnAssociate,   SwingUtility.myC(1, 0));
		p.add(btnSettings,    SwingUtility.myC(2, 0));
		return p;
	}
	
	public JPanel bottomPanel(){
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(SwingUtility.myBorder2());
		JScrollPane sp1 = new JScrollPane(descTable);
		sp1.setPreferredSize(new Dimension(200, 200));		
		
		p.add(sp1, BorderLayout.CENTER);		
		
		return p;
	}
	
	
	public JPanel midPanel(){
		JPanel p = new JPanel(new GridLayout(1, 2));
		p.setBorder(SwingUtility.myBorder2());
		txtIssues.setFont(SwingUtility.fnt_label1);
		JScrollPane sp2 = new JScrollPane(txtIssues);
		sp2.setBorder(SwingUtility.myBorder("Issues"));
		sp2.setPreferredSize(new Dimension(200, 200));		
		
		p.add(sp2);
		p.add(scoresPanel);
	//	p.add(logPanel());
		return p;
	}
	
	public JPanel leftPanel(){
		JPanel p = new JPanel(new BorderLayout());
		cmbFDsets.setFont(SwingUtility.fnt_label1);
		cmbFDsets.setRenderer(new MyListRenderer());
		cmbFDsets.addActionListener(this);
		
		p.add(cmbFDsets, BorderLayout.PAGE_START);
		p.add(fdSetPanel, BorderLayout.CENTER);
		
		return p;
	}
	
	public JPanel rightPanel(){
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(SwingUtility.myBorder("Recommendations"));
		recomList.setFont(SwingUtility.fnt_label1);
		recomList.setCellRenderer(new MyListRenderer());
		recomList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				//Point p = arg0.getPoint();
				boolean dblClick = arg0.getClickCount()==2;
				int idx = recomList.getSelectedIndex();
				if(idx>-1){
					if(dblClick){
						Recommendation rec = recommendations.get(idx);
						Decomposition d = rec.getDeco();
						tableau = d.makeTableau();
						table.load(tableau);
						loadDecomposition(d);						
					}					
				}				
			}
		});
		JScrollPane sp = new JScrollPane(recomList);
		sp.setPreferredSize(new Dimension(320, 200));		
		p.add(sp, BorderLayout.CENTER);		
		return p;
	}

	public JPanel logPanel(){
		JPanel p = new JPanel(new BorderLayout());
		txtLog.setFont(SwingUtility.fnt_label1);
		JScrollPane sp2 = new JScrollPane(txtLog);
		sp2.setPreferredSize(new Dimension(200, 80));

		p.add(sp2, BorderLayout.CENTER);
		
		return p;
	}

	
	//====================================================================

	public void clear(){
		table.clear();
		descrTableModel.clear();
		descTable.setModel(descrTableModel);
		updateColWidth();
		fdSetPanel.clear();
		txtLog.setText("");		
		scoresPanel.clear();
		recomListModel.clear();
		recomList.setModel(recomListModel);		
		this.tableau = HashBasedTable.create();
	}
	
	public void loadRelation(Relation relation){
		this.motherRelation = relation;
		clear();
		cmbFDsets.removeAllItems();
		cmbFDsets.addItem(null);
		ArrayList<FDSet> fdSets = relation.getFdSets();
		for (int i = 0; i < fdSets.size(); i++) {
			cmbFDsets.addItem(fdSets.get(i));
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if(src==cmbFDsets){
			if(motherRelation!=null){
				Object o = cmbFDsets.getSelectedItem();
				if(o!=null && o instanceof FDSet){
					motherFDSet = (FDSet) o;					
					clear();
					fdSetPanel.load(FDSetPanel2.makeNullMap(motherFDSet));					
				}
			}
		} else if(src == btnAddRelation){	
			if(motherRelation!=null){
				int n = 0;
				ArrayList<Relation> rls = new ArrayList<Relation>(tableau.rowKeySet());
				for(Relation rs:rls){
					String rf = rs.getName();
					String rf_ = rf.substring(rf.lastIndexOf("_")+1);
					int ix = Integer.parseInt(rf_);	//int n = relations.size();
					if(ix>n) n=ix;
				}	
				String r_name = motherRelation.getName()+"_"+(n+1);
				Relation r = new Relation(r_name, new AttributeSet());
				r.addFDSet(new FDSet("fds_"+r_name+"_1"));
				
				Set<Attribute> atts =  motherRelation.getAttributes();		 
				for(Attribute a:atts){
					tableau.put(r, a, false);
				}
				table.load(tableau);
			}
		} else if(src == btnSettings){
			SettingsFrame.showMessageDialog();
		} else if(src == btnAssociate){
			int n = motherRelation.getDecompositions().size();
			String decoName = "deco_"+motherRelation.getName()+"_"+n;
			Decomposition deco = Interactive.makeDecomposition(motherRelation, motherFDSet, tableau, decoName);
			motherRelation.addDecomposition(deco);
		}
	}
		
	public void loadDecomposition(Decomposition deco){
		InterSettings settings = Global.getInstance().getSettings();
		float[] scores = Interactive.scores(deco, settings.isPartialDP());
		float score = 0;
		for (int i = 0; i < scores.length; i++) {
			score += scores[i] * settings.getWeights()[i];
		}
		
		String log = "";
		ArrayList<Relation> rls = deco.getSubrelations();
		Collections.sort(rls);
		descrTableModel = new DescrTableModel();
		for (int i = 0; i < rls.size(); i++) {
			Relation sub = rls.get(i);
			FDSet f_ = sub.getFdSets().get(0);
			SecondNF _2nf = new SecondNF(sub, f_);
			ThirdNF  _3nf = new ThirdNF(sub, f_);
			BCNF     bcnf = new BCNF(sub, f_);
			boolean in2NF = _2nf.check();
			boolean in3NF = _3nf.check();
			boolean inBCNF= bcnf.check();						
			descrTableModel.describe(sub, f_, in2NF, in3NF, inBCNF);
		}
		
		descTable.setModel(descrTableModel);
		updateColWidth();
		
		boolean lossless = deco.is_lossless();
		log += "Join - Lossless? \t"+lossless+"\n";
		Map<FD, Boolean> fdmap = deco.checkFDsPreservation();
		fdSetPanel.load(fdmap);
		boolean dp = true;
		for(boolean b:fdmap.values()) dp = dp && b;
		log += "Dependency Preservation?\t"+dp+"\n";
			
		ArrayList<String> issues = Interactive.issues(deco);
		txtIssues.setText("");
		for (int i = 0; i < issues.size(); i++) {
			txtIssues.append(issues.get(i)+"\n");
		}
						
		scoresPanel.setScores(scores, score);
		
		txtLog.setText(log);		
		
		this.recommendations = Interactive.recommendations(deco);
		recomListModel.removeAllElements();
		int i;
		for (i = 0; i < recommendations.size(); i++) {
			Recommendation recom = recommendations.get(i);
			if(recom.getScore()>score){
				recom.setComp(Recommendation._Better);
			} else if(recom.getScore()==score) {
				recom.setComp(Recommendation._Equiv);
			} else { // if(recom.getScore() < score)
				recom.setComp(Recommendation._Worse);
			}
			recomListModel.addElement(recom);
		}
		recomList.setModel(recomListModel);
	}
	
	public void updateDecom(){
		InterTableModel itm = (InterTableModel) table.getModel();
		tableau = itm.getTableau();		
		Decomposition deco = Interactive.makeDecomposition(motherRelation, motherFDSet, tableau, "deco_");
		loadDecomposition(deco);
	}
	
	public void updateColWidth(){
		TableColumnModel tcm = descTable.getColumnModel();
		int[] w = {80, 120, -1, 56, 56, 56};
		for (int i = 0; i < w.length; i++) {
			if(w[i]!=-1){
				tcm.getColumn(i).setMinWidth(w[i]);
				tcm.getColumn(i).setMaxWidth(w[i]);
			}
		}
	}
	
	
}

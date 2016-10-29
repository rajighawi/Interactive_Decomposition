package gui.general;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import renderer.MyListRenderer;
import utilities.SwingUtility;
import fd.FD;
import fd.FDSet;

public class FDSetPanel2 extends JPanel {
	
	DefaultListModel<Entry<FD, Boolean>> fdListModel;
	JList<Entry<FD, Boolean>> fdList;
			
	public FDSetPanel2(){
		setLayout(new BorderLayout());			
		
		fdListModel = new DefaultListModel<Entry<FD, Boolean>>();
		fdList = new JList<Entry<FD, Boolean>>(fdListModel);
		fdList.setCellRenderer(new MyListRenderer());
		
		ListSelectionModel lsm1 = fdList.getSelectionModel();
		lsm1.addListSelectionListener(new MyListSelectionHandler());
		
		fdList.setFont(SwingUtility.fnt_data2);
		fdList.setCellRenderer(new MyListRenderer());
		JScrollPane sp1 = new JScrollPane(fdList);
		sp1.setPreferredSize(new Dimension(200, 200));
		add(sp1, BorderLayout.CENTER);
	}

	public void clear(){
		fdListModel.removeAllElements();
		fdList.setModel(fdListModel);
		//validFDs = new HashSet<FD>();
	}
	
	public void addFD(Entry<FD, Boolean> entry){
		fdListModel.addElement(entry);
		fdList.setModel(fdListModel);
	}
	
	public void load(Map<FD, Boolean> map){
		fdListModel.removeAllElements();
		for(Entry<FD, Boolean> entry : map.entrySet()){
			fdListModel.addElement(entry);
		}
		fdList.setModel(fdListModel);
	}

	/*
	public FDSet makeFDSet(Relation r, String fdsName){
		FDSet fdSet = new FDSet(r, fdsName);
		for (int i = 0; i < fdListModel.size(); i++) {
			FD fd = fdListModel.get(i);
			fdSet.add(fd);
		}
		return fdSet;
	}*/
	
	class MyListSelectionHandler implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {			
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();			
			if (lsm.isSelectionEmpty()) {
				//validFDs = new HashSet<FD>();
			} else { // Find out which indexes are selected.
				int minIndex = lsm.getMinSelectionIndex();
				int maxIndex = lsm.getMaxSelectionIndex();
				//validFDs = new HashSet<FD>();
				for (int i = minIndex; i <= maxIndex; i++) {
					if (lsm.isSelectedIndex(i)) {
						//FD fd = fdListModel.get(i);
						//selectedFDs.add(fd);
					}
				}			
			}			
		}
	}

	/*
	public Set<FD> getValidFDs() {
		return validFDs;
	}
	*/
	
	public static Map<FD, Boolean> makeNullMap(FDSet fdset){
		Map<FD, Boolean> res = new HashMap<FD, Boolean>();
		for(FD fd:fdset){
			res.put(fd, null);
		}
		return res;
	}
	
		
}

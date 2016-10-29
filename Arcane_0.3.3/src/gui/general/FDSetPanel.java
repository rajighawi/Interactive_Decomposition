package gui.general;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;

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

public class FDSetPanel extends JPanel {
	
	DefaultListModel<FD> fdListModel;
	JList<FD> fdList;
	
	Set<FD> selectedFDs;
		
	public FDSetPanel(){
		setLayout(new BorderLayout());			
		selectedFDs = new HashSet<FD>(); //new FDSet(r);
		
		fdListModel = new DefaultListModel<FD>();
		fdList = new JList<FD>(fdListModel);
		fdList.setCellRenderer(new MyListRenderer());
		ListSelectionModel lsm1 = fdList.getSelectionModel();
		lsm1.addListSelectionListener(new MyListSelectionHandler());
		
		fdList.setFont(SwingUtility.fnt_data2);
		
		JScrollPane sp1 = new JScrollPane(fdList);
		sp1.setPreferredSize(new Dimension(200, 140));
		add(sp1, BorderLayout.CENTER);
	}

	public void clear(){
		fdListModel.removeAllElements();
		fdList.setModel(fdListModel);
		selectedFDs = new HashSet<FD>();
	}
	
	public void addFD(FD fd){
		fdListModel.addElement(fd);
		fdList.setModel(fdListModel);
	}
	
	public void load(FDSet fdSet){
		fdListModel.removeAllElements();
		for(FD fd:fdSet){
			fdListModel.addElement(fd);
		}
		fdList.setModel(fdListModel);
	}

	public FDSet makeFDSet(String fdsName){
		FDSet fdSet = new FDSet(fdsName);
		for (int i = 0; i < fdListModel.size(); i++) {
			FD fd = fdListModel.get(i);
			fdSet.add(fd);
		}
		return fdSet;
	}
	
	class MyListSelectionHandler implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {			
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();			
			if (lsm.isSelectionEmpty()) {
				selectedFDs = new HashSet<FD>();
			} else { // Find out which indexes are selected.
				int minIndex = lsm.getMinSelectionIndex();
				int maxIndex = lsm.getMaxSelectionIndex();
				selectedFDs = new HashSet<FD>();
				for (int i = minIndex; i <= maxIndex; i++) {
					if (lsm.isSelectedIndex(i)) {
						FD fd = fdListModel.get(i);
						selectedFDs.add(fd);
					}
				}			
			}			
		}
	}

	public Set<FD> getSelectedFDs() {
		return selectedFDs;
	}
		
}

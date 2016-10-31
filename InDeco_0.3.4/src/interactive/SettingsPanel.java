package interactive;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Global;
import utilities.SwingUtility;

public class SettingsPanel extends JPanel implements ChangeListener{

	JCheckBox chkPartialDP = new JCheckBox("Partial Dependency-Preservation ?");

	JSlider[] slds	 = new JSlider[4];
	JLabel[] wLabels = new JLabel[4];
	int[] oldVs = new int[slds.length];
	
	public SettingsPanel(){
		super(new GridBagLayout());

		JPanel p1 = new JPanel(new GridBagLayout());
		p1.setBorder(SwingUtility.myBorder("Weights"));
		JLabel[] lbls = new JLabel[4];
		lbls[0] = new JLabel("Join-Lossless");
		lbls[1] = new JLabel("Dependency Preservation");
		lbls[2] = new JLabel("Normal Form");
		lbls[3] = new JLabel("Issues");	

		float[] ws = Global.getInstance().getSettings().getWeights(); 

		for (int j = 0; j < lbls.length; j++) {
			int w = Math.round(ws[j] * 100);
			slds[j] = new JSlider(0, 100, w);
			slds[j].addChangeListener(this);
			wLabels[j] = new JLabel(""+w+"");
			oldVs[j] = w;
			wLabels[j].setPreferredSize(new Dimension(50, 24));
			p1.add(lbls[j], 	SwingUtility.myC(0, j));	
			p1.add(slds[j], 	SwingUtility.myC(1, j));
			p1.add(wLabels[j], 	SwingUtility.myC(2, j));
		}

		JPanel p2 = new JPanel(new GridBagLayout());
		p2.setBorder(SwingUtility.myBorder2());

		p2.add(chkPartialDP, SwingUtility.myC(0, 0));

		add(p1, SwingUtility.myC(0, 0));
		add(p2, SwingUtility.myC(0, 1));

	}

	public InterSettings getSettings(){
		int[] ws = new int[4];
		for (int i = 0; i < ws.length; i++) {
			ws[i] = slds[i].getValue();
		}
		boolean partDP = chkPartialDP.isSelected();
		InterSettings settings = new InterSettings(partDP, ws);
		return settings;
	}

	public void loadSettings(InterSettings settings){
		int[] ws = settings.getWs();
		for (int i = 0; i < ws.length; i++) {
			int w = ws[i];
			slds[i].setValue(w);
			wLabels[i].setText(""+w+"");
		}
		chkPartialDP.setSelected(settings.isPartialDP());
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Object src = e.getSource();	
		for (int i = 0; i < slds.length; i++) {
			if(src==slds[i]){
				if (!slds[i].getValueIsAdjusting()) {
					int newVi = slds[i].getValue(); 
					wLabels[i].setText(""+newVi+"");
				}
			}
		}
			
	}



}

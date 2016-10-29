package interactive;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import utilities.SwingUtility;

public class ScoresPanel extends JPanel {
	
	JProgressBar barScore = new JProgressBar(0, 100);
	
	JProgressBar[] bars = new JProgressBar[4];	
	JLabel[] lblScores_ = new JLabel[4];
	
	JLabel lblScore_ = new JLabel("0");	
	
	Dimension dim  = new Dimension(80, 24);
	Dimension dim2 = new Dimension(160, 24);
	
	public ScoresPanel(){
		super(new BorderLayout());
		setBorder(SwingUtility.myBorder("Scores"));
		
		JPanel p1 = new JPanel(new GridBagLayout());
		p1.setBorder(SwingUtility.myBorder2());
		
		JLabel[] lbls = new JLabel[4];
		
		lbls[0] = new JLabel("Join-Lossless");
		lbls[1] = new JLabel("Dependency Preservation");
		lbls[2] = new JLabel("Normal Form");
		lbls[3] = new JLabel("Issues");		
		
		JLabel lblScore = new JLabel("Total Score            ");
		lblScore.setFont(SwingUtility.fnt_label0);
				
		for (int i = 0; i < bars.length; i++) {
			bars[i] = new JProgressBar(0, 100);
			bars[i].setStringPainted(true);
			bars[i].setPreferredSize(dim2);
			bars[i].setMinimumSize(dim2);
			lblScores_[i] = new JLabel("0"); 
			lblScores_[i].setFont(SwingUtility.fnt_label1);
			lblScores_[i].setPreferredSize(dim);
			lblScores_[i].setMinimumSize(dim);
			lbls[i].setFont(SwingUtility.fnt_label0);
		}		
		barScore.setStringPainted(true);
		barScore.setPreferredSize(dim2);
		barScore.setMinimumSize(dim2);
		lblScore_.setFont(SwingUtility.fnt_label1B);
		lblScore_.setPreferredSize(dim);
		lblScore_.setMinimumSize(dim);
		
		for (int i = 0; i < lbls.length; i++) {
		//	p1.add(lbls[i]); 	p1.add(bars4[i]);	p1.add(lblScores_[i]);
			
			p1.add(lbls[i], 	SwingUtility.myC(0, i)); 
			p1.add(bars[i],		SwingUtility.myC(1, i));
			p1.add(lblScores_[i], SwingUtility.myC(2, i));
		}
		
		JPanel p2 = new JPanel(new GridBagLayout());
		p2.setBorder(SwingUtility.myBorder2());
		p2.add(lblScore, 	SwingUtility.myC(0, 0)); 
		p2.add(barScore, 	SwingUtility.myC(1, 0)); 
		p2.add(lblScore_,	SwingUtility.myC(2, 0));
		
		add(p1, BorderLayout.CENTER);
		add(p2, BorderLayout.PAGE_END);
	}
	
	public void setScores(float[] scores, float score){
		for (int i = 0; i < scores.length; i++) {
			bars[i].setValue((int) (scores[i]*100));
			lblScores_[i].setText(scores[i]+"");
		}		
		barScore.setValue((int) (score *100));
		lblScore_.setText(score+"");
	}
	
	public void clear(){
		setScores(new float[4], 0);
	}
	
}

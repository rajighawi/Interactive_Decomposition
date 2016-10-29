package gui.tasks;

import fd.Attribute;
import fd.AttributeSet;
import fd.Relation;
import gui.utilities.MyLabel;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Set;

import javax.swing.JPanel;

import utilities.ColorUtility;

public class PNPPanel extends JPanel{

	MyLabel lblP  = new MyLabel(" ");
	MyLabel lblNP = new MyLabel(" ");
	
	public PNPPanel(){
		super(new GridLayout(2, 3));
		Color c = ColorUtility.blueviolet;
		MyLabel lblP_ = new MyLabel("Prime", c);
		MyLabel lblNP_ = new MyLabel("Non-Prime", c);
		
		add(lblP_);	add(lblNP_); 
		add(lblP);	add(lblNP);  
	}
	
	public void setP(String s){
		lblP.setText(s);
	}
	
	public void setNP(String s){
		lblNP.setText(s);
	}
	
	//----------------------------------------------------------
	
	public void clear(){
		setP(""); setNP(""); 
	}
		
	public void go(Relation r, Set<AttributeSet> keys){
		AttributeSet atts = r.getAttributes();
		AttributeSet P = new AttributeSet();
		AttributeSet NP = new AttributeSet(atts);		
		for(AttributeSet key: keys){
			for(Attribute a : key){
				P.add(a);
				NP.remove(a);
			}					
		}
		setP(P.toString2());
		setNP(NP.toString2());
	}
	
}


package gui.tasks;

import fd.Attribute;
import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.Relation;
import gui.utilities.MyLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import utilities.ColorUtility;

public class LMRPanel extends JPanel{

	MyLabel lblL = new MyLabel(" ");
	MyLabel lblM = new MyLabel(" ");
	MyLabel lblR = new MyLabel(" ");
	MyLabel lblO = new MyLabel(" ");
	
	public LMRPanel(){
		super(new BorderLayout());
		Color c = ColorUtility.brown;
		MyLabel lblL_ = new MyLabel("L", c);
		MyLabel lblM_ = new MyLabel("M", c);
		MyLabel lblR_ = new MyLabel("R", c);
		MyLabel lblO_ = new MyLabel("Others", c);
		
		JPanel p1 = new JPanel(new GridLayout(2, 3));
		p1.add(lblL_);	p1.add(lblM_); p1.add(lblR_);
		p1.add(lblL);	p1.add(lblM);  p1.add(lblR); 
		JPanel p2 = new JPanel(new GridLayout(1, 2));   
		p2.add(lblO_); 	p2.add(lblO);
		
		add(p1, BorderLayout.CENTER);
		add(p2, BorderLayout.PAGE_END);
	}
	
	public void setL(String s){
		lblL.setText(s);
	}
	
	public void setM(String s){
		lblM.setText(s);
	}
	
	public void setR(String s){
		lblR.setText(s);
	}
	
	public void setO(String s){
		lblO.setText(s);
	}
	
	//----------------------------------------------------------
	
	public void clear(){
		setL(""); setM(""); setR(""); setO("");
	}
	
	public void go(Relation r, FDSet f){
		AttributeSet atts = r.getAttributes();
		AttributeSet L = new AttributeSet();
		AttributeSet M = new AttributeSet();
		AttributeSet R = new AttributeSet();
		AttributeSet others = new AttributeSet();

		for(FD fd: f){
			for(Attribute la:fd.getLHS())
				L.add(la);			
			for(Attribute ra:fd.getRHS())
				R.add(ra);					
		}
		
		for(Attribute a:atts){
			if(L.contains(a) && R.contains(a)){
				M.add(a);
				L.remove(a);
				R.remove(a);
			}
		}
		
		for(Attribute a:atts){
			if(!L.contains(a) && !R.contains(a) && !M.contains(a)){
				others.add(a);
			}
		}
		setL(L.toString2());
		setM(M.toString2());
		setR(R.toString2());
		setO(others.toString2());
	}	
}

package tests;

import fd.Attribute;
import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class TestFDSetClosure {

	public static void main(String[] args) {
		System.out.println("\tAll\t1RHS\tAllnTriv\t1RHSnTriv");
		
		String s = "ABCD";
		for (char c = 'E'; c <= 'J'; c++) {
			s += c;
			//System.out.println(s);
			AttributeSet as = new AttributeSet();
			as.addAllC(s);
			Relation r = new Relation("R", as);
			
			//System.out.println(r);
			AttributeSet AB = new AttributeSet(); AB.addAllC("AB");
		//	AttributeSet A = new AttributeSet(new Attribute("A"));
		//	AttributeSet B = new AttributeSet(new Attribute("B"));
			AttributeSet C = new AttributeSet(new Attribute("C"));
		//	AttributeSet D = new AttributeSet(new Attribute("D"));
			AttributeSet DE = new AttributeSet(); DE.addAllC("DE");
		//	AttributeSet E = new AttributeSet(new Attribute("E"));
			
			FDSet F = new FDSet("F");
			F.add(new FD(AB, C));
			F.add(new FD(C, DE));
			
			//System.out.println(F);
			//System.out.println("----------------");
			FDSet f00 = FDUtility.closureFDSet(F, r, false, false);
			int n1 = f00.size();
			FDSet f01 = FDUtility.closureFDSet(F,  r, false, true);
			int n2 = f01.size();
			FDSet f10 = FDUtility.closureFDSet(F,  r, true, false);
			int n3 = f10.size();
			FDSet f11 = FDUtility.closureFDSet(F,  r, true, true);
			int n4 = f11.size();
			
			
			System.out.println(s.length()+"\t"+n1+"\t"+n2+"\t"+n3+"\t"+n4);
			
		}
		
		
		/*
		for(FD fd:Fclosure){
			System.out.println(fd);
		}*/
	}

}

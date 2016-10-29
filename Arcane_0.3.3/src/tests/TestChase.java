package tests;

import java.util.ArrayList;

import fd.AttributeSet;
import fd.ChasePack;
import fd.Decomposition;
import fd.FD;
import fd.FDSet;
import fd.Relation;

public class TestChase {

	public static void main(String[] args) {
		AttributeSet aa = new AttributeSet();
		aa.addAllC("ABCD");
		Relation R = new Relation("R", aa);
		
		AttributeSet A = new AttributeSet();  A.addAllC("A");
		AttributeSet B = new AttributeSet();  B.addAllC("B");
		AttributeSet CD = new AttributeSet(); CD.addAllC("CD");
		AttributeSet C = new AttributeSet();  C.addAllC("C");
		AttributeSet D = new AttributeSet();  D.addAllC("D");
		FDSet F = new FDSet();			
		F.add(new FD(A, B)); 	F.add(new FD(B, C)); 	F.add(new FD(CD, A));
		
		R.addFDSet(F);
		
		AttributeSet s1as = new AttributeSet(); s1as.addAllC("AD");
		AttributeSet s2as = new AttributeSet(); s2as.addAllC("AC");
		AttributeSet s3as = new AttributeSet(); s3as.addAllC("BCD");
		Relation S1 = new Relation("S1", s1as);
		Relation S2 = new Relation("S2", s2as);
		Relation S3 = new Relation("S3", s3as);
		 
		Decomposition decmp = new Decomposition(R, F, "");
		decmp.add(S1);
		decmp.add(S2);
		decmp.add(S3);
		  
		
		ArrayList<ChasePack> packs = decmp.chase();
		for (int i = 0; i < packs.size(); i++) {
			ChasePack  pack = packs.get(i);
			System.out.println(pack.getMsg1());
			System.out.println(Decomposition.printTableau(pack.getTableau()));
			System.out.println(pack.getMsg2());
			System.out.println("----------------");
		}
		
			 
		System.out.println("************************************");
		//System.out.println(report);
	}

}

package tests;

import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.MinCoverExplain;
import fd.Relation;

public class Test7 {

	public static void main(String[] args) {
		AttributeSet attributes = new AttributeSet();
		attributes.addAll("A,B,C,D,E,G");
		Relation r = new Relation("R", attributes);
		System.out.println(r);
		
		/*
		//F = {A -> D, BC -> AD, C -> B, E -> A, E -> D}		
		FD fd1 = new FD("A", "D");		
		FD fd2 = new FD("BC", "AD");		
		FD fd3 = new FD("C", "B");		
		FD fd4 = new FD("E", "A");
		FD fd5 = new FD("E", "D");
		FDSet F = new FDSet();		
		F.add(fd1);		F.add(fd2);
		F.add(fd3);		F.add(fd4);		
		F.add(fd5);
		System.out.println(F);
		System.out.println("-----------------");		
		F = Utility.explainMinimalCover(F);
		*/
	//	F = { AB -> C, C -> A, BC -> D, ACD -> B, D -> E, D -> G, BE -> C, CG -> B,	CG -> D, CE -> A, CE -> G}
		
		FD fd1 = new FD("AB", "C");		
		FD fd2 = new FD("C", "A");		
		FD fd3 = new FD("BC", "D");		
		FD fd4 = new FD("ACD", "B");
		FD fd5 = new FD("D", "E");		
		FD fd6 = new FD("D", "G");
		FD fd7 = new FD("BE", "C");
		FD fd8 = new FD("CG", "B");
		FD fd9 = new FD("CG", "D");
		FD fd10 = new FD("CE", "A");
		FD fd11 = new FD("CE", "G");
		
		
		FDSet F = new FDSet();		
		F.add(fd1);		F.add(fd2);
		F.add(fd3);		F.add(fd4);		
		F.add(fd5);		F.add(fd6);
		F.add(fd7);		F.add(fd8);		
		F.add(fd9);		F.add(fd10);
		F.add(fd11);
		
		System.out.println(F);
		System.out.println("-----------------");
		MinCoverExplain mce = new MinCoverExplain(F);
		F = mce.getOutput();
		System.out.println(mce.getLog());
		
	}

}

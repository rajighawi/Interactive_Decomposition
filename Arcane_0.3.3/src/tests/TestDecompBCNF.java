package tests;

import java.util.ArrayList;
import java.util.Set;

import normalization.BCNF_d;
import fd.AttributeSet;
import fd.ChasePack;
import fd.Decomposition;
import fd.FD;
import fd.FDSet;
import fd.Relation;

public class TestDecompBCNF {

	public static void main(String[] args) {
		AttributeSet aa = new AttributeSet();
		aa.addAllC("ABCDE");
		Relation r = new Relation("R", aa);
		
		AttributeSet A = new AttributeSet();  A.addAllC("A");
		AttributeSet B = new AttributeSet();  B.addAllC("B");
		AttributeSet AB = new AttributeSet(); AB.addAllC("AB");
		AttributeSet C = new AttributeSet();  C.addAllC("C");
		AttributeSet D = new AttributeSet();  D.addAllC("D");
		FD fd1 = new FD(AB, C);
		FD fd2 = new FD(C, B);
		FD fd3 = new FD(A, D);
		FDSet F = new FDSet();
		F.add(fd1); 
		F.add(fd2);
		F.add(fd3);
		
		r.addFDSet(F);
		System.out.println("\n\n==================================\n");

		Set<Decomposition> dds = BCNF_d.allDecompositions(r, F);
		for(Decomposition decmp : dds){
			System.out.println(decmp);
			System.out.println("-------------------------");
			ArrayList<ChasePack> packs = decmp.chase();
			for (int i = 0; i < packs.size(); i++) {
				ChasePack  pack = packs.get(i);
				System.out.println(pack.getMsg1());
				System.out.println(Decomposition.printTableau(pack.getTableau()));
				System.out.println(pack.getMsg2());
				System.out.println("----------------");
			}
			//System.out.println(report);
			System.out.println("-------------------------");
		}
		System.out.println("************************************");
		
	}

}

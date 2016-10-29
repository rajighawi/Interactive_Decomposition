package tests;

import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class Test6 {

	public static void main(String[] args) {
		AttributeSet a = new AttributeSet();
		a.addAllC("ABCDEFGH");
		Relation r = new Relation("R", a);
		FDSet f = FDUtility.allNonTrivialFDs(r);
		int i = 1;
		for(FD fd:f){
			System.out.println(i+"\t"+fd);
			i++;
		}
		System.out.println("--------------------------");
		FDSet e = FDUtility.reduceSingltonRHS(f);
		i = 1;
		for(FD fd:e){
			System.out.println(i+"\t"+fd);
			i++;
		}
		
	}

}

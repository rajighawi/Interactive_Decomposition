package tests;

import java.util.Set;

import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class Test4 {

	public static void main(String[] args) {
		AttributeSet attributes = new AttributeSet();
		attributes.addAll("A,B,C");
		Relation r = new Relation("R", attributes);
		System.out.println(r);
			
		
		FD fd1 = new FD("AB", "C");		
		FD fd2 = new FD("C", "A");	
		FDSet F = new FDSet("F");		
		F.add(fd1);		F.add(fd2);
		System.out.println(F);
				
		Set<AttributeSet> keys = FDUtility.findAllKeys_LMR(r, F);
		System.out.println("------- Keys ---------");
		for(AttributeSet k:keys){
			System.out.println(k);
		}
	}

}

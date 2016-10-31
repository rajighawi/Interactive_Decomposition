package tests;

import java.util.Set;

import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class Test2 {

	public static void main(String[] args) {
		AttributeSet attributes = new AttributeSet();
		attributes.addAll("A,B,C,D,E");
		Relation r = new Relation("R", attributes);
		System.out.println(r);
		
		//F = {A -> D, BC -> AD, C -> B, E -> A, E -> D}

		
		
		
		FD fd1 = new FD("A", "D");		
		FD fd2 = new FD("BC", "AD");		
		FD fd3 = new FD("C", "B");		
		FD fd4 = new FD("E", "A");
		FD fd5 = new FD("E", "D");
		FDSet F = new FDSet("F");		
		F.add(fd1);		F.add(fd2);
		F.add(fd3);		F.add(fd4);		
		F.add(fd5);
		System.out.println(F);
		
		AttributeSet a = new AttributeSet(); 
		a.addAllC("CE");
		
		AttributeSet aplus = FDUtility.closure(a, F);
		System.out.println("("+ a+")+ = " + aplus);
		
		AttributeSet x = new AttributeSet(); x.addAllC("BCE");
		boolean isA_SK = FDUtility.isSuperKey(r, x, F);
		System.out.println(x+"\tis superkey?\t"+isA_SK);
		
		boolean isA_Key = FDUtility.isKey(r, x, F);
		System.out.println(x+"\tis key?\t"+isA_Key);
		
		Set<AttributeSet> keys = FDUtility.findAllKeys_LMR(r, F);
		System.out.println("------- Keys ---------");
		for(AttributeSet k:keys){
			System.out.println(k);
		}
	}

}

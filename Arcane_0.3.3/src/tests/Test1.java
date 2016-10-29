package tests;

import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class Test1 {

	public static void main(String[] args) {
		AttributeSet attributes = new AttributeSet();
		attributes.addAll("A,B,C,D");
		
		Relation r = new Relation("R", attributes);
		System.out.println(r);
		
		
		FD fd1 = new FD("A", "B");		
		FD fd2 = new FD("B", "C");		
		FD fd3 = new FD("BC", "D");		
		FD fd4 = new FD("AD", "B");		
		FDSet F = new FDSet("F");		
		F.add(fd1);		F.add(fd2);
		F.add(fd3);		F.add(fd4);		
		System.out.println(F);
		
		AttributeSet a = new AttributeSet(); 
		a.addAllC("A");
		
		AttributeSet aplus = FDUtility.closure(a, F);
		System.out.println("("+ a +")+ = " + aplus);
		
		FD fd = new FD("B", "A");
		boolean fd_ = FDUtility.hold(fd, F);
		System.out.println(fd+"\t"+fd_);
		
		AttributeSet x = new AttributeSet(); x.addAllC("CDB");
		boolean isA_SK = FDUtility.isSuperKey(r, x, F);
		System.out.println(x+"\tis superkey?\t"+isA_SK);
	}

}

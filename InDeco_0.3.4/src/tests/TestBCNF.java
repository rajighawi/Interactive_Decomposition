package tests;

import java.util.Set;

import normalization.BCNF;
import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class TestBCNF {

	public static void main(String[] args) {
		testBCNF3();
	}
	
	public static void testBCNF3() {
		AttributeSet ratts = new AttributeSet();
		ratts.addAll("A,B,C,D");
		Relation r = new Relation("R", ratts);
		System.out.println(r);
		
		//F = {AB -> C, C -> D, D -> A}		
		FD fd1 = new FD("AB", "C");		
		FD fd2 = new FD("C", "D");
		FD fd3 = new FD("D", "A");
		FDSet F = new FDSet();		
		F.add(fd3);
		F.add(fd1);		
		F.add(fd2); 
		System.out.println(F);
		System.out.println("-----------------");	
		BCNF bcnf = new BCNF(r, F);
		System.out.println(bcnf.check());
		System.out.println();
		Set<Relation> subs = bcnf.decompose();
		for(Relation rf:subs){
			System.out.println(rf);
			System.out.println(rf.getFdSets().get(0));
			System.out.println();
		}
	}
	
	public static void testBCNF2() {
		AttributeSet ratts = new AttributeSet();
		ratts.addAll("car#,year,price,supplier,color");
		Relation r = new Relation("R", ratts);
		System.out.println(r);
		
		//F = {car# -> supplier, {car#, year} -> price, supplier -> colorg}	
		AttributeSet car = new AttributeSet(); car.add("car#");
		AttributeSet supplier = new AttributeSet(); supplier.add("supplier");
		
		AttributeSet caryear = new AttributeSet(); caryear.addAll("car#,year");
		AttributeSet price = new AttributeSet(); price.add("price");
		AttributeSet color = new AttributeSet(); color.add("color");
		
		FD fd1 = new FD(car, supplier);		
		FD fd2 = new FD(caryear, price);
		FD fd3 = new FD(supplier, color);	
		FDSet F = new FDSet("F");		
		F.add(fd1);		
		F.add(fd2);
		F.add(fd3);
		System.out.println(F);
		System.out.println("-----------------");	
		BCNF bcnf = new BCNF(r, F);
		System.out.println(bcnf.check());
		System.out.println();
		Set<Relation> subs = bcnf.decompose();
		for(Relation rf:subs){
			System.out.println(rf);
			System.out.println(rf.getFdSets().get(0));
			System.out.println();
		}
	}
	
	public static void testBCNF1() {
		AttributeSet ratts = new AttributeSet();
		ratts.addAll("A,B,C,D,E");
		Relation r = new Relation("R", ratts);
		System.out.println(r);
		
		//F = {A -> B, B -> C}		
		FD fd1 = new FD("A", "BE");		
		FD fd2 = new FD("C", "D");
		FDSet F = new FDSet();		
		F.add(fd1);		F.add(fd2); 
		System.out.println(F);
		System.out.println("-----------------");		
		System.out.println("-----------------");	
		BCNF bcnf = new BCNF(r, F);
		System.out.println(bcnf.check());
		System.out.println();
		Set<Relation> subs = bcnf.decompose();
		for(Relation rf:subs){
			System.out.println(rf);
			System.out.println(rf.getFdSets().get(0));
			System.out.println();
		}
	}
	
	public static void testProjectFDs(){
		AttributeSet ratts = new AttributeSet();
		ratts.addAll("A,B,C");
		Relation r = new Relation("R", ratts);
		System.out.println(r);
		AttributeSet ratts_ = new AttributeSet();
		ratts_.addAll("A,C");
		Relation r_ = new Relation("R_", ratts_);
		System.out.println(r_);
		
		//F = {A -> B, B -> C}		
		FD fd1 = new FD("A", "B");		
		FD fd2 = new FD("B", "C");
		FDSet F = new FDSet();		
		F.add(fd1);		F.add(fd2); 
		System.out.println(F);
		System.out.println("-----------------");		
		FDSet F_ = FDUtility.project(F, r, r_);
		System.out.println(F_);
	}

}

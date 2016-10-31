package tests;

import java.util.Set;

import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class Test5 {

	public static void main(String[] args) {
		AttributeSet attributes = new AttributeSet();
		attributes.addAll("car#, year, price, supplier, color");
		Relation r = new Relation("R", attributes);
		System.out.println(r);
		// car# ! supplier, fcar#, yearg ! price, supplier ! colorg
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
		
		
		Set<AttributeSet> keys = FDUtility.findAllKeys_LMR(r, F);
		System.out.println("------- Keys ---------");
		for(AttributeSet k:keys){
			System.out.println(k);
		}
	}

}

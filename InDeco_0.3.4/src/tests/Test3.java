package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import fd.AttributeSet;

public class Test3 {

	public static void main(String[] args) {
		AttributeSet attributes = new AttributeSet();
		attributes.addAll("A,B,C,D");
		System.out.println(attributes);
		System.out.println("------------------------");
		Set<AttributeSet> ps = attributes.powerSet();
		for(AttributeSet a:ps){
			System.out.println(a);
		}
		System.out.println("------------------------");
		List<AttributeSet> list = new ArrayList<AttributeSet>(ps);
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		
	}

}

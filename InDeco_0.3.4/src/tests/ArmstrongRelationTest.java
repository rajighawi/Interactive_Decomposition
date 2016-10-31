package tests;

import java.util.ArrayList;
import java.util.Collections;

import fd.Attribute;
import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class ArmstrongRelationTest {

	public static void main(String[] args) {
		AttributeSet atts = new AttributeSet();		
		atts.addAllC("ABCDE");
		Relation r = new Relation("R", atts);		
		ArrayList<Attribute> attributes = new ArrayList<Attribute>(atts);
		Collections.sort(attributes);
		
		FDSet F = new FDSet();
		F.add(new FD("A", "C"));
		F.add(new FD("AB", "D"));
		F.add(new FD("C", "E"));
		
		System.out.println(F);
		System.out.println();
		ArrayList<String[]> tuples = FDUtility.armstrong(r, F);
		for (int i = 0; i < attributes.size(); i++) {
			System.out.print(attributes.get(i)+"\t");
		}
		System.out.println();
		for (int i = 0; i < tuples.size(); i++) {
			String[] t = tuples.get(i);
			for (int j = 0; j < t.length; j++) {
				System.out.print(t[j]+"\t");
			}
			System.out.println();
		}
	}

}

package tests;

import java.io.File;

import xml.FDXParser;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class TestImp3NF {

	public static void main(String[] args) {
		File file = new File("data/3NF/univ_3nf_problem.fdx");
		Relation R = FDXParser.parse(file);
		System.out.println(R);
		FDSet F = R.getFdSets().get(0);
		System.out.println(F);
		
		FDUtility.CsuperfluousAttrDetection(R, F);
	}

}

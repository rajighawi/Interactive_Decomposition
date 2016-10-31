package tests;

import fd.Attribute;
import fd.AttributeSet;

public class TestPower {

	public static void main(String[] args) {
		AttributeSet aa = new AttributeSet();
		int n = 6;
		for (int i = 0; i < n; i++) {
			String a = (char)('A'+i) + "";
			aa.add(new Attribute(a));
		}
		 
		/*
		System.out.println("------------");
		Set<AttributeSet> pa = aa.powerSet();
		Set<AttributeSet> ntps = new HashSet<AttributeSet>();
		for(AttributeSet v:pa){
			if(v.size()==0 || v.size()==n) continue;
			ntps.add(v);
		}
		ArrayList<AttributeSet> list = new ArrayList<AttributeSet>(ntps);
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		System.out.println(ntps.size());
		*/
		 
		System.out.println(aa);
		System.out.println(aa.lessLevelPowerSet());
	}

	// 
	// #att #attSets	#FDs   
	// 2	2			2
	// 3	6			9
	// 4	14			28	
	// 5	30			75
	// 6	62			186	
	// n    2^n-2			
}

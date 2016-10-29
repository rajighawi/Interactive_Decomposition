package utilities;

import java.util.ArrayList;
import java.util.Collections;

public class ListUtility {

	public static ArrayList<ArrayList<String>> rest(ArrayList<ArrayList<String>> s){
		ArrayList<ArrayList<String>> rest = new ArrayList<ArrayList<String>>();
		if(s.size()<=1) return null;
		for (int i = 1; i < s.size(); i++) {
			rest.add(s.get(i));
		}
		return rest;
	}
	
	// ---------------------------------------------------------------------------
	
	public static void print(String prfx, ArrayList<String> list){
		for (int i = 0; i < list.size(); i++) {
			System.out.println(prfx+list.get(i));
		}
	}
	
	public static String listToString(String prfx, ArrayList<String> list){
		String s = "";
		for (int i = 0; i < list.size(); i++) {
			s += (prfx+list.get(i)+"\n");
		}
		return s;
	}
	
	public static void printLN(String prfx, ArrayList<String> list){
		for (int i = 0; i < list.size(); i++) {
			System.out.println(prfx+Utility.localName(list.get(i)));
		}
	}
	
	public static void printNS_LN(String prfx, ArrayList<String> list){
		for (int i = 0; i < list.size(); i++) {
			String uri = list.get(i);
			System.out.println(prfx+Utility.namespace(uri)+":"+Utility.localName(uri));
		}
	}
	
	public static void print(ArrayList<String> list){
		print("", list);
	}
	
	// list1 and list2 must be ordered
	public static ArrayList<String> intersection(ArrayList<String> list1, ArrayList<String> list2) {
		ArrayList<String> result = new ArrayList<String>();
		int i1 = 0;
		int i2 = 0;
		while (i1 < list1.size() && i2 < list2.size()) {
			String s1 = list1.get(i1);
			String s2 = list2.get(i2);
			int num = s1.compareTo(s2);
			if (num == 0) {
				result.add(s1);
				i1++;
				i2++;
			} else if (num < 0) {
				i1++;
			} else { // num > 0
				i2++;
			}
		}
		return result;
	}
	
	public static ArrayList<String> difference(ArrayList<String> list1, ArrayList<String> list2) {
		ArrayList<String> result = new ArrayList<String>();
		int i1 = 0;
		int i2 = 0;
		while (i1 < list1.size() && i2 < list2.size()) {
			String s1 = list1.get(i1);
			String s2 = list2.get(i2);
			int num = s1.compareTo(s2);
	//		System.out.println("i1 = "+i1+"\ti2 = "+i2+"\t s1 = '"+s1+"'\t s2 = '"+s2+"'");
			if (num == 0) {
				i1++;
				i2++;
			} else if (num < 0) {
				result.add(s1);
				i1++;
			} else { // num > 0
				i2++;
			}
		}
		
		if(i1<list1.size()){
			for (int i = i1; i < list1.size(); i++) {
				String s = list1.get(i);
				result.add(s);
			}
		}		
		return result;
	}
	
	public static ArrayList<String> union(ArrayList<String> list1, ArrayList<String> list2) {
		ArrayList<String> result = new ArrayList<String>();
		result.addAll(list1);
		result.addAll(list2);
		Collections.sort(result);		
		return distinct(result);
	}
	
	public static ArrayList<String> distinct(ArrayList<String> orderedList){
		ArrayList<String> list = new ArrayList<String>();
		if (orderedList.size() > 0) {
			list.add(orderedList.get(0));
			for (int i = 1; i < orderedList.size(); i++) {
				if (!orderedList.get(i).equals(orderedList.get(i-1))) {
					list.add(orderedList.get(i));
				}
			}
		}
		return list;
	}
}

package fd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttributeSet extends HashSet<Attribute> implements Comparable<AttributeSet> {

	public AttributeSet(){
		super();
	}
	
	public AttributeSet(Attribute a){ // {a}
		super();
		add(a);
	}

	public AttributeSet(AttributeSet a){
		super();
		for(Attribute x:a)
			add(x);
	}
	
	public AttributeSet(AttributeSet a, AttributeSet b){ // union
		this(a);
		addAll(b);
	}

	private AttributeSet(List<Attribute> subList) {
		super();
		for(Attribute x:subList)
			add(x);
	}
	
	@Override
	public boolean add(Attribute a){
		for(Attribute b:this){
			if(b.getName().equalsIgnoreCase(a.getName())){
				return false;
			}
		}
		return super.add(a);
	}

	public void add(String a){
		a = a.trim();
		if(a.contains(",")){
			System.err.println("Attribute Name '"+a+"' contains a comma!");
			return;
		} 
		add(new Attribute(a.trim()));
	}

	// attribute names are separated by a comma
	public void addAll(String a){
		String[] aa = a.split(",");
		for(String a1:aa){
			add(a1);
		}
	}

	// attribute names are characters of string `a` 
	public void addAllC(String a){
		for (int i = 0; i < a.length(); i++) {
			add(a.charAt(i)+"");
		}
	}

	public ArrayList<String> attNames(){
		ArrayList<String> atts = new ArrayList<String>();
		for(Attribute a:this){
			atts.add(a.getName());
		}
		Collections.sort(atts);
		return atts;
	}

	public String toString(){
		return "{"+toString2()+"}";
	}
	
	public String toString2(){
		String s = "";
		ArrayList<String> atts = attNames();
		if(atts.isEmpty()) return "";
		s += atts.get(0);
		for (int i = 1; i < atts.size(); i++) {
			s += ", "+atts.get(i);
		}
		return s;
	}
	
	//-------------- Set operations ----------------------
	
	public AttributeSet minus(AttributeSet B){ // return this\B
		AttributeSet C = new AttributeSet(this);
		C.removeAll(B);
		return C;
	}
	
	public AttributeSet minus(Attribute b){ // return this\{b}
		AttributeSet C = new AttributeSet(this);
		C.remove(b);
		return C;
	}
	
	public AttributeSet union(AttributeSet B){ // return this U B
		AttributeSet C = new AttributeSet(this);
		C.addAll(B);
		return C;
	}
	
	public AttributeSet intersect(AttributeSet B){ 
		AttributeSet C = new AttributeSet();
		for(Attribute a: this)
			if(B.contains(a))
				C.add(a);
		return C;
	}
	
	// ----------------------------------------------------------------------------
	
	public static AttributeSet intersect(AttributeSet A, AttributeSet B){ 
		AttributeSet C = new AttributeSet();
		for(Attribute a:A)
			if(B.contains(a))
				C.add(a);
		return C;
	}

	public static AttributeSet minus(AttributeSet A, AttributeSet B){ // return A/B
		AttributeSet C = new AttributeSet(A);
		C.removeAll(B);
		return C;
	}
	
	public static AttributeSet minus(AttributeSet A, Attribute b){ // return A/{b}
		AttributeSet C = new AttributeSet(A);
		C.remove(b);
		return C;
	}
	
	public static AttributeSet union(AttributeSet A, AttributeSet B){ // return A U B
		AttributeSet C = new AttributeSet(A);
		C.addAll(B);
		return C;
	}
	
	public static AttributeSet union(AttributeSet A, Attribute b){ // return A U {b}
		AttributeSet C = new AttributeSet(A);
		C.add(b);
		return C;
	}
	
	//---------------------------------------------------------------------

	public Set<AttributeSet> lessLevelPowerSet(){ // returns the proper subsets of size-1; e.g. ABC ==> AB, AC, BC
		Set<AttributeSet> sets = new HashSet<AttributeSet>();
		Set<AttributeSet> power = powerSet();
		for(AttributeSet a:power){
			if(a.size()==this.size()-1){
				sets.add(a);
			}
		}
		return sets;
	}
	
	public Set<AttributeSet> powerSet(){
		Set<AttributeSet> sets = new HashSet<AttributeSet>();
		if (this.isEmpty()) {
			sets.add(new AttributeSet());
			return sets;
		}
		List<Attribute> list = new ArrayList<Attribute>(this);
		Attribute head = list.get(0);
		AttributeSet rest = new AttributeSet(list.subList(1, list.size()));
		for (AttributeSet set : rest.powerSet()) {
			AttributeSet newSet = new AttributeSet();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		return sets;
	}
	
	public static Set<Set<Integer>> powerSet(Set<Integer> originalSet) {
		Set<Set<Integer>> sets = new HashSet<Set<Integer>>();
		if (originalSet.isEmpty()) {
			sets.add(new HashSet<Integer>());
			return sets;
		}
		List<Integer> list = new ArrayList<Integer>(originalSet);
		Integer head = list.get(0);
		Set<Integer> rest = new HashSet<Integer>(list.subList(1, list.size()));
		for (Set<Integer> set : powerSet(rest)) {
			Set<Integer> newSet = new HashSet<Integer>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		return sets;
	}

	@Override
	public int compareTo(AttributeSet o) {
		if(size()<o.size()) {
			return -1;
		} else if(size()==o.size()){
			return 0;
		} else {
			return 1;
		}
	}


}

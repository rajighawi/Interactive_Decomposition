package fd;

import java.util.ArrayList;

import jdbcTest.MyConnection;

public class Relation implements Comparable<Relation> {

	String name;
	AttributeSet attributes;
	
	ArrayList<FDSet> fdSets;
	ArrayList<Decomposition> decompositions;
	
	MyConnection conn;
	
	int nbDupl = 0;
	
	public Relation(String name, AttributeSet attributes) {
		this(name, attributes, null);
	}

	public Relation(String name, AttributeSet attributes, MyConnection conn) {
		this.name = name;
		this.attributes = attributes;
		this.fdSets = new ArrayList<FDSet>();
		this.decompositions = new ArrayList<Decomposition>();
		this.conn = conn;
		this.nbDupl = 0;
	}
	
	public Relation duplicate(){
		nbDupl++;
		Relation newR = new Relation(name+"_"+nbDupl, attributes, conn);
		for(FDSet f:fdSets){
			try {
				FDSet g = new FDSet(f.getName());
				g.addAll(f);				
				newR.addFDSet(g);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		return newR;
	}
	
	public void rename(String newname){
		this.name = newname;
	}
	
	public void addAttribute(Attribute a){
		attributes.add(a);
	}
	
	//------------------------------------------------
	
	public void setFdSets(ArrayList<FDSet> fdSets) {
		this.fdSets = fdSets;
	}

	public String getName() {
		return name;
	}

	public AttributeSet getAttributes() {
		return attributes;
	}
	
	public ArrayList<FDSet> getFdSets() {
		return fdSets;
	}
	
	public ArrayList<Decomposition> getDecompositions() {
		return decompositions;
	}

	public MyConnection getConn() {
		return conn;
	}

	public void setConn(MyConnection conn) {
		this.conn = conn;
	}

	public void addFDSet(FDSet fdSet){
		//System.out.println("Adding FDSet to: "+this.name);
		fdSets.add(fdSet);
	}
	
	public void addDecomposition(Decomposition decomposition){
		decompositions.add(decomposition);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relation other = (Relation) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String s = name +" (";
		for(Attribute a: attributes) {
			s += a.getName() + ",";
		}
		s = s.substring(0, s.length()-1) + ")";
		return s;
		//return name + "(" + attributes.toString() + ")";
	}

	@Override
	public int compareTo(Relation o) {
		return name.compareTo(o.getName());
	}	
	
}

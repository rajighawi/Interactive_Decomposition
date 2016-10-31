package fd;

import java.util.HashSet;
import java.util.Set;

public class DependencySet extends HashSet<Dependency> {

	String name;
		
	public DependencySet(String name){
		super();
		this.name = name;
	}

	public DependencySet(){
		super();
		this.name = null;
	}
	
	// -------------- copy constructors ----------------
	public DependencySet(DependencySet f){
		super();
		addAll(f);
	}
	
	public DependencySet(String name, DependencySet f){
		this(f);
		this.name = name;
	}
	
	//----------------------------------------------------
	
	public Set<MVD> M(){
		Set<MVD> mvds = new HashSet<MVD>();
		for(Dependency dep:this){
			if(dep instanceof MVD){
				mvds.add((MVD) dep);
			} else if(dep instanceof FD){
				FD fd = (FD) dep;
				System.out.println(fd);
				AttributeSet lhs = fd.getLHS();
				AttributeSet rhs = fd.getRHS();
				System.out.println(rhs);
				for(Attribute a: rhs){
					MVD mvd = new MVD(lhs, a);
					mvds.add(mvd);
				}
			}
		}
		return mvds;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString(){
		String s = "";
		if(name!=null){
			s += name + " = ";
		}
		s += "{";
		for(Dependency dep:this){
			s += dep.toString()+", ";
		}
		if(!isEmpty()){
			s = s.substring(0, s.length()-2);
		}
		s += "}";
		return s;
	}
	
	
}

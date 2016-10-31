package fd;

import java.util.HashSet;

public class FDSet extends HashSet<FD>{

	String name;
		
	public FDSet(String name){
		super();
		this.name = name;
	}

	public FDSet(){
		super();
		this.name = null;
	}
	
	// -------------- copy constructors ----------------
	public FDSet(FDSet f){
		super();
		addAll(f);
	}
	
	public FDSet(String name, FDSet f){
		this(f);
		this.name = name;
	}
	
	//----------------------------------------------------
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString(){
		String s = "";
		/*if(name!=null){
			s += name + " = ";
		}*/
		s += "{";
		for(FD fd:this){
			s += fd.toString()+", ";
		}
		if(!isEmpty()){
			s = s.substring(0, s.length()-2);
		}
		s += "}";
		return s;
	}
	
}

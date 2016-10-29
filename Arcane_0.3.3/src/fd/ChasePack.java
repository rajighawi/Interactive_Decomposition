package fd;

import com.google.common.collect.HashBasedTable;

public class ChasePack {

	String msg1;
	HashBasedTable<Relation, Attribute, Symbol> tableau;
	String msg2;
	String title; 
	
	public ChasePack(String msg1,
			HashBasedTable<Relation, Attribute, Symbol> tableau, String msg2, String title) {
		super();
		this.msg1 = msg1;
		this.tableau = tableau;
		this.msg2 = msg2;
		this.title = title;
	}

	public String getMsg1() {
		return msg1;
	}

	public HashBasedTable<Relation, Attribute, Symbol> getTableau() {
		return tableau;
	}

	public String getMsg2() {
		return msg2;
	}

	public String getTitle() {
		return title;
	}
	
	
	
	
}

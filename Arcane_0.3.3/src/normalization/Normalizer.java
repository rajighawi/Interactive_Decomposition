package normalization;

import java.util.Set;

import fd.Attribute;
import fd.AttributeSet;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public abstract class Normalizer {

	public static final String BCNF = "BCNF";
	public static final String _3NF = "3NF";
	public static final String _2NF = "2NF";
	
	String type;
	Relation relation;
	FDSet fdSet;
	
	Set<AttributeSet> keys;
	AttributeSet primes;
	AttributeSet nonprimes;
	String table;
	
	public Normalizer(String type, Relation relation, FDSet fdSet){
		this.type = type;
		this.relation = relation;
		this.fdSet = fdSet;
		this.table = relation.getName();
		// find keys
		this.keys = FDUtility.findAllKeys_LMR(relation, fdSet);
		// prepare prime and non-primes sets of attributes
		this.primes = new AttributeSet();
		this.nonprimes = new AttributeSet(relation.getAttributes());
		for(AttributeSet key:keys){
			for(Attribute k:key){
				primes.add(k);
				nonprimes.remove(k);
			}
		}

	}
	
	public abstract boolean check();
	public abstract String explain();
	public abstract Set<Relation> decompose();
	
	// ------------------------------------------------------
	
	public static String[] sql(Relation r, Set<Relation> subRels){
		String[] sqls = new String[subRels.size()];
		String t = r.getName();
		int i = 0;
		for(Relation r_: subRels){			
			String aas = r_.getAttributes().toString2();
			sqls[i] = "CREATE VIEW "+r_.getName()+" AS \n\tSELECT DISTINCT " + aas + "\n\tFROM "+t+";\n";
			i++;
		}
		// TODO find a way to join views in correct order
		//String sj = "CREATE VIEW "+t+"_bcnf_join AS \n\tSELECT * FROM ";
				
		return sqls;
	}

}

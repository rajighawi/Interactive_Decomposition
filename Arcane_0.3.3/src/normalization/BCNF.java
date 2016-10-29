package normalization;

import java.util.HashSet;
import java.util.Set;

import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class BCNF extends Normalizer {

	public BCNF(Relation relation, FDSet fdSet) {
		super(BCNF, relation, fdSet);
	}


	@Override
	public boolean check() {
		if(relation.getAttributes().size()==2) return true;
		for(FD fd:fdSet){
			AttributeSet lhs = fd.getLHS();
			if(!FDUtility.isSuperKey(relation, lhs, fdSet)){
				return false;
			}
		}
		return true;
	}

	@Override
	public String explain() {
		String s = "The keys of `" + table + "` are: " + keys +"\n";
		s += "The   prime   attributes are: " + primes + "\n";
		s += "The non-prime attributes are: " + nonprimes + "\n";
		
		boolean bcnf = true;
		for(FD fd:fdSet){
			AttributeSet lhs = fd.getLHS();
			boolean sk = FDUtility.isSuperKey(relation, lhs, fdSet); 
			s += "LHS of FD: " + fd + " is "+(sk?"":"NOT")+" a super key of the relation `" + table + "`\n";
			if(!sk){				
				bcnf = false;
			}
		}
		s += "Thus, the relation `"+ table +"` w.r.t FDSet: "+fdSet.getName()+" IS " +(bcnf?"":"NOT") + " in BCNF";
		return s;
	}

	@Override
	public Set<Relation> decompose() {	
		Set<Relation> subs = new HashSet<Relation>();
		FD start = null;
		for(FD fd:fdSet){
			AttributeSet lhs = fd.getLHS();
			if(!FDUtility.isSuperKey(relation, lhs, fdSet)){
				start = fd;
				break;				
			}
		}
		if(start!=null){
			AttributeSet lhs = start.getLHS();
			AttributeSet lhsPlus = FDUtility.closure(lhs, fdSet);
			AttributeSet r2atts = new AttributeSet(relation.getAttributes());
			r2atts.removeAll(lhsPlus);
			r2atts.addAll(lhs);
						
			Relation r1 = new Relation(table+"1", lhsPlus);			
			Relation r2 = new Relation(table+"2", r2atts);
			FDSet f1 = FDUtility.project(fdSet, relation, r1);
			FDSet f2 = FDUtility.project(fdSet, relation, r2);
			f1.setName("fds_"+r1.getName()+"_0");
			f2.setName("fds_"+r2.getName()+"_0");
			try {
				r1.addFDSet(f1);
				r2.addFDSet(f2);				
				BCNF nf1 = new BCNF(r1, f1);
				if(nf1.check()) subs.add(r1);
				else subs.addAll(nf1.decompose());				
				BCNF nf2 = new BCNF(r2, f2);				
				if(nf2.check()) subs.add(r2);
				else subs.addAll(nf2.decompose());
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}		
		return subs;
	}



}

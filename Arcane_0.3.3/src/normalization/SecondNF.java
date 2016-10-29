package normalization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fd.Attribute;
import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class SecondNF extends Normalizer {

	Set<FD> violatingFDs;
	String log;
	boolean satisfied;
	
	public SecondNF(Relation relation, FDSet fdSet) {
		super(_2NF, relation, fdSet);
		violatingFDs = new HashSet<FD>();
		satisfied = true;
		go();
	}
	
	public void go(){
		log = "The keys of `" + table + "` are: " + keys +"\n";
		log += "The   prime   attributes are: " + primes + "\n";
		log += "The non-prime attributes are: " + nonprimes + "\n";		
		AttributeSet nprhs = new AttributeSet(); 
		for(FD fd : fdSet){
			AttributeSet lhs = fd.getLHS();
			AttributeSet rhs = fd.getRHS();
			for(Attribute a: rhs.minus(lhs)){
				if(nonprimes.contains(a)){ // A is a non-prime attribute
					log += "RHS of FD: " + fd + " has a non-prime attribute: `"+a+"`\n";
					nprhs.add(a);
				}
			}
		}		
		if(nprhs.isEmpty()){
			log += "There is no FD whose RHS is non-prime attribute.\n";
			log += "Therefore, the relation `"+ table +"` w.r.t FDSet: "+fdSet.getName()+" IS in 2NF";
			satisfied = true;
		} else {
			Set<AttributeSet> allKeysSubsets = new HashSet<AttributeSet>();
			for(AttributeSet key : keys){
				if(key.size()>1){
					Set<AttributeSet> subsets = key.lessLevelPowerSet();
					allKeysSubsets.addAll(subsets);
				}
			}
			log += "The l-1 proper subsets of all keys are: "+allKeysSubsets + "\n";
			if(allKeysSubsets.isEmpty()){
				satisfied = true;
			} else {
				Set<AttributeSet> violSubsets = new HashSet<AttributeSet>();
				for(AttributeSet ss: allKeysSubsets){
					AttributeSet c = FDUtility.closure(ss, fdSet);
					for(Attribute a:nprhs){
						if(c.contains(a)){
							log += "The closure of the subset "+ss+ " contains the non-prime attribute "+a + "\n";
							FD fd = new FD(ss, a);
							violatingFDs.add(fd);
							log += "That is: " + fd + " belongs to closure(F); this violates 2NF.\n";
							violSubsets.add(ss);
							satisfied = false;
						}
					}
				}
				
				if(violSubsets.isEmpty()){
					log += "There is no subset of any key that determines any non-primes attribute of "+nprhs+"\n";
					log += "Therefore, the relation `"+ table +"` w.r.t FDSet: "+fdSet.getName()+" IS in 2NF";
				} else {
					log += "In summary, the relation `"+ table +"` w.r.t FDSet: "+fdSet.getName()+" IS NOT in 2NF";
				}			
			
			}
		}

	}
	
	@Override
	public boolean check() {
		return satisfied;
	}

	@Override
	public String explain() {		
		return log;
	}

	@Override
	public Set<Relation> decompose() {	
		Set<Relation> subs = new HashSet<Relation>();
		if(satisfied){
			System.out.println("Relation "+table + " is already in "+type);
			return null;
		}
		System.out.println(violatingFDs);
		FD start = new ArrayList<FD>(violatingFDs).get(0);
			
		// same decomposition as BCNF
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
				
				SecondNF nf1 = new SecondNF(r1, f1);
				if(nf1.check()) subs.add(r1);
				else subs.addAll(nf1.decompose());
				
				SecondNF nf2 = new SecondNF(r2, f2);				
				if(nf2.check()) subs.add(r2);
				else subs.addAll(nf2.decompose());
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}		
		return subs;
	}

}


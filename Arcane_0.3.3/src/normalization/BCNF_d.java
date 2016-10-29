package normalization;

import java.util.HashSet;
import java.util.Set;

import fd.AttributeSet;
import fd.Decomposition;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class BCNF_d {
 
	public static boolean check(Relation relation, FDSet fdSet) {
		for(FD fd:fdSet){
			AttributeSet lhs = fd.getLHS();
			if(!FDUtility.isSuperKey(relation, lhs, fdSet)){
				return false;
			}
		}
		return true;
	}
	
	
	public static Set<Decomposition> allDecompositions(Relation relation, FDSet fdSet) {
		Set<Decomposition> dds = BCNF_d.decompose(relation, fdSet);
		Set<Set<AttributeSet>> ocean = new HashSet<Set<AttributeSet>>();
		for(Decomposition decmp : dds){
			ocean.add(decmp.pool());			
		}
		Set<Decomposition> decos = new HashSet<Decomposition>();
		for(Set<AttributeSet> pool:ocean){
			int idx = 1;
			Decomposition deco = new Decomposition(relation, fdSet, "deco_"+relation.getName()+"_"+idx);
			for(AttributeSet ras:pool){
				Relation r = new Relation(relation.getName()+idx, ras);
				FDSet fr = FDUtility.project(fdSet, relation, r);
				r.addFDSet(fr);
				deco.add(r);
				idx++;
			}
			decos.add(deco);
		}		
		return decos;
	}
	
	public static Set<Decomposition> decompose(Relation relation, FDSet fdSet) {	
		String table = relation.getName();
		
		Set<FD> violations = new HashSet<FD>();
		for(FD fd:fdSet){
			AttributeSet lhs = fd.getLHS();
			if(!FDUtility.isSuperKey(relation, lhs, fdSet)){
				violations.add(fd);				
			}
		}
		
		Set<Decomposition> result = new HashSet<Decomposition>();
		for(FD start:violations){
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
			r1.addFDSet(f1);
			r2.addFDSet(f2);			
			boolean b1 = check(r1, f1);
			boolean b2 = check(r2, f2);			
			if(b1){ 				
				if(b2) {	
					Decomposition decmp = new Decomposition(relation, fdSet, "");
					decmp.add(r1);
					decmp.add(r2);
					result.add(decmp);				
				} else {
					Set<Decomposition> ds2 = decompose(r2, f2); 
					for(Decomposition d:ds2){
						Decomposition newD = new Decomposition(relation, fdSet, "");
						newD.addAll(d);
						newD.add(r1);
						result.add(newD);
					}
				}
			} else {
				if(b2){
					Set<Decomposition> ds1 = decompose(r1, f1);					
					for(Decomposition d:ds1){
						Decomposition newD = new Decomposition(relation, fdSet, "");
						newD.addAll(d);						
						newD.add(r2);
						result.add(newD);
					}
				} else {
					Set<Decomposition> ds1 = decompose(r1, f1); 					
					Set<Decomposition> ds2 = decompose(r2, f2); 
					for(Decomposition d:ds1){
						for(Decomposition e:ds2){
							Decomposition newD = new Decomposition(relation, fdSet, "");
							newD.addAll(d);
							newD.addAll(e);							
							result.add(newD);
						}						
					}									
				}				
			}
		}		
		return result;
	}

}

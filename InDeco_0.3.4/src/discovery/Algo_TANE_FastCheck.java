package discovery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fd.Attribute;
import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class Algo_TANE_FastCheck extends DiscoveryAlgo {

	Map<AttributeSet, AttributeSet> Cplus;
	
	FastFDCheck fastFDCheck;
		
	public Algo_TANE_FastCheck(Relation relation){
		super(relation, "TANE_Fast");
		
		long start = System.currentTimeMillis();		
		this.fastFDCheck = new FastFDCheck(relation);				 
		this.Cplus = new HashMap<AttributeSet, AttributeSet>();				
		go();
		count_requests = fastFDCheck.getCount_request();
		time_ms = System.currentTimeMillis() - start;		
	}
	
	public void go(){
		Set<AttributeSet> L0 = new HashSet<AttributeSet>(); 
		L0.add(new AttributeSet()); // L0 = { {} }
		Set<AttributeSet> L1 = new HashSet<AttributeSet>();
		for (Attribute A:R) {
			AttributeSet A_s = new AttributeSet(); 
			A_s.add(A);
			L1.add(A_s);			
		}
		Cplus.put(new AttributeSet(), R);
		int l = 1;
		
		Set<AttributeSet> Ll = new HashSet<AttributeSet>(); Ll.addAll(L1);
		while (!Ll.isEmpty()) {
			compute_dependencies(Ll);
			Ll = prune(Ll);
			Ll = generate_next_level(Ll);
			l++;
			System.out.println(l);
			// break; // delete this
		}
	}
	
	public FDSet getValidFDs() {
		return validFDs;
	}
	
	public int getCount_checks() {
		return count_checks;
	}

	public long getTime_ms() {
		return time_ms;
	}
	
	public int getCount_requests() {
		return count_requests;
	}
	
	public String report(){
		String s = "";
		s += "Relation        : " + relation.getName() + "\n";
		s += "Algorithm       : TANE_Fast " + "\n";
		s += "Count of Checks = "+count_checks + "\n";
		s += "# Valid FDs     = "+validFDs.size() + "\n";
		s += "# Requests      = "+count_requests + "\n";
		s += "Time (msec)     = "+time_ms + "\n";
		return s;
	}

	public static Set<AttributeSet> generate_next_level(Set<AttributeSet> L_l){
		Set<AttributeSet> L_l1 = new HashSet<AttributeSet>();
		Set<Set<AttributeSet>> prefix_blocks = prefix_blocks(L_l);
		for(Set<AttributeSet> K:prefix_blocks){
			ArrayList<AttributeSet> Klist = new ArrayList<AttributeSet>(K);
			for (int i = 0; i < Klist.size()-1; i++) {
				AttributeSet Y = Klist.get(i);
				for (int j = i+1; j < Klist.size(); j++) {
					AttributeSet Z = Klist.get(j);
					AttributeSet X = AttributeSet.union(Y, Z);
					
					// if for all A in X, X\{A} in Ll
					boolean q = true;
					for(Attribute a:X){
						AttributeSet x_a = AttributeSet.minus(X, a);
						q = q && L_l.contains(x_a);
					}
					if(q){
						L_l1.add(X);
					}
				}
			}			
		}
 		return L_l1;
	}
	
	public void compute_dependencies(Set<AttributeSet> L_l){		
		for(AttributeSet X:L_l){
			AttributeSet CpX = new AttributeSet(R);
			for(Attribute A:X){
				AttributeSet x_a = AttributeSet.minus(X, A);
				AttributeSet cpx_a = Cplus.get(x_a);
				CpX = AttributeSet.intersect(CpX, cpx_a);
			}
			Cplus.put(X, CpX);
		}		
		for(AttributeSet X:L_l){
			AttributeSet x_cpx = AttributeSet.intersect(X, Cplus.get(X));
			for(Attribute A:x_cpx){
				// check if X\{A} -> A is valid
				AttributeSet x_a = AttributeSet.minus(X, A);
				if(x_a.isEmpty()) continue;
				FD fd = new FD(x_a, new AttributeSet(A));
				boolean b = checkFD(fd);
				count_checks++;
				if(b){
					validFDs.add(fd);
					Cplus.get(X).remove(A);
					AttributeSet R_x = AttributeSet.minus(R, X);
					for(Attribute B:R_x){
						Cplus.get(X).remove(B);
					}
				}
			}			
		}
	}
	
	public Set<AttributeSet> prune(Set<AttributeSet> L_l){
		Set<AttributeSet> H = new HashSet<AttributeSet>(L_l);
		for(AttributeSet X:L_l){
			if(Cplus.get(X).isEmpty()){
				H.remove(X);
				continue;
			}
			// key pruning
			if(FDUtility.isSuperKey(relation, X, validFDs)){
				AttributeSet cpx_x = AttributeSet.minus(Cplus.get(X), X);
				for(Attribute A:cpx_x){					
					AttributeSet CpXa_B = new AttributeSet(R);
					for(Attribute B:X){
						AttributeSet xa_B = AttributeSet.minus(AttributeSet.union(X, A), B);
						if(Cplus.containsKey(xa_B)){
							AttributeSet cpx_a = Cplus.get(xa_B);
							CpXa_B = AttributeSet.intersect(CpXa_B, cpx_a);
						} /*else {
							CpXa_B = new AttributeSet();
							break;
						}*/
					}
					if(CpXa_B.contains(A)){
						FD fd = new FD(X, A);
						validFDs.add(fd);
					}					
				}
				H.remove(X);
			}			
		}
		return H;
	}

	public static Set<Set<AttributeSet>> prefix_blocks(Set<AttributeSet> L_l){
		Map<AttributeSet, Set<AttributeSet>> map = new HashMap<AttributeSet, Set<AttributeSet>>(); // <prefix, {X, ...}>		
		for(AttributeSet X:L_l){
			ArrayList<Attribute> XList = new ArrayList<Attribute>(X);
			Collections.sort(XList);
			List<Attribute> ats = XList.subList(0, XList.size()-1); 
			AttributeSet prefix = new AttributeSet(); prefix.addAll(ats);
			if(map.containsKey(prefix)){
				map.get(prefix).add(X);
			} else {
				Set<AttributeSet> XS = new HashSet<AttributeSet>();
				XS.add(X);
				map.put(prefix, XS);
			}			
		}	
		for(AttributeSet prfx:map.keySet()){
			System.out.println(prfx+"\t"+map.get(prfx));
		}		
		return new HashSet<Set<AttributeSet>>(map.values());
	}
		
	//--------------------------------------------------------------

	public boolean checkFD(FD fd){
		return fastFDCheck.checkFD(fd);
	}
 
}

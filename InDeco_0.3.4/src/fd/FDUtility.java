package fd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import normalization.ThirdNF;

public class FDUtility {

	public static void CsuperfluousAttrDetection(Relation r, FDSet f){
		// FDSet f_ = FDUtility.minimalCover(f);
		ThirdNF _3nf = new ThirdNF(r, f);
		Set<Relation> schemes = _3nf.decompose();
		for(Relation sub:schemes){
			AttributeSet subAtts = sub.getAttributes();
			for(Attribute b:subAtts){
				Set<AttributeSet> k_ = superfluousAttrDetection(r, f, sub, b);
				if(k_!=null){
					System.out.println("Attribute "+b+" in relation "+sub.toString()+" is superfluous");
					System.out.println(k_);
				}
			}
		}
		
		
	}
	
	public static Set<AttributeSet> superfluousAttrDetection(Relation r, FDSet G, Relation sub, Attribute b){
		boolean superfluous = true;
		AttributeSet Ai = sub.getAttributes();
		FDSet Gi = sub.getFdSets().get(0);
		Set<AttributeSet> Ki = findAllKeys_LMR(sub, Gi);
		Set<AttributeSet> Ki_ = new HashSet<AttributeSet>();
		FDSet Gi_b = new FDSet();
		for(AttributeSet k:Ki){
			if(k.equals(Ai)){
				superfluous = false;
				return null;
			} else {
				superfluous = true;
				AttributeSet k_ = k.minus(b);
				Ki_.add(k_);
				for(FD fd:Gi){
					if(!fd.getLHS().union(fd.getRHS()).contains(b)){ // removing all dependencies involving B
						Gi_b.add(fd);
					}
				}				
			}
		}
		// Check restorability.
		if(!Ki_.isEmpty()){
			for(AttributeSet k:Ki_){
				FD fdk = new FD(k, b);
				if(!hold(fdk, Gi_b)){
					superfluous = false;
					return null;
				}
			}
		}
		
		// Check nonessentiality
		Set<AttributeSet> Ki_Ki_ = new HashSet<AttributeSet>(); // Ki - Ki'
		for(AttributeSet k:Ki){
			if(!Ki_.contains(k)){
				Ki_Ki_.add(k);
			}
		}
		
		for(AttributeSet k:Ki_Ki_){
			if(superfluous){
				FD fd = new FD(k, Ai);
				if(!hold(fd, Gi_b)){
					AttributeSet M = closure(k, Gi_b);
					AttributeSet MAi_b = (M.intersect(Ai)).minus(b);
					FD fd2 = new FD(MAi_b, Ai);
					if(hold(fd2, G)){
						superfluous = false;
					} else {
						for(AttributeSet kk:Ki){
							if(MAi_b.containsAll(kk)){
								Ki_.add(kk);
							}
						}
					}
				}
			}
		}
		
		if(superfluous){
			return Ki_;
		} else {
			return null;
		}		 
	}
	
	public static ArrayList<String[]> armstrong(Relation r, FDSet F){
		int n = r.getAttributes().size();
		ArrayList<Attribute> attList = new ArrayList<Attribute>(r.getAttributes());
		Collections.sort(attList);
		
		FDSet all = allNonTrivialFDs(r);
		FDSet Fplus = closureFDSet(F, r, true, true);
		//FDSet Fminus = new FDSet();
		Set<AttributeSet> F_lhsS = new HashSet<AttributeSet>();
		for(FD fd:all){
			if(!Fplus.contains(fd)){
				//Fminus.add(fd);
				F_lhsS.add(fd.lhs);
			}
		}
		
		ArrayList<String[]> tuples = new ArrayList<String[]>();
		int k = 0;
		for(AttributeSet X:F_lhsS){
			AttributeSet Xplus = closure(X, F);
			String[] t1 = new String[n];
			String[] t2 = new String[n];
			for (int i = 0; i < n; i++) {
				char a = (char)('a'+i);
				t1[i] = a+""+k;
				Attribute Ai = attList.get(i);
				if(Xplus.contains(Ai)){
					t2[i] = a+""+k;
				} else {
					t2[i] = a+""+(k+1);
				}
			}
			tuples.add(t1);
			tuples.add(t2);
			k+=2;
		}
		
		return tuples;
	}
	
	public static AttributeSet R_operation(AttributeSet Z, AttributeSet R, FDSet F){
		AttributeSet ZtR = AttributeSet.intersect(Z, R);
		AttributeSet ZtRplus = closure(ZtR, F);
		AttributeSet ZtRplustR = AttributeSet.intersect(ZtRplus, R);
		return AttributeSet.union(Z, ZtRplustR);
	}
	
	public static FDSet closureFDSet(FDSet F, Relation r, boolean exTriv, boolean singleRHS){
		AttributeSet all = r.getAttributes();
		FDSet Fplus = new FDSet(F.getName()+"_closure");
		Set<AttributeSet> p = all.powerSet();
		for(AttributeSet lhs: p){
			if(lhs.isEmpty()) continue;			
			AttributeSet aplus = FDUtility.closure(lhs, F);
			
			if(singleRHS){
				for(Attribute rhs:aplus){
					if(exTriv){
						if(!lhs.contains(rhs)){ // trivial, skip it
							FD fd = new FD(lhs, rhs);
							Fplus.add(fd);	
						}						
					} else {
						FD fd = new FD(lhs, rhs);
						Fplus.add(fd);	
					}					
				}
			} else {
				Set<AttributeSet> pp = aplus.powerSet();
				for(AttributeSet rhs:pp){
					if(rhs.isEmpty()) continue;
					if(exTriv){
						if(!lhs.containsAll(rhs)){ // trivial, skip it
							FD fd = new FD(lhs, rhs);
							Fplus.add(fd);	
						}
					} else {
						FD fd = new FD(lhs, rhs);
						Fplus.add(fd);
					}					
				}
			}		
		}
		return Fplus;
	}
	
	public static AttributeSet closure(AttributeSet x, FDSet F){
		AttributeSet xplus    = new AttributeSet(x);
		AttributeSet oldXplus = new AttributeSet();
		do {
			oldXplus = new AttributeSet(xplus);
			for(FD f:F){
				if(xplus.containsAll(f.getLHS())){
					xplus.addAll(f.getRHS());
				}
			}
		} while(!xplus.equals(oldXplus));		
		return xplus;
	}
	
	public static Map<FD, Boolean> checkImplication(FDSet inputF, FDSet refF){
		Map<FD, Boolean> res = new HashMap<FD, Boolean>();
		for(FD fd:inputF){
			boolean b = hold(fd, refF);
			res.put(fd, b);
		}
		return res;
	}
	
	public static boolean checkEquivalence(FDSet F, FDSet E){
		Map<FD, Boolean> resF_E = checkImplication(F, E);
		Map<FD, Boolean> resE_F = checkImplication(E, F);
		boolean f1 = resF_E.values().contains(false);
		boolean f2 = resE_F.values().contains(false);
		return f1 && f2;		
	}
	
	public static boolean hold(FD g, FDSet F){
		AttributeSet lhs = g.getLHS();
		AttributeSet lhsPlus = closure(lhs, F);
		return lhsPlus.containsAll(g.getRHS());
	}

	public static boolean isSuperKey(Relation r, AttributeSet a, FDSet F){
		AttributeSet aplus = closure(a, F);
		return aplus.containsAll(r.getAttributes());
	}
	
	public static boolean isKey(Relation r, AttributeSet a, FDSet F){
		boolean isSK = isSuperKey(r, a, F);
		if(!isSK) return false;
		
		Set<AttributeSet> aPowerSet = a.powerSet();
		for (AttributeSet subset:aPowerSet) {
			if(subset.isEmpty()) continue;
			if(subset.equals(a)) continue;
			if(isSuperKey(r, subset, F)){
			//	System.out.println("the subset "+subset+" is superkey!");
				return false;
			}
		}
		return true;
	}
	
	public static Set<AttributeSet> findAllKeys_Greedy(Relation r, FDSet F){
		Set<AttributeSet> keys = new HashSet<AttributeSet>();
		Set<AttributeSet> aPowerSet = r.getAttributes().powerSet();
		for (AttributeSet subset:aPowerSet) {
			if(subset.isEmpty()) continue;
			if(isKey(r, subset, F)){
				keys.add(subset);
			}
		}
		return keys;
	}
	
	public static Set<AttributeSet> findAllKeys_LMR(Relation r, FDSet F){
		AttributeSet atts = r.getAttributes();
		Set<AttributeSet> keys = new HashSet<AttributeSet>();
		AttributeSet L = new AttributeSet();
		AttributeSet M = new AttributeSet();
		AttributeSet R = new AttributeSet();
		AttributeSet others = new AttributeSet();

		for(FD fd:F){
			for(Attribute la:fd.getLHS())
				L.add(la);			
			for(Attribute ra:fd.getRHS())
				R.add(ra);					
		}
		
		for(Attribute a:atts){
			if(L.contains(a) && R.contains(a)){
				M.add(a);
				L.remove(a);
				R.remove(a);
			}
		}
		
		for(Attribute a:atts){
			if(!L.contains(a) && !R.contains(a) && !M.contains(a)){
				others.add(a);
			}
		}
		
		M.addAll(others);
		
		Set<AttributeSet> mps = M.powerSet();
		List<AttributeSet> mplist = new ArrayList<AttributeSet>(mps);
		Collections.sort(mplist);		
		
		if(L.isEmpty()){
			for (int i = 0; i < mplist.size(); i++) {
				AttributeSet v = mplist.get(i);	
				boolean includekey = false;
				for(AttributeSet k:keys){
					if(v.containsAll(k)){
						includekey = true;
						break;
					}
				}				
				if(!includekey && isKey(r, v, F)){
					keys.add(v);
				}
			}
		} else {
			if(isKey(r, L, F)){
				keys.add(L);
			} else {				
				for (int i = 0; i < mplist.size(); i++) {
					AttributeSet ms = mplist.get(i);
					AttributeSet v = new AttributeSet(L, ms);	
					boolean includekey = false;
					for(AttributeSet k:keys){
						if(v.containsAll(k)){
							includekey = true;
							break;
						}
					}				
					if(!includekey && isKey(r, v, F)){
						keys.add(v);
					}
				}
			}
		}
		return keys;
	}
	
	//--------------------------------------------------------------------------
	
	public static FDSet project(FDSet F, Relation R, Relation R_){
		AttributeSet r_ = R_.getAttributes();
		Set<AttributeSet> r_ps = r_.powerSet();
		List<AttributeSet> list = new ArrayList<AttributeSet>(r_ps);
		Collections.sort(list);
		FDSet F_ = new FDSet();
		Set<AttributeSet> except = new HashSet<AttributeSet>();
		
		for (int i = 0; i < list.size(); i++) {
			AttributeSet a = list.get(i);
			boolean skip = false;
			for(AttributeSet ex:except){
				if(a.containsAll(ex)){
					skip = true;
					break;
				}					
			}
			if(skip) continue;
			AttributeSet aPlus = closure(a, F);
			if(aPlus.equals(R.getAttributes())){
				except.add(a);
			}
			aPlus.remove(a);
			for(Attribute b:aPlus){
				if(r_.contains(b)){
					AttributeSet bs = new AttributeSet();
					bs.add(b);					
					// derive new FDs from the closure
					FD fd = new FD(a, bs);
					F_.add(fd);
				}
			}			
		}
		return minimalCover(F_);
	}	
	
	public static boolean imply(FDSet F, FDSet G){
		for(FD fd:F){
			if(!hold(fd, G))
				return false;
		}
		return true;
	}
	
	public static boolean equivalent(FDSet F, FDSet G){
		return imply(F, G) && imply(G, F);
	}
	
	//--------------------------------------------------------------------------

	
	public static FDSet minimalCover(FDSet f){
		return reduceNoRedundantFDs(reduceNoExtraLHS(reduceSingltonRHS(f)));
	}
	
	
	//--------------------------------------------------------------------------
	
	public static FDSet reduceSingltonRHS(FDSet f){
		FDSet e = new FDSet();
		for(FD fd:f){
			AttributeSet rhs = fd.getRHS();
			if(rhs.size()>1){
				AttributeSet lhs = fd.getLHS();
				for(Attribute x:rhs){
					AttributeSet x_ = new AttributeSet(); 	x_.add(x);
					FD fd_ = new FD(lhs, x_);
					if(!fd_.isTrivial()){
						e.add(fd_);
					}
				}
			} else {
				e.add(fd);
			}
		}
		return e;
	}

	public static FDSet reduceNoExtraLHS(FDSet f){
		FDSet e = new FDSet();
		for(FD fd:f){
			AttributeSet lhs = fd.getLHS();
			if(lhs.size()>1){
				FD fd_ = reduceLHS(fd, f);
				while(!fd.equals(fd_)){
					fd = fd_;
					fd_ = reduceLHS(fd, f);					
				}
				e.add(fd_);				
			} else {
				e.add(fd);
			}
		}
		return e;
	}
	
	public static FD reduceLHS(FD fd, FDSet f){		
		AttributeSet lhs = fd.getLHS();
		if(lhs.size()>1){
			List<Attribute> llhs = new ArrayList<Attribute>(lhs);
			try {
				int i = 0;
				while(i<llhs.size() ){
					Attribute a = llhs.get(i);					
					if(isExtraLHS(a, fd, f)){
						AttributeSet lhs_a = new AttributeSet(lhs);
						lhs_a.remove(a);
						FD fd_ = new FD(lhs_a, fd.getRHS());
						return fd_;
					} else {
						i++;
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}	
			return fd;
		} else {
			return fd;
		}
	}
		
	public static boolean isExtraLHS(Attribute a, FD fd, FDSet f) throws Exception{
		AttributeSet lhs = fd.getLHS();
		if(lhs.size()==1) throw new Exception("LHS of "+fd+" is singlton!");
		if(!lhs.contains(a)) throw new Exception("the attriute "+a+" is not in the LHS of "+fd);
		
		AttributeSet lhs_a = new AttributeSet(lhs);
		lhs_a.remove(a);
		AttributeSet lhs_aPlus = closure(lhs_a, f); 		
		return lhs_aPlus.contains(a);
	}
	
	public static FDSet reduceNoRedundantFDs(FDSet f){
		FDSet e = new FDSet(f);
		for(FD fd:f){
			if(isRedundantFD(fd, e)){
				e.remove(fd);
			}
		}
		return e;
	}
	
	public static boolean isRedundantFD(FD fd, FDSet f){
		FDSet f_fd = new FDSet(f);
		f_fd.remove(fd);		
		return hold(fd, f_fd);
	}
	
	//--------------------------------------------------------------------------

	
	
	//--------------------------------------------------------------------------
	
	public static FDSet allNonTrivialFDs(Relation r){ // Singleton RHS
		AttributeSet a = r.getAttributes();
		FDSet fds = new FDSet();
		Set<AttributeSet> lhss = a.powerSet();		
		for(AttributeSet lhs:lhss){
			if(lhs.isEmpty()) continue;
			if(lhs.equals(a)) continue;
			for(Attribute rhsa: a){				
				if(lhs.contains(rhsa)) continue; // trivial
				FD fd = new FD(lhs, rhsa);
				fds.add(fd);
			}		
		}		
		return fds;
	}
	
}

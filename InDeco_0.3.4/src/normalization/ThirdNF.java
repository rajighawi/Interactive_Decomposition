package normalization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fd.Attribute;
import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class ThirdNF extends Normalizer {

	public ThirdNF(Relation relation, FDSet fdSet) {
		super(_3NF, relation, fdSet);
	}
	
	@Override
	public boolean check() {
		for(FD fd:fdSet){
			AttributeSet lhs = fd.getLHS();
			if(!FDUtility.isSuperKey(relation, lhs, fdSet)){
				AttributeSet rhs = fd.getRHS();	
				for(Attribute x:rhs){
					if(!primes.contains(x)){
						return false;
					}
				}				
			}
		}
		return true;
	}

	@Override
	public String explain() {
		String s = "The keys of `" + table + "` are: " + keys +"\n";
		s += "The   prime   attributes are: " + primes + "\n";
		s += "The non-prime attributes are: " + nonprimes + "\n";
		
		boolean _3nf = true;
		for(FD fd:fdSet){
			AttributeSet lhs = fd.getLHS();
			if(!FDUtility.isSuperKey(relation, lhs, fdSet)){
				AttributeSet rhs = fd.getRHS();	
				for(Attribute x:rhs){
					s += "LHS of FD: " + fd + " is NOT a super key of the relation `" + relation.getName() + "`\n";
					if(!primes.contains(x)){
						_3nf = false;						
						s += "and RHS has a non-prime attribute `"+x + "`\n\n";
					} else {
						s += "but RHS has a prime attribute `" + x + "`\n";
					}
				}				
			} else {
				s += "LHS of FD: " + fd + " is a super key of the relation `" + relation.getName() + "`\n\n";
			}
		}		
		s += "Thus, the relation `"+relation.getName()+"` w.r.t FDSet: "+fdSet.getName()+" is " +(_3nf?"":"NOT ") + "in 3NF";
		return s;
	}
	
	/*
	// Bernsteins Synthesis
	@Override
	public Set<Relation> decompose() {
		FDSet H = FDUtility.minimalCover(fdSet);
		 
		// Step 3: Partition H into groups such that all of the FDs in each group have identical left sides.
		Set<Set<FD>> partition = new HashSet<Set<FD>>();
		Map<AttributeSet, Set<FD>> map = new HashMap<AttributeSet, Set<FD>>();
		for(FD fd: H){
			AttributeSet lhs = fd.getLHS();
			if(map.containsKey(lhs)){
				map.get(lhs).add(fd);
			} else {
				Set<FD> dd = new HashSet<FD>();
				dd.add(fd);
				map.put(lhs, dd);
			}
		}
		
		Set<FD> J = new HashSet<FD>();
		ArrayList<AttributeSet> lhss = new ArrayList<AttributeSet>(map.keySet());
		for (int i = 0; i < lhss.size(); i++) {
			for (int j = i+1; j < lhss.size(); j++) {
				AttributeSet x = lhss.get(i);
				AttributeSet y = lhss.get(j);
				FD xy = new FD(x,y);
				FD yx = new FD(y,x);
				if(FDUtility.hold(xy, H) && FDUtility.hold(yx, H)){ // x <-> y
					Set<FD> H12 = new HashSet<FD>();
					H12.addAll(map.get(x));
					H12.addAll(map.get(y));
					
					J.add(xy);
					J.add(yx);
					
					for(Attribute a: y) 
						H.remove(new FD(x, a)); // if H contains (x -> a), then delete from H					
					for(Attribute b:x)
						H.remove(new FD(y, b));
					
					
				}
				
			}
		}
		
		Map<Relation, AttributeSet> keyMap = new HashMap<Relation, AttributeSet>();
		Set<FDSet> subs = new HashSet<FDSet>();
		int idx = 1;
		Set<Relation> setR = new HashSet<Relation>();
		for(FD fd : G){
			AttributeSet x = new AttributeSet();
			x.addAll(fd.getLHS());
			x.addAll(fd.getRHS());
			Relation r_ = new Relation(relation.getName()+""+idx, x);
			FDSet f_ = new FDSet("fds_"+r_.getName()+"_0");
			f_.add(fd);	
			keyMap.put(r_, fd.getLHS());
			r_.addFDSet(f_);
			setR.add(r_);
			subs.add(f_);				
			idx++;
		}		
		boolean keyInSub = false;
		for(Relation rf:setR){
			AttributeSet atts = rf.getAttributes();
			for(AttributeSet key:keys){
				if(atts.containsAll(key)){
					keyInSub = true;
					break;
				}
			}
			if(keyInSub) break;
		}
		if(!keyInSub){
			for(AttributeSet key:keys){
				Relation keyRel = new Relation(table+"_k", key);
				FDSet keyRelFDs = new FDSet("fds_"+keyRel.getName()+"_0");
				try {
					keyRel.addFDSet(keyRelFDs);
					setR.add(keyRel);
					break;
				} catch (Exception e) {					
					e.printStackTrace();
				}				
			}
		}		
		return setR;
	}
	*/
	
	
	// Bernsteins Synthesis
	@Override
	public Set<Relation> decompose() {
		FDSet E = FDUtility.minimalCover(fdSet);
		Set<AttributeSet> lhsS = new HashSet<AttributeSet>();
		for(FD fd:E){
			lhsS.add(fd.getLHS());
		}
		FDSet G = new FDSet();
		for(AttributeSet lhs:lhsS){
			AttributeSet rhs = new AttributeSet();
			for(FD fd:E){
				if(fd.getLHS().equals(lhs)){
					rhs.addAll(fd.getRHS());
				}
			}
			FD fd_ = new FD(lhs, rhs);
			G.add(fd_);
		}
		
		Map<Relation, AttributeSet> keyMap = new HashMap<Relation, AttributeSet>();
		Set<FDSet> subs = new HashSet<FDSet>();
		int idx = 1;
		Set<Relation> setR = new HashSet<Relation>();
		for(FD fd : G){
			AttributeSet x = new AttributeSet();
			x.addAll(fd.getLHS());
			x.addAll(fd.getRHS());
			Relation r_ = new Relation(relation.getName()+""+idx, x);
			FDSet f_ = new FDSet("fds_"+r_.getName()+"_0");
			f_.add(fd);	
			keyMap.put(r_, fd.getLHS());
			r_.addFDSet(f_);
			setR.add(r_);
			subs.add(f_);				
			idx++;
		}		
		boolean keyInSub = false;
		for(Relation rf:setR){
			AttributeSet atts = rf.getAttributes();
			for(AttributeSet key:keys){
				if(atts.containsAll(key)){
					keyInSub = true;
					break;
				}
			}
			if(keyInSub) break;
		}
		if(!keyInSub){
			for(AttributeSet key:keys){
				Relation keyRel = new Relation(table+"_k", key);
				FDSet keyRelFDs = new FDSet("fds_"+keyRel.getName()+"_0");
				try {
					keyRel.addFDSet(keyRelFDs);
					setR.add(keyRel);
					break;
				} catch (Exception e) {					
					e.printStackTrace();
				}				
			}
		}		
		return setR;
	}
	
	
	
	/*
	// Bernsteins Synthesis
	//@Override
	public Set<Relation> decompose_old() {
		//** Step 1: Find the Minimal Cover * /
		FDSet E = FDUtility.minimalCover(fdSet);
		// regroup by LHS, union RHS;	e.g. {A->B, A->C} ==> {A->BC} 
		Set<AttributeSet> lhsS = new HashSet<AttributeSet>();
		for(FD fd:E){
			lhsS.add(fd.getLHS());
		}
		FDSet G = new FDSet();
		for(AttributeSet lhs:lhsS){
			AttributeSet rhs = new AttributeSet();
			for(FD fd:E){
				if(fd.getLHS().equals(lhs)){
					rhs.addAll(fd.getRHS());
				}
			}
			FD fd_ = new FD(lhs, rhs);
			G.add(fd_);
		}

		//** Step 2: Create a relation for each FD * /
		Map<Relation, AttributeSet> keyMap = new HashMap<Relation, AttributeSet>();
		Set<FDSet> subs = new HashSet<FDSet>();
		int i = 1;
		ArrayList<Relation> relList = new ArrayList<Relation>();
		for(FD fd : G){
			AttributeSet x = new AttributeSet();
			x.addAll(fd.getLHS());
			x.addAll(fd.getRHS());
			Relation r_ = new Relation(relation.getName()+""+i, x);
			FDSet f_ = new FDSet("fds_"+r_.getName()+"_0");
			f_.add(fd);	
			keyMap.put(r_, fd.getLHS());
			r_.addFDSet(f_);
			relList.add(r_);
			subs.add(f_);				
			i++;
		}		


		//** Step 3: It is never necessary to use a relation which is a proper subset of another relation,  so we can drop it* /		
		Collections.sort(relList, new MyRelComparator());
		Set<Integer> relIdxDel = new HashSet<Integer>(); // indices of relations to be removed
		for (int j = 0; j < relList.size(); j++) {
			Relation Rj = relList.get(j);
			AttributeSet asj = Rj.getAttributes();
			for (int k = 0; k < relList.size(); k++) {
				if(k==j) continue;
				Relation Rk = relList.get(k);
				AttributeSet ask = Rk.getAttributes();	

				if(ask.containsAll(asj)){ // Rj is subset of Rk, so merge Rj into Rk
					// mark j as `to be removed` 
					relIdxDel.add(j);
					// add FDs of Rj to Rk
					FDSet y = Rj.getFdSets().get(0);
					y = new FDSet(y);
					//y.setRelation(Rk);
					Rk.getFdSets().get(0).addAll(y);					
				}				
			}
		}		
		Set<Relation> setR = new HashSet<Relation>();
		for (int j = 0; j < relList.size(); j++) {
			if(!relIdxDel.contains(j)){
				setR.add(relList.get(j));
			}
		}				

		//** Finally, check whether the key is contained in some relation * /

		boolean keyInSub = false;
		//for(FDSet rf:subs){
		for(Relation rf:setR){
			AttributeSet atts = rf.getAttributes();
			for(AttributeSet key:keys){
				if(atts.containsAll(key)){
					keyInSub = true;
					break;
				}
			}
			if(keyInSub) break;
		}
		/* * If not, add a relation that contains the key* /
		if(!keyInSub){
			//int j = 1;
			for(AttributeSet key:keys){
				Relation keyRel = new Relation(table+"_key", key);
				FDSet keyRelFDs = new FDSet("fds_"+keyRel.getName()+"_0");
				try {
					keyRel.addFDSet(keyRelFDs);
					setR.add(keyRel);
					//j++;
					break;
				} catch (Exception e) {					
					e.printStackTrace();
				}				
			}
		}		
		return setR;
	}


	/*
	// Bernsteins Synthesis
	@Override
	public Set<Relation> decompose() {
		
		/* * Step 1: Find the Minimal Cover * /
		FDSet E = FDUtility.minimalCover(fdSet);
		// regroup by LHS, union RHS;	e.g. {A->B, A->C} ==> {A->BC} 
		Set<AttributeSet> lhsS = new HashSet<AttributeSet>();
		for(FD fd:E){
			lhsS.add(fd.getLHS());
		}
		FDSet G = new FDSet();
		for(AttributeSet lhs:lhsS){
			AttributeSet rhs = new AttributeSet();
			for(FD fd:E){
				if(fd.getLHS().equals(lhs)){
					rhs.addAll(fd.getRHS());
				}
			}
			FD fd_ = new FD(lhs, rhs);
			G.add(fd_);
		}
		
		/* * Step 2: Create a relation for each FD * /
		Map<Relation, AttributeSet> keyMap = new HashMap<Relation, AttributeSet>();
		Set<FDSet> subs = new HashSet<FDSet>();
		int idx = 1;
		ArrayList<Relation> relList = new ArrayList<Relation>();
		for(FD fd : G){
			AttributeSet x = new AttributeSet();
			x.addAll(fd.getLHS());
			x.addAll(fd.getRHS());
			Relation r_ = new Relation(relation.getName()+""+idx, x);
			FDSet f_ = new FDSet("fds_"+r_.getName()+"_0");
			f_.add(fd);	
			keyMap.put(r_, fd.getLHS());
			r_.addFDSet(f_);
			relList.add(r_);
			subs.add(f_);				
			idx++;
		}		
		Collections.sort(relList, new MyRelComparator());
		
		
		/* * Step 3: If some relations have equivalent keys, merge them* / 
		Map<Integer, Set<Integer>> intmap = new HashMap<Integer, Set<Integer>>();
		for (int i = 0; i < relList.size(); i++) {
			Set<Integer> si = new HashSet<Integer>();
			si.add(i);
			intmap.put(i, si);
		}		
		for (int j = 0; j < relList.size()-1; j++) {
			Relation Rj = relList.get(j);
			AttributeSet KeyRj = keyMap.get(Rj);
			for (int k = j+1; k < relList.size(); k++) {
				Relation Rk = relList.get(k);
				AttributeSet KeyRk = keyMap.get(Rk);
				FD fjk = new FD(KeyRj, KeyRk);
				FD fkj = new FD(KeyRk, KeyRj);
				if(FDUtility.hold(fjk, fdSet) && FDUtility.hold(fkj, fdSet)){
					intmap.get(j).add(k);
					intmap.get(k).add(j);
				}
			}			
		}
		
		Set<Relation> setR = new HashSet<Relation>();
		Set<Integer> processed = new HashSet<Integer>();
		Set<Set<Integer>> groups = new HashSet<Set<Integer>>(intmap.values());
		int g = 0;
		for(Set<Integer> gr: groups){
			if(gr.size()<2) continue;
			AttributeSet attributes = new AttributeSet(); 
			FDSet f_g = new FDSet();
			for (int i : gr) {
				Relation ri = relList.get(i);
				attributes.addAll(ri.getAttributes());
				f_g.addAll(ri.getFdSets().get(0)); 
				processed.add(i);
			}
			Relation r_g = new Relation(relation.getName()+"__"+g, attributes);
			r_g.addFDSet(f_g);	
			setR.add(r_g);
			g++;
		}
			
		for (int i = 0; i < relList.size(); i++) {
			if(!processed.contains(i)){
				setR.add(relList.get(i));
			}
		}
		
		int j = 1;
		for(Relation rf:setR){
			String tn = relation.getName()+"_"+j;
			rf.rename(tn);
			rf.getFdSets().get(0).setName("fds_"+tn+"_0");
			j++;
		}
		
		/* * Finally, check whether the key is contained in some relation * /
		
		boolean keyInSub = false;
		//for(FDSet rf:subs){
		for(Relation rf:setR){
			AttributeSet atts = rf.getAttributes();
			for(AttributeSet key:keys){
				if(atts.containsAll(key)){
					keyInSub = true;
					break;
				}
			}
			if(keyInSub) break;
		}
		/* * If not, add a relation that contains the key* /
		if(!keyInSub){
			for(AttributeSet key:keys){
				Relation keyRel = new Relation(table+"_k", key);
				FDSet keyRelFDs = new FDSet("fds_"+keyRel.getName()+"_0");
				try {
					keyRel.addFDSet(keyRelFDs);
					setR.add(keyRel);
					break;
				} catch (Exception e) {					
					e.printStackTrace();
				}				
			}
		}		
		return setR;
	}
	*/
}




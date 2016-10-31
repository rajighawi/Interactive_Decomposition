package fd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashBasedTable;

public class Decomposition {

	Relation relation;	
	FDSet fdSet;
	ArrayList<Relation> subrelations;
	String name;

	public Decomposition(Relation relation, FDSet fdSet, String name){
		this.relation = relation;	
		this.fdSet = fdSet;
		this.name = name;
		this.subrelations = new ArrayList<Relation>();
	}

	public int size(){
		return subrelations.size();
	}

	public Relation getRelation() {
		return relation;
	}

	public FDSet getFDSet() {
		return fdSet;
	}

	public ArrayList<Relation> getSubrelations() {
		return subrelations;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	//-------------------------------------
	public void add(Relation sub){
		subrelations.add(sub);
	}

	public void addAll(Set<Relation> subs){
		subrelations.addAll(subs);
	}

	public void addAll(Decomposition dd){
		subrelations.addAll(dd.getSubrelations());
	}

	public Set<AttributeSet> pool(){
		Set<AttributeSet> pool = new HashSet<AttributeSet>();
		for(Relation r: subrelations){
			pool.add(r.getAttributes());
		}
		return pool;
	}
	
	public boolean checkFDsPreservation_naive(){
		FDSet G = new FDSet();
		for(Relation ri:subrelations){
			G.addAll(FDUtility.project(fdSet, relation, ri)); // perhaps F+ should be used instead of F
			// G.addAll(ri.getFdSets().get(0));
		}		
		return FDUtility.checkEquivalence(fdSet, G);		
	}

	public Map<FD, Boolean> checkFDsPreservation(){	
		Map<FD, Boolean> map = new HashMap<FD, Boolean>();
		boolean allvalid = true;
		for(FD fd:fdSet){			
			//System.out.println(fd);
			boolean valid = false;
			AttributeSet Z = new AttributeSet(fd.getLHS());	// Z := X
			AttributeSet rhs = fd.getRHS();
			//System.out.println("Z = "+Z);
			boolean change = true;
			while(change){
				change = false;
				
				for (int i = 0; i < subrelations.size(); i++) {
					Relation ri = subrelations.get(i);
					//System.out.print(ri+"\t");
					AttributeSet Ri = ri.getAttributes();
					AttributeSet Z_ = FDUtility.R_operation(Z, Ri, fdSet);
					//System.out.print("Z  = " + Z+"\t");
					//System.out.print("Z' = " + Z_+"\t");
					if(!Z_.equals(Z)){
						change = true;
						Z = Z_;
						//System.out.println("change");						
					} 
				//	else System.out.println();
					if(Z.containsAll(rhs)){
						valid = true;
						//System.out.println("valid");
						break;
					}
				}
				if(valid) break;
			}
			allvalid = allvalid && valid;
			map.put(fd, valid);
			if(!valid){
				//System.out.println("FD "+fd+" IS NOT preserved!");
			} else {
				//System.out.println("FD "+fd+" IS preserved!");	
			}
		}
		return map;
	}
	
	public ArrayList<ChasePack> chase(){
		ArrayList<ChasePack> packs = new ArrayList<ChasePack>();
		FDSet fdSet_ = FDUtility.reduceSingltonRHS(fdSet);
		ArrayList<Relation> rs = new ArrayList<Relation>(subrelations);
		Collections.sort(rs);		
		ArrayList<Attribute> aa = new ArrayList<Attribute>(relation.getAttributes());
		Collections.sort(aa);
		// initial tableau
		HashBasedTable<Relation, Attribute, Symbol> tableau = HashBasedTable.create(); // <Ri, A, ai|bij>
		for (int i = 0; i < rs.size(); i++) {
			Relation ri = rs.get(i);
			for (int j = 0; j < aa.size(); j++) {
				Attribute a = aa.get(j);			
				String a_ = a.getName().toLowerCase();
				if(ri.getAttributes().contains(a)){
					tableau.put(ri, a, new Symbol(a_, false));					
				} else {
					tableau.put(ri, a, new Symbol(a_+(i+1), true));
				}
			}
		}

		HashBasedTable<Relation, Attribute, Symbol> tableau0 = HashBasedTable.create(tableau);
		ChasePack pack0 = new ChasePack("Initial tableau", tableau0, "", "Initial tableau");
		packs.add(pack0);

		String msg1 = "";

		// applying FDs

		boolean change = true;
		while(change){

			change = false;
			
			for(FD fd:fdSet_){
				msg1 = "Apply "+fd.toString();

				AttributeSet lhs = fd.getLHS();
				ArrayList<Attribute> lhsL = new ArrayList<Attribute>(lhs);
				Collections.sort(lhsL);			
				Attribute rhs = new ArrayList<Attribute>(fd.getRHS()).get(0);
				Map<Integer, Set<Integer>> intmap = new HashMap<Integer, Set<Integer>>();
				for (int i = 0; i < rs.size(); i++) {
					Set<Integer> si = new HashSet<Integer>();
					si.add(i);
					intmap.put(i, si);
				}
				for (int i = 0; i < rs.size()-1; i++) {
					Relation ri = rs.get(i);
					Map<Attribute, Symbol> rowi = tableau.row(ri);
					String[] lArri = fragment(rowi, lhsL);				
					for (int j = i+1; j < rs.size(); j++) {
						Relation rj = rs.get(j);
						Map<Attribute, Symbol> rowj = tableau.row(rj);
						String[] lArrj = fragment(rowj, lhsL);
						if(Arrays.equals(lArri, lArrj)){ // lhs parts are equal
							msg1   += "\nrows "+i+" and "+j+" have equal values for LHS: "+lhs;
							intmap.get(i).add(j);
							intmap.get(j).add(i);						
						}					
					}				
				}

				// group eqs			
				Set<Set<Integer>> groups = new HashSet<Set<Integer>>(intmap.values());
				for(Set<Integer> gr: groups){
					if(gr.size()<2) continue;				
					Set<Symbol> distinctRHS = new HashSet<Symbol>();
					boolean unsub = false;
					Symbol unsubSym = null;
					Symbol subSym = null;
					for (int i : gr) {
						Relation ri = rs.get(i);
						Map<Attribute, Symbol> rowi = tableau.row(ri);								
						Symbol rArri = rowi.get(rhs);
						distinctRHS.add(rArri);
						if(!rArri.subscriped){
							unsub = true;
							unsubSym = rArri;
						} else {
							subSym = rArri;
						}
					}
					if(distinctRHS.size()>1){ // rhs are different, a change is needed
						for (int i : gr) {
							Relation ri = rs.get(i);
							tableau.put(ri, rhs, unsub?unsubSym:subSym);					
						}	
						change = true;
					}					
				}		

				// check complete row
				for (int i = 0; i < rs.size(); i++) {
					Relation ri = rs.get(i);
					boolean complete = true;
					Map<Attribute, Symbol> rowi = tableau.row(ri);
					for(Symbol ss:rowi.values()){
						if(ss.subscriped){
							complete = false;
							break;
						}
					}
					if(complete){
						HashBasedTable<Relation, Attribute, Symbol> tableau_ = HashBasedTable.create(tableau);
						ChasePack cp = new ChasePack(msg1, tableau_, "", fd.toString());
						packs.add(cp);
						
						String msg1_ = "Line "+i+" is complete (all symbols are unsubscriped)\n";
						String msg2_ = "Decomposition IS Join-Lossless";
						ChasePack cp_ = new ChasePack(msg1_, tableau, msg2_, "Success");
						packs.add(cp_);
						return packs;
					}
				}				
				HashBasedTable<Relation, Attribute, Symbol> tableau_ = HashBasedTable.create(tableau);
				ChasePack cp = new ChasePack(msg1, tableau_, change?"":"Tableau is not changed!", fd.toString());
				packs.add(cp);				
			}
		}
		String msg2 = "All FDs have been checked. No more changes!\n";
		msg2 += "Decomposition IS NOT Join-Lossless";
		HashBasedTable<Relation, Attribute, Symbol> tableauF = HashBasedTable.create(tableau);
		ChasePack cp = new ChasePack(msg1, tableauF, msg2, "Failure");
		packs.add(cp);
		return packs;
	}
	
	public boolean is_lossless(){
		FDSet fdSet_ = FDUtility.reduceSingltonRHS(fdSet);
		ArrayList<Relation> rs = new ArrayList<Relation>(subrelations);
		Collections.sort(rs);		
		ArrayList<Attribute> aa = new ArrayList<Attribute>(relation.getAttributes());
		Collections.sort(aa);
		// initial tableau
		HashBasedTable<Relation, Attribute, Symbol> tableau = HashBasedTable.create(); // <Ri, A, ai|bij>
		for (int i = 0; i < rs.size(); i++) {
			Relation ri = rs.get(i);
			for (int j = 0; j < aa.size(); j++) {
				Attribute a = aa.get(j);			
				String a_ = a.getName().toLowerCase();
				if(ri.getAttributes().contains(a)){
					tableau.put(ri, a, new Symbol(a_, false));					
				} else {
					tableau.put(ri, a, new Symbol(a_+(i+1), true));
				}
			}
		}

		boolean change = true;
		while(change){
			change = false;			
			for(FD fd:fdSet_){
				AttributeSet lhs = fd.getLHS();
				ArrayList<Attribute> lhsL = new ArrayList<Attribute>(lhs);
				Collections.sort(lhsL);			
				Attribute rhs = new ArrayList<Attribute>(fd.getRHS()).get(0);
				Map<Integer, Set<Integer>> intmap = new HashMap<Integer, Set<Integer>>();
				for (int i = 0; i < rs.size(); i++) {
					Set<Integer> si = new HashSet<Integer>();
					si.add(i);
					intmap.put(i, si);
				}
				for (int i = 0; i < rs.size()-1; i++) {
					Relation ri = rs.get(i);
					Map<Attribute, Symbol> rowi = tableau.row(ri);
					String[] lArri = fragment(rowi, lhsL);				
					for (int j = i+1; j < rs.size(); j++) {
						Relation rj = rs.get(j);
						Map<Attribute, Symbol> rowj = tableau.row(rj);
						String[] lArrj = fragment(rowj, lhsL);
						if(Arrays.equals(lArri, lArrj)){ // lhs parts are equal
							intmap.get(i).add(j);
							intmap.get(j).add(i);						
						}					
					}				
				}
				// group eqs			
				Set<Set<Integer>> groups = new HashSet<Set<Integer>>(intmap.values());
				for(Set<Integer> gr: groups){
					if(gr.size()<2) continue;				
					Set<Symbol> distinctRHS = new HashSet<Symbol>();
					boolean unsub = false;
					Symbol unsubSym = null;
					Symbol subSym = null;
					for (int i : gr) {
						Relation ri = rs.get(i);
						Map<Attribute, Symbol> rowi = tableau.row(ri);								
						Symbol rArri = rowi.get(rhs);
						distinctRHS.add(rArri);
						if(!rArri.subscriped){
							unsub = true;
							unsubSym = rArri;
						} else {
							subSym = rArri;
						}
					}
					if(distinctRHS.size()>1){ // rhs are different, a change is needed
						for (int i : gr) {
							Relation ri = rs.get(i);
							tableau.put(ri, rhs, unsub?unsubSym:subSym);					
						}	
						change = true;
					}					
				}
				// check complete row
				for (int i = 0; i < rs.size(); i++) {
					Relation ri = rs.get(i);
					boolean complete = true;
					Map<Attribute, Symbol> rowi = tableau.row(ri);
					for(Symbol ss:rowi.values()){
						if(ss.subscriped){
							complete = false;
							break;
						}
					}
					if(complete){
						return true;
					}
				}			
			}
		}
		return false;
	}

	public String[] fragment(Map<Attribute, Symbol> row, ArrayList<Attribute> hsL){
		String[] arr = new String[hsL.size()];
		for (int k = 0; k < hsL.size(); k++) {
			Attribute ak = hsL.get(k);
			arr[k] = row.get(ak).getStr();
		}
		return arr;
	}
	
	public HashBasedTable<Relation, Attribute, Boolean> makeTableau(){ 
		HashBasedTable<Relation, Attribute, Boolean> tableau = HashBasedTable.create();
		ArrayList<Attribute> mas = new ArrayList<Attribute>(relation.getAttributes());
		Collections.sort(mas);
		Collections.sort(subrelations);		
		for (int i = 0; i < subrelations.size(); i++) {
			Relation sub = subrelations.get(i);
			for (int j = 0; j < mas.size(); j++) {
				Attribute a = mas.get(j);
				tableau.put(sub, a, sub.getAttributes().contains(a));
			}
		}
		return tableau;
	}

	public static String printTableau(HashBasedTable<Relation, Attribute, Symbol> tableau){
		Set<Attribute> ass = tableau.columnKeySet();		
		ArrayList<Attribute> as = new ArrayList<Attribute>(ass);
		Collections.sort(as);
		Set<Relation> ras = tableau.rowKeySet();
		ArrayList<Relation> rs = new ArrayList<Relation>(ras);
		Collections.sort(rs);
		String s = "";
		String l1 = "", l2 = "";
		for (int i = 0; i < as.size(); i++) {
			l1 += as.get(i)+"\t";
			l2 += "========";
		}
		s += l1+"\n";
		s += l2+"\n";
		for (int i = 0; i < rs.size(); i++) {
			Relation ri = rs.get(i);
			for (int j = 0; j < as.size(); j++) {
				Attribute a = as.get(j);
				s += tableau.get(ri, a)+"\t";
			}
			s += "\n";
		}
		return s;
	}

	@Override
	public String toString() {
		String s = "Decomposition [origin=" + relation + "]\n";
		for(Relation r: subrelations){
			s += r.toString()+"\n";
			ArrayList<FDSet> fdSets = r.getFdSets(); 
			s += fdSets.size()>0?r.getFdSets().get(0).toString():"{ }";
			s += "\n\n";
		}

		return s;
	}

}


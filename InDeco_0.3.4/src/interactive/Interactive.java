package interactive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashBasedTable;

import normalization.BCNF;
import normalization.SecondNF;
import normalization.ThirdNF;
import fd.Attribute;
import fd.AttributeSet;
import fd.Decomposition;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;

public class Interactive {

	public static int[] defaultWeights = {25, 25, 20, 20};

	public static ArrayList<Recommendation> recommendations(Decomposition deco){
		ArrayList<Recommendation> recommendations = new ArrayList<Recommendation>();		
		Relation motherRelation	 = deco.getRelation();
		FDSet motherFDSet		 = deco.getFDSet();		
		ArrayList<Relation> rls  = deco.getSubrelations();
		Collections.sort(rls);		
		AttributeSet atts = motherRelation.getAttributes();
		
		int n = 0;
		for(Relation rs:rls){
			String rf = rs.getName();
			String rf_ = rf.substring(rf.lastIndexOf("_")+1);
			int ix = Integer.parseInt(rf_);	//int n = relations.size();
			if(ix>n) n=ix;
		}
				
		// Recommendations type: Add or remove an attribute
		for (int i = 0; i < rls.size(); i++) {
			Relation sub = rls.get(i);
			AttributeSet subatts = sub.getAttributes();
			AttributeSet diff = atts.minus(subatts);

			// Add attribute recommendations
			if(diff.size()>1){ // if |diff|=1, then adding the last att will let the sub equals the mother!
				for(Attribute a:diff){
					AttributeSet nas = new AttributeSet(subatts);
					nas.add(a);					
					String action = "Add attribute `"+a.getName()+"` to subscheme `" + sub.getName() + "`" ;					
					Relation sub_i = new Relation(sub.getName(), nas);
					FDSet f_i = FDUtility.project(motherFDSet, motherRelation, sub_i);
					sub_i.addFDSet(f_i);					
					Decomposition newDeco = new Decomposition(motherRelation, motherFDSet, "");
					newDeco.add(sub_i);					
					for (int j = 0; j < rls.size(); j++) {
						if(i==j) continue;
						newDeco.add(rls.get(j));						
					}					
					Recommendation rec = new Recommendation(action, newDeco);
					System.out.println(rec);
					recommendations.add(rec);
				}				
			}			

			// Remove attribute recommendations
			if(subatts.size()>2){
				for(Attribute a:subatts){
					AttributeSet nas = new AttributeSet(subatts);
					nas.remove(a);					
					String action = "Remove attribute `"+a.getName()+"` from subscheme `" + sub.getName() + "`" ;					
					Relation sub_i = new Relation(sub.getName(), nas);
					FDSet f_i = FDUtility.project(motherFDSet, motherRelation, sub_i);
					sub_i.addFDSet(f_i);					
					Decomposition newDeco = new Decomposition(motherRelation, motherFDSet, "");
					newDeco.add(sub_i);					
					for (int j = 0; j < rls.size(); j++) {
						if(i==j) continue;
						newDeco.add(rls.get(j));						
					}					
					Recommendation rec = new Recommendation(action, newDeco);
					System.out.println(rec);
					recommendations.add(rec);
				}				
			}			
		}

		// Recommendations type: Remove a relation
		if(rls.size()>2){
			for (int i = 0; i < rls.size(); i++) {
				Relation sub_i = rls.get(i);
				String action = "Remove subscheme: "+ sub_i.getName() + "`" ;					

				Decomposition newDeco = new Decomposition(motherRelation, motherFDSet, "");
				for (int j = 0; j < rls.size(); j++) {
					if(i!=j) {
						newDeco.add(rls.get(j));						
					}
				}					
				Recommendation rec = new Recommendation(action, newDeco);
				System.out.println(rec);
				recommendations.add(rec);
			}
		}
		
		// Recommendations type: Add a relation (key)
		Set<AttributeSet> keys = FDUtility.findAllKeys_LMR(motherRelation, motherFDSet);
		for(AttributeSet key:keys){
			boolean mentioned = false;
			for (int i = 0; i < rls.size(); i++) {
				Relation sub_i = rls.get(i);									
				if(sub_i.getAttributes().equals(key)){
					mentioned = true;
					break;
				}				
			}
			if(!mentioned){
				Decomposition newDeco = new Decomposition(motherRelation, motherFDSet, "");
				String action = "Add new subscheme: "+ key.toString() + "" ;
				Relation rkey = new Relation(motherRelation.getName()+"_"+(n+1), key);
				rkey.addFDSet(new FDSet());
				newDeco.add(rkey);
				for (int j = 0; j < rls.size(); j++) {
					newDeco.add(rls.get(j));					
				}					
				Recommendation rec = new Recommendation(action, newDeco);
				System.out.println(rec);
				recommendations.add(rec);
			}			
		}

		// Recommendations type: Add a relation (FD)
		FDSet ff = FDUtility.minimalCover(motherFDSet);
		for(FD fd:ff){
			boolean mentioned = false;
			AttributeSet as = new AttributeSet();
			as.addAll(fd.getLHS()); as.addAll(fd.getRHS());
			for (int i = 0; i < rls.size(); i++) {
				Relation sub_i = rls.get(i);									
				if(sub_i.getAttributes().equals(as)){
					mentioned = true;
					break;
				}				
			}
			if(!mentioned){
				Decomposition newDeco = new Decomposition(motherRelation, motherFDSet, "");
				String action = "Add new subscheme: "+ as.toString() + "" ;
				Relation rfd = new Relation(motherRelation.getName()+"_"+(n+1), as);
				rfd.addFDSet(FDUtility.project(motherFDSet, motherRelation, rfd));
				newDeco.add(rfd);
				for (int j = 0; j < rls.size(); j++) {
					newDeco.add(rls.get(j));					
				}					
				Recommendation rec = new Recommendation(action, newDeco);
				System.out.println(rec);
				recommendations.add(rec);
			}			
		}

		Collections.sort(recommendations);
		return recommendations;
	}

	public static float[] scores(Decomposition deco, boolean partDP){

		ArrayList<Relation> rls = deco.getSubrelations();
		Collections.sort(rls);
		int sumNfScore = 0;
		
		for (int i = 0; i < rls.size(); i++) {
			Relation sub = rls.get(i);
			FDSet f_ = sub.getFdSets().get(0);
			SecondNF _2nf = new SecondNF(sub, f_);
			ThirdNF _3nf = new ThirdNF(sub, f_);
			BCNF bcnf = new BCNF(sub, f_);
			boolean in2NF = _2nf.check();
			boolean in3NF = _3nf.check();
			boolean inBCNF = bcnf.check();			
			int nfScore = inBCNF?3:(in3NF?2:(in2NF?1:0));
			sumNfScore += nfScore;
		}		
		boolean lossless = deco.is_lossless();
		
		float dpScore = 0;
		Map<FD, Boolean> fdmap = deco.checkFDsPreservation();
		if(partDP){
			float n = fdmap.size();
			int m = 0;
			for(boolean b:fdmap.values()) if(b) m++;
			dpScore = m/n;
		} else {
			boolean dp = true;
			for(boolean b:fdmap.values()) dp = dp && b;
			dpScore = dp?1:0;
		}
		
		
		ArrayList<String> issues = issues(deco);

		float jlScore = lossless?1:0;
		float nfScore = ((float)sumNfScore)/(3*rls.size());
		float isScore = 1f/(1+issues.size()); // issues score = 1/(1+#issues)
		
		float[] scores = new float[4];
		scores[0] = jlScore;
		scores[1] = dpScore;
		scores[2] = nfScore;
		scores[3] = isScore;
		return scores;
	}

	public static ArrayList<String> issues(Decomposition deco){
		ArrayList<String> issues = new ArrayList<String>();
		ArrayList<Relation> rls = deco.getSubrelations();
		Collections.sort(rls);
		for (int i = 0; i < rls.size(); i++) {
			Relation sub = rls.get(i);
			AttributeSet ras = sub.getAttributes();
			if(ras.isEmpty()){
				issues.add("Relation `" + sub.getName()+"` has no attributes!");
			} else if(ras.size()==1){
				issues.add("Relation `" + sub.getName()+"` has only 1 attribute!");
			} else if(ras.equals(deco.getRelation().getAttributes())){
				issues.add("Relation `" + sub.getName()+"` is same as the original relation!");
			}
		}

		AttributeSet allatts = deco.getRelation().getAttributes();
		ArrayList<Attribute> atts = new ArrayList<Attribute>(allatts);
		for (int j = 0; j < atts.size(); j++) {
			Attribute a = atts.get(j);
			boolean mentioned = false;
			for (int i = 0; i < rls.size(); i++) {
				Relation sub = rls.get(i);
				AttributeSet ras = sub.getAttributes();
				if(ras.contains(a)){
					mentioned = true;
					break;
				}
			}
			if(!mentioned){
				issues.add("Attribute `"+a.getName()+"` is not mentioned in any relation!");
			}
		}

		for (int i = 0; i < rls.size()-1; i++) {
			Relation subi = rls.get(i);
			AttributeSet rasi = subi.getAttributes();
			if(rasi.isEmpty()) continue;
			for (int j = i+1; j < rls.size(); j++) {
				Relation subj = rls.get(j);
				AttributeSet rasj = subj.getAttributes();
				if(rasj.isEmpty()) continue;

				if(rasi.equals(rasj)){
					issues.add("Relations `"+subi.getName()+"` and `"+subj.getName()+"` have the same attributes!");
				} else if(rasi.containsAll(rasj)){
					issues.add("Relation `"+subj.getName()+"` is a proper subset of relation `"+subi.getName()+"` !");
				} else if(rasj.containsAll(rasi)){
					issues.add("Relation `"+subi.getName()+"` is a proper subset of relation `"+subj.getName()+"` !");
				}				
			}
		}

		for (int i = 0; i < rls.size(); i++) {
			Relation subi = rls.get(i);
			AttributeSet rasi = subi.getAttributes();
			//if(rasi.isEmpty()) continue;
			boolean joinable = false;
			for (int j = 0; j < rls.size(); j++) {
				if(i==j) continue;
				Relation subj = rls.get(j);
				AttributeSet rasj = subj.getAttributes();
				if(!rasi.intersect(rasj).isEmpty()){
					joinable = true;
					break;
				}
			}
			if(!joinable){
				issues.add("Relation `"+subi.getName()+"` can not be joined with other relations!");
			}
		}

		return issues;
	}

	public static Decomposition makeDecomposition(Relation mother, FDSet fdSet, 
			HashBasedTable<Relation, Attribute, Boolean> tableau, String decoName){
		Decomposition deco = new Decomposition(mother, fdSet, decoName);
		AttributeSet mas = mother.getAttributes();
		Set<Relation> rs = tableau.rowKeySet();
		for(Relation r_: rs){
			AttributeSet r_as = new AttributeSet();
			for(Attribute a_:mas){
				boolean a_in_r = tableau.get(r_, a_);
				if(a_in_r){
					r_as.add(a_);
				}
			}
			Relation r = new Relation(r_.getName(), r_as);
			
			FDSet f_ = FDUtility.project(fdSet, mother, r);
			r.addFDSet(f_);
			deco.add(r);
		}		
		return deco;
	}
	
	/*public static HashBasedTable<Relation, Attribute, Boolean> makeTableau(Decomposition deco){ 
		Relation mother = deco.getRelation();
		HashBasedTable<Relation, Attribute, Boolean> tableau = HashBasedTable.create();
		ArrayList<Attribute> mas = new ArrayList<Attribute>(mother.getAttributes());
		Collections.sort(mas);
		ArrayList<Relation> subschemes= deco.getSubrelations();
		Collections.sort(subschemes);		
		for (int i = 0; i < subschemes.size(); i++) {
			Relation sub = subschemes.get(i);
			for (int j = 0; j < mas.size(); j++) {
				Attribute a = mas.get(j);
				tableau.put(sub, a, sub.getAttributes().contains(a));
			}
		}
		return tableau;
	}*/

}

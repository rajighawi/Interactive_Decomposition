package fd;

import java.util.HashSet;
import java.util.Set;

public class MVDUtility {

	public static Set<AttributeSet> computeDependencyBasis(AttributeSet X, Set<MVD> M, AttributeSet U){ // X <= U
		Set<AttributeSet> S = new HashSet<AttributeSet>();
		S.add(U.minus(X));
		boolean change = true;
		while(change){
			change = false;			
			for(MVD mvd:M){
				AttributeSet V = mvd.getLHS();
				AttributeSet W = mvd.getRHS();				
				Set<AttributeSet> newS = new HashSet<AttributeSet>();
				for(AttributeSet Y : S){
					AttributeSet YW = Y.intersect(W);
					AttributeSet YV = Y.intersect(V);					
					if(YW.size()>0 && YV.size()==0){ // Y intersects W but not V
						AttributeSet Y_W = Y.minus(W);
						newS.add(YW);
						if(Y_W.size()>0) newS.add(Y_W);
					} else {
						newS.add(Y);
					}					
				}
				if(!S.equals(newS))
					change = true;
				S = new HashSet<AttributeSet>(newS);
			}			
		}		
		return S;
	}
	
	public static void main(String[] args){
		AttributeSet U = new AttributeSet();
		U.addAllC("ABCDE");
		DependencySet D = new DependencySet();
		AttributeSet A = new AttributeSet(new Attribute("A"));
		AttributeSet C = new AttributeSet(new Attribute("C"));
		AttributeSet BC = new AttributeSet(); BC.addAllC("BC");
		AttributeSet DE = new AttributeSet(); DE.addAllC("DE");
		System.out.println(C);
		D.add(new MVD(A, BC));
		D.add(new MVD(DE, C));
		System.out.println(D);
		
		Set<MVD> M = D.M();
		System.out.println(M);
		System.out.println("-----------------------");
		Set<AttributeSet> S = computeDependencyBasis(A, M, U);
		System.out.println(S);
	}
	
	public static void main1(String[] args){
		AttributeSet U = new AttributeSet();
		U.addAllC("CTHRSG");
		DependencySet D = new DependencySet();
		AttributeSet C = new AttributeSet(new Attribute("C"));
		AttributeSet T = new AttributeSet(new Attribute("T"));
		AttributeSet HR = new AttributeSet(); HR.addAllC("HR");
		System.out.println(C);
		D.add(new FD(C, T));
		D.add(new MVD(C, HR));
		System.out.println(D);
		
		Set<MVD> M = D.M();
		System.out.println(M);
		System.out.println("-----------------------");
		Set<AttributeSet> S = computeDependencyBasis(C, M, U);
		System.out.println(S);
	}
	
	
}

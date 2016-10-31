package discovery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import fd.Attribute;
import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.FDUtility;
import fd.Relation;
import jdbcTest.MyConnection;

public class FDDiscovery {

	public static void main(String[] args) {
		String dbname = "FDworkshop";
		MyConnection conn = MyConnection.Postgre(dbname, "localhost", dbname, "Raji", "sasasa");
		conn.connect();
		
		String table = "iris"; // "kids";
		ArrayList<String> cols = conn.columns(table);
		AttributeSet atts = new AttributeSet();
		for (int j = 0; j < cols.size(); j++) {
			String col = cols.get(j);
			atts.add(col);
		}
		Relation r = new Relation(table, atts);
		System.out.println(r);
		
		System.out.println("--------------- FDs ----------------");
		FDSet F = discover(conn, r);
		for(FD fd:F){
			System.out.println(fd);
		}
		
		FDSet E = FDUtility.minimalCover(F);
		
		System.out.println("--------------- Minimal Cover ----------------");		
		for(FD fd:E){
			System.out.println(fd);
		}
		
		System.out.println("--------------- Keys ----------------");		
		Set<AttributeSet> keys = FDUtility.findAllKeys_LMR(r, E);
		for(AttributeSet key:keys){
			System.out.println(key);
		}
		/*
		System.out.println("----------------- BCNF ------------------");
		System.out.println(NormUtility.isBCNF(r, E));
		Set<RelFDs> subs = NormUtility.decomposeBCNF(r, E);		
		for(RelFDs rf:subs){
			System.out.println(rf);
		}
		System.out.println("----------------- 3NF ------------------");
		System.out.println(NormUtility.is3NF(r, E));
		Set<RelFDs> subs2 = NormUtility.decompose3NF(r, E);		
		for(RelFDs rf:subs2){
			System.out.println(rf);
		}
		*/
	}
	
	public static FDSet prepareList1(Relation r){
		AttributeSet atts = r.getAttributes();
		ArrayList<Attribute> list = new ArrayList<Attribute>(atts);
		FDSet F = new FDSet();			
		int cnt = 0;
		for (int i = 0; i < list.size(); i++) {
			AttributeSet lhs = new AttributeSet(list.get(i));
			for (int j = 0; j < list.size(); j++) {
				if(i==j) continue;
				AttributeSet rhs = new AttributeSet(list.get(j));
				FD fd = new FD(lhs, rhs);
				F.add(fd);
				cnt++;
			} 			
		}
		System.out.println(cnt);
		return F;
	}
	
	
	public static FDSet prepareList(Relation r){
		AttributeSet atts = r.getAttributes();
		FDSet F = new FDSet();		
		Set<AttributeSet> lps = atts.powerSet();
		List<AttributeSet> lpsList = new ArrayList<AttributeSet>(lps);
		Collections.sort(lpsList);		
		//int cnt = 0;
		for (int i = 0; i < lpsList.size(); i++) {
			AttributeSet lhs = lpsList.get(i);
			if(lhs.isEmpty() || lhs.size()==atts.size()) continue;			
			for(Attribute rhs:atts){				
				if(!lhs.contains(rhs)){ // if non-trivial
					AttributeSet rhss = new AttributeSet();
					rhss.add(rhs);					
					FD fd = new FD(lhs, rhss);	
					/*
					// check if the FD has already a subset LHS in F, then skip it
					boolean extra = false;					
					for(FD fd_:F){
						if(rhss.equals(fd_.getRHS()) && lhs.containsAll(fd_.getLHS())){ // skip
							extra = true;
							break;
						}
					}					
					if(extra) continue;
					*/
					F.add(fd);
				}	
				
			}
		}
		//System.out.println(cnt);
		return F;
	}
	
	
	public static FDSet discover(MyConnection conn, Relation r){
		AttributeSet atts = r.getAttributes();
		FDSet F = new FDSet();		
		Set<AttributeSet> lps = atts.powerSet();
		List<AttributeSet> lpsList = new ArrayList<AttributeSet>(lps);
		Collections.sort(lpsList);		
		int cnt = 0;
		for (int i = 0; i < lpsList.size(); i++) {
			AttributeSet lhs = lpsList.get(i);
			if(lhs.isEmpty() || lhs.size()==atts.size()) continue;			
			for(Attribute rhs:atts){				
				if(!lhs.contains(rhs)){ // if non-trivial
					AttributeSet rhss = new AttributeSet();
					rhss.add(rhs);					
					FD fd = new FD(lhs, rhss);						
					boolean extra = false;
					// check if the FD has already a subset LHS in F, then skip it
					for(FD fd_:F){
						if(rhss.equals(fd_.getRHS()) && lhs.containsAll(fd_.getLHS())){ // skip
							extra = true;
							break;
						}
					}					
					if(extra) continue;
					boolean hold = checkFD(conn, r.getName(), fd);	
					cnt++;
					if(hold){
						F.add(fd);
					}
				}	
				
			}
		}
		System.out.println(cnt);
		return F;
	}
	
	public static boolean checkFD(MyConnection conn, String table, FD fd){
		List<Attribute> lhs = new ArrayList<Attribute>(fd.getLHS());
		AttributeSet u = new AttributeSet(fd.getLHS(), fd.getRHS());
		List<Attribute> fdAtts = new ArrayList<Attribute>(u);		
		String w1 = lhs.get(0).getName();
		for (int i = 1; i < lhs.size(); i++) {
			w1 += "," + lhs.get(i).getName();
		}
		String w2 = fdAtts.get(0).getName();
		for (int i = 1; i < fdAtts.size(); i++) {
			w2 += "," + fdAtts.get(i).getName();
		}		
		String sqlL = "SELECT COUNT(DISTINCT ("+w1+")) FROM "+table;
		//System.out.println(sqlL);
		String sqlLR = "SELECT COUNT(DISTINCT ("+w2+")) FROM "+table;
		//System.out.println(sqlLR);
		//System.out.println("---------------");
		boolean f = false;
		try {
			Statement stmt = conn.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sqlL);
			rs.next();
			int c1 = rs.getInt(1);
			rs.close();
			rs = stmt.executeQuery(sqlLR);
			rs.next();
			int c2 = rs.getInt(1);
			
			f = c1==c2;
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	//	System.out.println(sql);
		return f;
	}	
}

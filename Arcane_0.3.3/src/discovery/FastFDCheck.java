package discovery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import fd.Relation;
import jdbcTest.MyConnection;

public class FastFDCheck {

	Relation relation;
	MyConnection conn;
	Map<AttributeSet, Integer> countMap;
	int count_request;
	
	public FastFDCheck(Relation relation){
		this.relation = relation;
		conn = relation.getConn();
		String table = relation.getName();
		countMap = new HashMap<AttributeSet, Integer>();
		count_request = 0;
		AttributeSet as = relation.getAttributes();
		Set<AttributeSet> pas = as.powerSet(); // _2powerSet(as);//
		try {
			Statement stmt = conn.getConnection().createStatement();
			for(AttributeSet u:pas){
				if(u.isEmpty()) continue;
				// if(u.size()>2) continue; // delete this
				List<Attribute> uAtts = new ArrayList<Attribute>(u);
				Collections.sort(uAtts);			
				String w1 = "";
				for (int i = 0; i < uAtts.size(); i++) {
					w1 += (i==0?"":",") + uAtts.get(i).getName();
				}
				String sql = "SELECT COUNT(DISTINCT ("+w1+")) cnt FROM "+table; 
				ResultSet rs = stmt.executeQuery(sql); count_request++;
				int cnt = 0;
				if(rs.next()){
					cnt = rs.getInt(1);
				}
				countMap.put(u, cnt);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public int getCount_request() {
		return count_request;
	}

	public boolean checkFD(FD fd){
		AttributeSet lhs = fd.getLHS();
		AttributeSet u = new AttributeSet(fd.getLHS(), fd.getRHS());
		int cL = countMap.get(lhs);
		int cLR = countMap.get(u);
		return cL == cLR;
	}
	
	public static Set<AttributeSet> _2powerSet(AttributeSet r){ // subsets of size 1 or 2 only: A, AB
		Set<AttributeSet> sets = new HashSet<AttributeSet>();
		if (r.isEmpty()) {
			sets.add(new AttributeSet());
			return sets;
		}
		List<Attribute> list = new ArrayList<Attribute>(r);
		for (int i = 0; i < list.size(); i++) {
			Attribute a = list.get(i);
			AttributeSet A = new AttributeSet(a);
			sets.add(A);
			for (int j = i+1; j < list.size(); j++) {
				Attribute b = list.get(j);
				AttributeSet AB = new AttributeSet();
				AB.add(a); AB.add(b);
				sets.add(AB);
			}
		}
		return sets;
	}
	
}

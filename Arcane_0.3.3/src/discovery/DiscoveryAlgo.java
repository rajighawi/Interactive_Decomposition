package discovery;

import java.sql.SQLException;
import java.sql.Statement;

import fd.AttributeSet;
import fd.FD;
import fd.FDSet;
import fd.Relation;
import jdbcTest.MyConnection;

public abstract class DiscoveryAlgo {

	Relation relation;
	AttributeSet R;
	FDSet validFDs;
	
	int count_checks;
	int count_requests;
	long time_ms;
	
	MyConnection conn;
	Statement stmt;
	
	public abstract void go();
	public abstract boolean checkFD(FD fd);
		
	public DiscoveryAlgo(Relation relation, String algoName){
		this.relation = relation;
		this.R = relation.getAttributes();		
		this.count_checks = 0;
		this.count_requests = 0;
		this.time_ms = 0;		
		this.validFDs = new FDSet("fds_"+relation.getName()+"_"+algoName);
		this.conn = relation.getConn();
		try {
			stmt = conn.getConnection().createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
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
		s += "Algorithm       : TANE_Original " + "\n";
		s += "Count of Checks = "+count_checks + "\n";
		s += "# Valid FDs     = "+validFDs.size() + "\n";
		s += "# Requests      = "+count_requests + "\n";
		s += "Time (msec)     = "+time_ms + "\n";		
		return s;
	}
	
}

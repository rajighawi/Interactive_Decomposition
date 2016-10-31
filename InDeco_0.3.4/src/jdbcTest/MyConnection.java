package jdbcTest;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import fd.Attribute;
import fd.AttributeSet;
import fd.Relation;

public class MyConnection {

	public static final String oracle_driver  = "oracle.jdbc.driver.OracleDriver";
	public static final String mysql_driver   = "com.mysql.jdbc.Driver";
	public static final String postgre_driver = "org.postgresql.Driver";
	
	public static final String oracle_url  = "jdbc:oracle:oci:@"; // + 127.0.0.1
	public static final String mysql_url   = "jdbc:mysql://"; 		// + localhost + / + dbname
	public static final String postgre_url = "jdbc:postgresql://";	// + localhost + / + dbname
	
	public static final String ORACLE  = "Oracle";
	public static final String MYSQL   = "MySQL";
	public static final String POSTGRE = "PostgreSQL";
	
	String id;
	String dbms;
	String host;
	String database;
	String user;
	String password;
	
	private boolean connected = false;
	private String  driver    = null;
	private Connection connection = null;
	
	public MyConnection(String id, String dbms, String host, String database, String user, String password) {
		super();
		this.id       = id;
		this.dbms     = dbms;
		this.host     = host;
		this.database = database;
		this.user     = user;
		this.password = password;
		this.driver = getDriver();
	}

	// -------------------------------------------------------------------------------------------------
	
	public String getURL(){
		String url = null;
		if(dbms.equals(MYSQL)){
			url = mysql_url + host + "/" + database;
		} else if(dbms.equals(ORACLE)){
			url = oracle_url + "/" + host;
		} else if(dbms.equals(POSTGRE)){
			url = postgre_url + host + "/" + database;
		}		
		return url;
	}
	
	public String getDriver(){
		String url = null;
		if(dbms.equals(MYSQL)){
			url = mysql_driver;
		} else if(dbms.equals(ORACLE)){
			url = oracle_driver;
		} else if(dbms.equals(POSTGRE)){
			url = postgre_driver;
		}		
		return url;
	}
	
	public void connect(){
		connected = false;
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(getURL(), user, password);	
			connected = true;
		} catch (SQLException|NullPointerException|ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	public void disconnect(){
		connected = false;
		connection = null; 
	}
	
	public static MyConnection MySQL(String id, String host, String database, String user, String password) {
		MyConnection con = new MyConnection(id, MYSQL, host, database, user, password);
		con.driver = mysql_driver;
		return con;
	}
	
	public static MyConnection Postgre(String id, String host, String database, String user, String password) {
		MyConnection con =  new MyConnection(id, POSTGRE, host, database, user, password);
		con.driver = postgre_driver;
		return con;
	}
	
	public static MyConnection Oracle(String id, String host, String user, String password) {
		MyConnection con =  new MyConnection(id, ORACLE, host, null, user, password);
		con.driver = oracle_driver;
		return con;
	}
	
	// ----------------------------------------------------------------------------

	public ArrayList<String> tables(){
		ArrayList<String> tables = new ArrayList<String>();
		try {
			DatabaseMetaData md = connection.getMetaData();			
			String[] types = {"TABLE"};
			ResultSet rs = md.getTables(database, null, null, types);
			while(rs.next()){
				String t = rs.getString("table_name");
				tables.add(t);
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return tables;
	}
	
	public ArrayList<String> columns(String table){
		ArrayList<String> columns = new ArrayList<String>();
		try {
			DatabaseMetaData md = connection.getMetaData();
			ResultSet rs = md.getColumns(database, null, table, null);
			while(rs.next()){
				String t = rs.getString("column_name");
				//System.out.println(t);
				columns.add(t);
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return columns;
	}	
	
	public ArrayList<Relation> getRelations(){
		ArrayList<String> tables = tables();
		ArrayList<Relation> relations = new ArrayList<Relation>();
		for (int i = 0; i < tables.size(); i++) {
			String t = tables.get(i);
			ArrayList<String> cols = columns(t);
			AttributeSet as = new AttributeSet();
			for (int j = 0; j < cols.size(); j++) {
				as.add(new Attribute(cols.get(j)));
			}
			Relation r = new Relation(t, as);
			relations.add(r);			
		}
		return relations;
	}
	
	// ----------------------------------------------------------------------------

	public void executeUpdate(String sql){		 
		if(connected){
			try {				 
				Statement statement = connection.createStatement();
				statement.executeUpdate(sql);
				statement.close(); 
			} catch (SQLException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, e2.getMessage(), "SQLException", JOptionPane.ERROR_MESSAGE);
			}
		} 
	}
	
	public ResultSet executeSQLQuery(String sql){		
		ResultSet rset = null;
		if(connected){
			try {				 
				Statement statement = connection.createStatement();
				rset = statement.executeQuery(sql);
				statement.close(); 
			} catch (SQLException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, e2.getMessage(), "SQLException", JOptionPane.ERROR_MESSAGE);
			}
		} 
		return rset;
	}
		
	// ----------------------------------------------------------------------------
	
	public String getId() {
		return id;
	}

	public String getDbms() {
		return dbms;
	}

	public String getHost() {
		return host;
	}

	public String getDatabase() {
		return database;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public String toString() {
		return "\n Driver   = " + driver + ";\n URL      = " + getURL() + ";\n User     = "
				+ user + ";\n Password = " + password + "\n";
	}

	public String toString1() {
		return  getURL()+ " (" + user+ ")";
	}
	
	
	
	
}

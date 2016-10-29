package jdbcTest;

import java.util.ArrayList;

public class ConnTest {

	public static void main(String[] args) {
		String dbname = "moocs";
		MyConnection conn = MyConnection.Postgre(dbname, "localhost", dbname, "postgres", "postgres");
		conn.connect();
		ArrayList<String> tables = conn.tables();
		for (int i = 0; i < tables.size(); i++) {
			String t = tables.get(i);
			System.out.println(t);			
			System.out.println("-------");
			ArrayList<String> cols = conn.columns(t);
			for (int j = 0; j < cols.size(); j++) {
				System.out.println(cols.get(j));
			}
			System.out.println("========================");
		}
	}

}

package unist.vdi.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DBManager {
	private Connection conn;

	public DBManager() {
		try {
			// Class.forName("oracle.jdbc.OracleDriver");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vdi_portal", "root", "Un!s7i!vdipt");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public PreparedStatement getPreparedStatement(String sql) throws Exception {
		return conn.prepareStatement(sql);
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {}
	}
}
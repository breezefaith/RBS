package uidemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionInstance {
	private static Connection connection=null;
	private String url="JDBC:sqlite:src/sqlite/rbs.db";
	private ConnectionInstance() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection=DriverManager.getConnection(url);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unused")
	private static ConnectionInstance connectionInstance=new ConnectionInstance();
	public static Connection getInstance() {
		return connection;
	}
}

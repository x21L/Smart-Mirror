package lukas.wais.smart.mirror.model;

import java.sql.*;

public class DBConnection {

	private Connection connection;
	private static final String DB_URL = "jdbc:h2:~/SmartMirror";
	private static DBConnection instance;

	public DBConnection() throws SQLException {
		/*
		 * try { Class.forName("org.apache.derby.jdbc.ClientDriver"); this.connection =
		 * DriverManager.getConnection(DB_URL); } catch (ClassNotFoundException e) {
		 * System.out.println("Can not find Client Driver " + e.getMessage()); }
		 * 
		 */

		try {
			connection = DriverManager.getConnection(DB_URL, "admin", "admin");

		} catch (SQLException throwables) {
			System.out.println("Could not connect to the database" + throwables.getMessage());
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public static DBConnection getInstance() {
		if (instance == null) {
			try {
				instance = new DBConnection();
			} catch (SQLException throwables) {
				System.out.println("Can not establish database connection \n" + throwables.getMessage());
			}
		}
		return instance;
	}
}

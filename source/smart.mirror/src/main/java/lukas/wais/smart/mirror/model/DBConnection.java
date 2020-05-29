package lukas.wais.smart.mirror.model;

import java.sql.*;

public class DBConnection {
	static Connection connection;
	
	public DBConnection() {
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection("jdbc:h2:~/SmartMirror", "admin", "admin");
			//System.out.println("Connection Successful");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Connection openConnection() {
		return connection;
	}

	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
}

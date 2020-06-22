/*
 * @author Omar Duenas
 * @version 1.0
 * @since 1.0
 */
package lukas.wais.smart.mirror.model;

import java.sql.*;

/**
 * The Class DBConnection.
 */
public class DBConnection {

	/** The connection to the database will be stored in this variable */
	private Connection connection;

	/** The Constant for the database URL */
	private static final String DB_URL = "jdbc:h2:~/SmartMirror";

	/** The instance for the database connection */
	private static DBConnection instance;

	/**
	 * Constructor for the DBConnection. Creation a connection to the H2 database
	 *
	 * @throws SQLException the SQL exception for connection issues to database
	 */
	public DBConnection() throws SQLException {
		try {
			connection = DriverManager.getConnection(DB_URL, "admin", "admin");

		} catch (SQLException throwables) {
			System.out.println("Could not connect to the database" + throwables.getMessage());
		}
	}

	/**
	 * Getter method for the private variable connection. The connection to the
	 * database will be stored here.
	 * 
	 * @return the connection to the database
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Gets the single instance of DBConnection. With this method we implement the
	 * Singleton Pattern in oder to avoid multiple access/instance to the database
	 *
	 * @return single instance of DBConnection
	 */
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

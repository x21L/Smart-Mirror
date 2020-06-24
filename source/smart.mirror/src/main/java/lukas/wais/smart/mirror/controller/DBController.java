package lukas.wais.smart.mirror.controller;

import java.sql.Connection;
import java.sql.SQLException;

import lukas.wais.smart.mirror.model.DBConnection;

/**
 * The Class DBController creates the connection to the database.
 * @author Omar Duenas
 */
public class DBController {
	
	/**
	 * Establish the connection to the database. This class will be extended by
	 * multiple controllers and with this method we avoid code duplication in other classes.
	 *
	 * @return the connection to the H2 database
	 * @throws SQLException the SQL exception in case something went run while establishing a connection
	 */
	public static Connection getConnection() throws SQLException {
		return DBConnection.getInstance().getConnection();
	}

}

package lukas.wais.smart.mirror.controller;

import java.sql.Connection;
import java.sql.SQLException;

import lukas.wais.smart.mirror.model.DBConnection;

public class DBController {
	private Connection connection; 

	public DBController() {
		this.connection = DBConnection.openConnection();
	}

	public Connection getConnection() throws SQLException {
		if (connection.isClosed()) {
			return connection = DBConnection.openConnection();
		}
		return connection;
	}

}

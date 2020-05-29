package lukas.wais.smart.mirror.controller;

import java.sql.Connection;
import java.sql.SQLException;

import lukas.wais.smart.mirror.model.DBConnection;


public class DBController {
	public static Connection getConnection() throws SQLException {
		return DBConnection.getInstance().getConnection();
	}

}

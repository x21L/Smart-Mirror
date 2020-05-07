package lukas.wais.smart.mirror.model;


import java.sql.*;

public class DBConnection {
	static Connection conn = null; 
	public static void main(String[] a) throws Exception { 
		openConnection();
		
		System.out.println("Bin dann wieder weg");
		
		closeConnection();
    } 
	
	public static void openConnection() {
		try {
			Class.forName("org.h2.Driver"); 
			conn = DriverManager.getConnection( 
                    "jdbc:h2:~/SmartMirror", "admin", "admin"); 
            System.out.println("Connection Successful");
		} catch (Exception e) {
			 e.printStackTrace(); 
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace(); 
				}
			}
		}
	}
	
	public static void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		} 
	}
}

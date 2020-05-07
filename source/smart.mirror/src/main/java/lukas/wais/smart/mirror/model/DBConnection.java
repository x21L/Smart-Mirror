package lukas.wais.smart.mirror.model;


import java.sql.*;

public class DBConnection {
	public static void main(String[] a) throws Exception { 
        Connection conn = null; 
        String tab = "TESTTABELLE"; 

        try { 
             
           Class.forName("org.h2.Driver"); 
             
            conn = DriverManager.getConnection( 
                    "jdbc:h2:~/MMS", "omar", "Omar"); 

            DatabaseMetaData md = conn.getMetaData(); 

            String[] types = { "TABLE", "SYSTEM TABLE" }; 
             
            ResultSet metaRS = md.getTables(null, null, "%", types); 

            while (metaRS.next()) { 

                // Catalog 
                String tableCatalog = metaRS.getString(1); 
                System.out.println("Catalog: " + tableCatalog); 

                // Schemata 
                String tableSchema = metaRS.getString(2); 
                System.out.println("Tabellen-Schema: " + tableSchema); 

                // Tabellennamen 
                String tableName = metaRS.getString(3); 
                System.out.println("Tabellen-Name: " + tableName); 

                // Tabellentyp 
                String tableType = metaRS.getString(4); 
                System.out.println("Tabellen-Typ: " + tableType + "\n"); 
            } 

//            Statement stmt = conn.createStatement(); 
//
//            String dropQ = "DROP TABLE IF EXISTS " + tab; 
//            stmt.executeUpdate(dropQ); 
//
//            String createQ = "CREATE TABLE IF NOT EXISTS " 
//                    + tab 
//                    + "(ID INT PRIMARY KEY AUTO_INCREMENT(1,1) NOT NULL, NAME VARCHAR(255))"; 
//            stmt.executeUpdate(createQ); 
//
//            String insertQ = "INSERT INTO " + tab 
//                    + " VALUES(TRANSACTION_ID(),'Hello World!')"; 
//            stmt.executeUpdate(insertQ); 
//
//            ResultSet selectRS = stmt.executeQuery("SELECT * FROM " + tab); 
//            while (selectRS.next()) { 
//                System.out.printf("%s, %s\n", selectRS.getString(1), 
//                        selectRS.getString(2)); 
//            } 
//
//            System.out.println("Liste Tabellen..."); 
//            String tablesQ = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC'"; 
//            ResultSet tablesRS = stmt.executeQuery(tablesQ); 
//            while (tablesRS.next()) { 
//                System.out.printf("Tabelle %s vorhanden \n", 
//                        tablesRS.getString(1)); 
//            } 

            conn.close(); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } finally { 
            if (conn != null) 
                try { 
                    conn.close(); 
                } catch (SQLException e) { 
                    e.printStackTrace(); 
                } 
        } 
    } 
}

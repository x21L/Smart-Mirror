/*
 * @author Omar Duenas
 * @version 1.0
 * @since 1.0
 */
package lukas.wais.smart.mirror.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lukas.wais.smart.mirror.model.DBConnection;

/**
 * The Class TableToXML contains the methods for creating/saving the database structure and the already existing data.
 */
public class TableToXML extends DBController {

	/**
	 * Generate an XML file according to the given table as an input parameter. 
	 * The file contains the tag for each column as well as the format for each table field
	 * and the corresponding data if exists.
	 *
	 * @param table which table should be extract from the database
	 * @return document with the table data
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	public Document generateXML(String table) throws ParserConfigurationException {

		Connection con = null;
		con = DBConnection.getInstance().getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element results = doc.createElement("Table");
		doc.appendChild(results);

		try {
			pstmt = con.prepareStatement(table);
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData(); // to retrieve table name, column name, column type and column
														// precision, etc..
			int colCount = rsmd.getColumnCount();

			Element tableName = doc.createElement("TableName");
			tableName.appendChild(doc.createTextNode(rsmd.getTableName(1)));

			results.appendChild(tableName);
			Element structure = doc.createElement("TableStructure");
			results.appendChild(structure);

			Element col = null;
			for (int i = 1; i <= colCount; i++) {
				col = doc.createElement("Column" + i);
				results.appendChild(col);
				Element columnNode = doc.createElement("ColumnName");
				columnNode.appendChild(doc.createTextNode(rsmd.getColumnName(i)));
				col.appendChild(columnNode);

				Element typeNode = doc.createElement("ColumnType");
				typeNode.appendChild(doc.createTextNode(String.valueOf((rsmd.getColumnTypeName(i)))));
				col.appendChild(typeNode);

				Element lengthNode = doc.createElement("Length");
				lengthNode.appendChild(doc.createTextNode(String.valueOf((rsmd.getPrecision(i)))));
				col.appendChild(lengthNode);

				structure.appendChild(col);
			}

			Element productList = doc.createElement("TableData");
			results.appendChild(productList);

			int l = 0;
			while (rs.next()) {
				Element row = doc.createElement("Product" + (++l));
				results.appendChild(row);
				for (int i = 1; i <= colCount; i++) {
					String columnName = rsmd.getColumnName(i);
					Object value = rs.getObject(i);
					Element node = doc.createElement(columnName);
					node.appendChild(doc.createTextNode((value != null) ? value.toString() : ""));
					row.appendChild(node);
				}
				productList.appendChild(row);
			}
		} catch (SQLException e) {
			System.out.println("SQL Exception during xml export \n" + e.getMessage());
		}
		return doc;

	}

	/**
	 * With this function the database structure will be created base on an XML file.
	 * The XML contain the table structure and data to be inserted. 
	 * In case the table already exists in the database only the data for the corresponding 
	 * table fields will be inserted. 
	 *
	 * @param doc is the input parameter with the XML file and structure
	 * @throws SQLException the SQL exception in case the connection to the database is not possible
	 */
	public static void xmlToTable(Document doc) throws SQLException {
		Connection connection = null;
		connection = DBConnection.getInstance().getConnection();

		StringBuffer ddl = new StringBuffer(
				"create table " + doc.getElementsByTagName("TableName").item(0).getTextContent() + "(");

		StringBuffer dml = new StringBuffer(
				"insert into  " + doc.getElementsByTagName("TableName").item(0).getTextContent() + "(");

		NodeList tableStructure = doc.getElementsByTagName("TableStructure");

		int no_of_columns = tableStructure.item(0).getChildNodes().getLength();

		for (int i = 0; i < no_of_columns; i++) {
			ddl.append(doc.getElementsByTagName("ColumnName").item(i).getTextContent() + " "
					+ doc.getElementsByTagName("ColumnType").item(i).getTextContent() + "("
					+ doc.getElementsByTagName("Length").item(i).getTextContent() + "),");
			dml.append(doc.getElementsByTagName("ColumnName").item(i).getTextContent() + ",");

		}

		ddl = ddl.replace(ddl.length() - 1, ddl.length(), ")");
		dml = dml.replace(dml.length() - 1, dml.length(), ") values(");

		for (int k = 0; k < no_of_columns; k++) {
			dml.append("?,");
		}

		dml = dml.replace(dml.length() - 1, dml.length(), ")");

		try {
			Statement stmt = connection.createStatement();
			// to create table One time only;
			stmt.executeUpdate(ddl.toString());

		} catch (Exception e) {
			System.out.println("Tables already created, skipping table creation process" + e.getMessage());
		}

		NodeList tableData = doc.getElementsByTagName("TableData");
		int tdlen = tableData.item(0).getChildNodes().getLength();
		PreparedStatement prepStmt = connection.prepareStatement(dml.toString());

		String colName = "";
		for (int i = 0; i < tdlen; i++) {
			System.out.println("Outer" + i);

			for (int j = 0; j < tableStructure.item(0).getChildNodes().getLength(); j++) {
				colName = doc.getElementsByTagName("ColumnName").item(j).getTextContent();
				prepStmt.setString(j + 1, doc.getElementsByTagName(colName).item(i).getTextContent());
			}
			prepStmt.addBatch();
		}
	}
}
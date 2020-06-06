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

public class TableToXML extends DBController {

	public Document generateXML(String table) throws TransformerException, ParserConfigurationException {

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

			ResultSetMetaData rsmd = rs.getMetaData();// to retrieve table name, column name, column type and column
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

			//System.out.println("Col count = " + colCount);

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
		} catch (SQLException sqlExp) {

			System.out.println("SQLExcp:" + sqlExp.toString());
		}

		return doc;

	}

	public static void xmlToTable(Document doc) throws SQLException

	{
		Connection connection = null;
		connection = DBConnection.getInstance().getConnection();

		System.out.println("Table Name= " + doc.getElementsByTagName("TableName").item(0).getTextContent());

		StringBuffer ddl = new StringBuffer(
				"create table " + doc.getElementsByTagName("TableName").item(0).getTextContent() + "1 (");

		StringBuffer dml = new StringBuffer(
				"insert into  " + doc.getElementsByTagName("TableName").item(0).getTextContent() + "1 (");

		NodeList tableStructure = doc.getElementsByTagName("TableStructure");

		int no_of_columns = tableStructure.item(0).getChildNodes().getLength();

		for (int i = 0; i < no_of_columns; i++) {
			ddl.append(doc.getElementsByTagName("ColumnName").item(i).getTextContent() + " "
					+ doc.getElementsByTagName("ColumnType").item(i).getTextContent() + "("
					+ doc.getElementsByTagName("Length").item(i).getTextContent() + "),");
			dml.append(doc.getElementsByTagName("ColumnName").item(i).getTextContent() + ",");

		}

		System.out.println(" DDL " + ddl.toString());
		System.out.println(" dml " + dml.toString());

		ddl = ddl.replace(ddl.length() - 1, ddl.length(), ")");
		dml = dml.replace(dml.length() - 1, dml.length(), ") values(");

		System.out.println(" DDL " + ddl.toString());

		for (int k = 0; k < no_of_columns; k++)
			dml.append("?,");

		dml = dml.replace(dml.length() - 1, dml.length(), ")");

		System.out.println(" dml " + dml.toString());

		Statement stmt = null;

		try {
			stmt = connection.createStatement();
			// to create table One time only;
			stmt.executeUpdate(ddl.toString());

		} catch (Exception e) {
			System.out.println("Tables already created, skipping table creation process" + e.toString());
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

				System.out.println("Data  =" + doc.getElementsByTagName(colName).item(i).getTextContent());

			}

			prepStmt.addBatch();

		}

		int[] numUpdates = prepStmt.executeBatch();

		System.out.println(numUpdates + " records inserted");

	}

}
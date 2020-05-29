package lukas.wais.smart.mirror.controller;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.h2.bnf.context.DbColumn;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import lukas.wais.smart.mirror.model.DBConnection;

public class TableToXML extends DBController {

	private final static String SELECTALL = "SELECT * FROM SM_USERS";

	public static Document generateXML() throws TransformerException, ParserConfigurationException {

		Connection con = null;
		try {
			con = getConnection();
		} catch (SQLException e) {
			System.out.println("Could not connect to database \n" + e.getMessage());
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		DOMSource domSource = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element results = doc.createElement("Table");
		doc.appendChild(results);

		try {
			
			pstmt = con.prepareStatement(SELECTALL);
			System.out.println("HALLO");
			rs = pstmt.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();// to retrieve table name, column name, column type and column
			System.out.println("HALLO1");								// precision, etc..
			int colCount = rsmd.getColumnCount();
			System.out.println("HALLO2");
			Element tableName = doc.createElement("TableName");
			tableName.appendChild(doc.createTextNode(rsmd.getTableName(1)));
			System.out.println("HALLO3");
			results.appendChild(tableName);
			System.out.println("HALLO4");
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

			System.out.println("Col count = " + colCount);

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

			domSource = new DOMSource(doc);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);

			System.out.println("Xml document 1" + sw.toString());

			System.out.println("********************************");

		} catch (SQLException sqlExp) {

			System.out.println("SQLExcp:" + sqlExp.toString());

		} finally {
			try {

				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (SQLException expSQL) {
				System.out.println("CourtroomDAO::loadCourtList:SQLExcp:CLOSING:" + expSQL.toString());
			}
		}

		// return sw.toString();

		return doc;

	}

	public void xmlToTable(Document doc) throws SQLException

	{
		Connection connection = null;
		try {
			 connection = getConnection();
		} catch (SQLException e) {
			System.out.println("Could not connect to database \n" + e.getMessage());
		}

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
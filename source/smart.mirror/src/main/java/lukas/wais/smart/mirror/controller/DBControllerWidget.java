/*
 * @author Omar Duenas
 * @version 1.0
 * @since 1.0
 */
package lukas.wais.smart.mirror.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The Class DBControllerWidget provide the statements for the data manipulation for the 
 * database tables SM_WIDGET and SM_PROFILE
 */
public class DBControllerWidget extends DBController {

	/** The Constant INSERTPROFILE contains the SQL Statement for inserting new data into SM_PROFILE table */
	private static final String INSERTPROFILE = "INSERT INTO SM_PROFILE (PRFUSRID, PRFWIDID) "
			+ "SELECT (SELECT USRID FROM SM_USERS WHERE  USRID = ?) as PRFUSRID, "
			+ "(SELECT WIDID FROM SM_WIDGET WHERE WIDNAME  =?) as PRFWIDID";

	/** The Constant SELECTALL contains the SQL Statement for selecting the widgets 
	 * which are in the profile table for the corresponding user */
	private static final String SELECTALL = "SELECT WIDNAME FROM SM_WIDGET WHERE WIDID IN ("
			+ "SELECT PRFWIDID   FROM SM_PROFILE   WHERE  PRFUSRID =?)";

	/** The Constant DELETEPROFILE contains the SQL Statement for deleting the profile according to the user ID*/
	private static final String DELETEPROFILE = "DELETE FROM SM_PROFILE WHERE PRFUSRID = ?";

	/**
	 * Select Widget Name which are assigned to certain users. 
	 * The SQl statement stored in the constant SELECTALL will be executed in the H2 Database. 
	 * As a result a List with the widgets name will be return. 
	 *
	 * @param iD input parameter to select the desire user. 
	 * @return the list with the widgets name for the corresponding user
	 */
	public static ArrayList<String> selectWidget(String iD) {
		ArrayList<String> widgetList = new ArrayList<String>();
		try {
			try (PreparedStatement selectPersonNk = getConnection().prepareStatement(SELECTALL)) {
				selectPersonNk.setString(1, iD);
				try (final ResultSet resultSet = selectPersonNk.executeQuery()) {
					while (resultSet.next()) {
						widgetList.add(resultSet.getString("WIDNAME"));
					}
				}
			}
		}
		catch (SQLException throwables) {
			System.out.println("Could not select widgets from Profile \n" + throwables.getMessage());
		}
		return widgetList;
	}

	/**
	 * Insert profile in oder to recognize which widgets were assigned to which users.
	 * The constant INSERTPROFILE, contain the SQL statement to be execute in the H2 Database.
	 * Input parameters are the ID for the according user and the desire widget which should be assigned
	 *
	 * @param iD of the corresponding user
	 * @param widget the name of the widget which will be assigned to the user
	 */
	public static void insertProfile(String iD, String widget) {
		try (PreparedStatement insert = getConnection().prepareStatement(INSERTPROFILE)) {

			insert.setString(1, iD);
			insert.setString(2, widget);

			getConnection().setAutoCommit(true);
			final int affectedRows = insert.executeUpdate();
			if (affectedRows != 1) {
				throw new RuntimeException("Failed to add new Profile to database");
			}
		} catch (SQLException throwables) {
			System.out.println("Could not insert Profile \n" + throwables.getMessage());
		}
	}

	/**
	 * Delete profile according to the user ID. In the Profile tables are multiple
	 * entries for one user (1...n to one user could be more than one widget assigned)
	 *
	 * @param iD of the user to be deleted in the profile table.
	 */
	public static void deleteProfile(String iD ) {
		try (PreparedStatement delete = getConnection().prepareStatement(DELETEPROFILE)) {
			delete.setString(1, iD);

			final int affectedRows = delete.executeUpdate();
			System.out.println(affectedRows +" rows has been deleted");
		} catch (SQLException throwables) {
			System.out.println("Could not delete Profile \n" + throwables.getMessage());
		}
	}
	
	public static void setUniqueAttribute(String statement) {
		try (PreparedStatement unique = getConnection().prepareStatement(statement)) {
			System.out.println("Unique set with "+statement);
		} catch (SQLException throwables) {
			System.out.println("Could not set table's unique \n" + throwables.getMessage());
		}
	}
}

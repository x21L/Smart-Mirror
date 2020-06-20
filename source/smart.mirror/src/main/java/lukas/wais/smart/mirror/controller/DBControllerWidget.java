package lukas.wais.smart.mirror.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lukas.wais.smart.mirror.model.Person;
import lukas.wais.smart.mirror.model.PersonFields;

public class DBControllerWidget extends DBController{
	
	private static final String INSERTPROFILE = 
			"INSERT INTO SM_PROFILE (PRFUSRID, PRFWIDID) "
			+ "SELECT (SELECT USRID FROM SM_USERS WHERE  USRID = ?) as PRFUSRID, "
			+ "(SELECT WIDID FROM SM_WIDGET WHERE WIDNAME  =?) as PRFWIDID";

	
	private static final String SELECTALL = "SELECT WIDNAME " + 
			" FROM SM_WIDGET " + 
			" WHERE WIDID IN (" + 
			"   SELECT PRFWIDID " + 
			"   FROM SM_PROFILE " + 
			"   WHERE "+  PersonFields.USRID +" =?";
	
	public static List<String>  selectWidget(int iD) {
		List<String> widgetList= new ArrayList<String>();
		try {
			try (PreparedStatement selectPersonNk = getConnection().prepareStatement(SELECTALL)) {
				selectPersonNk.setInt(1, iD);
				try (final ResultSet resultSet = selectPersonNk.executeQuery()) {
					while (resultSet.next()) {
						widgetList.add(resultSet.toString());
					}
				}
			}
		}
		// TODO proper exception handling
		catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return widgetList;
	}

	public void insertPerson() {
		try (PreparedStatement insert = getConnection().prepareStatement(INSERTPROFILE)) {
//			insert.setInt(1, Person.getID());
//			insert.setString(2, widget);
			getConnection().setAutoCommit(true);
			final int affectedRows = insert.executeUpdate();
			if (affectedRows != 1) {
				throw new RuntimeException("Failed to add new Person to database");
			}
		} catch (SQLException throwables) {
			System.out.println("Could not insert Person \n" + throwables.getMessage());
		}
	}
}

package lukas.wais.smart.mirror.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lukas.wais.smart.mirror.model.Person;

public class DBControllerPerson extends DBController {

	private static final String TABLENAME = "SM_USERS";

	private static final String SELECTALL = "SELECT * FROM" + TABLENAME + " WHERE " + PersonFields.USRID +" =?";

	private static final String INSERTPERSON = "INSERT INTO " + TABLENAME + " (" + PersonFields.USRFNAME + ","
			+ PersonFields.USRLNAME + "," + PersonFields.USRNKNAME + "," + PersonFields.USREMAIL + ") "
			+ "VALUES(?,?,?,?)";

	private static final String UPDATEPERSONVN = "UPDATE " + TABLENAME + " SET " + PersonFields.USRFNAME + "=? WHERE "
			+ PersonFields.USRID + "=?";

	private static final String UPADTEPERSONNN = "UPDATE " + TABLENAME + " SET " + PersonFields.USRLNAME + "= ? WHERE "
			+ PersonFields.USRID + "=?";

	private static final String UPADTEPERSONNK = "UPDATE " + TABLENAME + " SET " + PersonFields.USRNKNAME + "=? WHERE "
			+ PersonFields.USRID + "=?";

	private static final String UPADTEPERSONEM = "UPDATE " + TABLENAME + " SET " + PersonFields.USREMAIL + "= WHERE "
			+ PersonFields.USRID + "=?";

	private static final String DeletePersion = "DELETE FROM " + TABLENAME + " WHERE " + PersonFields.USRID;

	public Person selectPerson(int iD) {
		Person nk = null;
		try {
			try (PreparedStatement selctPersonNk = getConnection().prepareStatement(SELECTALL)) {
				try (final ResultSet resultSet = selctPersonNk.executeQuery()) {
					while (resultSet.next()) {
						nk = new Person(resultSet.getString(PersonFields.USRFNAME.toString()),
								resultSet.getString(PersonFields.USRLNAME.toString()),
								resultSet.getString(PersonFields.USRNKNAME.toString()),
								resultSet.getString(PersonFields.USREMAIL.toString())
								);
					}
				}
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return nk;
	}
	
	 public void insertPerson(Person person) {
	        if (person == null) {
	            throw new IllegalArgumentException("the invoice item must not be null");
	        }

	        try (PreparedStatement insert = getConnection().prepareStatement(INSERTPERSON)) {
	            insert.setString(1, person.getFirstName());
	            insert.setString(2, person.getLastName());
	            insert.setString(2, person.getNickname());
	            insert.setString(2, person.getEmail());

	            final int affectedRows = insert.executeUpdate();
	            if (affectedRows != 1) {
	                throw new RuntimeException("Failed to add new Person to database");
	            }
	        } catch (SQLException throwables) {
	            System.out.println("Could not insert Person \n" + throwables.getMessage());
	        }
	    }

}

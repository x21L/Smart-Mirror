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
import java.util.List;

import lukas.wais.smart.mirror.model.Person;
import lukas.wais.smart.mirror.model.PersonFields;

/**
 * The Class DBControllerPerson.
 */
public class DBControllerPerson extends DBController {

	/**
	 * The Constant TABLENAME contains the table to be use for the SQL statements
	 */
	private static final String TABLENAME = "SM_USERS";

	/**
	 * The Constant SELECTALLPERSONS contains the select statement to select all the
	 * table data
	 */
	private static final String SELECTALLPERSONS = "SELECT * FROM " + TABLENAME;

	/**
	 * The Constant SELECTALL contains the select statement to select the table data
	 * for a specific user
	 */
	private static final String SELECTALL = "SELECT * FROM " + TABLENAME + " WHERE " + PersonFields.USRID + " =?";

	/**
	 * The Constant INSERTPERSON contains the statement to insert to select a new person
	 */
	private static final String INSERTPERSON = "INSERT INTO " + TABLENAME + " (" + PersonFields.USRID + ","
			+ PersonFields.USRFNAME + "," + PersonFields.USRLNAME + "," + PersonFields.USRNKNAME + ","
			+ PersonFields.USREMAIL + ") " + "VALUES(?,?,?,?,?)";

	/**
	 * The Constant UPDATEPERSONFN contains the statement to update the person's
	 * Firstname
	 */
	private static final String UPDATEPERSONFN = "UPDATE " + TABLENAME + " SET " + PersonFields.USRFNAME + "=? WHERE "
			+ PersonFields.USRID + "=?";

	/**
	 * The Constant UPADTEPERSONLN contains the statement to update the person's
	 * Lastname
	 */
	private static final String UPADTEPERSONLN = "UPDATE " + TABLENAME + " SET " + PersonFields.USRLNAME + "= ? WHERE "
			+ PersonFields.USRID + "=?";

	/**
	 * The Constant UPADTEPERSONNK contains the statement to update the person's
	 * nickname
	 */
	private static final String UPADTEPERSONNK = "UPDATE " + TABLENAME + " SET " + PersonFields.USRNKNAME + "=? WHERE "
			+ PersonFields.USRID + "=?";

	/**
	 * The Constant UPADTEPERSONEM contains the statement to update the person's email
	 */
	private static final String UPADTEPERSONEM = "UPDATE " + TABLENAME + " SET " + PersonFields.USREMAIL + "= ? WHERE "
			+ PersonFields.USRID + "=?";

	/** The Constant DELETEPERSON contains the statement to delete a certain person */
	private static final String DELETEPERSON = "DELETE FROM " + TABLENAME + " WHERE " + PersonFields.USRID + " =?";

	/**
	 * Select all the information of a person for the corresponding ID.
	 *
	 * @param iD of the desire user as an input parameter
	 * @return the person data found in the database
	 */
	public static Person selectPerson(String iD) {
		Person nk = null;
		try {
			try (PreparedStatement selectPersonNk = getConnection().prepareStatement(SELECTALL)) {
				selectPersonNk.setString(1, iD);
				try (final ResultSet resultSet = selectPersonNk.executeQuery()) {
					while (resultSet.next()) {
						nk = new Person(resultSet.getString(PersonFields.USRFNAME.toString()),
								resultSet.getString(PersonFields.USRLNAME.toString()),
								resultSet.getString(PersonFields.USRNKNAME.toString()),
								resultSet.getString(PersonFields.USREMAIL.toString()));
					}
				}
			}
		} catch (SQLException throwables) {
			System.out.println("Could not select a person \n" + throwables.getMessage());
		}
		return nk;
	}

	/**
	 * Select all the information of the persons in the table SM_USERS.
	 *
	 * @return the list of found persons
	 */
	public static List<Person> selectAllPersons() {
		List<Person> listPerson = new ArrayList<Person>();
		try {
			try (PreparedStatement selectPersonNk = getConnection().prepareStatement(SELECTALLPERSONS)) {
				try (final ResultSet resultSet = selectPersonNk.executeQuery()) {
					while (resultSet.next()) {
						listPerson.add(new Person(resultSet.getString(PersonFields.USRID.toString()),
								resultSet.getString(PersonFields.USRFNAME.toString()),
								resultSet.getString(PersonFields.USRLNAME.toString()),
								resultSet.getString(PersonFields.USRNKNAME.toString()),
								resultSet.getString(PersonFields.USREMAIL.toString())));
					}
				}
			}
		} catch (SQLException throwables) {
			System.out.println("Could not select a person \n" + throwables.getMessage());
		}
		return listPerson;
	}

	/**
	 * Insert a new person to the table SM_USERS.
	 *
	 * @param person the person
	 */
	public void insertPerson(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Person must not be null");
		}

		try (PreparedStatement insert = getConnection().prepareStatement(INSERTPERSON)) {
			insert.setString(1, person.getID());
			insert.setString(2, person.getFirstName());
			insert.setString(3, person.getLastName());
			insert.setString(4, person.getNickname());
			insert.setString(5, person.getEmail());
			getConnection().setAutoCommit(true);
			final int affectedRows = insert.executeUpdate();
			if (affectedRows != 1) {
				throw new RuntimeException("Failed to add new Person to database");
			}
		} catch (SQLException throwables) {
			System.out.println("Could not insert Person \n" + throwables.getMessage());
		}
	}

	/**
	 * Update the person firstname for the given ID
	 *
	 * @param firstName the new first name to be updated
	 * @param iD        the ID of the person to be updated
	 */
	public void updateFN(String firstName, String iD) {
		try (PreparedStatement update = getConnection().prepareStatement(UPDATEPERSONFN)) {
			update.setString(1, firstName);
			update.setString(2, iD);

			getConnection().setAutoCommit(true);
			final int affectedRows = update.executeUpdate();
			if (affectedRows != 1) {
				throw new RuntimeException("Failed to update first name");
			}

		} catch (SQLException throwables) {
			System.out.println("Could not update first name \n" + throwables.getMessage());
		}
	}

	/**
	 * Update the person lastname for the given ID
	 *
	 * @param lastName the new last name to be updated
	 * @param iD       the ID of the person to be updated
	 */
	public void updateLN(String lastName, String iD) {
		try (PreparedStatement update = getConnection().prepareStatement(UPADTEPERSONLN)) {
			update.setString(1, lastName);
			update.setString(2, iD);
			getConnection().setAutoCommit(true);
			final int affectedRows = update.executeUpdate();
			if (affectedRows != 1) {
				throw new RuntimeException("Failed to update last name");
			}

		} catch (SQLException throwables) {
			System.out.println("Could not update last name \n" + throwables.getMessage());
		}
	}

	/**
	 * Update the person nickname for the given ID
	 *
	 * @param nikeName the new nickname to be updated
	 * @param iD       the ID of the person to be updated
	 */
	public void updateNK(String nikeName, String iD) {
		try (PreparedStatement update = getConnection().prepareStatement(UPADTEPERSONNK)) {
			update.setString(1, nikeName);
			update.setString(2, iD);
			getConnection().setAutoCommit(true);
			final int affectedRows = update.executeUpdate();
			if (affectedRows != 1) {
				throw new RuntimeException("Failed to update nikename");
			}

		} catch (SQLException throwables) {
			System.out.println("Could not update nikename \n" + throwables.getMessage());
		}
	}

	/**
	 * Update the person email for the given ID
	 *
	 * @param email the new email to be updated
	 * @param iD    the ID of the person to be updated
	 */
	public void updateEM(String email, String iD) {
		try (PreparedStatement update = getConnection().prepareStatement(UPADTEPERSONEM)) {
			update.setString(1, email);
			update.setString(2, iD);
			getConnection().setAutoCommit(true);
			final int affectedRows = update.executeUpdate();
			if (affectedRows != 1) {
				throw new RuntimeException("Failed to email");
			}

		} catch (SQLException throwables) {
			System.out.println("Could not update email \n" + throwables.getMessage());
		}
	}

	/**
	 * Delete the desire person by ID 
	 *
	 * @param iD the iD of the person to be deleted
	 */
	public void deletePerson(String iD) {
		try (PreparedStatement delete = getConnection().prepareStatement(DELETEPERSON)) {
			delete.setString(1, iD);

			final int affectedRows = delete.executeUpdate();
			if (affectedRows != 1) {
				throw new RuntimeException("Failed to delete Person");
			}
		} catch (SQLException throwables) {
			System.out.println("Could not delete Person \n" + throwables.getMessage());
		}
	}

}

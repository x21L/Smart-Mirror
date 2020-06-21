package lukas.wais.smart.mirror.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lukas.wais.smart.mirror.model.Person;
import lukas.wais.smart.mirror.model.PersonFields;

public class DBControllerPerson extends DBController {

	private static final String TABLENAME = "SM_USERS";
	
	private static final String SELECTALLPERSONS = "SELECT * FROM "+ TABLENAME;
	private static final String SELECTALL = "SELECT * FROM " + TABLENAME + " WHERE " + PersonFields.USRID + " =?";

	private static final String INSERTPERSON = "INSERT INTO " + TABLENAME + " (" + PersonFields.USRID+ "," + PersonFields.USRFNAME + ","
			+ PersonFields.USRLNAME + "," + PersonFields.USRNKNAME + "," + PersonFields.USREMAIL + ") "
			+ "VALUES(?,?,?,?,?)";

	private static final String UPDATEPERSONFN = "UPDATE " + TABLENAME + " SET " + PersonFields.USRFNAME + "=? WHERE "
			+ PersonFields.USRID + "=?";

	private static final String UPADTEPERSONLN = "UPDATE " + TABLENAME + " SET " + PersonFields.USRLNAME + "= ? WHERE "
			+ PersonFields.USRID + "=?";

	private static final String UPADTEPERSONNK = "UPDATE " + TABLENAME + " SET " + PersonFields.USRNKNAME + "=? WHERE "
			+ PersonFields.USRID + "=?";

	private static final String UPADTEPERSONEM = "UPDATE " + TABLENAME + " SET " + PersonFields.USREMAIL + "= ? WHERE "
			+ PersonFields.USRID + "=?";

	private static final String DELETEPERSON = "DELETE FROM " + TABLENAME + " WHERE " + PersonFields.USRID + " =?";

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
		}
		// TODO proper exception handling
		catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return nk;
	}

	public static List<Person> selectAllPersons() {
		List<Person> listPerson = new ArrayList<Person>();
		try {
			try (PreparedStatement selectPersonNk = getConnection().prepareStatement(SELECTALLPERSONS)) {
				try (final ResultSet resultSet = selectPersonNk.executeQuery()) {
					while (resultSet.next()) {
						listPerson.add(new Person(
								resultSet.getString(PersonFields.USRID.toString()),
								resultSet.getString(PersonFields.USRFNAME.toString()),
								resultSet.getString(PersonFields.USRLNAME.toString()),
								resultSet.getString(PersonFields.USRNKNAME.toString()),
								resultSet.getString(PersonFields.USREMAIL.toString())));
					}
				}
			}
		}
		// TODO proper exception handling
		catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return listPerson;
	}
	
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

	public void deletePerson(String iD ) {
		try (PreparedStatement delete = getConnection().prepareStatement(DELETEPERSON)) {
			delete.setString(2, iD);

			final int affectedRows = delete.executeUpdate();
			if (affectedRows != 1) {
				throw new RuntimeException("Failed to delete Person");
			}
		} catch (SQLException throwables) {
			System.out.println("Could not delete Person \n" + throwables.getMessage());
		}
	}

}

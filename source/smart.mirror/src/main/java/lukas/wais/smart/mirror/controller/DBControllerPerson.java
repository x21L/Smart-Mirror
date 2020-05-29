package lukas.wais.smart.mirror.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lukas.wais.smart.mirror.model.Person;

public class DBControllerPerson extends DBController {

	private static final String TABLENAME = "SM_USERS";

	private static final String SELECTALL = "SELECT * FROM" + TABLENAME + " WHERE " + PersonFields.USRID + " =?";

	private static final String INSERTPERSON = "INSERT INTO " + TABLENAME + " (" + PersonFields.USRFNAME + ","
			+ PersonFields.USRLNAME + "," + PersonFields.USRNKNAME + "," + PersonFields.USREMAIL + ") "
			+ "VALUES(?,?,?,?)";

	private static final String UPDATEPERSONFN = "UPDATE " + TABLENAME + " SET " + PersonFields.USRFNAME + "=? WHERE "
			+ PersonFields.USRID + "=?";

	private static final String UPADTEPERSONLN = "UPDATE " + TABLENAME + " SET " + PersonFields.USRLNAME + "= ? WHERE "
			+ PersonFields.USRID + "=?";

	private static final String UPADTEPERSONNK = "UPDATE " + TABLENAME + " SET " + PersonFields.USRNKNAME + "=? WHERE "
			+ PersonFields.USRID + "=?";

	private static final String UPADTEPERSONEM = "UPDATE " + TABLENAME + " SET " + PersonFields.USREMAIL + "= WHERE "
			+ PersonFields.USRID + "=?";

	private static final String DELETEPERSON = "DELETE FROM " + TABLENAME + " WHERE " + PersonFields.USRID;

	public Person selectPerson(int iD) {
		Person nk = null;
		try {
			try (PreparedStatement selctPersonNk = getConnection().prepareStatement(SELECTALL)) {
				try (final ResultSet resultSet = selctPersonNk.executeQuery()) {
					while (resultSet.next()) {
						nk = new Person(resultSet.getString(PersonFields.USRFNAME.toString()),
								resultSet.getString(PersonFields.USRLNAME.toString()),
								resultSet.getString(PersonFields.USRNKNAME.toString()),
								resultSet.getString(PersonFields.USREMAIL.toString()));
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
			getConnection().setAutoCommit(true);
			final int affectedRows = insert.executeUpdate();
			if (affectedRows != 1) {
				throw new RuntimeException("Failed to add new Person to database");
			}
		} catch (SQLException throwables) {
			System.out.println("Could not insert Person \n" + throwables.getMessage());
		}
	}
	
	public void updateFN(String firstName) {
		try (PreparedStatement update = getConnection().prepareStatement(UPDATEPERSONFN)) {
            update.setString(1, firstName);
            getConnection().setAutoCommit(true);
            final int affectedRows = update.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Failed to update first name");
            }

        } catch (SQLException throwables) {
            System.out.println("Could not update first name \n" + throwables.getMessage());
        }
	}
	
	public void updateLN(String lastName) {
		try (PreparedStatement update = getConnection().prepareStatement(UPADTEPERSONLN)) {
            update.setString(1, lastName);
            getConnection().setAutoCommit(true);
            final int affectedRows = update.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Failed to update last name");
            }

        } catch (SQLException throwables) {
            System.out.println("Could not update last name \n" + throwables.getMessage());
        }
	}
	
	public void updateNK(String nikeName) {
		try (PreparedStatement update = getConnection().prepareStatement(UPADTEPERSONNK)) {
            update.setString(1, nikeName);
            getConnection().setAutoCommit(true);
            final int affectedRows = update.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Failed to update nikename");
            }

        } catch (SQLException throwables) {
            System.out.println("Could not update nikename \n" + throwables.getMessage());
        }
	}
	
	public void updateEM(String email) {
		try (PreparedStatement update = getConnection().prepareStatement(UPADTEPERSONEM)) {
            update.setString(1, email);
            getConnection().setAutoCommit(true);
            final int affectedRows = update.executeUpdate();
            if (affectedRows != 1) {
                throw new RuntimeException("Failed to email");
            }

        } catch (SQLException throwables) {
            System.out.println("Could not update email \n" + throwables.getMessage());
        }
	}

}

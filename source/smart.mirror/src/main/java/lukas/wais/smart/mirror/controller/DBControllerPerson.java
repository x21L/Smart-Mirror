package lukas.wais.smart.mirror.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lukas.wais.smart.mirror.model.Person;

public class DBControllerPerson extends DBController {

	private static final String TABLENAME = "SM_USERS";

	private static final String SELECTALL = "SELECT * FROM" + TABLENAME + " WHERE " + PersonFields.USRID +" =?";

	private static final String insertPerson = "INSERT INTO " + TABLENAME + " (" + PersonFields.USRVNAME + ","
			+ PersonFields.USRNNAME + "," + PersonFields.USRNKNAME + "," + PersonFields.USREMAIL + ") "
			+ "VALUES(?,?,?,?)";

	private static final String UPDATEPERSONVN = "UPDATE " + TABLENAME + " SET " + PersonFields.USRVNAME + "=? WHERE "
			+ PersonFields.USRID + "=?";

	private static final String UPADTEPERSONNN = "UPDATE " + TABLENAME + " SET " + PersonFields.USRNNAME + "= ? WHERE "
			+ PersonFields.USRID + "=?";

	private static final String UPADTEPERSONNK = "UPDATE " + TABLENAME + " SET " + PersonFields.USRNKNAME + "=? WHERE "
			+ PersonFields.USRID + "=?";

	private static final String UPADTEPERSONEM = "UPDATE " + TABLENAME + " SET " + PersonFields.USREMAIL + "= WHERE "
			+ PersonFields.USRID + "=?";

	private static final String DeletePersion = "DELETE FROM " + TABLENAME + " WHERE " + PersonFields.USRID;

	public Person selectPerson(int iD) {
		Person nk = null;

		try {
			// try-statements for auto-closing
			try (PreparedStatement selctPersonNk = getConnection().prepareStatement(SELECTALL)) {
				try (final ResultSet resultSet = selctPersonNk.executeQuery()) {
					while (resultSet.next()) {
//						nk = new Person(resultSet.getString(PersonFields.USRVNAME.toString()),
//								resultSet.getString(PersonFields.USRNNAME.toString()),
//								resultSet.getString(PersonFields.USRNKNAME.toString()),
//								resultSet.getString(PersonFields.USREMAIL.toString())
//								);

					}
				}
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return null;

	}

}

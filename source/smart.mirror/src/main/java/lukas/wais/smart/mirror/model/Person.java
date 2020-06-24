package lukas.wais.smart.mirror.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * Represents Persons, the users of the mirror.
 * 
 * @author Lukas Wais
 *
 */
public class Person {
	private String ID;
	private final StringProperty firstName;
	private final StringProperty lastName;
	private final StringProperty nickname;
	private final StringProperty email;

	public Person(String firstName, String lastName, String nickname, String email) {
		super();
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.nickname = new SimpleStringProperty(nickname);
		this.email = new SimpleStringProperty(email);
	}

	public Person(String ID, String firstName, String lastName, String nickname, String email) {
		super();
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.nickname = new SimpleStringProperty(nickname);
		this.email = new SimpleStringProperty(email);
		this.ID = ID;
	}

	/**
	 * 
	 * @return ID of the User as String.
	 */
	public String getID() {
		return ID;
	}

	/**
	 * 
	 * @return ID of the User as StringProperty.
	 */
	public StringProperty firstNameProperty() {
		return firstName;
	}

	/**
	 * 
	 * @return the last name of the User as StringProperty.
	 */
	public StringProperty lastNameProperty() {
		return lastName;
	}

	/**
	 * 
	 * @return the nickname of the User as StringProperty.
	 */
	public StringProperty nicknameProperty() {
		return nickname;
	}

	/**
	 * 
	 * @return the email of the User as StringProperty.
	 */
	public StringProperty emailProperty() {
		return email;
	}

	/**
	 * 
	 * @return the first name of the User as String.
	 */
	public String getFirstName() {
		return firstName.get();
	}

	/**
	 * 
	 * @return the last name of the User as String.
	 */
	public String getLastName() {
		return lastName.get();
	}

	/**
	 * 
	 * @return the nickname of the User as String.
	 */
	public String getNickname() {
		return nickname.get();
	}

	/**
	 * 
	 * @return the email of the User as String.
	 */
	public String getEmail() {
		return email.get();
	}

	/**
	 * Sets the ID of the user.
	 * 
	 * @param iD
	 */
	public void setID(String ID) {
		this.ID = ID;
	}

	@Override
	public String toString() {
		return nickname.get();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
}

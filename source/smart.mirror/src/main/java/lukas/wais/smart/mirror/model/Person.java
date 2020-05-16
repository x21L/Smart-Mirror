package lukas.wais.smart.mirror.model;

import java.util.UUID;

import javafx.beans.property.StringProperty;

public class Person {
	private final String ID;
	private final StringProperty firstName;
	private final StringProperty secondName;
	private final StringProperty email;

	public Person(StringProperty firstName, StringProperty secondName, StringProperty email) {
		super();
		this.ID = UUID.randomUUID().toString();
		this.firstName = firstName;
		this.secondName = secondName;
		this.email = email;
	}

	public String getID() {
		return ID;
	}

	public StringProperty getFirstName() {
		return firstName;
	}

	public StringProperty getSecondName() {
		return secondName;
	}

	public StringProperty getEmail() {
		return email;
	}
}

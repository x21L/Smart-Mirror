package lukas.wais.smart.mirror.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javafx.beans.property.StringProperty;

public class Person {
	private final String ID;
	private final StringProperty firstName;
	private final StringProperty secondName;
	private final StringProperty email;
	private final List<Integer> widgets;

	public Person(StringProperty firstName, StringProperty secondName, StringProperty email) {
		super();
		this.ID = UUID.randomUUID().toString();
		this.firstName = firstName;
		this.secondName = secondName;
		this.email = email;
		this.widgets = new ArrayList<>();
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

	public List<Integer> getWidgets() {
		return widgets;
	}

	@Override
	public String toString() {
		return "Person [ID=" + ID + ", firstName=" + firstName + ", secondName=" + secondName + ", email=" + email
				+ ", widgets=" + widgets + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
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
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}
}

package lukas.wais.smart.mirror.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.StringProperty;

public class Person {
	private int ID;
	private final StringProperty firstName;
	private final StringProperty secondName;
	private final StringProperty nickname;
	private final StringProperty email;
	private final List<Integer> widgets;

	public Person(StringProperty firstName, StringProperty secondName, StringProperty nickname, StringProperty email) {
		super();
		this.firstName = firstName;
		this.secondName = secondName;
		this.nickname = nickname;
		this.email = email;
		this.widgets = new ArrayList<>();
	}
	
	public Person(StringProperty firstName, StringProperty secondName, StringProperty nickname, StringProperty email, int ID) {
		super();
		this.firstName = firstName;
		this.secondName = secondName;
		this.nickname = nickname;
		this.email = email;
		this.widgets = new ArrayList<>();
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}

	public StringProperty firstNameProperty() {
		return firstName;
	}

	public StringProperty secondNameProperty() {
		return secondName;
	}

	public StringProperty nicknameProperty() {
		return nickname;
	}

	public StringProperty emailProperty() {
		return email;
	}

	public String getFirstName() {
		return firstName.get();
	}

	public String getSecondName() {
		return secondName.get();
	}

	public String getNickname() {
		return nickname.get();
	}

	public String getEmail() {
		return email.get();
	}

	public void setID(int iD) {
		ID = iD;
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
		result = prime * result + ID;
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
		if (ID != other.ID)
			return false;
		return true;
	}
}

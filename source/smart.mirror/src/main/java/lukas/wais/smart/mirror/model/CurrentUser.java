package lukas.wais.smart.mirror.model;

import java.util.ArrayList;
import java.util.List;

public class CurrentUser {
	Person person;
	ArrayList<String> widgets;
	public static CurrentUser instance;
	
	public static CurrentUser getInstance() {
		if (instance == null) instance = new CurrentUser();
		return instance;
	}

	public Person getUser() {
		return person;
	}

	public void setUser(Person person) {
		this.person = person;
	}



	public ArrayList<String> getWidgets() {
		return widgets;
	}	
}

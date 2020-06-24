package lukas.wais.smart.mirror.model;

import java.util.ArrayList;
/**
 * Defines the current user. 
 * Implemented as Singleton. The default value of the user is null.
 *
 * @author Lukas Wais
 *
 */
public class CurrentUser {
	Person person;
	ArrayList<String> widgets;
	public static CurrentUser instance;
	
	/**
	 * 
	 * @return instance of the CurrentUser
	 */
	public static CurrentUser getInstance() {
		if (instance == null) instance = new CurrentUser();
		return instance;
	}

	/**
	 * This methods sets the current user. You are changing the current profile indirectly. 
	 * It personalizes the mirror.
	 * 
	 * @param person sets the new person from which the settings are loaded.
	 */
	public void setUser(Person person) {
		this.person = person;
	}

	public ArrayList<String> getWidgets() {
		return widgets;
	}	
}

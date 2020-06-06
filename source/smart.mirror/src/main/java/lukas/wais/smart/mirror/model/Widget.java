package lukas.wais.smart.mirror.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.geometry.Pos;
import javafx.scene.Node;

public class Widget {
	/*
	 * Constants for widgets
	 */
	private final String name;
	/*
	 * Widget variables
	 */
	private final int width;
	private final int height;

	public Widget() {
		this.name = "Omar";
		this.width = 150;
		this.height = 150;
	}

	/*
	 * text widgets for greetings
	 */
	public Node getGreetings() {
		String greetings = "";
		Date date = new Date();   // given date
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(date);   // assigns calendar to given date 
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		/*
		 * text for greeting
		 */
		if (hour >= 6 && hour < 10) greetings = "Good morning";
		else if (hour >= 10 && hour < 13) greetings = "Good day";	 
		else if (hour >= 13 && hour < 18) greetings = "Good afternoon";
		else if (hour >= 18 && hour < 21) greetings = "Good evening";
		else greetings = "Good night";
		
		System.out.println(hour);
					
		return TileBuilder.create().skinType(SkinType.TEXT)
				.prefSize(300, 75)
				.description(greetings + " " + name)
				.descriptionAlignment(Pos.CENTER)
				.textVisible(true).build();
	}
	
	/*
	 * calendar
	 */
	public Node getCalendar() {
		return TileBuilder
				.create()
				.skinType(SkinType.CALENDAR)
				.build();
	}
}

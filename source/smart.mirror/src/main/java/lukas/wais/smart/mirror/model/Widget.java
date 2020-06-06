package lukas.wais.smart.mirror.model;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfxweather.ConditionAndIcon;
import eu.hansolo.tilesfxweather.DataPoint;
import eu.hansolo.tilesfxweather.EphemerisTileSkin;
import eu.hansolo.tilesfxweather.Unit;
import eu.hansolo.tilesfxweather.WeatherTileSkin;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.tools.Country;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class Widget {
	/*
	 * Constants for widgets
	 */
	private final String name;
	private final Locale location;
	/*
	 * Widget variables
	 */
	private final int width;
	private final int height;

	public Widget() {
		this.name = "Omar";
		this.width = 150;
		this.height = 150;
		this.location = Locale.GERMANY;
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
				.prefSize(width, height)
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
	
	/*
	 * clock
	 */
	public Node getClock( ) {
		return TileBuilder.create()
                .skinType(SkinType.CLOCK)
                .prefSize(width, height)
                .dateVisible(true)
                .locale(location)
                .running(true)
                .build();
	}
	
	/*
	 * world map
	 */
	public Node getWorldMap() {
		// colors
	      for (int i = 0; i < Country.values().length ; i++) {
	            double value = new Random().nextInt(10);
	            Color  color;
	            if (value > 8) {
	                color = Tile.RED;
	            } else if (value > 6) {
	                color = Tile.ORANGE;
	            } else if (value > 4) {
	                color = Tile.YELLOW_ORANGE;
	            } else if (value > 2) {
	                color = Tile.GREEN;
	            } else {
	                color = Tile.BLUE;
	            }
	            Country.values()[i].setColor(color);
	        }
		return TileBuilder.create()
                .prefSize(300, height)
                .skinType(SkinType.WORLDMAP)
                .title("World Map")
                .textVisible(false)
                .build();
	}
	
	/*
	 * TODO make weather dynamic with JSON 
	 * weather
	 */
	public Node getWeather() {
		// the following must be made dynamic
		DataPoint today = new DataPoint();
		today.setTime(LocalDateTime.now());
		today.setSummary("Partly Cloudy");
		today.setCondition(ConditionAndIcon.PARTLY_CLOUDY_DAY);
		today.setTemperature(9.65);
		today.setPressure(1020.7);
		today.setHumidity(0.55);
		today.setWindSpeed(15.94);
		today.setTemperatureMin(0);
		today.setTemperatureMax(0);
		today.setSunriseTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(5, 38)));
		today.setSunsetTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 44)));
		// now the actual widget starts
		Tile weatherTile = TileBuilder
				.create()
				.skinType(SkinType.CUSTOM)
				.prefSize(width, height)
				.title("Weather").unit("\u00B0C")
				.minValue(0)
				.maxValue(150)
				.decimals(1)
				.tickLabelDecimals(0)
				.customDecimalFormatEnabled(true)
				.customDecimalFormat(new DecimalFormat("#"))
				.time(ZonedDateTime.now(ZoneId.of("Europe/Berlin")))
				.build();

		WeatherTileSkin weatherTileSkin = new WeatherTileSkin(weatherTile);
		weatherTileSkin.setDataPoint(today, Unit.CA);
		weatherTile.setSkin(weatherTileSkin);

		Tile tile2 = new Tile();
		TileBuilder.create()
		.skinType(SkinType.CUSTOM)
		.prefSize(width, height)
		.title("Ephemeris")
		.build();
		tile2.setSkin(new EphemerisTileSkin(tile2));
		return tile2;
	}
	
	/*
	 * TODO make stocks dynamic with JSON
	 * stocks
	 */
	public Node getStocks() {
		return TileBuilder.create()
        .skinType(SkinType.STOCK)
        .prefSize(width, height)
        .title("Stock Tile")
        .minValue(0)
        .maxValue(1000)
        .averagingPeriod(100)
        .build();
	}
}

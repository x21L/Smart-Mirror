package lukas.wais.smart.mirror.model;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
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
import javafx.scene.web.WebView;

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
	public Node getGreetings(String greetings) {
		return TileBuilder.create().skinType(SkinType.TEXT).prefSize(width, height).description(greetings)
				.descriptionAlignment(Pos.CENTER).textVisible(true).build();
	}

	/*
	 * calendar
	 */
	public Node getCalendar() {
		return TileBuilder.create().skinType(SkinType.CALENDAR).build();
	}

	/*
	 * clock
	 */
	public Node getClock() {
		return TileBuilder.create().skinType(SkinType.CLOCK).prefSize(width, height).dateVisible(true).locale(location)
				.running(true).build();
	}

	/*
	 * world map
	 */
	public Node getWorldMap() {
		// colors
		for (int i = 0; i < Country.values().length; i++) {
			double value = new Random().nextInt(10);
			Color color;
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
				.prefSize(width, height)
				.skinType(SkinType.WORLDMAP)
				.title("World Map")
				.textVisible(false).build();
	}
		
	/*
	 * wheater
	 */
	public Node getWeather() {
		WebView weatherView = new WebView();
		weatherView.getEngine().load("https://openweathermap.org/weathermap");
		weatherView.setMaxSize(300, 300);
		return weatherView;
	}

	/*
	 * html widgets
	 */	
	public Node getAppleStocks() {
		return htmlToNode("../html/applstocks.html", 300, 300);
	}
	
	public Node getMarkets() {
		return htmlToNode("../html/markets.html", 300, 300);
	}
	
	public Node getCovid() {
		return htmlToNode("../html/covid.html", 300, 300);
	}
	
	private Node htmlToNode(String path, double width, double height) {
		URL url = this.getClass().getResource(path);
		WebView webView = new WebView();
		webView.getEngine().load(url.toString());
		webView.setMaxSize(width, height);
		return webView;
	}
}

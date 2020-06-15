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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

public class Widget {
	/*
	 * Constants for widgets
	 */
	private final String name;
	private final Locale location;

	public Widget() {
		this.name = "Omar";
		this.location = Locale.GERMANY;
	}

	/*
	 * text widgets for greetings
	 */
	public Node getGreetings(String greetings) {
		return TileBuilder.create().skinType(SkinType.TEXT).description(greetings).prefSize(695, 340)
				.descriptionAlignment(Pos.CENTER).textVisible(true).build();
	}

	/*
	 * jokes
	 */
	public Node getJoke() {
		Button jokeButton = new Button("Tell me a funny joke");
		jokeButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		       new Joke().tell();
		    }
		}); // getOnAction(new Joke().tell()).roundedCorners(true).animated(true);
		return TileBuilder.create().skinType(SkinType.CUSTOM).title("Jokes")
				.graphic(jokeButton).build();
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
		return TileBuilder.create().skinType(SkinType.CLOCK).dateVisible(true).locale(location).running(true).build();
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
		return TileBuilder.create().skinType(SkinType.WORLDMAP).title("World Map").build();
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
	 * public transport
	 */
	public Node getPublicTransport() {
		WebView transportView = new WebView();
		transportView.getEngine().load("https://maps.google.com/landing/transit/index.html");
		return transportView;
	}

	/*
	 * html widgets
	 */
	public Node getMarkets() {
		return htmlToNode("../html/markets.html", 680, 680);
	}

	public Node getCovid() {
		return htmlToNode("../html/covid.html", 680, 680);
	}

	private Node htmlToNode(String path, double width, double height) {
		URL url = this.getClass().getResource(path);
		WebView webView = new WebView();
		webView.getEngine().load(url.toString());
		webView.setPrefSize(width, height);
		return webView;
	}
}

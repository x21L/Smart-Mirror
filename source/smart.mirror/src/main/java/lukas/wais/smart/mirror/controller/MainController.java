package lukas.wais.smart.mirror.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.tools.DoubleExponentialSmoothingForLinearSeries.Model;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfxweather.ConditionAndIcon;
import eu.hansolo.tilesfxweather.DataPoint;
import eu.hansolo.tilesfxweather.EphemerisTileSkin;
import eu.hansolo.tilesfxweather.Unit;
import eu.hansolo.tilesfxweather.WeatherTileSkin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import lukas.wais.smart.mirror.model.Widget;
 

public class MainController {

	@FXML // fx:id="background"
	private Pane background; // Value injected by FXMLLoader

	@FXML // fx:id="videoBackground"
	private MediaView videoBackground; // Value injected by FXMLLoader

	@FXML // fx:id="settingsBtn"
	private Button settingsBtn; // Value injected by FXMLLoader

	@FXML // fx:id="tilePane"
	private TilePane tilePane; // Value injected by FXMLLoader

	
	private final static String SELECTUSER= "SELECT * FROM SM_USERS";
	private final static String SELECTWIDGET = "SELECT * FROM SM_WIDGET";
	private final static String SELECTPROFILE = "SELECT * FROM SM_PROFILE";

	@FXML
	private void initialize() {
		/*
		dbToXML(SELECTUSER, "userTable");
		dbToXML(SELECTWIDGET,"widgetTable");
		dbToXML(SELECTPROFILE,"profileTable");
		*/
		
		/*
		 * background video
		 */
		MediaPlayer mediaPlayer = new MediaPlayer(
				new Media(getClass().getResource("../videos/Beach.mp4").toExternalForm()));
		videoBackground.setMediaPlayer(mediaPlayer);
		// mediaPlayer.setAutoPlay(true);
		mediaPlayer.setOnEndOfMedia((Runnable) () -> {
			mediaPlayer.seek(Duration.ZERO);
			mediaPlayer.play();
		});
		mediaPlayer.play();

		/*
		 * tile pane with the widgets
		 */
		double TILE_WIDTH = 150;
		double TILE_HEIGHT = 150;
		Widget widget = new Widget();
		tilePane.setHgap(10);
		tilePane.setVgap(10);
		tilePane.getChildren().add(widget.getGreetings());
		tilePane.getChildren().add(TileBuilder.create().skinType(SkinType.CLOCK).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("Clock Tile").text("Whatever text").dateVisible(true).locale(Locale.US).running(true).build());
		tilePane.getChildren().add(widget.getCalendar());

		/*
		 * weather
		 */
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

		Tile weatherTile = TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("Weather").unit("\u00B0C").minValue(0).maxValue(150).decimals(1).tickLabelDecimals(0)
				.customDecimalFormatEnabled(true).customDecimalFormat(new DecimalFormat("#"))
				.time(ZonedDateTime.now(ZoneId.of("Europe/Berlin"))).build();

		WeatherTileSkin weatherTileSkin = new WeatherTileSkin(weatherTile);
		weatherTileSkin.setDataPoint(today, Unit.CA);
		weatherTile.setSkin(weatherTileSkin);

		Tile tile2 = new Tile();
		TileBuilder.create().skinType(SkinType.CUSTOM).prefSize(TILE_WIDTH, TILE_HEIGHT).title("Ephemeris").build();
		tile2.setSkin(new EphemerisTileSkin(tile2));

		tilePane.getChildren().add(tile2);
	}

	@FXML
	void openSettings() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/CreateUserUI.fxml"));
		try {
			Parent parent = fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Settings");
			stage.setResizable(false);
			stage.setScene(new Scene(parent));
			stage.show();
		} catch (IOException e) {
			System.out.println("Could create user ui \n");
			System.out.println(e.getMessage());
		}
	}
	/*
	private void dbToXML() {
//		System.out.println(getClass().getResource("../xml/H2DB.xml"));
		try {
			Document dbToXml = new TableToXML().generateXML();
			DOMSource source = new DOMSource(dbToXml);

			File file = new File("../xml/H2DB.xml");
			System.out.println("file = " + file);
			FileWriter writer = new FileWriter(file);
			StreamResult result = new StreamResult(writer);

//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
//			Transformer transformer = transformerFactory.newTransformer();
//			transformer.transform(source, result);

		} catch (TransformerException e) {
			System.out.println("Could not create XML file (TransformerException) \n" + e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("Could not create XML file (ParserConfigurationException) \n" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Could not create XML file (IOException) \n" + e.getMessage());
		}
	}
	*/

}

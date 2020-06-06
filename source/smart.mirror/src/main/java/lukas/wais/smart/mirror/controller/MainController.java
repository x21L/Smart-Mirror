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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
import javafx.scene.Node;
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
		tilePane.setHgap(10);
		tilePane.setVgap(10);

		// add the widgets
		getWidgets().forEach((key, value) -> {
			tilePane.getChildren().add((Node) value);
		});
		

		/*
		 * weather
		 */
		
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
	
	// gets all the widgets the user wants
	private Map<Integer, Object> getWidgets() {
		Widget widget = new Widget();
		
		Map<Integer, Object> widgets = new HashMap<>();
		Integer key = 0;
		/*
		 * TODO some loop for the widgets from the db
		 */
		widgets.put(key++, widget.getGreetings());
		widgets.put(key++, widget.getClock());
		widgets.put(key++, widget.getWorldMap());
		widgets.put(key++, widget.getCalendar());
		widgets.put(key++, widget.getWeather());
		
		return widgets;
	}
}

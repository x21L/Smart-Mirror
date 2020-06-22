/*
 * @author Omar Duenas
 * @author Lukas Wais
 * @version 1.0
 * @since 1.0
 */
package lukas.wais.smart.mirror.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
import lukas.wais.smart.mirror.model.Person;
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

	@FXML // fx:id="greetingsPane"
	private Pane greetingsPane; // Value injected by FXMLLoader
	/*
	 * select person
	 */
	private final static Person user = DBControllerPerson.selectPerson("1");
	// new Person("Peter", "Griffin", "Pete", "...");//
	private final static List<String> widgetsUser = DBControllerWidget
			.selectWidget("1");

	@FXML
	private void initialize() {
		System.out.println(DBControllerPerson.selectAllPersons());

		xmlToDb("../xml/userTable.xml");
		xmlToDb("../xml/widgetTable.xml");
		xmlToDb("../xml/profileTable.xml");

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
//		tilePane.setHgap(10);
//		tilePane.setVgap(10);

		// add the widgets
		greetingsPane.getChildren().add(new Widget().getGreetings(setGreetings(user.getNickname())));
		getWidgets().forEach(node -> tilePane.getChildren().add(node));

		// Polly.speak(setGreetings(user.getNickname()));
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

	/**
	 * With this function the data from the XML will inserted in the database
	 * In case the tables do not exits, they will be created
	 * Otherwise the data will be inserted to the corresponding tables.
	 * 
	 * @param inputFile XML file where the DB Data is stored
	 */
	private void xmlToDb(String inputFile) {
		System.out.println("input file = " + inputFile);
		String path = getClass().getResource(inputFile).toString();
		System.out.println("path = " + path);
		File file = new File(getClass().getResource(inputFile).getFile());
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);

			TableToXML.xmlToTable(document);
		} catch (ParserConfigurationException e) {
			System.out.println("Error with Parser configuration \n " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Error with SAX \n " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error with I/O \n " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error with SQL \n " + e.getMessage());
		}
	}

	// gets all the widgets the user wants
	private List<Node> getWidgets() {
		Widget widget = new Widget();

		List<Node> widgets = new ArrayList<>();

		widgets.add(widget.getClock());
//		widgets.add(widget.getWorldMap());
		widgets.add(widget.getJoke());
		widgets.add(widget.getCalendar());
		widgets.add(widget.getMarkets());
		widgets.add(widget.getCovid());
		widgets.add(widget.getPublicTransport());
//		widgets.add(widget.getWeather());

		return widgets;
	}

	private String setGreetings(String name) {
		String greetings = "";
		Date date = new Date(); // given date
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		calendar.setTime(date); // assigns calendar to given date
		int hour = calendar.get(Calendar.HOUR_OF_DAY); // text for greeting

		if (hour >= 6 && hour < 10)
			greetings = "Good morning";
		else if (hour >= 10 && hour < 13)
			greetings = "Good day";
		else if (hour >= 13 && hour < 18)
			greetings = "Good afternoon";
		else if (hour >= 18 && hour < 21)
			greetings = "Good evening";
		else
			greetings = "Good night";
		return greetings + " " + name;
	}
}

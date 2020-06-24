package lukas.wais.smart.mirror.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import lukas.wais.smart.mirror.model.CurrentUser;
import lukas.wais.smart.mirror.model.ImageDetection;
import lukas.wais.smart.mirror.model.Person;
import lukas.wais.smart.mirror.model.Polly;
import lukas.wais.smart.mirror.model.Widget;

/**
 * @author Omar Duenas
 * @author Lukas Wais
 * @author Jakob Zethofer
 *
 */
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

	private final List<String> widgetsUser = new ArrayList<>();
	private boolean mirrorActive = true;
	
	
	@FXML
	private void initialize() {

//		drop table sm_profile;
//		drop table sm_users;
//		drop table sm_widget;

//		xmlToDb("../xml/userTable.xml");
//		xmlToDb("../xml/widgetTable.xml");
//		xmlToDb("../xml/profileTable.xml");
		Person user = CurrentUser.getInstance().getUser();
		System.out.println("before user = " + user);
		if (user == null) {
			user = DBControllerPerson.selectPerson("1");
			widgetsUser.addAll(DBControllerWidget.selectWidget("1"));

			user = new Person("Name", "name", "pete", "");// DBControllerPerson.selectPerson("295ff6e2-b025-4bd6-bd3e-47b3de9ea4d4");
			widgetsUser.addAll(DBControllerWidget.selectWidget("295ff6e2-b025-4bd6-bd3e-47b3de9ea4d4"));
		} else {
			widgetsUser.addAll(DBControllerWidget.selectWidget(user.getID()));
		}
		System.out.println("user = " + user);
		System.out.println("widgets = " + widgetsUser);

		/*
		 * background video
		 */
		MediaPlayer mediaPlayer = new MediaPlayer(
				new Media(getClass().getResource("../videos/Beach.mp4").toExternalForm()));
		videoBackground.setMediaPlayer(mediaPlayer);
		mediaPlayer.setOnEndOfMedia((Runnable) () -> {
			mediaPlayer.seek(Duration.ZERO);
			mediaPlayer.play();
		});
		mediaPlayer.play();

		/*
		 * tile pane with the widgets
		 */

		// add the widgets
		greetingsPane.getChildren().add(new Widget().getGreetings(setGreetings(user.getNickname())));
		// start face detection
		startFaceDetection();

		// only display the widgets, the user set
		getWidgets().forEach((name, node) -> {
			if (widgetsUser.contains(name))
			tilePane.getChildren().add(node);
		});
		
		// greetings from Polly
		Polly.speak(setGreetings(user.getNickname()));
	}

	/**
	 * This method handles the start of the face detection.
	 * The {@link ImageDetection} class is initialized and the face detection method is called in a loop and set to an {@link ImageView}
	 */
	private void startFaceDetection() {
		ImageView view = new ImageView();
		view.setFitWidth(320);
		view.setFitHeight(320);
		tilePane.getChildren().add(view);
		nu.pattern.OpenCV.loadLocally();
		CascadeClassifier cascadeClassifier = new CascadeClassifier();
		File classifierFile = new File(getClass().getResource("../Classifier/haarcascade_frontalface_alt.xml").getFile());
		cascadeClassifier.load(classifierFile.getAbsolutePath());
		ImageDetection.init(cascadeClassifier, new VideoCapture(0));

		new AnimationTimer(){
			long lastUpdate = 0;
			@Override
			public void handle(long now) {
				if(now-lastUpdate>250) {
					lastUpdate = now;
					view.setImage(ImageDetection.getCaptureWithFaceDetection());
					if (ImageDetection.detected && !mirrorActive) {
						getWidgets().forEach((name, node) -> {
							if (widgetsUser.contains(name))
								tilePane.getChildren().add(node);
						});
						mirrorActive = true;
					} else if (!ImageDetection.detected && mirrorActive) {
						tilePane.getChildren().clear();
						tilePane.getChildren().add(view);
						mirrorActive = false;
					}
				}

			}
		}.start();
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
		Stage stage = (Stage) background.getScene().getWindow();
		stage.close();
	}

	/**
	 * With this function the data from the XML will inserted in the database
	 * In case the tables do not exits, they will be created
	 * Otherwise the data will be inserted to the corresponding tables.
	 * 
	 * @param inputFile XML file where the DB Data is stored
	 */
	private void xmlToDb(String inputFile) {
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
	/**
	 * Loads all available widgets from the factory in a map.
	 * The key is the name and the value is the Node object created at the factory.
	 * 
	 * @return A map with all available widgets.
	 */
	private Map<String, Node> getWidgets() {
		Widget widget = new Widget();

		Map<String, Node> widgets = new HashMap<>();

		widgets.put("Clock", widget.getClock());
		widgets.put("Jokes", widget.getJoke());
		widgets.put("Calendar", widget.getCalendar());
		widgets.put("Stocks", widget.getMarkets());
		widgets.put("Covid", widget.getCovid());
		widgets.put("Public Transport", widget.getPublicTransport());
		return widgets;
	}

	/**
	 * Defines the text for the greetings. Depending on the day time and the user's name.
	 * @param name name of the user
	 * @return text for the greetings pane
	 */
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

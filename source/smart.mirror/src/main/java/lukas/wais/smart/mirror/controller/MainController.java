package lukas.wais.smart.mirror.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

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
import lukas.wais.smart.mirror.model.Polly;
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

	private final static String SELECTUSER = "SELECT * FROM SM_USERS";
	private final static String SELECTWIDGET = "SELECT * FROM SM_WIDGET";
	private final static String SELECTPROFILE = "SELECT * FROM SM_PROFILE";
	/*
	 * select person
	 */
	private final static Person user = DBControllerPerson.selectPerson(1);

	@FXML
	private void initialize() {

		dbToXML(SELECTUSER, "userTable");
		dbToXML(SELECTWIDGET, "widgetTable");
		dbToXML(SELECTPROFILE, "profileTable");

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

		// TODO speak after loading
		speak(setGreetings(user.getNickname()));
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

	private void dbToXML(String table, String outputFile) {

		try {
			String path = "../smart.mirror/src/main/resources/lukas/wais/smart/mirror/xml/" + outputFile + ".xml";
			DOMSource domSource = new DOMSource(new TableToXML().generateXML(table));
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			File file = new File(path);

			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);

			FileWriter wr = new FileWriter(file);
			String out = sw.toString();
			System.out.println(out);
			wr.write(out);
			wr.flush();
			wr.close();

		} catch (TransformerException e) {
			System.out.println("Could not create XML file (TransformerException) \n" + e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("Could not create XML file (ParserConfigurationException) \n" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Could not create XML file (IOException) \n" + e.getMessage());
		}
	}

	// gets all the widgets the user wants
	private Map<Integer, Object> getWidgets() {
		Widget widget = new Widget();

		Map<Integer, Object> widgets = new HashMap<>();
		Integer key = 0;

		// TODO some loop for the widgets from the db

		widgets.put(key++, widget.getGreetings(setGreetings(user.getNickname())));
		widgets.put(key++, widget.getClock());
		widgets.put(key++, widget.getWorldMap());
		widgets.put(key++, widget.getCalendar());
		widgets.put(key++, widget.getWeather());

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
		return greetings = greetings + " " + name;
	}

	private void speak(String text) {
		Polly polly = new Polly(Region.getRegion(Regions.DEFAULT_REGION));
		polly.play(text);
	}
}

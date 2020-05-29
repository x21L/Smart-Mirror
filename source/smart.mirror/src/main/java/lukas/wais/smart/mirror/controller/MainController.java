package lukas.wais.smart.mirror.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController {
	/*
	 * nice MainController should come here
	 */
	@FXML
	private Pane background;

	@FXML
	private Button settingsBtn;

	@FXML
	private MediaView videoBackground;

	@FXML
	private void initialize() {
		MediaPlayer mediaPlayer = new MediaPlayer(
				new Media(getClass().getResource("../videos/Beach.mp4").toExternalForm()));
		videoBackground.setMediaPlayer(mediaPlayer);
		// mediaPlayer.setAutoPlay(true);
		mediaPlayer.setOnEndOfMedia((Runnable) () -> {
			mediaPlayer.seek(Duration.ZERO);
			mediaPlayer.play();
		});
		mediaPlayer.play();

		dbToXML();

	}

	@FXML
	void openSettings() {
		System.out.println(getClass().getResource("../fxml/CreateUserUI.fxml"));
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

	@FXML
	private void dbToXML() {
		Document doc = null;
		try {
			doc = new TableToXML().generateXML();
		} catch (TransformerException | ParserConfigurationException e) {
			System.out.println("Could not generate XML \n");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// XML to table data
//		try {
//			new TableToXML().xmlToTable(doc);
//		} catch (SQLException e) {
//			System.out.println("Could not restored XML \n");
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
	}
}

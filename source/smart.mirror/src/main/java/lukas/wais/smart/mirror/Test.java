package lukas.wais.smart.mirror;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class Test {
	public void start(Stage stage) {
		// Create and set the Scene.
		Scene scene = new Scene(new Group(), 540, 209);
		stage.setScene(scene);

		// Name and display the Stage.
		stage.setTitle("Hello Media");
		stage.show();

		// Create the media source.
		Media media = new Media("http://../videos/Beach.mp4");

		// Create the player and set to play automatically.
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setAutoPlay(true);

		// Create the view and add it to the Scene.
		MediaView mediaView = new MediaView(mediaPlayer);
		((Group) scene.getRoot()).getChildren().add(mediaView);
	}
}

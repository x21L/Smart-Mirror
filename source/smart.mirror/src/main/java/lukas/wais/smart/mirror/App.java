package lukas.wais.smart.mirror;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
    	System.out.println(getClass().getResource("fxml/MainUI.fxml"));
    	final FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/MainUI.fxml"));
    	final Parent root = loader.load();
    	primaryStage.setTitle("Smart Mirror");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
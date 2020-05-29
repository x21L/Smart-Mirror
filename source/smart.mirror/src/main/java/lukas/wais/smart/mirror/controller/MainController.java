package lukas.wais.smart.mirror.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {
	/*
	 * nice MainController should come here
	 */
    @FXML
    private Pane webView;

    @FXML
    void settingsBtn() {
    	System.out.println(getClass().getResource("../fxml/CreateUserUI.fxml"));
    	
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/CreateUserUI.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Create New User");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            System.out.println("Could create user ui");
            e.printStackTrace();
        }
    }
}

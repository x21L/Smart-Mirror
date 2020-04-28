package lukas.wais.smart.mirror.controller;
import javafx.fxml.FXML;
import javafx.scene.web.*;
public class MainController {
	
	@FXML
	private WebView testView = new WebView();
	
	@FXML
	private void initialize() {
		testView.getEngine().load("https://www.9gag.com");
	}
}

package lukas.wais.smart.mirror.controller;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateUserUI {

	@FXML // fx:id="pane"
	private TabPane pane; // Value injected by FXMLLoader

	@FXML // fx:id="firstname"
	private TextField firstname; // Value injected by FXMLLoader

	@FXML // fx:id="lastname"
	private TextField lastname; // Value injected by FXMLLoader

	@FXML // fx:id="nickname"
	private TextField nickname; // Value injected by FXMLLoader

	@FXML // fx:id="email"
	private TextField email; // Value injected by FXMLLoader

	@FXML // fx:id="submitBtn"
	private Button submitBtn; // Value injected by FXMLLoader

	@FXML // fx:id="cancelBtn"
	private Button cancelBtn; // Value injected by FXMLLoader

	@FXML
	void submit() {
		// load the css
		pane.getScene().getStylesheets().add(getClass().getResource("../css/application.css").toString());
		// required input
		if (firstname.getText().isEmpty()) {
			firstname.setPromptText("Please insert a firstname");
			firstname.getStyleClass().add("error");
		} else {
			firstname.getStyleClass().remove("error");
		}

		if (lastname.getText().isEmpty()) {
			lastname.setPromptText("Please insert a lastname");
			lastname.getStyleClass().add("error");
		} else {
			lastname.getStyleClass().remove("error");
		}
		if (nickname.getText().isEmpty()) {
			nickname.setPromptText("Please insert a nickname");
			nickname.getStyleClass().add("error");
		} else {
			nickname.getStyleClass().remove("error");
		}
		if (email.getText().isEmpty()) {
			email.setPromptText("Please insert an email");
			email.getStyleClass().add("error");
		} else {
			email.getStyleClass().remove("error");
		}
	}

	@FXML
	void cancel() {
		Stage stage = (Stage) pane.getScene().getWindow();
		stage.close();
	}
}

package lukas.wais.smart.mirror.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lukas.wais.smart.mirror.model.CurrentUser;
import lukas.wais.smart.mirror.model.Person;

public class SettingsUI {

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

	@FXML // fx:id="gridPane"
	private GridPane gridPane; // Value injected by FXMLLoader

	@FXML // fx:id="clockBox"
	private CheckBox clockBox; // Value injected by FXMLLoader

	@FXML // fx:id="jokeBox"
	private CheckBox jokeBox; // Value injected by FXMLLoader

	@FXML // fx:id="calendarBox"
	private CheckBox calendarBox; // Value injected by FXMLLoader

	@FXML // fx:id="publicTransBox"
	private CheckBox publicTransBox; // Value injected by FXMLLoader

	@FXML // fx:id="stocksBox"
	private CheckBox stocksBox; // Value injected by FXMLLoader

	@FXML // fx:id="covidBox"
	private CheckBox covidBox; // Value injected by FXMLLoader

	@FXML // fx:id="gridPaneSettings"
	private GridPane gridPaneSettings; // Value injected by FXMLLoader

	@FXML // fx:id="clockBox1"
	private CheckBox clockBox1; // Value injected by FXMLLoader

	@FXML // fx:id="jokeBox1"
	private CheckBox jokeBox1; // Value injected by FXMLLoader

	@FXML // fx:id="calendarBox1"
	private CheckBox calendarBox1; // Value injected by FXMLLoader

	@FXML // fx:id="publicTransBox1"
	private CheckBox publicTransBox1; // Value injected by FXMLLoader

	@FXML // fx:id="stocksBox1"
	private CheckBox stocksBox1; // Value injected by FXMLLoader

	@FXML // fx:id="covidBox1"
	private CheckBox covidBox1; // Value injected by FXMLLoader

	@FXML // fx:id="submitBtnSettings"
	private Button submitBtnSettings; // Value injected by FXMLLoader

	@FXML // fx:id="cancelBtnSettings"
	private Button cancelBtnSettings; // Value injected by FXMLLoader

	@FXML // fx:id="userChoice"
	private ChoiceBox<Person> userChoice; // Value injected by FXMLLoader
	
    @FXML // fx:id="switchUser"
    private Button switchUser; // Value injected by FXMLLoader

	@FXML
	public void initialize() {
		ObservableList<Person> persons = FXCollections.observableArrayList(DBControllerPerson.selectAllPersons());
		userChoice.getItems().addAll(persons);
		userChoice.getSelectionModel().selectFirst();
	}
	/*
	 * create user pane
	 */

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

		/*
		 * choose the widgets
		 */
		String ID = UUID.randomUUID().toString();
		if (!firstname.getText().isEmpty() && !lastname.getText().isEmpty() && !nickname.getText().isEmpty()
				&& !email.getText().isEmpty()) {
			new DBControllerPerson().insertPerson(
					new Person(ID, firstname.getText(), lastname.getText(), nickname.getText(), email.getText()));
			List<String> selectedCheckBoxes = getCheckedBoxes(gridPane);
			selectedCheckBoxes.forEach(widget -> DBControllerWidget.insertProfile(ID, widget));
			
			Stage stage = (Stage) pane.getScene().getWindow();
			stage.close();
			openMain();
		}
	}

	@FXML
	void cancel() {
		Stage stage = (Stage) pane.getScene().getWindow();
		stage.close();
		openMain();
	}

	/*
	 * settings pane
	 */
	@FXML
	void submitSettings() {
		List<String> selectedCheckBoxes = getCheckedBoxes(gridPaneSettings);
		System.out.println("selected user = " + userChoice.getSelectionModel().getSelectedItem().getID());

		if(!DBControllerWidget.selectWidget(userChoice.getSelectionModel().getSelectedItem().getID()).isEmpty()) {
			DBControllerWidget.deleteProfile(userChoice.getSelectionModel().getSelectedItem().getID());
		}
		selectedCheckBoxes.forEach(widget -> DBControllerWidget.insertProfile(userChoice.getSelectionModel().getSelectedItem().getID(), widget));
		System.out.println("selected user = " + userChoice.getSelectionModel().getSelectedItem().getID());
		System.out.println(selectedCheckBoxes);
		openMain();
	}
	
	@FXML
    void switchUser() {
		Person person = userChoice.getSelectionModel().getSelectedItem();
//		ArrayList<String> widgets = new ArrayList<>(getCheckedBoxes(gridPaneSettings));
//		CurrentUser.getInstance().changeUser(person, widgets);
		CurrentUser.getInstance().setUser(person);
		System.out.println("person = " + person);
		Stage stage = (Stage) pane.getScene().getWindow();
		stage.close();
		openMain();
    }

	private List<String> getCheckedBoxes(Pane pane) {
		List<String> selectedCheckBoxes = new ArrayList<>();
		pane.getChildren().forEach(node -> {
			if (node instanceof CheckBox) {
				CheckBox checkBox = (CheckBox) node;
				if (checkBox.isSelected()) {
					selectedCheckBoxes.add(checkBox.getText());
				}
				if (!checkBox.isSelected()) {
					selectedCheckBoxes.remove(checkBox.getText());
				}
			}
		});
		return selectedCheckBoxes;
	}
	
	private void openMain() {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/MainUI.fxml"));
    	Parent root;
		try {
			root = loader.load();
			Stage stage = new Stage();
	    	stage.setTitle("Smart Mirror");
	    	stage.setScene(new Scene(root));
	    	stage.setResizable(false);
	    	stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
}

package lukas.wais.smart.mirror;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lukas.wais.smart.mirror.controller.TableToXML;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
//    	System.out.println(getClass().getResource("fxml/MainUI.fxml"));
    	final FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/MainUI.fxml"));
    	final Parent root = loader.load();
    	primaryStage.setTitle("Smart Mirror");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    @Override
    public void stop() {
    	
    	String SELECTUSER = "SELECT * FROM SM_USERS";
    	dbToXML(SELECTUSER, "../xml/userTable.xml");
    	String SELECTWIDGET = "SELECT * FROM SM_WIDGET";
    	dbToXML(SELECTWIDGET, "../xml/widgetTable.xml");
    	String SELECTPROFILE = "SELECT * FROM SM_PROFILE";
    	dbToXML(SELECTPROFILE, "../xml/profileTable.xml");
    }
    
    /**
     * dbToXML extract the table structure and table data for a given table and stored
     * the result in a XML file. The file can be found the the target file of the project.
     * 
     * @param table which table should be extract from database
     * @param outputFile where should the table/table data be stored
     */
    private void dbToXML(String table, String outputFile) {
		try {
			DOMSource domSource = new DOMSource(new TableToXML().generateXML(table));
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();

			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);

			//FileWriter wr = new FileWriter("../smart.mirror/src/main/resources/lukas/wais/smart/mirror/xml/" + outputFile);
			FileWriter wr = new FileWriter (getClass().getResource(outputFile).getFile());
			String out = sw.toString();
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

    public static void main(String[] args) {
		launch(args);
	}
}
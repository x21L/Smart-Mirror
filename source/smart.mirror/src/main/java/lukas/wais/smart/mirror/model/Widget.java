package lukas.wais.smart.mirror.model;

import java.net.URL;
import java.util.Locale;

import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.Tile.TextSize;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;

/**
 * Widget factory. Every widget that is supported is created here.
 * 
 * @author Lukas Wais
 *
 */
public class Widget {
	
	/**
	 * Greetings Panel
	 * 
	 * @param greetings text for the greetings panel.
	 * @return new Node for the GUI
	 */
	public Node getGreetings(String greetings) {
		return TileBuilder.create().skinType(SkinType.TEXT).description(greetings).prefSize(1920, 300)
				.descriptionAlignment(Pos.CENTER).textVisible(true).textSize(TextSize.BIGGER).build();
	}

	/**
	 * Jokes panel with the button that toggles the joke method of the Joke class.
	 * 
	 * @see Joke
	 * @return new Node for the GUI
	 */
	public Node getJoke() {
		Button jokeButton = new Button("Tell me a funny joke");
		jokeButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		       new Joke().tell();
		    }
		});
		return TileBuilder.create().skinType(SkinType.CUSTOM).title("Jokes")
				.graphic(jokeButton).build();
	}

	/**
	 * Calendar panel from TilesFX.
	 * 
	 * @see <a href="https://github.com/HanSolo/tilesfx">TilesFX</a>
	 * @return new Node for the GUI
	 */
	public Node getCalendar() {
		return TileBuilder.create().skinType(SkinType.CALENDAR).build();
	}

	/**
	 * Clock panel from TilesFX.
	 * 
	 * @see <a href="https://github.com/HanSolo/tilesfx">TilesFX</a>
	 * @return new Node for the GUI
	 */
	public Node getClock() {
		return TileBuilder.create().skinType(SkinType.CLOCK).dateVisible(true).locale(Locale.GERMANY).running(true).build();
	}
	
	/*
	 * html widgets
	 */
	
	/**
	 * WebView of Google's public transport service.
	 * 
	 * @see <a href="https://maps.google.com/landing/transit/index.html">Google Transit</a>
	 * @return new Node for the GUI
	 */
	public Node getPublicTransport() {
		WebView transportView = new WebView();
		transportView.getEngine().load("https://maps.google.com/landing/transit/index.html");
		return transportView;
	}

	/**
	 * Stock market widget. Created by Trading view. 
	 * Stored locally in the project.
	 * 
	 * @see <a href="https://www.tradingview.com/widget/">TradingView</a>
	 * @return new Node for the GUI
	 */
	public Node getMarkets() {
		return htmlToNode("../html/markets.html", 340, 340);
	}

	/**
	 * Covid world cases widget. Created by Trading view. 
	 * Stored locally in the project.
	 * 
	 * @see <a href="https://www.tradingview.com/widget/">TradingView</a>
	 * @return new Node for the GUI
	 */
	public Node getCovid() {
		return htmlToNode("../html/covid.html", 340, 340);
	}

	/**
	 * This method creates the new nodes from custom HTML files
	 * 
	 * @param path to the HTML file
	 * @param width preferred width of the WebView
	 * @param height preferred height of the WebView
	 * @return
	 */
	private Node htmlToNode(String path, double width, double height) {
		URL url = this.getClass().getResource(path);
		WebView webView = new WebView();
		webView.getEngine().load(url.toString());
		webView.setPrefSize(width, height);
		return webView;
	}
}

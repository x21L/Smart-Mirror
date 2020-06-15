package lukas.wais.smart.mirror.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Joke {
	private final List<String> jokes;
	
	public Joke() {
		jokes = new ArrayList<>();
		jokes.add("What’s the best thing about Switzerland? "
				+ "I don’t know, but the flag is a big plus.");
		jokes.add("I’d like to start with the chimney jokes –"
				+ " I’ve got a stack of them. The first one is on the house.");
		jokes.add("As a scarecrow, people say I’m outstanding in my field. "
				+ "But hay – it’s in my jeans.");
		jokes.add("I went to buy some camouflage trousers the other day, "
				+ "but I couldn’t find any.");
		jokes.add("I waited and stayed up all night and tried to figure "
				+ "out where the sun was. Then it dawned on me");
		jokes.add("I have kleptomania. But when it gets bad, I take something for it.");
	}
	
	public void tell() {
		int index = new Random().nextInt(jokes.size());
		Polly.speak(jokes.get(index));
		System.out.println("funny joke");
	}
}

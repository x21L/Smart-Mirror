package lukas.wais.smart.mirror.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.VoiceId;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
/**
 * 
 * This class represents the AI Polly from AWS.
 * @author Lukas Wais
 *
 */

public class Polly {
	private final AmazonPollyClient pollyClient;
	private static Polly instance;

	public Polly(Region region) {
		// create an Amazon Polly client in a specific region
		pollyClient = new AmazonPollyClient(new DefaultAWSCredentialsProviderChain(), new ClientConfiguration());
		pollyClient.setRegion(region);
	}

	/**
	 * This method plays an arbitrary String. 
	 * It is the heart of Polly. Moreover some output is printed to console for testing.
	 * It is synchronized for the thread executor.
	 * 
	 * @param text you want to have spoken by Polly.
	 */
	public synchronized void play(String text) {
		// get the audio stream
		InputStream speechStream;
		try {
			speechStream = this.synthesize(text, OutputFormat.Mp3);
			// create an MP3 player
			AdvancedPlayer player = new AdvancedPlayer(speechStream,
					javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());

			player.setPlayBackListener(new PlaybackListener() {
				@Override
				public void playbackStarted(PlaybackEvent evt) {
					System.out.println("Playback started");
				}

				@Override
				public void playbackFinished(PlaybackEvent evt) {
					System.out.println("Playback finished");
				}
			});
			// play it!
			player.play();
		} catch (IOException e) {
			System.out.println("Problem with the speech stream \n" + e.getMessage());
		} catch (JavaLayerException e) {
			System.out.println("Problem with the player \n" + e.getMessage());
		}
	}

	/**
	 * The actual synthesize process of Polly. 
	 * We send the String to AWS and get an output format return.
	 * 
	 * @param text you want to have spoken by Polly.
	 * @param format output format of the synthesized text.
	 * @return InputStream of the synthesized text.
	 * @throws IOException if something is wrong with the InputStream.
	 */
	private InputStream synthesize(String text, OutputFormat format) throws IOException {
		SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest().withText(text).withVoiceId(VoiceId.Amy)
				.withOutputFormat(format);
		SynthesizeSpeechResult synthRes = pollyClient.synthesizeSpeech(synthReq);

		return synthRes.getAudioStream();
	}
	
	/**
	 * Singleton instance of Polly.
	 * 
	 * @return the Polly instance.
	 */
	public static Polly getInstance() {
		if (Polly.instance == null) {
			Polly.instance = new Polly(Region.getRegion(Regions.DEFAULT_REGION));
		}
		return Polly.instance;
	}
	
	/**
	 * Speak method of Polly. It executes the generated MP3, so you can hear Polly.
	 * 
	 * @param format output format of the synthesized text.
	 */
	public static void speak(String text) {
		Executor executor = Executors.newSingleThreadExecutor();
		Thread speakerThread = new Thread (() -> {
			Polly.getInstance().play(text);
		});
		executor.execute(speakerThread);
	}
}

package lukas.wais.smart.mirror.model;

import java.io.IOException;
import java.io.InputStream;

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


public class Polly {
	private final AmazonPollyClient pollyClient;

	public Polly(Region region) {
		// create an Amazon Polly client in a specific region
		pollyClient = new AmazonPollyClient(new DefaultAWSCredentialsProviderChain(), new ClientConfiguration());
		pollyClient.setRegion(region);
	}

	public void play(String text) {
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

	private InputStream synthesize(String text, OutputFormat format) throws IOException {
		SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest().withText(text).withVoiceId(VoiceId.Amy)
				.withOutputFormat(format);
		SynthesizeSpeechResult synthRes = pollyClient.synthesizeSpeech(synthReq);

		return synthRes.getAudioStream();
	}
	
	public static void speak(String text) {
		Thread speakerThread = new Thread (() -> {
			new Polly(Region.getRegion(Regions.DEFAULT_REGION)).play(text);
		});
		
		speakerThread.setDaemon(true);
		speakerThread.start();
	}
}

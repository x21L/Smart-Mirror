package lukas.wais.smart.mirror.model;
import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;
import com.amazonaws.services.polly.model.VoiceId;

;


public class Polly {
	private final AmazonPollyClient polly;
	private final Voice voice;

	public Polly(Region region) {
		// create an Amazon Polly client in a specific region
		polly = new AmazonPollyClient(new DefaultAWSCredentialsProviderChain(), 
		new ClientConfiguration());
		polly.setRegion(region);
		// Create describe voices request.
		DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();

		// Synchronously ask Amazon Polly to describe available TTS voices.
		DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
		voice = describeVoicesResult.getVoices().get(0);
	}
	
	public InputStream synthesize(String text, OutputFormat format) throws IOException {
		SynthesizeSpeechRequest synthReq = 
		new SynthesizeSpeechRequest().withText(text).withVoiceId(VoiceId.Joanna)
				.withOutputFormat(format);
		SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);

		return synthRes.getAudioStream();
	}
}

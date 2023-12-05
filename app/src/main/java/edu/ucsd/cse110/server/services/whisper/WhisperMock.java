package edu.ucsd.cse110.server.services.whisper;

import java.io.IOException;
import java.net.URISyntaxException;

public class WhisperMock implements WhisperInterface {
    public String transcribe(byte[] audio) throws IOException, URISyntaxException {
		System.out.println(audio[0]);
		if (audio[4] == 36) { // I want lunch wav file
			return "I would like to have lunch.";
		} 
		else if(audio[4] == -40) {
			return "Hello I would like supper please.";
		}
		else if(audio[4] == 114){
			return "I want lunch and dinner.";
		}else if(audio[4] == 92) {
			return "I have bananas, peppers, onions, green onions, salts, and carrots.";
		} else {
			return "!Exception";
		}
	}
}

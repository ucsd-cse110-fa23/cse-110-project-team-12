package edu.ucsd.cse110.api;

import java.io.IOException;
import java.net.URISyntaxException;

public class WhisperMock implements WhisperInterface {
    public String transcribe(byte[] audio) throws IOException, URISyntaxException {
		if (audio[0] == 0) {
			return "I would like to have lunch.";
		} else if(audio[0] == 1) {
			return "I have bananas, peppers, onions, green onions, salts, and carrots.";
		} else {
			return "!Exception";
		}
	}
}

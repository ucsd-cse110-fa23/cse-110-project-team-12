package edu.ucsd.cse110.api;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class WhisperMock implements WhisperInterface {
    public String transcribe(File file) throws IOException, URISyntaxException {
		if(file.getPath() == "./src/main/java/edu/ucsd/cse110/api/assets/iwantlunch.wav") {
			return "I would like to have lunch.";
		} else if(file.getPath() == "./src/main/java/edu/ucsd/cse110/api/assets/ingredients.wav") {
			return "I have bananas, peppers, onions, green onions, salts, and carrots.";
		}else {
			return "!Exception";
		}
	}
}

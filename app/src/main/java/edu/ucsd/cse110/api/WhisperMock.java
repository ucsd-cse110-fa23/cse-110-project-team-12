package edu.ucsd.cse110.api;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class WhisperMock implements WhisperInterface {
    public String transcribe(File file) throws IOException, URISyntaxException{
		return "I would like to have lunch, please!";
	}
}

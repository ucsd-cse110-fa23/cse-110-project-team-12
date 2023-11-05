package edu.ucsd.cse110.api;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class MockWhisper implements WhisperInterface {
	public String transcribe(File file) throws IOException, URISyntaxException{
		return "Lunch";
	}
}
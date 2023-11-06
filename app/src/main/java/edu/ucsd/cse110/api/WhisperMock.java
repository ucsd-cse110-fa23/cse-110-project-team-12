package edu.ucsd.cse110.api;

import java.io.File;

public class WhisperMock implements WhisperInterface {
	public String transcribe(File file) {
		return "Lunch";
	}
}

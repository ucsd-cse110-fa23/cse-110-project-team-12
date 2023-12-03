package edu.ucsd.cse110.server.services.whisper;

import java.io.IOException;
import java.net.URISyntaxException;

public interface WhisperInterface {
	public String transcribe(byte[] audio) throws IOException, URISyntaxException;
}

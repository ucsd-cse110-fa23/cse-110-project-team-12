package edu.ucsd.cse110.api;

import java.io.IOException;
import java.net.URISyntaxException;

// Simple interface for interchanging between real and mock GPT during testing
public interface ChatGPTInterface {
	public String[] promptGPT(String mealType, String ingredients) 
			throws IOException, InterruptedException, URISyntaxException;
}

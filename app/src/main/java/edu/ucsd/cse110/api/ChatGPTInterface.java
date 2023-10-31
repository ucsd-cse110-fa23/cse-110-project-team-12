package edu.ucsd.cse110.api;

// Simple interface for interchanging between real and mock GPT during testing
public interface ChatGPTInterface {
	public String[] promptGPT(String mealType, String ingredients);
}

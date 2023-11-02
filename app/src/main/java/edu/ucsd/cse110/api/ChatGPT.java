package edu.ucsd.cse110.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

// Class to send and recieve api reqiest calls
public class ChatGPT {
	private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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;"; // TO-DO
    private static final String MODEL = "gpt-3.5-turbo";
	private static final int MAX_TOKENS = 100;

    public String[] promptGPT(String mealType, String ingredients) 
			throws IOException, InterruptedException, URISyntaxException {

        // Set request parameters
        String prompt = "Make me a recipe for " + mealType 
				+ " with the following ingredients:\n"+ ingredients;

        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", MAX_TOKENS);
        requestBody.put("temperature", 1.0);

        // Create the HTTP Client
        HttpClient client = HttpClient.newHttpClient();

        // Create the request object
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(API_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", String.format("Bearer %s", API_KEY))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        // Send the request and receive the response
        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        // Process the response
        String responseBody = response.body();
        JSONObject responseJson = new JSONObject(responseBody);

        JSONArray choices = responseJson.getJSONArray("choices");
        String generatedText = choices.getJSONObject(0).getString("text");

		generatedText = generatedText.trim();
		String[] result = generatedText.split("\n", 2);
		result[1] = result[1].substring(1);
		
		return result;
    }

	public static void main(String[] args) {
		ChatGPT test = new ChatGPT();
		try{
			String[] result = test.promptGPT("dinner", "I have chicken, broccoli, garlic, and rice.");
			System.out.println(result[0]);
			System.out.println(result[1]);
		}
		catch (Exception e) {
			System.err.println(e);
		}
	}
}

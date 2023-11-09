package edu.ucsd.cse110.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

// Class to send and receive API request calls
public class ChatGPT implements ChatGPTInterface {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";
    private static final String promptPartOne = "For a ";
    private static final String promptPartTwo = " type of meal, create a detailed recipe for me with the ingredients I will be giving to you. For the recipe, the first line of the message should be the title, and then include the ingredients/instructions. The ingredients are given by a user who is listing the ingredients they currently have for this meal they want to cook. The recipe generated can include fundamental ingredients like seasonings, spices, oil, and water, even if the user hasn't explicitly listed them. The key is to ensure that the recipe remains practical and accurate. Create a complete and coherent recipe. The ingredients are: ";
    private static final int MAX_TOKENS = 500;
    
    public String[] promptGPT(String mealType, String ingredients)
    throws IOException, InterruptedException, URISyntaxException {
        
        // Set request parameters
        String prompt = promptPartOne + mealType
        + promptPartTwo + ingredients;
        
        // Create a request body which you will pass into the request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", MAX_TOKENS);
        requestBody.put("temperature", 1.0);
        
        // System.out.println("Request Body: " + requestBody.toString());
        
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
        
        // System.out.println("Sending Request: " + request.toString());
        
        // Send the request and receive the response
        HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString());
        
        // System.out.println("Response Code: " + response.statusCode());
        
        // Process the response
        String responseBody = response.body();
        // System.out.println("Response Body: " + responseBody);
        
        JSONObject responseJson = new JSONObject(responseBody);
        
        JSONArray choices = responseJson.getJSONArray("choices");
        // System.out.println("Number of Choices: " + choices.length());
        
        if (choices.length() > 0) {
            String generatedText = choices.getJSONObject(0).getString("text");
            generatedText = generatedText.trim();
            String[] result = generatedText.split("\n", 2);
            if (result[0].charAt(0) == '\"' && result[0].charAt(result.length - 1) == '\"') {
                result[0] = result[0].substring(1, result[0].length() - 1);
            }
            result[1] = result[1].substring(0);
            return result;
        } else {
            return new String[] { "No response received", "" };
        }
    }
}

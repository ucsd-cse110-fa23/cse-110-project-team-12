package edu.ucsd.cse110.server.services.dalle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.imageio.ImageIO;

import org.json.JSONObject;

public class DallE implements DallEInterface {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/images/generations";
    private static final String API_KEY = "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"; //Tiujj9KHpCxW1k31B5QmT3BlbkFJp9YsVkUhoQlMYoLTxNuH";
    private static final String MODEL = "dall-e-2";

    @Override
    public BufferedImage promptDallE(String prompt) {
        int n = 1;
        
        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("n", n);
        requestBody.put("size", "256x256");
        
        
        // Create the HTTP client
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
        HttpResponse<String> response;
        try {
            response = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
            );
        } catch (IOException | InterruptedException e) {
            return null;
        }
        
        
        // Process the response
        String responseBody = response.body();
        
        
        JSONObject responseJson = new JSONObject(responseBody);

        String generatedImageURL = responseJson.getJSONArray("data").getJSONObject(0).getString("url");
        
        System.out.println("DALL-E Response:");
        System.out.println(generatedImageURL);


        // Download the Generated Image to Current Directory
        InputStream in;
        BufferedImage bufferedImage = null;
        try {
            in = new URI(generatedImageURL).toURL().openStream();
            bufferedImage = ImageIO.read(in);
        } catch (IOException | URISyntaxException e) {
            return null;
        }
        return bufferedImage;
    }
    
}

package edu.ucsd.cse110.server.services.whisper;

import java.io.*;
import java.net.*;
import org.json.*;

public class Whisper implements WhisperInterface {
    private final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private final String TOKEN = "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";
    private final String MODEL = "whisper-1";

    public Whisper() {}
    
    private static void writeParameterToOutputStream(
    OutputStream outputStream,
    String parameterName,
    String parameterValue,
    String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(("Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n").getBytes());
        outputStream.write((parameterValue + "\r\n").getBytes());
    }
    
    private static void writeFileToOutputStream(
    OutputStream outputStream,
    byte[] audio,
    String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
        ("Content-Disposition: form-data; name=\"file\"; filename=audio\"" +
        "\"\r\n").getBytes());
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());
        
        // FileInputStream fileInputStream = new FileInputStream(file);
        // byte[] buffer = new byte[1024];
        // int bytesRead;
        // while ((bytesRead = fileInputStream.read(buffer)) != -1) {
        //     outputStream.write(buffer, 0, bytesRead);
        // }
        outputStream.write(audio);
        // fileInputStream.close();
    }
    
    private static String handleSuccessResponse(HttpURLConnection connection)
    throws IOException, JSONException {
        BufferedReader in = new BufferedReader(
        new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        JSONObject responseJson = new JSONObject(response.toString());
        
        String generatedText = responseJson.getString("text");
        
        // Print the transcription result
        return generatedText;
    }
    
    private static String handleErrorResponse(HttpURLConnection connection)
    throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(
        new InputStreamReader(connection.getErrorStream()));
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        return "!Error Result: " + errorResult;
    }
    
    public String transcribe(byte[] audio) {
        // Set up HTTP connection
        try {
            URL url = new URI(API_ENDPOINT).toURL();
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            
            // Set up request headers
            String boundary = "Boundary-" + System.currentTimeMillis();
            connection.setRequestProperty(
            "Content-Type",
            "multipart/form-data; boundary=" + boundary);
            
            connection.setRequestProperty("Authorization", "Bearer " + TOKEN);
            
            // Set up output stream to write request body
            OutputStream outputStream = connection.getOutputStream();
            
            // Write model parameter to request body
            writeParameterToOutputStream(outputStream, "model", MODEL, boundary);
            
            // Write file parameter to request body
            writeFileToOutputStream(outputStream, audio, boundary);
            
            // Write closing boundary to request body
            outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
            
            // Flush and close output stream
            outputStream.flush();
            outputStream.close();
            
            // Get response code
            int responseCode = connection.getResponseCode();
            
            // Check response code and handle response accordingly
            String result = "";
            if (responseCode == HttpURLConnection.HTTP_OK) {
                result = handleSuccessResponse(connection);
            } else {
                result = handleErrorResponse(connection);
            }
            
            // Disconnect connection
            connection.disconnect();
            
            return result;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return "!Exception";
        }
    }
}


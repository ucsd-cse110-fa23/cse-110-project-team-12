package edu.ucsd.cse110.server.handlers;

import com.sun.net.httpserver.*;

import edu.ucsd.cse110.api.WhisperInterface;
import edu.ucsd.cse110.server.services.Utils;

import java.io.*;
import java.util.*;

public class WhisperRequestHandler implements HttpHandler {
    private WhisperInterface whisper;
    public WhisperRequestHandler(WhisperInterface w) {
        whisper = w;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (method.equals("POST")) {
            handlePost(httpExchange);
        }
    }

    // Expects a base64 encoding of an audio in th request body.
    private void handlePost(HttpExchange httpExchange) throws IOException {
        Scanner scanner = new Scanner(httpExchange.getRequestBody());
        String data = "";
        while (scanner.hasNext())
            data += scanner.nextLine();

        byte[] audioBytes = Utils.decodeBase64(data);

        String audioTranscription = "";
        try {
            audioTranscription = whisper.transcribe(audioBytes);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        httpExchange.sendResponseHeaders(200, audioTranscription.getBytes().length);
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(audioTranscription.getBytes());
        outStream.close();
    }
}

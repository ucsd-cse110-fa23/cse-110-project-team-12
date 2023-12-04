package edu.ucsd.cse110.server.handlers;

import com.sun.net.httpserver.*;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.services.Utils;
import edu.ucsd.cse110.server.services.dalle.DallEInterface;

import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;

public class DalleRequestHandler implements HttpHandler {
    private DallEInterface dalle;
    public DalleRequestHandler(DallEInterface dle) {
        dalle = dle;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (method.equals("POST")) {
            handlePost(httpExchange);
        }
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Scanner scanner = new Scanner(httpExchange.getRequestBody());
        String data = "";
        while (scanner.hasNext()) {
            data += scanner.nextLine() + "\n";
        }
        RecipeSchema recipe = Utils.unmarshalJson(data, RecipeSchema.class);
        String prompt = "food photography, photography, Super-Resolution, " + recipe.title;
        BufferedImage dalleImage = dalle.promptDallE(prompt);

        if (dalleImage != null) {
            String encodedImage = Utils.encodeBufferedImageToBase64(dalleImage);
            recipe.base64ImageEncoding = encodedImage;
    
            String response = Utils.marshalJson(recipe);
            httpExchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(response.getBytes());
            outStream.close();
        }
        else {
            httpExchange.sendResponseHeaders(400, 0);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write("".getBytes());
            outStream.close(); 
        }
    }
}

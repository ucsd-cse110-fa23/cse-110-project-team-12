package edu.ucsd.cse110.server.handlers;

import com.sun.net.httpserver.*;

import edu.ucsd.cse110.server.services.chatgpt.ChatGPTInterface;
import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.services.Utils;

import java.io.*;
import java.util.*;

public class ChatGPTRequestHandler implements HttpHandler {
    private ChatGPTInterface chatgpt;
    public ChatGPTRequestHandler(ChatGPTInterface gpt) {
        chatgpt = gpt;
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

        try {
            String[] r = chatgpt.promptGPT(recipe.mealType, recipe.ingredients);
            recipe.title = r[0];
            recipe.description = r[1];
        } catch (Exception e) {
            e.printStackTrace();
            httpExchange.sendResponseHeaders(500, "".getBytes().length);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write("".getBytes());
            outStream.close();
        }
        String generatedRecipe = Utils.marshalJson(recipe);

        httpExchange.sendResponseHeaders(200, generatedRecipe.getBytes().length);
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(generatedRecipe.getBytes());
        outStream.close();
    }
}

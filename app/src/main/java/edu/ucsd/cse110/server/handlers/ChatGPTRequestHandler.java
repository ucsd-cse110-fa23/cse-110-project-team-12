package edu.ucsd.cse110.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;

import edu.ucsd.cse110.api.ChatGPT;
import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.services.Utils;

import java.io.*;
import java.util.*;
import java.time.*;


public class ChatGPTRequestHandler implements HttpHandler {
    private ChatGPT chatgpt;

    public ChatGPTRequestHandler(ChatGPT gpt) {
        chatgpt = gpt;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET")) {
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
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            String genRecipe = Utils.marshalJson(recipe);

            httpExchange.sendResponseHeaders(200, genRecipe.getBytes().length);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(genRecipe.getBytes());
            outStream.close();
        }
    }
}

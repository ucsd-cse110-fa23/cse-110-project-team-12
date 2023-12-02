package edu.ucsd.cse110.server.handlers;

import com.sun.net.httpserver.*;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.services.Utils;
import edu.ucsd.cse110.server.services.mongodb.MongoDBInterface;

import java.io.*;
import java.util.*;

public class RecipeRequestHandler implements HttpHandler {
    private MongoDBInterface mongodb;
    public RecipeRequestHandler(MongoDBInterface db) {
        mongodb = db;
    }

    private void send404(HttpExchange httpExchange) throws IOException {
        final byte[] msg = "Resource not found.".getBytes();
        httpExchange.sendResponseHeaders(404, msg.length);
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(msg);
        outStream.close();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET")) {
            handleGet(httpExchange);
        }
        else if (method.equals("POST")) {
            handlePost(httpExchange);
        }
        else if (method.equals("PUT")) {
            handleUpdate(httpExchange);
        }
        else if (method.equals("DELETE")) {
            handleDelete(httpExchange);
        }
    }

    private void handleGet(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryVals = Utils.getQueryPairs(httpExchange);

        // If no query for userId specified, then return all the recipes for user.
        if (queryVals.containsKey("userId")) {
            String userId = queryVals.get("userId");
            List<RecipeSchema> recipes = mongodb.getRecipeList(userId);

            String jsonString = Utils.marshalJson(recipes);
            httpExchange.sendResponseHeaders(200, jsonString.getBytes().length);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(jsonString.getBytes());
            outStream.close();
        }
        else if (queryVals.containsKey("recipeId")) {
            String recipeId = queryVals.get("recipeId");
            RecipeSchema recipe = mongodb.getRecipe(recipeId);

            if (recipe == null) {
                send404(httpExchange);
            }
            else {
                String jsonString = Utils.marshalJson(recipe);
                httpExchange.sendResponseHeaders(200, jsonString.getBytes().length);
                OutputStream outStream = httpExchange.getResponseBody();
                outStream.write(jsonString.getBytes());
                outStream.close();
            }
        }
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Scanner scanner = new Scanner(httpExchange.getRequestBody());
        String data = "";
        while (scanner.hasNext()) {
            data += scanner.nextLine() + "\n";
        }
        RecipeSchema recipe = Utils.unmarshalJson(data, RecipeSchema.class);
        
        RecipeSchema recipeWithId = mongodb.saveRecipe(recipe);
        String jsonString = Utils.marshalJson(recipeWithId);
        httpExchange.sendResponseHeaders(201, jsonString.getBytes().length);
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(jsonString.getBytes());
        outStream.close();
    }

    private void handleUpdate(HttpExchange httpExchange) throws IOException {
        Scanner scanner = new Scanner(httpExchange.getRequestBody());
        String data = "";
        while (scanner.hasNext())
            data += scanner.nextLine() + "\n";
        RecipeSchema recipe = Utils.unmarshalJson(data, RecipeSchema.class);

        mongodb.updateRecipe(recipe._id, recipe.title, recipe.description);

        httpExchange.sendResponseHeaders(200, 0);
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write("".getBytes());
        outStream.close();
    }

    private void handleDelete(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryVals = Utils.getQueryPairs(httpExchange);
        String recipeId = queryVals.get("recipeId");

        mongodb.deleteRecipe(recipeId);

        httpExchange.sendResponseHeaders(200, 0);
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write("".getBytes());
        outStream.close();
    }
}

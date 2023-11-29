package edu.ucsd.cse110.server.handlers;

import com.sun.net.httpserver.*;
import com.google.gson.Gson;

// TODO(eth003): move this to services in server
import edu.ucsd.cse110.api.MongoDBInterface;
import edu.ucsd.cse110.client.Recipe;
import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.services.Utils;

import java.io.*;
import java.util.*;

public class RecipeRequestHandler implements HttpHandler {
    private MongoDBInterface mongodb;

    public RecipeRequestHandler(MongoDBInterface db) {
        mongodb = db;
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
        else if (method.equals("PATCH")) {

        }
        else if (method.equals("DELETE")) {
            handleDelete(httpExchange);
        }
    }

    public void handleGet(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryVals = Utils.getQueryPairs(httpExchange);

        // If no query specified, then return all the
        if (queryVals.size() == 0) {
            List<Recipe> recipes = mongodb.getRecipeList();
            List<RecipeSchema> recipeSchemas = new ArrayList<>();
            for (Recipe r : recipes) {
                RecipeSchema rs = new RecipeSchema();
                rs.title = r.getName();
                rs.description = r.getInformation();
                rs.mealType = r.getMealType();
                recipeSchemas.add(rs);
            }
            Gson gson = new Gson();
            String json = "";
            if (recipeSchemas.size() > 0)
                json = gson.toJson(recipeSchemas);
            httpExchange.sendResponseHeaders(200, json.getBytes().length);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(json.getBytes());
            outStream.close();
        }
        else {
            String title = queryVals.get("title");

            Recipe r = mongodb.getRecipe(title);
            System.out.println(title);
            RecipeSchema rs = new RecipeSchema();
            String json = "nothign found";
            Gson gson = new Gson();
            if (r != null) {
                rs.title = r.getName();
                rs.description = r.getInformation();
                rs.mealType = r.getMealType();
                json = gson.toJson(rs);
            }

            httpExchange.sendResponseHeaders(200, json.getBytes().length);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(json.getBytes());
            outStream.close();
        }
    }

    public void handlePost(HttpExchange httpExchange) throws IOException {
        Scanner scanner = new Scanner(httpExchange.getRequestBody());
        String data = "";
        while (scanner.hasNext()) {
            data += scanner.nextLine() + "\n";
        }
        Gson gson = new Gson();
        RecipeSchema rs = new RecipeSchema();
        try {
            rs = gson.fromJson(data, RecipeSchema.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mongodb.saveRecipe(rs.title, rs.description, rs.mealType);
        httpExchange.sendResponseHeaders(201, 0);
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write("".getBytes());
        outStream.close();
    }

    public void handleDelete(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryVals = Utils.getQueryPairs(httpExchange);
        String title = queryVals.get("title");

        mongodb.deleteRecipe(title);

        httpExchange.sendResponseHeaders(201, 0);
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write("".getBytes());
        outStream.close();
    }
}

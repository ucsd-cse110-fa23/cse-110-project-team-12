package edu.ucsd.cse110.server.handlers;

import com.sun.net.httpserver.*;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.services.Utils;
import edu.ucsd.cse110.server.services.mongodb.MongoDBInterface;

import java.io.*;
import java.util.*;
import java.nio.file.Files;

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
        final String NoRecipeFoundHTML = "<!DOCTYPE html> <html lang=\"en\"><head><meta charset=\"UTF-8\"><title>No Recipe Found</title><style>body {font-family: \"Helvetica Neue\", sans-serif;background-color: #5b5b5b;}h1 {color: #fff;text-align: center;}</style></head><body><h1>Error: Recipe Not Found</h1></body></html>";
        final String RecipeFoundHTML = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>{{Title}}</title><style>body {font-family: \"Helvetica Neue\", sans-serif;background-color: #5b5b5b;}h1 {color: #fff;font-size: 48px;}p {color: #fff;text-wrap: normal;}h3{color: #fff;display: inline;font-size: 30px;}.logo{display: inline;width: 30px;height: 30px;}.parent{display: flex;flex-direction: row;justify-content: space-between;}.left{position: sticky;width: 35%;padding: 40px;}.right{width: 65%;padding: 10px;overflow-x: hidden;}@media screen and (max-width: 600px) {.parent{flex-direction: column;}.left{width: 100%;}.right{width: 100%;} }.foodPhoto{width: min(100%, 500px);height: min(100%, 500px);display: block;}</style></head><body><div class=\"parent\"><div class=\"left\"><img src=\"data:image/png;base64,{{RecipeImage}}\" alt=\"Recipe Image\" class=\"foodPhoto\"><h3>PantryPal</h3><img class=\"logo\" src=\"data:image/png;base64,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ICAgeG1sbnM6R0lNUD0iaHR0cDovL3d3dy5naW1wLm9yZy94bXAvIgogICAgeG1sbnM6ZGM9Imh0dHA6Ly9wdXJsLm9yZy9kYy9lbGVtZW50cy8xLjEvIgogICAgeG1sbnM6dGlmZj0iaHR0cDovL25zLmFkb2JlLmNvbS90aWZmLzEuMC8iCiAgICB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iCiAgIHhtcE1NOkRvY3VtZW50SUQ9ImdpbXA6ZG9jaWQ6Z2ltcDo0Y2UxNGU2Zi1mNmQ5LTRiYWItYmM0My1lZjRjZDlmZjE3Y2MiCiAgIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NjM1YTdmMDYtZjA1OC00NDBmLTg4NDktZmI2YjA2NTg1N2EwIgogICB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9InhtcC5kaWQ6ZDQzZmEwM2ItOWQ4MC00MTU1LTg5NjctMWM2ZTM1YTkwNDA2IgogICBHSU1QOkFQST0iMi4wIgogICBHSU1QOlBsYXRmb3JtPSJNYWMgT1MiCiAgIEdJTVA6VGltZVN0YW1wPSIxNjk4NDUyMzE2MzUyNzAwIgogICBHSU1QOlZlcnNpb249IjIuMTAuMzIiCiAgIGRjOkZvcm1hdD0iaW1hZ2UvcG5nIgogICB0aWZmOk9yaWVudGF0aW9uPSIxIgogICB4bXA6Q3JlYXRvclRvb2w9IkdJTVAgMi4xMCIKICAgeG1wOk1ldGFkYXRhRGF0ZT0iMjAyMzoxMDoyN1QxNzoxODozNC0wNzowMCIKICAgeG1wOk1vZGlmeURhdGU9IjIwMjM6MTA6MjdUMTc6MTg6MzQtMDc6MDAiPgogICA8eG1wTU06SGlzdG9yeT4KICAgIDxyZGY6U2VxPgogICAgIDxyZGY6bGkKICAgICAgc3RFdnQ6YWN0aW9uPSJzYXZlZCIKICAgICAgc3RFdnQ6Y2hhbmdlZD0iLyIKICAgICAgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDo1MmRlOTRkMC03MTY5LTRkZTktYjllOC1hYjdjYTc3NTY5OGUiCiAgICAgIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkdpbXAgMi4xMCAoTWFjIE9TKSIKICAgICAgc3RFdnQ6d2hlbj0iMjAyMy0xMC0yN1QxNzoxODozNi0wNzowMCIvPgogICAgPC9yZGY6U2VxPgogICA8L3htcE1NOkhpc3Rvcnk+CiAgPC9yZGY6RGVzY3JpcHRpb24+CiA8L3JkZjpSREY+CjwveDp4bXBtZXRhPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgCjw/eHBhY2tldCBlbmQ9InciPz5gmCcaAAAABmJLR0QAAAAAAAD5Q7t/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH5wocABIk0x3lHgAABGJJREFUeNrt3UtWGzEQQNEu7X/PYZJRBgGOpVZ97tsASKprNYnBzyNJkiRJkiRJkiRJkiRJkiRJkv78zU6oQusmFNsvQCARIJAIEEg0rsgEISLCkQgQSNq242kg0wxExs2DZBaIzLMQWTcTkrkoMs1CZN5YSGbDyDAPkX2DIQHj5jxEhc2GBIxb8xBVNh0SMG7Mw3JoqrjPb32fUW1hbhIvQG/Ow3KQqrynp7/3ZVNmw7CXDYFAYv/eWsuyMXCoKRCHbb9Or2vZHDjUHIjDtz+n1rhsEBwaAsQw2A9ADIV9AMRwWP9QIG++l2rqkMDhBjEs1guIobFOQAyPAIHE2gAxSNYEiIESIIfL8GuzXZDA7gYxXHAAYsgECCRgAwKJ7nTiZ9vVaTFTkMDsBjF8AgQSgAGBRIUf2QGBRFmBVPhD1JmQAOsRCxKleqEFBBL9D5/hq/FYCOid83CDGFQBAokKA6n4sWqQzJgbN0gBJDB6xIJEKZ861rQFQwKHGwQSAQKJ22MYkMqPWZC4QQTJuBfQZRMggcMNAgkcfYB0uUUg8TOIIGn9YrlsDCRmwA0CCRz9gHS7RSCpd+5uEEjgqAyk4y0CSZ2zDgNVYyi6g8r6QugRC3w4Otwg3YfpJ0PScf3ZH6GXzYTfeTa5QdwkPdZe6YVu2WD4nV2zG2T6TVJ13RVf2ACBBIyuQCDJvfYOj8J+ew8SOLoDgSTP2rv9A4rfuYAEjClApiN5e/2d/7m9LRBIzu7BBBTtgUCyby+mgRgDBBIBAgkkgEACCSCQQAIIJJAAAgkkgEACCSCQQALI5MVDIkAggQQQSCABBBJIAIEEEkAggQQQSCABBBJIAIEEEkAEiQCBBBJAIIEEEEggOZ+PYPsFgu4D5OPg3CA/HpCOHz/gJgFk66snJLIJHzxWQQKIH7wh8ZcV4fhsUCABZDQOSOYi8VdNIIEEkL1DDAkgcEACyeNv80ICCSAnhxUSQOCAZCwSb76DBJKJQG4MJST9WnAYoK57Dkixg4LEI9bYA3rrY5k9bgHSGgckgMABSRskAcfdAXjrPzFv7VN1JAHH/YPftY7vvidIBgHpguOT9VT62acqkoAjz0F/t65dXxuSxkC64vh3fae/FiQNgXTHMWU/K+1lOMyZOCBpBAQOSACBAxJA4LDftfY6HBYckBQDAgckgMABSYEzCIcCByQFgMABScYzCTjggCQxEDggyXxGAYcgSQgEDkgqnFnAIUgSAYEDkkpnGHAIkgRA4ICk4pkGHILkIhA4IKl8xgGHILkABA5IOpx5wCFIXgQChzohCTgEyQtA4FBHJAGHIDkIBA51RlL2QzzhcF5vtCa+KgiStjcIHJAAAgckgMABCSBwqPS5Lpso51sUCByQAAKHEp/3slly7oeAHHn/PRyQuEHg0Pk5SPFmxefZ85YTOLRzrlK93T3LYgTJ7nm6+iu3YCj7TF35qyZgaOdcmSdJkiRJkiRJkiRJkiRJkiSpY18XyEikUyVzvAAAAABJRU5ErkJggg==\" alt=\"PantryPal Logo\"/></div><div class=\"right\"><h1>{{Title}}</h1><p>{{Decription}}</p></div></div></body></html>";
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
        } else if (queryVals.containsKey("id")) {
            RecipeSchema requestedRecipe = mongodb.getRecipe(queryVals.get("id"));
            if(requestedRecipe == null) {
                httpExchange.sendResponseHeaders(404, NoRecipeFoundHTML.getBytes().length);
                OutputStream outStream = httpExchange.getResponseBody();
                outStream.write(NoRecipeFoundHTML.getBytes());
                outStream.close();
            } else {
                String recipePageString = RecipeFoundHTML;

                recipePageString = recipePageString.replace("{{Title}}", requestedRecipe.title);
                recipePageString = recipePageString.replace("{{Description}}", requestedRecipe.description);
                recipePageString = recipePageString.replace("\n", "<br>");

                httpExchange.sendResponseHeaders(200, recipePageString.getBytes().length);
                OutputStream outStream = httpExchange.getResponseBody();
                outStream.write(recipePageString.getBytes());
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

        mongodb.updateRecipe(recipe._id, recipe.title, recipe.description, recipe.base64ImageEncoding);

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

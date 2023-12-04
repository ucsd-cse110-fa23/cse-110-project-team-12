package edu.ucsd.cse110.server.handlers;

import com.sun.net.httpserver.*;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.services.Utils;
import edu.ucsd.cse110.server.services.mongodb.MongoDBInterface;

import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ShareRequestHandler implements HttpHandler{
    private MongoDBInterface mongodb;

    public ShareRequestHandler(MongoDBInterface mongodb) {
        this.mongodb = mongodb;
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
        try {
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET")) {
                handleGet(httpExchange);
            }
            else{
                send404(httpExchange);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGet(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryVals = Utils.getQueryPairs(httpExchange);
        String noRecipeFoundHTML = new String(Files.readAllBytes(Paths.get("./src/main/java/edu/ucsd/cse110/server/handlers/noRecipeFound.html")));
        String recipeFoundHTML = new String(Files.readAllBytes(Paths.get("./src/main/java/edu/ucsd/cse110/server/handlers/recipe.html")));
        
        RecipeSchema requestedRecipe = mongodb.getRecipe(queryVals.get("recipeId"));
        if(requestedRecipe == null) {
            httpExchange.sendResponseHeaders(404, noRecipeFoundHTML.getBytes().length);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(noRecipeFoundHTML.getBytes());
            outStream.close();
        } else {
            String recipePageString = recipeFoundHTML;
            byte[] logo = Files.readAllBytes(Paths.get("./src/main/java/edu/ucsd/cse110/client/resources/PPIcon.png"));
            String base64logo = Utils.encodeBase64(logo);

            requestedRecipe.description = requestedRecipe.description.replace("\n", "<br>");

            recipePageString = recipePageString.replace("{{Title}}", requestedRecipe.title);
            recipePageString = recipePageString.replace("{{Description}}", requestedRecipe.description);
            recipePageString = recipePageString.replace("{{RecipeImage}}", requestedRecipe.base64ImageEncoding);
            recipePageString = recipePageString.replace("{{Logo}}", base64logo);

            httpExchange.sendResponseHeaders(200, recipePageString.getBytes().length);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(recipePageString.getBytes());
            outStream.close();
        }
    }
}

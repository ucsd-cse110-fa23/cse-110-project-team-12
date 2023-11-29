package edu.ucsd.cse110.server.handlers;

import com.google.gson.Gson;
import com.google.gson.internal.Excluder;
import com.sun.net.httpserver.*;

import edu.ucsd.cse110.api.ChatGPT;
import edu.ucsd.cse110.client.Recipe;
import edu.ucsd.cse110.server.schemas.RecipeSchema;

import java.io.*;
import java.net.*;
import java.util.*;
import java.time.*;


public class GenerateRequestHandler implements HttpHandler {
    private ChatGPT chatgpt;

    public GenerateRequestHandler(ChatGPT gpt) {
        chatgpt = gpt;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (method.equals("POST")) {
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

            try {
                String[] r = chatgpt.promptGPT(rs.mealType, rs.ingredients);
                rs.title = r[0];
                rs.description = r[1];
                rs.timeCreated = LocalDateTime.now().toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            String json = "fale";
            gson = new Gson();
            json = gson.toJson(rs);

            httpExchange.sendResponseHeaders(200, json.getBytes().length);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(json.getBytes());
            outStream.close();
        }
    }
}

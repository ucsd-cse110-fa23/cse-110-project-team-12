package edu.ucsd.cse110.server.handlers;

import com.sun.net.httpserver.*;

import edu.ucsd.cse110.server.schemas.UserSchema;
import edu.ucsd.cse110.server.services.Utils;
import edu.ucsd.cse110.server.services.mongodb.MongoDBInterface;

import java.io.*;
import java.util.*;

public class UserRequestHandler implements HttpHandler {
    private MongoDBInterface mongodb;

    public UserRequestHandler(MongoDBInterface db) {
        mongodb = db;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET")) {
            handleGet(httpExchange);
        } else if (method.equals("POST")) {
            handlePost(httpExchange);
        }
    }

    private void handleGet(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryVals = Utils.getQueryPairs(httpExchange);
        String username = queryVals.get("username");
        String password = queryVals.get("password");

        UserSchema user = mongodb.getUser(username, password);

        if (user == null) {
            httpExchange.sendResponseHeaders(404, 0);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write("".getBytes());
            outStream.close();
        } else {
            String jsonString = Utils.marshalJson(user);
            httpExchange.sendResponseHeaders(200, jsonString.getBytes().length);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(jsonString.getBytes());
            outStream.close();
        }
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        Map<String, String> queryVals = Utils.getQueryPairs(httpExchange);
        String username = queryVals.get("username");
        String password = queryVals.get("password");

        UserSchema createdUser = mongodb.createUser(username, password);
        if (createdUser != null) {
            String jsonString = Utils.marshalJson(createdUser);
            httpExchange.sendResponseHeaders(201, jsonString.getBytes().length);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(jsonString.getBytes());
            outStream.close();
        } else {
            // 409 status code means duplicate resouce.
            httpExchange.sendResponseHeaders(409, 0);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write("".getBytes());
            outStream.close();
        }
    }
}

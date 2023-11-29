package edu.ucsd.cse110.server.handlers;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class UserRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET")) {
            // TODO(eth003): link user with mongo
        }
        else if (method.equals("POST")) {
            // TODO(eth003): link user with mongo
        }
    }
}

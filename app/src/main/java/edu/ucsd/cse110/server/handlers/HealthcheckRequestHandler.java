package edu.ucsd.cse110.server.handlers;

import com.sun.net.httpserver.*;
import java.io.*;

public class HealthcheckRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write("".getBytes());
        outStream.close();
    }
}

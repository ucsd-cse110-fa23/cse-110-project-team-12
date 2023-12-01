package edu.ucsd.cse110.server;

import com.sun.net.httpserver.*;

import edu.ucsd.cse110.api.ChatGPT;
import edu.ucsd.cse110.server.handlers.GenerateRequestHandler;
import edu.ucsd.cse110.server.handlers.RecipeRequestHandler;
import edu.ucsd.cse110.server.handlers.UserRequestHandler;
import edu.ucsd.cse110.server.services.mongodb.MongoDB;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class Server {
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";
    public static final String mongoURI = "mongodb+srv://akjain:92Tc0QE0BB1nCNTr@pantrypal.lzohxez.mongodb.net/?retryWrites=true&w=majority";

    public static void main(String[] args) throws IOException {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 0);
            server.createContext("/recipe", new RecipeRequestHandler(new MongoDB(mongoURI)));
            server.createContext("/user", new UserRequestHandler());
            server.createContext("/generate", new GenerateRequestHandler(new ChatGPT()));
    
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
            server.setExecutor(threadPoolExecutor);
    
            server.start();
    
            System.out.println("Server started on " + SERVER_HOSTNAME + ":" + SERVER_PORT);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

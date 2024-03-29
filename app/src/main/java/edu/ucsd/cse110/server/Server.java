package edu.ucsd.cse110.server;

import com.sun.net.httpserver.*;

import edu.ucsd.cse110.server.handlers.ChatGPTRequestHandler;
import edu.ucsd.cse110.server.handlers.DalleRequestHandler;
import edu.ucsd.cse110.server.handlers.RecipeRequestHandler;
import edu.ucsd.cse110.server.handlers.UserRequestHandler;
import edu.ucsd.cse110.server.handlers.WhisperRequestHandler;
import edu.ucsd.cse110.server.handlers.ShareRequestHandler;
import edu.ucsd.cse110.server.services.mongodb.MongoDB;
import edu.ucsd.cse110.server.services.mongodb.MongoDBInterface;
import edu.ucsd.cse110.server.services.mongodb.MongoDBMock;
import edu.ucsd.cse110.server.services.whisper.Whisper;
import edu.ucsd.cse110.server.services.whisper.WhisperInterface;
import edu.ucsd.cse110.server.services.whisper.WhisperMock;
import edu.ucsd.cse110.server.services.chatgpt.ChatGPT;
import edu.ucsd.cse110.server.services.chatgpt.ChatGPTInterface;
import edu.ucsd.cse110.server.services.chatgpt.ChatGPTMock;
import edu.ucsd.cse110.server.services.dalle.DallE;
import edu.ucsd.cse110.server.services.dalle.DallEInterface;
import edu.ucsd.cse110.server.services.dalle.DallEMock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class Server {
    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";
    public static final String mongoURI = "mongodb+srv://akjain:92Tc0QE0BB1nCNTr@pantrypal.lzohxez.mongodb.net/?retryWrites=true&w=majority";

    public static void main(String[] args) throws IOException {
        try {
            MongoDBInterface mongodb = new MongoDBMock();
            ChatGPTInterface chatgpt = new ChatGPTMock();
            DallEInterface dalle = new DallEMock();
            WhisperInterface whisper = new WhisperMock();

            if (args.length == 0) {
                mongodb = new MongoDB(mongoURI);
                chatgpt = new ChatGPT();
                dalle = new DallE();
                whisper = new Whisper();
            }

            HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 0);
            server.createContext("/recipe", new RecipeRequestHandler(mongodb));
            server.createContext("/user", new UserRequestHandler(mongodb));
            server.createContext("/chatgpt", new ChatGPTRequestHandler(chatgpt));
            server.createContext("/whisper", new WhisperRequestHandler(whisper));
            server.createContext("/dalle", new DalleRequestHandler(dalle));
            server.createContext("/share", new ShareRequestHandler(mongodb));

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

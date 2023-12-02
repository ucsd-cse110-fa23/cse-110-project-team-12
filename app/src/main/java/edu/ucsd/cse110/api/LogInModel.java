package edu.ucsd.cse110.api;

import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import edu.ucsd.cse110.server.schemas.UserSchema;
import edu.ucsd.cse110.server.services.Utils;

public class LogInModel implements ModelInterface {
    private Controller controller;
    
    private String storagePath = "./src/main/java/edu/ucsd/cse110/api/assets/";
    
    public LogInModel(Controller c) {
        this.controller = c;
        
        // automaic sign-in
        File f = new File(storagePath + "savelogin.txt");
        if(f.exists()) { 
            try {
                String username = "", password = "";
                BufferedReader br = new BufferedReader(new FileReader(f));
                username = br.readLine();
                password = br.readLine();

                UserSchema user = getUser(username, password);
                if(username != null && password != null && user != null) {
                    logIn(user);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.LogInView.LogInButton) {
            String username = (String) m.getKey("Username");
            String password = (String) m.getKey("Password");
            boolean rememberMe = (boolean) m.getKey("AutomaticLogIn");

            UserSchema user = getUser(username, password);
            if (user != null) {
                logIn(user);
                if (rememberMe) {
                    try {
                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(storagePath + "savelogin.txt")));
                        pw.println(username);
                        pw.println(password);
                        pw.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if (m.getMessageType() == Message.LogInView.SignUpButton) {
            controller.receiveMessageFromModel(new Message(Message.LogInModel.CloseLogInView));
            controller.receiveMessageFromModel(new Message(Message.LogInModel.StartCreateAccountView));
        }
    }

    private void logIn(UserSchema user){
        controller.receiveMessageFromModel(new Message(Message.LogInModel.SetUser,
            Map.ofEntries(Map.entry("User", user))));
        controller.receiveMessageFromModel(new Message(Message.LogInModel.CloseLogInView));
        controller.receiveMessageFromModel(new Message(Message.LogInModel.StartHomeView));
    }
    
    // Returns null if no user found with specified username and password.
    private UserSchema getUser(String username, String password) {
        if (username == null || password == null)
            return null;
        try {
            // Needed here so special characters can be passed into the url.
            String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
            String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8);
            
            String urlString = Controller.serverUrl + "/user?username=" + encodedUsername + "&password=" + encodedPassword;
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                Scanner in = new Scanner(conn.getInputStream());
                String jsonString = "";
                while (in.hasNext())
                    jsonString += in.nextLine();
                in.close();
                return Utils.unmarshalJson(jsonString, UserSchema.class);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Object getState() {
        return this;
    }
}
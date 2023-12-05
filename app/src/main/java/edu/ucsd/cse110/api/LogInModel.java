package edu.ucsd.cse110.api;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import edu.ucsd.cse110.server.schemas.UserSchema;
import edu.ucsd.cse110.server.services.Utils;

public class LogInModel implements ModelInterface {
    Controller controller;
    
    String storagePath = "./src/main/java/edu/ucsd/cse110/api/assets/";
    
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
            String username = m.getKey("Username");
            String password = m.getKey("Password");
            boolean rememberMe = m.getKey("AutomaticLogIn");

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

    private void logIn(UserSchema user) {
        controller.receiveMessageFromModel(new Message(Message.LogInModel.SetUser, "User", user));
        controller.receiveMessageFromModel(new Message(Message.LogInModel.CloseLogInView));
        controller.receiveMessageFromModel(new Message(Message.LogInModel.StartHomeView));
    }
    
    // Returns null if no user found with specified username and password.
    private UserSchema getUser(String username, String password) {
        if (username == null || password == null)
            return null;

        // Needed here so special characters can be passed into the url.
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
        String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8);
        String urlString = Controller.serverUrl + "/user?username=" + encodedUsername + "&password=" + encodedPassword;
        ServerResponse response = controller.server.makeHttpRequest(urlString, "GET", "");
        
        if (response.getStatusCode() == 200)
            return Utils.unmarshalJson(response.getResponseBody(), UserSchema.class);
        else
            return null;
    }
}
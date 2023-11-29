package edu.ucsd.cse110.api;

import java.io.*;

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
                if(username != null && password != null && userValid(username, password)) {
                    logIn(username, password);
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
            if (userValid(username, password)) {
                logIn(username, password);
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
        } else if(m.getMessageType() == Message.LogInView.SignUpButton) {
            controller.receiveMessageFromModel(new Message(Message.LogInModel.CloseLogInView));
            controller.receiveMessageFromModel(new Message(Message.LogInModel.StartCreateAccountView));
        }
    }

    private void logIn(String username, String password){
        controller.username = username;
        controller.password = password;
        controller.receiveMessageFromModel(new Message(Message.LogInModel.CloseLogInView));
        controller.receiveMessageFromModel(new Message(Message.LogInModel.StartHomeView));
    }
    
    private boolean userValid(String username, String password) {
        return controller.mongoDB.isValidUser(username, password);
    }
    
    public Object getState() {
        return this;
    }
}
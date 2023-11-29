package edu.ucsd.cse110.api;

import java.io.*;

public class LogInModel implements ModelInterface {
    private Controller controller;

    public LogInModel(Controller c) {
        this.controller = c;
    }

    @Override
    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.LogInView.LogInButton) {
            String username = (String) m.getKey("Username");
            String password = (String) m.getKey("Password");
            boolean rememberMe = (boolean) m.getKey("AutomaticLogIn");
            if (userValid(username, password) || true) {
                controller.username = username;
                controller.password = password;
                controller.receiveMessageFromModel(new Message(Message.LogInModel.CloseLogInView));
                controller.receiveMessageFromModel(new Message(Message.LogInModel.StartHomeView));
                if (rememberMe) {
                    try {
                        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./src/main/java/edu/ucsd/cse110/api/assets/savelogin.txt")));
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

    private boolean userValid(String username, String password) {
        return controller.mongoDB.isValidUser(username, password);
    }

    @Override
    public Object getState() {
        return this;
    }
    
}

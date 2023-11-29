package edu.ucsd.cse110.api;

import java.io.FileWriter;

public class LogInModel implements ModelInterface {
    private Controller controller;

    public LogInModel(Controller c) {
        this.controller = c;
    }

    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.LoginView.Login) {
            String username = (String) m.getKey("username");
            String password = (String) m.getKey("password");
            boolean rememberMe = (boolean) m.getKey("savelogin");
            if (userValid(username, password) || true) {
                controller.receiveMessageFromModel(new Message(Message.LoginModel.Login));
                if (rememberMe) {
                    try {
                        FileWriter fw = new FileWriter("./src/main/java/edu/ucsd/cse110/api/assets/savelogin.txt");
                        fw.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean userValid(String username, String password) {
        return controller.mongoDB.isValidUser(username, password);
    }

    public Object getState() {
        return this;
    }
}
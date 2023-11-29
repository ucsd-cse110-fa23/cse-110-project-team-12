package edu.ucsd.cse110.api;

import edu.ucsd.cse110.api.Controller.UIType;

public class LoginModel implements ModelInterface {
    private Controller controller;

    public LoginModel(Controller c) {
        this.controller = c;
    }

    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.LoginView.Login) {
            String username = (String) m.getKey("username");
            String password = (String) m.getKey("password");
            if (userValid(username, password) || true) {
                controller.receiveMessageFromModel(new Message(Message.LoginModel.Login));
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

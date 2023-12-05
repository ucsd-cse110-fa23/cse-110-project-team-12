package edu.ucsd.cse110.api;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import edu.ucsd.cse110.server.schemas.UserSchema;
import edu.ucsd.cse110.server.services.Utils;
public class CreateAccountModel implements ModelInterface {

    private Controller controller;

    public CreateAccountModel(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void receiveMessage(Message m) {
        if(m.getMessageType() == Message.CreateAccountView.BackButton) {
            controller.receiveMessageFromModel(new Message(Message.CreateAccountModel.CloseCreateAccountView));
            controller.receiveMessageFromModel(new Message(Message.CreateAccountModel.StartLogInView));
        }
        else if(m.getMessageType() == Message.CreateAccountView.SignUpButton) {
            String username = m.getKey("Username");
            String password = m.getKey("Password");
            boolean rememberMe = m.getKey("AutomaticLogIn");

            UserSchema newUser = createUser(username, password);
            if (newUser != null) {
                controller.receiveMessageFromModel(new Message(Message.CreateAccountModel.SetUser, "User", newUser));
                controller.receiveMessageFromModel(new Message(Message.CreateAccountModel.CloseCreateAccountView));
                controller.receiveMessageFromModel(new Message(Message.CreateAccountModel.StartHomeView));

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
            else {
                controller.receiveMessageFromModel(new Message(Message.CreateAccountModel.ErrorUsernameTaken));
            }
        }
    }

    private UserSchema createUser(String username, String password) {
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
        String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8);
        String urlString = Controller.serverUrl + "/user?username=" + encodedUsername + "&password=" + encodedPassword;
        ServerResponse response = HttpUtils.makeHttpRequest(urlString, "POST", "");
        
        if (response.getStatusCode() == 201)
            return Utils.unmarshalJson(response.getResponseBody(), UserSchema.class);
        else {
            controller.receiveMessageFromModel(new Message(Message.HttpRequest.ServerError));
            return null;
        }
    }
    
}

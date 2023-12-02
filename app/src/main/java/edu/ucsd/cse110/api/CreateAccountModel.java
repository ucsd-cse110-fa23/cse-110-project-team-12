package edu.ucsd.cse110.api;

import java.io.*;
import java.net.*;
import java.util.*;

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
            String username = (String) m.getKey("Username");
            String password = (String) m.getKey("Password");
            boolean rememberMe = (boolean) m.getKey("AutomaticLogIn");

            UserSchema newUser = createUser(username, password);
            if (newUser != null) {
                controller.receiveMessageFromModel(new Message(Message.CreateAccountModel.SetUser,
                    Map.ofEntries(Map.entry("User", newUser))));
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
        try {
            String urlString = Controller.serverUrl + "/user?username=" + username + "&password=" + password;
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode == 201) {
                // 201 code means created.
                Scanner in = new Scanner(conn.getInputStream());
                String jsonString = "";
                while (in.hasNext())
                    jsonString += in.nextLine();
                in.close();
                return Utils.unmarshalJson(jsonString, UserSchema.class);
            }
            else if (responseCode == 409) {
                // 409 code means duplicate resource.
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getState() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getState'");
    }
    
}

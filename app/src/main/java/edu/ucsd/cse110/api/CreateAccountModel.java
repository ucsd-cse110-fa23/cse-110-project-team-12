package edu.ucsd.cse110.api;

public class CreateAccountModel implements ModelInterface{

    private Controller controller;

    public CreateAccountModel(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void receiveMessage(Message m) {
        if(m.getMessageType() == Message.CreateAccountView.BackButton) {
            controller.receiveMessageFromModel(new Message(Message.CreateAccountModel.CloseCreateAccountView));
            controller.receiveMessageFromModel(new Message(Message.CreateAccountModel.StartLogInView));
        }else if(m.getMessageType() == Message.CreateAccountView.SignUpButton) {
            String username = (String) m.getKey("Username");
            String password = (String) m.getKey("Password");
            boolean rememberMe = (boolean) m.getKey("AutomaticLogIn");
            if (controller.mongoDB.createUser(username, password)){
                controller.username = username;
                controller.password = password;

                controller.receiveMessageFromModel(new Message(Message.CreateAccountModel.CloseCreateAccountView));
                controller.receiveMessageFromModel(new Message(Message.CreateAccountModel.StartHomeView));
            }else{
                controller.receiveMessageFromModel(new Message(Message.CreateAccountModel.ErrorUsernameTaken));
            }
        }
    }

    @Override
    public Object getState() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getState'");
    }
    
}

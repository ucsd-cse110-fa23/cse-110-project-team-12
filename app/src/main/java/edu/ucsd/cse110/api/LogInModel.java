package edu.ucsd.cse110.api;

public class LogInModel implements ModelInterface{
    private Controller controller;
    public LogInModel(Controller controller){
        this.controller = controller;
    }

    @Override
    public void receiveMessage(Message m) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'receiveMessage'");
    }

    @Override
    public Object getState() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getState'");
    }
    
}

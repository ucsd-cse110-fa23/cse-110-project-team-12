package edu.ucsd.cse110.client;

import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import javafx.scene.Node;
import javafx.scene.Parent;

public class NoUI implements UIInterface {

    @Override
    public void receiveMessage(Message m) {
        return;
    }

    @Override
    public void addChild(Node ui) {
        return;
    }

    @Override
    public void removeChild(Node ui) {
        return;
    }

    @Override
    public Parent getUI() {
        return null;
    }
    
}

package edu.ucsd.cse110.client;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class NoUI implements UIInterface {

    @Override
    public void addNode(Node node) {
        return;
    }

    @Override
    public void removeNode(Node node) {
        return;
    }
    
    @Override
    public BorderPane getUI() {
        return null;
    }
    
}

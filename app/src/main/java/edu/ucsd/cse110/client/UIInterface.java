package edu.ucsd.cse110.client;

import javafx.scene.Parent;
import javafx.scene.Node;

public interface UIInterface {
    public void addNode(Node node);
    public void removeNode(Node node);
    public Parent getUI();
}

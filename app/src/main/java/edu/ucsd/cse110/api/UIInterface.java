package edu.ucsd.cse110.api;

import javafx.scene.Node;
import javafx.scene.Parent;

public interface UIInterface {
    void receiveMessage(Message m);
    void addChild(Node ui);
    void removeChild(Node ui);
    Parent getUI();
}

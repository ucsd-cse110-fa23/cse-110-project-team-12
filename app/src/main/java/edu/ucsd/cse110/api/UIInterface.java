package edu.ucsd.cse110.api;

import javafx.scene.Node;

public interface UIInterface {
    void receiveMessage(Message m);
    void addChild(Node ui);
    void removeChild(Node ui);
}

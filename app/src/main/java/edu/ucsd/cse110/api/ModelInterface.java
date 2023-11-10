package edu.ucsd.cse110.api;

public interface ModelInterface {
    void receiveMessage(Message m);
    Object getState(); // mainly for testing
}

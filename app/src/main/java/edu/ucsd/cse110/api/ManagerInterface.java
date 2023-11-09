package edu.ucsd.cse110.api;

import edu.ucsd.cse110.client.NoUI;
import edu.ucsd.cse110.client.UIInterface;
import javafx.scene.Parent;

public abstract class ManagerInterface {
    UIInterface ui;
    public ManagerInterface() {
        ui = new NoUI();
    }
    public void addUI(UIInterface ui) {
        this.ui = ui;
    }
    public Parent getUI() {
        return ui.getUI();
    }
}

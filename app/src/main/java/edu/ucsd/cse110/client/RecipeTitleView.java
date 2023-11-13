package edu.ucsd.cse110.client;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class RecipeTitleView extends HBox implements UIInterface {
    private Controller controller;
    private Button titleButton;
    private String recipeTitle;
    private String recipeBody;

    public RecipeTitleView(Controller c, String recipeTitle, String recipeBody) {
        controller = c;
        this.recipeTitle = recipeTitle;
        this.recipeBody = recipeBody;
        titleButton = new Button(recipeTitle);
        titleButton.setId("recipe-title-view");
        titleButton.setWrapText(true);
        this.getChildren().add(titleButton);
    }

    @Override
    public void receiveMessage(Message m) {
    }

    @Override
    public void addChild(Node n) {
    }

    @Override
    public void removeChild(Node n) {
    }

    @Override
    public Parent getUI() {
        return this;
    }
}

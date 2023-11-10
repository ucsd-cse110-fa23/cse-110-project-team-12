package edu.ucsd.cse110.api;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;


public class RecipeDetailedModel extends StackPane implements UIInterface {
    public RecipeDetailedModel(String recipeName, String recipeBody) {
        Label name = new Label(recipeName);
        Label body = new Label(recipeBody);
        getChildren().addAll(name, body);
    }
    public void receiveMessage(Message m) {

    }
    public void addChild(Node ui) {

    }
    public void removeChild(Node ui) {

    }
}

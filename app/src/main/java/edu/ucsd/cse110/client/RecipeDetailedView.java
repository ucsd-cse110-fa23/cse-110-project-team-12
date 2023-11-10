package edu.ucsd.cse110.client;

import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;


public class RecipeDetailedView extends StackPane implements UIInterface {
    public RecipeDetailedView(String recipeName, String recipeBody) {
        Label name = new Label(recipeName);
        Label body = new Label(recipeBody);
        getChildren().addAll(name, body);
        System.out.println(recipeName + " " + recipeBody);
    }
    public void receiveMessage(Message m) {

    }
    public void addChild(Node ui) {

    }
    public void removeChild(Node ui) {

    }
}

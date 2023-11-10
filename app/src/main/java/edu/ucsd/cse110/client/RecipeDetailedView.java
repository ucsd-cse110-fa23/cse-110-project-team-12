package edu.ucsd.cse110.client;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;


public class RecipeDetailedView extends StackPane implements UIInterface {
    private Controller controller;

    String recipeName;
    String recipeBody;

    public RecipeDetailedView(Controller c) {
        controller = c;
        
        Label name = new Label(recipeName);
        Label body = new Label(recipeBody);
        this.getChildren().addAll(name, body);

        
    }
    @Override
    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.CreateRecipeModel.SendTitleBody) {
            this.recipeName = (String) m.getKey("RecipeTitle");
            this.recipeBody = (String) m.getKey("RecipeBody");
        }
    }
    @Override
    public void addChild(Node ui) {

    }
    @Override
    public void removeChild(Node ui) {

    }
    @Override
    public Parent getUI() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUI'");
    }
}

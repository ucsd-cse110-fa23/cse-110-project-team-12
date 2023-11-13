package edu.ucsd.cse110.client;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.util.*;

public class RecipeTitleView extends HBox implements UIInterface {
    private Controller controller;
    private Button titleButton;
    private Recipe recipe;

    public RecipeTitleView(Controller c, Recipe r) {
        controller = c;
        this.recipe = r;
        titleButton = new Button(r.getName());
        titleButton.setId("recipe-title-view");
        titleButton.setWrapText(true);
        this.getChildren().add(titleButton);

        titleButton.setOnAction(e -> {
            controller.receiveMessageFromModel(new Message(Message.CreateRecipeModel.StartRecipeDetailedView));
            controller.receiveMessageFromModel(
                new Message(Message.CreateRecipeModel.SendTitleBody,
                Map.ofEntries(Map.entry("Recipe", recipe)))
            );
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveUnsavedLayout));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));  
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.AddBackButton));
        });
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

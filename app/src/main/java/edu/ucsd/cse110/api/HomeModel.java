package edu.ucsd.cse110.api;

import java.util.List;

import edu.ucsd.cse110.api.Controller.UIType;
import edu.ucsd.cse110.client.Recipe;

public class HomeModel implements ModelInterface {
    private Controller controller;

    // TODO: this is for our list of recipes
    private List<Recipe> savedRecipes;
    private UIType currentView;

    public HomeModel(Controller c) {
        this.controller = c;
        currentView = UIType.HomePage;
    }
    
    public void receiveMessage(Message m) {
        if(m.getMessageType() == Message.HomeView.CreateRecipeButton) {
            currentView = UIType.CreateRecipe;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.StartCreateRecipeView));
        }
        if(m.getMessageType() == Message.CreateRecipeModel.CloseCreateRecipeView) {
            currentView = UIType.HomePage;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.CloseCreateRecipeView));
        }
        if(m.getMessageType() == Message.CreateRecipeModel.StartRecipeDetailedView) {
            currentView = UIType.DetailedView;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.StartRecipeDetailedView));
        }
        if(m.getMessageType() == Message.RecipeDetailedModel.CloseRecipeDetailedView) {
            currentView = UIType.HomePage;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.CloseRecipeDetailedView));
        }
    }

    public UIType getCurrentView() {
        return currentView;
    }

    @Override
    public Object getState() {
        return this;
    }
}

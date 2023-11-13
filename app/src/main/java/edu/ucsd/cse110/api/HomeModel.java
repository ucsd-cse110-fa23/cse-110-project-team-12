package edu.ucsd.cse110.api;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import edu.ucsd.cse110.api.Controller.UIType;
import edu.ucsd.cse110.client.Recipe;

public class HomeModel implements ModelInterface {
    private Controller controller;

    private UIType currentView;

    public HomeModel(Controller c) {
        this.controller = c;
        currentView = UIType.HomePage;
        updateRecipeList();
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
        if (m.getMessageType() == Message.HomeView.UpdateRecipeList) {
            updateRecipeList();
        }
    }

    public UIType getCurrentView() {
        return currentView;
    }

    private void updateRecipeList() {
        try {
            Path path = Paths.get(Controller.storagePath + "csv");
            List<String> allLines = Files.readAllLines(path);
            List<Recipe> recipes = new ArrayList<>();
            for (String line : allLines) {
                // https://stackoverflow.com/a/15739042
                String[] recipeInfo = line.split(",(?=([^\"]|\"[^\"]*\")*$)");
                recipeInfo[0] = recipeInfo[0].replace("\"", "");
                recipes.add(new Recipe(recipeInfo[0], recipeInfo[1]));
            }
            controller.receiveMessageFromModel(
                new Message(Message.HomeModel.UpdateRecipeList,
                Map.ofEntries(Map.entry("Recipes", recipes))
            ));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getState() {
        return this;
    }
}

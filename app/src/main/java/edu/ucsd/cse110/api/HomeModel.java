package edu.ucsd.cse110.api;

import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.client.Recipe;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HomeModel implements ModelInterface {
    private Controller controller;
    private List<Recipe> recipes;
    private UIFactory.Type currentView;

    public HomeModel(Controller c) {
        this.controller = c;
        currentView = UIFactory.Type.HomePage;
        updateRecipeList();
    }

    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.HomeView.CreateRecipeButton) {
            currentView = UIFactory.Type.CreateRecipe;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.StartCreateRecipeView));
        }
        if (m.getMessageType() == Message.CreateRecipeModel.CloseCreateRecipeView) {
            currentView = UIFactory.Type.HomePage;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.CloseCreateRecipeView));
        }
        if (m.getMessageType() == Message.CreateRecipeModel.StartRecipeDetailedView) {
            currentView = UIFactory.Type.DetailedView;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.StartRecipeDetailedView));
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.CloseRecipeDetailedView) {
            currentView = UIFactory.Type.HomePage;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.CloseRecipeDetailedView));
        }
        if (m.getMessageType() == Message.HomeView.UpdateRecipeList) {
            updateRecipeList();
        }
        if (m.getMessageType() == Message.HomeView.OpenRecipe) {
            currentView = UIFactory.Type.DetailedView;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.StartRecipeDetailedView));
            Recipe openRecipe = (Recipe) m.getKey("Recipe");
            controller.receiveMessageFromModel(new Message(Message.HomeModel.SendTitleBody,
                    Map.ofEntries(Map.entry("Recipe", openRecipe))));
        }
        if (m.getMessageType() == Message.HomeView.LogOut) {
            currentView = UIFactory.Type.HomePage;
            controller.username = null;
            controller.password = null;
            deleteLogInFile();

            controller.receiveMessageFromModel(new Message(Message.HomeModel.CloseHomeView));
            controller.receiveMessageFromModel(new Message(Message.HomeModel.StartLogInView));
        }
    }

    private String storagePath = "./src/main/java/edu/ucsd/cse110/api/assets/";
    private void deleteLogInFile() {
        try {
            Path path = Paths.get(storagePath + "savelogin.txt");
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UIFactory.Type getCurrentView() {
        return currentView;
    }

    private void updateRecipeList() {
        recipes = controller.mongoDB.getRecipeList(controller.username, controller.password);
        controller.receiveMessageFromModel(
                        new Message(Message.HomeModel.UpdateRecipeList,
                                Map.ofEntries(Map.entry("Recipes", recipes))));
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    @Override
    public Object getState() {
        return this;
    }
}

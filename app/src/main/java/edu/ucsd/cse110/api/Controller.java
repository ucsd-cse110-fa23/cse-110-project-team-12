package edu.ucsd.cse110.api;

import java.util.*;

import edu.ucsd.cse110.api.Message.Key;
import edu.ucsd.cse110.client.CreateRecipeView;
import edu.ucsd.cse110.client.HomeView;
import edu.ucsd.cse110.client.RecipeDetailedView;
import javafx.scene.Node;
import javafx.scene.Parent;

public class Controller {
    public enum ModelType {
        CreateRecipe,
        HomePage,
        DetailedView,
    }
    
    public enum UIType {
        CreateRecipe,
        HomePage,
        DetailedView,
    }

    private Map<ModelType, ModelInterface> models;
    private Map<UIType, UIInterface> uis;

    public Controller() {
        models = new HashMap<>();
        uis = new HashMap<>();

        HomeModel homeModel = new HomeModel(this);
        HomeView homeView = new HomeView(this);
        models.put(ModelType.HomePage, homeModel);
        uis.put(UIType.HomePage, homeView);
    }

    public Parent getUIRoot() {
        return (Parent) uis.get(UIType.HomePage);
    }

    public void addModel(ModelType type, ModelInterface model) {
        models.put(type, model);
    }

    public void addUI(UIType type, UIInterface ui) {
        uis.put(type, ui);
    }

    public void receiveMessageFromModel(Message m) {
        if (m.getMessageType() == Message.Type.ButtonCloseCreateRecipe) {
            uis.get(UIType.HomePage).removeChild((Node) uis.get(UIType.CreateRecipe));
        }
        else if (m.getMessageType() == Message.Type.FinishedCreatingRecipe) {
            String recipeName = (String) m.getKey(Key.RecipeTitle);
            String recipeBody = (String) m.getKey(Key.RecipeBody);
            RecipeDetailedView detailedView = new RecipeDetailedView(recipeName, recipeBody);
            uis.get(UIType.HomePage).removeChild((Node) uis.get(UIType.CreateRecipe));
            uis.get(UIType.HomePage).addChild((Node) detailedView);
        }
        uis.forEach((uiType, ui) -> ui.receiveMessage(m));
    }

    public void receiveMessageFromUI(Message m) {
        if (m.getMessageType() == Message.Type.ButtonCreateRecipe) {
            CreateRecipeView createRecipeView = new CreateRecipeView(this);
            CreateRecipeModel createRecipeModel = new CreateRecipeModel(this, new VoicePrompt("./voice.wav"), new Whisper(), new ChatGPT());

            uis.put(UIType.CreateRecipe, createRecipeView);
            models.put(ModelType.CreateRecipe, createRecipeModel);

            uis.get(UIType.HomePage).addChild((Node) createRecipeView);
        }
        models.forEach((mType, model) -> model.receiveMessage(m));
    }
}

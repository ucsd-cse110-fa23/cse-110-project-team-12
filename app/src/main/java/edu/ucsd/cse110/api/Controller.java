package edu.ucsd.cse110.api;

import java.util.*;

import edu.ucsd.cse110.client.CreateRecipeView;
import edu.ucsd.cse110.client.HomeView;
import edu.ucsd.cse110.client.RecipeDetailedView;
import edu.ucsd.cse110.client.NoUI;
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

    private UIInterface make(UIType type) {
        if (useUI) {
            if (type == UIType.CreateRecipe)
                return new CreateRecipeView(this);
            else if (type == UIType.HomePage)
                return new HomeView(this);
            else if (type == UIType.DetailedView)
                return new RecipeDetailedView(this);
            else
                return new NoUI();
        } else
            return new NoUI();
    }

    private Map<ModelType, ModelInterface> models;
    private Map<UIType, UIInterface> uis;

    public boolean useUI;
    public static final String storagePath = "./src/main/java/edu/ucsd/cse110/api/assets/savedRecipes.";
    private VoicePromptInterface voicePrompt;
    private WhisperInterface whisper;
    private ChatGPTInterface chatGPT;

    public Controller(boolean useUI, VoicePromptInterface voicePrompt, WhisperInterface whisper,
            ChatGPTInterface chatGPT) {
        this.useUI = useUI;
        this.voicePrompt = voicePrompt;
        this.whisper = whisper;
        this.chatGPT = chatGPT;

        models = new EnumMap<>(ModelType.class);
        uis = new EnumMap<>(UIType.class);

        UIInterface homeView = make(UIType.HomePage);
        uis.put(UIType.HomePage, homeView);
        HomeModel homeModel = new HomeModel(this);
        models.put(ModelType.HomePage, homeModel);
    }

    public Parent getUIRoot() {
        return uis.get(UIType.HomePage).getUI();
    }

    public void addModel(ModelType type, ModelInterface model) {
        models.put(type, model);
    }

    public void addUI(UIType type, UIInterface ui) {
        uis.put(type, ui);
    }

    public void receiveMessageFromModel(Message m) {
        // Controller intercepts all message that update UI Types
        if (m.getMessageType() == Message.HomeModel.StartCreateRecipeView) {
            CreateRecipeModel createRecipeModel = new CreateRecipeModel(this, voicePrompt, whisper, chatGPT);
            addModel(ModelType.CreateRecipe, createRecipeModel);

            UIInterface createRecipeView = make(UIType.CreateRecipe);
            addUI(UIType.CreateRecipe, createRecipeView);

            uis.get(UIType.HomePage).addChild(createRecipeView.getUI());
        } else if (m.getMessageType() == Message.HomeModel.CloseCreateRecipeView) {
            uis.get(UIType.HomePage).removeChild(uis.get(UIType.CreateRecipe).getUI());
        } else if (m.getMessageType() == Message.HomeModel.StartRecipeDetailedView) {
            RecipeDetailedModel detailedModel = new RecipeDetailedModel(this);
            addModel(ModelType.DetailedView, detailedModel);

            UIInterface detailedView = make(UIType.DetailedView);
            addUI(UIType.DetailedView, detailedView);

            uis.get(UIType.HomePage).addChild(detailedView.getUI());
        } else if (m.getMessageType() == Message.HomeModel.CloseRecipeDetailedView) {
            uis.get(UIType.HomePage).removeChild(uis.get(UIType.DetailedView).getUI());
        }
        uis.forEach((uiType, ui) -> ui.receiveMessage(m));
        models.forEach((mType, model) -> model.receiveMessage(m));
    }

    public void receiveMessageFromUI(Message m) {
        models.forEach((mType, model) -> model.receiveMessage(m));
    }

    // Testing Use
    public Object getState(ModelType type) {
        return models.get(type).getState();
    }
}
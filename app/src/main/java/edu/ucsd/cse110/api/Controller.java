package edu.ucsd.cse110.api;

import java.util.*;

import edu.ucsd.cse110.client.CreateAccountView;
import edu.ucsd.cse110.client.CreateRecipeView;
import edu.ucsd.cse110.client.HomeView;
import edu.ucsd.cse110.client.LogInView;
import edu.ucsd.cse110.client.RecipeDetailedView;
import edu.ucsd.cse110.client.Root;
import edu.ucsd.cse110.client.NoUI;
import javafx.scene.Parent;

public class Controller {
    public enum ModelType {
        CreateRecipe,
        HomePage,
        DetailedView,
        CreateAccount,
        LogIn,
    }

    public enum UIType {
        CreateRecipe,
        HomePage,
        DetailedView,
		CreateAccount,
		LogIn,
    }

    private UIInterface make(UIType type) {
        if (useUI) {
            if (type == UIType.CreateRecipe)
                return new CreateRecipeView(this);
            else if (type == UIType.HomePage)
                return new HomeView(this);
            else if (type == UIType.DetailedView)
				return new RecipeDetailedView(this);
			else if (type == UIType.CreateAccount)
				return new CreateAccountView(this);
			else if (type == UIType.LogIn)
				return new LogInView(this);
            else
                return new NoUI();
        } else
            return new NoUI();
    }

    private Map<ModelType, ModelInterface> models;
    private Map<UIType, UIInterface> uis;
	private UIInterface root;

    public boolean useUI;
    
    public static final String mongoURI = "mongodb+srv://akjain:92Tc0QE0BB1nCNTr@pantrypal.lzohxez.mongodb.net/?retryWrites=true&w=majority";
   
    // API Interfaces
    public VoicePromptInterface voicePrompt;
    public WhisperInterface whisper;
    public ChatGPTInterface chatGPT;
    public MongoDBInterface mongoDB;

    public Controller(boolean useUI, VoicePromptInterface voicePrompt, WhisperInterface whisper,
            ChatGPTInterface chatGPT, MongoDBInterface mongoDB) {
        this.useUI = useUI;
        this.voicePrompt = voicePrompt;
        this.whisper = whisper;
        this.chatGPT = chatGPT;
        this.mongoDB = mongoDB;

        models = new EnumMap<>(ModelType.class);
        uis = new EnumMap<>(UIType.class);
		    root = new Root();
		    this.receiveMessageFromModel(new Message(Message.HomeModel.StartLogInView));
    }

    public Parent getUIRoot() {
        return root.getUI();
    }

    public void addModel(ModelType type, ModelInterface model) {
        models.put(type, model);
    }

    public void addUI(UIType type, UIInterface ui) {
        uis.put(type, ui);
    }

    public void receiveMessageFromModel(Message m) {
        // Controller intercepts all message that update UI Types
        if (m.getMessageType() == Message.CreateAccountModel.StartLogInView || m.getMessageType() == Message.HomeModel.StartLogInView) {
            LogInModel logInModel = new LogInModel(this);
            addModel(ModelType.LogIn, logInModel);

            UIInterface logInView = make(UIType.LogIn);
            addUI(UIType.LogIn, logInView);

            root.addChild(logInView.getUI());
        } else if(m.getMessageType() == Message.LogInModel.CloseLogInView) {
            models.remove(ModelType.LogIn);
            root.removeChild(uis.get(UIType.LogIn).getUI());
        } else if(m.getMessageType() == Message.LogInModel.StartCreateAccountView) {
            CreateAccountModel createAccountModel = new CreateAccountModel(this);
            addModel(ModelType.CreateAccount, createAccountModel);

            UIInterface createAccountView = make(UIType.CreateAccount);
            addUI(UIType.CreateAccount, createAccountView);

            root.addChild(createAccountView.getUI());
        }
        else if (m.getMessageType() == Message.CreateAccountModel.CloseCreateAccountView) {
            models.remove(ModelType.CreateAccount);
            root.removeChild(uis.get(UIType.CreateAccount).getUI());
        }else if (m.getMessageType() == Message.LogInModel.StartHomeView || m.getMessageType() == Message.CreateAccountModel.StartHomeView) {
            HomeModel homeModel = new HomeModel(this);
            addModel(ModelType.HomePage, homeModel);

            UIInterface homeView = make(UIType.HomePage);
            addUI(UIType.HomePage, homeView);
            this.receiveMessageFromUI(new Message(Message.HomeView.UpdateRecipeList));

            root.addChild(homeView.getUI());
        } else if (m.getMessageType() == Message.HomeModel.CloseHomeView) {
            models.remove(ModelType.HomePage);
            root.removeChild(uis.get(UIType.HomePage).getUI());
        } else if (m.getMessageType() == Message.HomeModel.StartCreateRecipeView) {
            CreateRecipeModel createRecipeModel = new CreateRecipeModel(this);
            addModel(ModelType.CreateRecipe, createRecipeModel);

            UIInterface createRecipeView = make(UIType.CreateRecipe);
            addUI(UIType.CreateRecipe, createRecipeView);

            uis.get(UIType.HomePage).addChild(createRecipeView.getUI());
        } else if (m.getMessageType() == Message.HomeModel.CloseCreateRecipeView) {
            models.remove(ModelType.CreateRecipe);
            uis.get(UIType.HomePage).removeChild(uis.get(UIType.CreateRecipe).getUI());
        } else if (m.getMessageType() == Message.HomeModel.StartRecipeDetailedView) {
            RecipeDetailedModel detailedModel = new RecipeDetailedModel(this);
            addModel(ModelType.DetailedView, detailedModel);

            UIInterface detailedView = make(UIType.DetailedView);
            addUI(UIType.DetailedView, detailedView);

            uis.get(UIType.HomePage).addChild(detailedView.getUI());
        } else if (m.getMessageType() == Message.HomeModel.CloseRecipeDetailedView) {
            models.remove(ModelType.DetailedView);
            uis.get(UIType.HomePage).removeChild(uis.get(UIType.DetailedView).getUI());
        }
        uis.forEach((uiType, ui) -> ui.receiveMessage(m));
        models.forEach((mType, model) -> model.receiveMessage(m));
    }

    public void receiveMessageFromUI(Message m) {
        models.forEach((mType, model) -> model.receiveMessage(m));
    }

    // Saving username and password for access
    public String username, password;
   
    // Testing Use
    public Object getState(ModelType type) {
        return models.get(type).getState();
    }

    public boolean existsModel(ModelType type) {
        return models.containsKey(type);
    }

    public boolean existsUI(UIType type) {
        return uis.containsKey(type);
    }
}
package edu.ucsd.cse110.api;

import java.util.*;

import edu.ucsd.cse110.client.CreateAccountView;
import edu.ucsd.cse110.client.CreateRecipeView;
import edu.ucsd.cse110.client.HomeView;
import edu.ucsd.cse110.client.LogInView;
import edu.ucsd.cse110.client.RecipeDetailedView;
import edu.ucsd.cse110.client.Root;
import edu.ucsd.cse110.server.schemas.UserSchema;
import edu.ucsd.cse110.client.NoUI;
import javafx.scene.Parent;

public class Controller {
    private Map<ModelFactory.Type, ModelInterface> models;
    private Map<UIFactory.Type, UIInterface> uis;
	private UIInterface root;
    private UserSchema currentUser;
    public boolean useUI;
    public static final String serverUrl = "http://localhost:8100";

    // API Interfaces
    public VoicePromptInterface voicePrompt;
    public WhisperInterface whisper;
    public ChatGPTInterface chatGPT;

    public Controller(boolean useUI, VoicePromptInterface voicePrompt, WhisperInterface whisper,
            ChatGPTInterface chatGPT) {
        this.useUI = useUI;
        this.voicePrompt = voicePrompt;
        this.whisper = whisper;
        this.chatGPT = chatGPT;

        models = new EnumMap<>(ModelFactory.Type.class);
        uis = new EnumMap<>(UIFactory.Type.class);
        root = new Root();
        
        this.receiveMessageFromModel(new Message(Message.HomeModel.StartLogInView));
    }

    public Parent getUIRoot() {
        return root.getUI();
    }

    public UserSchema getCurrentUser() {
        return currentUser;
    }

    public void makeOrReplaceModel(ModelFactory.Type type) {
        ModelInterface model = ModelFactory.make(type, this);
        models.put(type, model);
    }

    public void makeOrReplaceUI(UIFactory.Type type) {
        UIInterface ui = UIFactory.make(type, this);
        uis.put(type, ui);
    }

    public void receiveMessageFromModel(Message m) {
        // Controller intercepts all message that update UI Types
        if (m.getMessageType() == Message.CreateAccountModel.StartLogInView || m.getMessageType() == Message.HomeModel.StartLogInView) {
            makeOrReplaceUI(UIFactory.Type.LogIn);
            root.addChild(uis.get(UIFactory.Type.LogIn).getUI());

            makeOrReplaceModel(ModelFactory.Type.LogIn);
        }
        else if(m.getMessageType() == Message.LogInModel.CloseLogInView) {
            root.removeChild(uis.get(UIFactory.Type.LogIn).getUI());
            models.remove(ModelFactory.Type.LogIn);
        }
        else if(m.getMessageType() == Message.LogInModel.StartCreateAccountView) {
            makeOrReplaceUI(UIFactory.Type.CreateAccount);
            root.addChild(uis.get(UIFactory.Type.CreateAccount).getUI());
            
            makeOrReplaceModel(ModelFactory.Type.CreateAccount);           
        }
        else if (m.getMessageType() == Message.CreateAccountModel.CloseCreateAccountView) {
            root.removeChild(uis.get(UIFactory.Type.CreateAccount).getUI());
            models.remove(ModelFactory.Type.CreateAccount);
        }
        else if (m.getMessageType() == Message.CreateAccountModel.SetUser || m.getMessageType() == Message.LogInModel.SetUser) {
            currentUser = (UserSchema) m.getKey("User");
            System.out.println(currentUser._id + currentUser.username + currentUser.password);
        }
        else if (m.getMessageType() == Message.LogInModel.StartHomeView || m.getMessageType() == Message.CreateAccountModel.StartHomeView) {
            makeOrReplaceUI(UIFactory.Type.HomePage);
            root.addChild(uis.get(UIFactory.Type.HomePage).getUI());
            
            makeOrReplaceModel(ModelFactory.Type.HomePage);
        }
        else if (m.getMessageType() == Message.HomeModel.CloseHomeView) {
            root.removeChild(uis.get(UIFactory.Type.HomePage).getUI());
            models.remove(ModelFactory.Type.HomePage);
        }
        else if (m.getMessageType() == Message.HomeModel.StartCreateRecipeView) {
            makeOrReplaceUI(UIFactory.Type.CreateRecipe);
            uis.get(UIFactory.Type.HomePage).addChild(uis.get(UIFactory.Type.CreateRecipe).getUI());

            makeOrReplaceModel(ModelFactory.Type.CreateRecipe);
        }
        else if (m.getMessageType() == Message.HomeModel.CloseCreateRecipeView) {
            uis.get(UIFactory.Type.HomePage).removeChild(uis.get(UIFactory.Type.CreateRecipe).getUI());
            models.remove(ModelFactory.Type.CreateRecipe);
        }
        else if (m.getMessageType() == Message.HomeModel.StartRecipeDetailedView) {
            makeOrReplaceUI(UIFactory.Type.DetailedView);
            uis.get(UIFactory.Type.HomePage).addChild(uis.get(UIFactory.Type.DetailedView).getUI());
            
            makeOrReplaceModel(ModelFactory.Type.DetailedView);
        }
        else if (m.getMessageType() == Message.HomeModel.CloseRecipeDetailedView) {
            uis.get(UIFactory.Type.HomePage).removeChild(uis.get(UIFactory.Type.DetailedView).getUI());
            models.remove(ModelFactory.Type.DetailedView);
        }
        uis.forEach((uiType, ui) -> ui.receiveMessage(m));
        models.forEach((mType, model) -> model.receiveMessage(m));
    }

    public void receiveMessageFromUI(Message m) {
        models.forEach((mType, model) -> model.receiveMessage(m));
    }
   
    // Testing Use
    public Object getState(ModelFactory.Type type) {
        return models.get(type).getState();
    }

    public boolean existsModel(ModelFactory.Type type) {
        return models.containsKey(type);
    }

    public boolean existsUI(UIFactory.Type type) {
        return uis.containsKey(type);
    }
}

class ModelFactory {
    public enum Type {
        CreateRecipe,
        HomePage,
        DetailedView,
        CreateAccount,
        LogIn,
    }

    public static ModelInterface make(Type type, Controller c) {
        if (type == Type.CreateRecipe)
            return new CreateRecipeModel(c);
        else if (type == Type.HomePage)
            return new HomeModel(c);
        else if (type == Type.DetailedView)
            return new RecipeDetailedModel(c);
        else if (type == Type.CreateAccount)
            return new CreateAccountModel(c);
        else if (type == Type.LogIn)
            return new LogInModel(c);
        return null;
    }
}

class UIFactory {
    public enum Type {
        CreateRecipe,
        HomePage,
        DetailedView,
		CreateAccount,
		LogIn,
        NoUI,
    }

    public static UIInterface make(Type type, Controller c) {
        if (type == Type.CreateRecipe)
            return new CreateRecipeView(c);
        else if (type == Type.HomePage)
            return new HomeView(c);
        else if (type == Type.DetailedView)
            return new RecipeDetailedView(c);
        else if (type == Type.CreateAccount)
            return new CreateAccountView(c);
        else if (type == Type.LogIn)
            return new LogInView(c);
        else if (type == Type.NoUI)
            return new NoUI();
        return null;
    }
}
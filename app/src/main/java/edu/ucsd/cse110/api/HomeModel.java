package edu.ucsd.cse110.api;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.services.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.*;
import java.util.*;

public class HomeModel implements ModelInterface {
    private Controller controller;
    private List<RecipeSchema> recipes;
    private UIFactory.Type currentView;

    public HomeModel(Controller c) {
        this.controller = c;
        currentView = UIFactory.Type.HomePage;
        recipes = new ArrayList<>();
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
            RecipeSchema openRecipe = (RecipeSchema) m.getKey("Recipe");
            controller.receiveMessageFromModel(new Message(Message.HomeModel.SendRecipe,
                    Map.ofEntries(Map.entry("Recipe", openRecipe))));
        }
        if (m.getMessageType() == Message.HomeView.LogOut) {
            currentView = UIFactory.Type.HomePage;
            // controller.username = null;
            // controller.password = null;
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
        try {
            String userId = controller.getCurrentUser()._id;
            String urlString = Controller.serverUrl + "/recipe?userId=" + userId;
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    
            conn.setRequestMethod("GET");
            conn.connect();
    
            int responseCode = conn.getResponseCode();
    
            if (responseCode == 200) {
                Scanner in = new Scanner(conn.getInputStream());
                String jsonString = "";
                while (in.hasNext())
                    jsonString += in.nextLine();
                in.close();
                recipes = Arrays.asList(Utils.unmarshalJson(jsonString, RecipeSchema[].class));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        controller.receiveMessageFromModel(
            new Message(Message.HomeModel.UpdateRecipeList,
                    Map.ofEntries(Map.entry("Recipes", recipes))));
    }

    public List<RecipeSchema> getRecipes() {
        return recipes;
    }

    @Override
    public Object getState() {
        return this;
    }
}

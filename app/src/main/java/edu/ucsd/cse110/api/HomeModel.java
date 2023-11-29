package edu.ucsd.cse110.api;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import java.util.Collections;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import edu.ucsd.cse110.api.Controller.UIType;
import edu.ucsd.cse110.client.Recipe;

public class HomeModel implements ModelInterface {
    private Controller controller;
    private List<Recipe> recipes;
    private UIType currentView;

    public HomeModel(Controller c) {
        this.controller = c;
        currentView = UIType.HomePage;
        updateRecipeList();
    }

    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.HomeView.CreateRecipeButton) {
            currentView = UIType.CreateRecipe;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.StartCreateRecipeView));
        }
        if (m.getMessageType() == Message.CreateRecipeModel.CloseCreateRecipeView) {
            currentView = UIType.HomePage;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.CloseCreateRecipeView));
        }
        if (m.getMessageType() == Message.CreateRecipeModel.StartRecipeDetailedView) {
            currentView = UIType.DetailedView;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.StartRecipeDetailedView));
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.CloseRecipeDetailedView) {
            currentView = UIType.HomePage;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.CloseRecipeDetailedView));
        }
        if (m.getMessageType() == Message.HomeView.UpdateRecipeList) {
            updateRecipeList();
        }
        if (m.getMessageType() == Message.HomeView.OpenRecipe) {
            currentView = UIType.DetailedView;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.StartRecipeDetailedView));
            Recipe openRecipe = (Recipe) m.getKey("Recipe");
            controller.receiveMessageFromModel(new Message(Message.HomeModel.SendTitleBody,
                    Map.ofEntries(Map.entry("Recipe", openRecipe))));
        }
    }

    public UIType getCurrentView() {
        return currentView;
    }

    private void updateRecipeList() {
        if (controller.getUseMongoDB()) {
            try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
                recipes = new ArrayList<>();
                MongoDatabase database = mongoClient.getDatabase("PantryPal");
                MongoCollection<Document> recipesCollection = database.getCollection("recipes");

                FindIterable<Document> iterable = recipesCollection.find();
                MongoCursor<Document> cursor = iterable.iterator();

                while (cursor.hasNext()) {
                    Document recipeDocument = cursor.next();
                    String name = recipeDocument.getString("name");
                    String description = recipeDocument.getString("description");
                    String mealType = recipeDocument.getString("mealType");
                    System.out.println("Received recipe \'" + name + "\' from MongoDB.");
                    recipes.add(new Recipe(name, description, mealType));
                }
                Collections.reverse(recipes);
                controller.receiveMessageFromModel(
                        new Message(Message.HomeModel.UpdateRecipeList,
                                Map.ofEntries(Map.entry("Recipes", recipes))));
            } catch (MongoException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Path path = Paths.get(Controller.storagePath + "csv");
                List<String> allLines = Files.readAllLines(path);
                recipes = new ArrayList<>();
                for (String line : allLines) {
                    // https://stackoverflow.com/a/15739042
                    String[] recipeInfo = line.split("\",\"");
                    recipeInfo[0] = recipeInfo[0].replace("\"", "");
                    recipeInfo[1] = recipeInfo[1].replace("{NEWLINE}", "\n");
                    recipeInfo[2] = recipeInfo[2].replace("\"", "");
                    recipes.add(new Recipe(recipeInfo[0], recipeInfo[1], recipeInfo[2]));
                }
                Collections.reverse(recipes);
                controller.receiveMessageFromModel(
                        new Message(Message.HomeModel.UpdateRecipeList,
                                Map.ofEntries(Map.entry("Recipes", recipes))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateRecipeListCSV() {
        try {
            Path path = Paths.get(Controller.storagePath + "csv");
            List<String> allLines = Files.readAllLines(path);
            recipes = new ArrayList<>();
            for (String line : allLines) {
                // https://stackoverflow.com/a/15739042
                String[] recipeInfo = line.split("\",\"");
                recipeInfo[0] = recipeInfo[0].replace("\"", "");
                recipeInfo[1] = recipeInfo[1].replace("{NEWLINE}", "\n");
                recipeInfo[2] = recipeInfo[2].replace("\"", "");
                recipes.add(new Recipe(recipeInfo[0], recipeInfo[1], recipeInfo[2]));
            }
            Collections.reverse(recipes);
            controller.receiveMessageFromModel(
                    new Message(Message.HomeModel.UpdateRecipeList,
                            Map.ofEntries(Map.entry("Recipes", recipes))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    @Override
    public Object getState() {
        return this;
    }
}

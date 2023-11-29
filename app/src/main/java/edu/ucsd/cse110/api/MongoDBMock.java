package edu.ucsd.cse110.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.ucsd.cse110.client.Recipe;

public class MongoDBMock implements MongoDBInterface{
    public static final String storagePath = "./src/main/java/edu/ucsd/cse110/api/assets/savedRecipes.";
    public MongoDBMock(){
        
    }

    @Override
    public List<Recipe> getRecipeList() {
        List<Recipe> recipes = new ArrayList<>();
        try {
            Path path = Paths.get(storagePath + "recipes.json");
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);
                String title = recipeObject.getString("title");
                String body = recipeObject.getString("body");
                String mealType = recipeObject.getString("mealType");
                recipes.add(new Recipe(title, body, mealType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    @Override
    public Recipe getRecipe(String recipeTitle) {
        try {
            Path path = Paths.get(storagePath + "recipes.json");
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);
                if (recipeObject.getString("title").equals(recipeTitle)) {
                    return new Recipe(recipeTitle, recipeObject.getString("body"),recipeObject.getString("mealType"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Recipe with title '" + recipeTitle + "' not found.");
        return null;
    }

    @Override
    public void saveRecipe(String recipeTitle, String recipeBody, String recipeMealType) {
         try {
            Path path = Paths.get(storagePath + "recipes.json");
            Files.createDirectories(path.getParent());

            JSONArray jsonArray;
            File file = path.toFile();

            if (file.exists()) {
                String content = new String(Files.readAllBytes(path));
                jsonArray = new JSONArray(content);
            } else {
                jsonArray = new JSONArray();
            }

            JSONObject newRecipe = new JSONObject();
            newRecipe.put("title", recipeTitle);
            newRecipe.put("body", recipeBody);
            newRecipe.put("mealType", recipeMealType);

            jsonArray.put(newRecipe);

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(jsonArray.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRecipe(String recipeTitle, String updatedRecipeBody, String updatedRecipeMealType) {
        try {
            Path path = Paths.get(storagePath + "recipes.json");
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);
                if (recipeObject.getString("title").equals(recipeTitle)) {
                    recipeObject.put("body", updatedRecipeBody);
                    recipeObject.put("mealType", updatedRecipeMealType);
                    break;
                }
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(jsonArray.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRecipe(String recipeTitle) {
        try {
            Path path = Paths.get(storagePath + "recipes.json");
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);
            JSONArray updatedJsonArray = new JSONArray();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);
                if (!recipeObject.getString("title").equals(recipeTitle)) {
                    updatedJsonArray.put(recipeObject);
                }
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(updatedJsonArray.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearDB() {
        try {
            Path path = Paths.get(storagePath + "recipes.json");
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

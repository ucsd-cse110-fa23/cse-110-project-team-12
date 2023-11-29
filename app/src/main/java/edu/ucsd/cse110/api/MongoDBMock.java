package edu.ucsd.cse110.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.ucsd.cse110.client.Recipe;

public class MongoDBMock implements MongoDBInterface {
    public static final String storagePath = "./src/main/java/edu/ucsd/cse110/api/assets/savedRecipes.";
    
    private String getUserId(String username, String password) {
        String userId = null;
        try {
            Path path = Paths.get(storagePath + "users.json");
            JSONArray users = new JSONArray();
            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                users = new JSONArray(content);
            }
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("username").equals(username) && user.getString("password").equals(password)) {
                    userId = user.getString("_id");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userId;
    }

    @Override
    public boolean isValidUser(String username, String password) {
        try {
            Path path = Paths.get(storagePath + "users.json");
            JSONArray users = new JSONArray();
            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                users = new JSONArray(content);
            }
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("username").equals(username) && user.getString("password").equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createUser(String username, String password) {
        try {
            Path path = Paths.get(storagePath + "users.json");
            JSONArray users = new JSONArray();
            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                users = new JSONArray(content);
            }

            // check if username already exists
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.getString("username").equals(username)) {
                    return false;
                }
            }

            String id = UUID.randomUUID().toString(); // mock object id

            JSONObject newUser = new JSONObject();
            newUser.put("_id", id);
            newUser.put("username", username);
            newUser.put("password", password);
            users.put(newUser);

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(users.toString());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Recipe> getRecipeList(String username, String password) {
        String userId = getUserId(username, password);

        List<Recipe> recipes = new ArrayList<>();
        try {
            Path path = Paths.get(storagePath + "recipes.json");
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);
                if(!recipeObject.getString("userId").equals(userId)) continue;
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
    public Recipe getRecipe(String recipeTitle, String username, String password) {
        String userId = getUserId(username, password);

        try {
            Path path = Paths.get(storagePath + "recipes.json");
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);
                if(!recipeObject.getString("userId").equals(userId)) continue;

                if (recipeObject.getString("title").equals(recipeTitle)) {
                    return new Recipe(recipeTitle, recipeObject.getString("body"),recipeObject.getString("mealType"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveRecipe(String recipeTitle, String recipeBody, String recipeMealType, String username, String password) {
        String userId = getUserId(username, password);

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
            String id = UUID.randomUUID().toString();
            newRecipe.put("_id", id);
            newRecipe.put("title", recipeTitle);
            newRecipe.put("body", recipeBody);
            newRecipe.put("mealType", recipeMealType);
            newRecipe.put("userId", userId);

            jsonArray.put(newRecipe);

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(jsonArray.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRecipe(String recipeTitle, String updatedRecipeBody, String updatedRecipeMealType, String username, String password) {
        String userId = getUserId(username, password);

        try {
            Path path = Paths.get(storagePath + "recipes.json");
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);
                if(!recipeObject.getString("userId").equals(userId)) continue;
                
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
    public void deleteRecipe(String recipeTitle, String username, String password) {
        String userId = getUserId(username, password);

        try {
            Path path = Paths.get(storagePath + "recipes.json");
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);
            JSONArray updatedJsonArray = new JSONArray();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);
                if (!(recipeObject.getString("userId").equals(userId) && recipeObject.getString("title").equals(recipeTitle))) {
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
            path = Paths.get(storagePath + "users.json");
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}

package edu.ucsd.cse110.server.services.mongodb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.schemas.UserSchema;

public class MongoDBMock implements MongoDBInterface {
    public static final String storagePath = "./src/main/java/edu/ucsd/cse110/server/services/mongodb/";

    @Override
    public UserSchema getUser(String username, String password) {
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
                    UserSchema userSchema = new UserSchema();
                    userSchema.username = username;
                    userSchema.password = password;
                    userSchema._id = user.getString("_id");
                    return userSchema;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserSchema createUser(String username, String password) {
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
                    return null;
                }
            }

            UserSchema us = new UserSchema();
            us.username = username;
            us.password = password;
            us._id = UUID.randomUUID().toString(); // mock id

            JSONObject newUser = new JSONObject();
            newUser.put("_id", us._id);
            newUser.put("username", us.username);
            newUser.put("password", us.password);
            users.put(newUser);

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(users.toString());
            }

            return us;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RecipeSchema> getRecipeList(String userId) {
        List<RecipeSchema> recipes = new ArrayList<>();
        try {
            Path path = Paths.get(storagePath + "recipes.json");
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);
                if (!recipeObject.getString("userId").equals(userId))
                    continue;
                RecipeSchema rs = new RecipeSchema();
                rs._id = recipeObject.getString("_id");
                rs.title = recipeObject.getString("title");
                rs.description = recipeObject.getString("description");
                rs.mealType = recipeObject.getString("mealType");
                rs.ingredients = recipeObject.getString("ingredients");
                rs.timeCreated = recipeObject.getString("timeCreated");
                rs.userId = recipeObject.getString("userId");
                recipes.add(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    @Override
    public RecipeSchema getRecipe(String recipeId) {
        RecipeSchema rs = null;
        try {
            Path path = Paths.get(storagePath + "recipes.json");
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);
                if (!recipeObject.getString("_id").equals(recipeId))
                    continue;

                rs = new RecipeSchema();
                rs._id = recipeObject.getString("_id");
                rs.title = recipeObject.getString("title");
                rs.description = recipeObject.getString("description");
                rs.mealType = recipeObject.getString("mealType");
                rs.ingredients = recipeObject.getString("ingredients");
                rs.timeCreated = recipeObject.getString("timeCreated");
                rs.userId = recipeObject.getString("userId");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public RecipeSchema saveRecipe(RecipeSchema recipe) {
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

            recipe._id = UUID.randomUUID().toString(); // mock id
            recipe.timeCreated = LocalDateTime.now().toString();

            JSONObject newRecipe = new JSONObject();
            newRecipe.put("_id", recipe._id);
            newRecipe.put("title", recipe.title);
            newRecipe.put("description", recipe.description);
            newRecipe.put("mealType", recipe.mealType);
            newRecipe.put("ingredients", recipe.ingredients);
            newRecipe.put("timeCreated", recipe.timeCreated);
            newRecipe.put("userId", recipe.userId);
            jsonArray.put(newRecipe);

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(jsonArray.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return recipe;
    }

    @Override
    public void updateRecipe(String recipeId, String newTitle, String newDescription, String newImageEncoding) {
        try {
            Path path = Paths.get(storagePath + "recipes.json");
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);
                if (recipeObject.getString("_id").equals(recipeId)) {
                    recipeObject.put("title", newTitle);
                    recipeObject.put("description", newDescription);
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
    public void deleteRecipe(String recipeId) {
        try {
            Path path = Paths.get(storagePath + "recipes.json");
            String content = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(content);
            JSONArray updatedJsonArray = new JSONArray();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);
                if (!(recipeObject.getString("_id").equals(recipeId))) {
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
    public void dropCollection(String collectionName) {
        if (collectionName.equals("users")) {
            try {
                Path path = Paths.get(storagePath + "users.json");
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (collectionName.equals("recipes")) {
            try {
                Path path = Paths.get(storagePath + "recipes.json");
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

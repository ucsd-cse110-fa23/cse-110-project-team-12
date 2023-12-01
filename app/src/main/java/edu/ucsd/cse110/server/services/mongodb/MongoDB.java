package edu.ucsd.cse110.server.services.mongodb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;

import org.bson.Document;
import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import edu.ucsd.cse110.client.Recipe;
import edu.ucsd.cse110.server.schemas.RecipeSchema;

public class MongoDB implements MongoDBInterface {
    private MongoDatabase database;

    public MongoDB(String uri) {
        try {
            MongoClient mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase("PantryPal");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // users
    private ObjectId getUserId(String username, String password) {
        ObjectId id = null;

        MongoCollection<Document> collection = database.getCollection("users");

        Document query = new Document()
                .append("username", username)
                .append("password", password);
        Document foundDocument = collection.find(query).first();

        if (foundDocument != null) {
            id = foundDocument.getObjectId("_id");
        }

        return id;
    }

    @Override
    public boolean isValidUser(String username, String password) {
        MongoCollection<Document> collection = database.getCollection("users");

        Document query = new Document()
                .append("username", username)
                .append("password", password);
        Document foundDocument = collection.find(query).first();

        if (foundDocument != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean createUser(String username, String password) {
        if (username.replace(" ", "").equals("") || password.replace(" ", "").equals(""))
            return false;
        
        MongoCollection<Document> users = database.getCollection("users");

        long count = users.countDocuments(Filters.eq("username", username));
        if (count > 0) {
            return false;
        }

        Document newUser = new Document("_id", new ObjectId())
                .append("username", username)
                .append("password", password);
        users.insertOne(newUser);

        return true;
    }

    // recipes
    @Override
    public List<Recipe> getRecipeList(String username, String password) {
        ObjectId userId = getUserId(username, password);

        List<Recipe> recipes = new ArrayList<>();
        MongoCollection<Document> recipesCollection = database.getCollection("recipes");

        Document query = new Document()
                .append("userId", userId);
        FindIterable<Document> iterable = recipesCollection.find(query);
        MongoCursor<Document> cursor = iterable.iterator();

        while (cursor.hasNext()) {
            Document recipeDocument = cursor.next();
            String name = recipeDocument.getString("name");
            String description = recipeDocument.getString("description");
            String mealType = recipeDocument.getString("mealType");
            recipes.add(new Recipe(name, description, mealType));
        }
        Collections.reverse(recipes);
        return recipes;
    }

    @Override
    public Recipe getRecipe(String recipeTitle, String username, String password) {
        ObjectId userId = getUserId(username, password);

        Recipe recipe = null;
        MongoCollection<Document> recipes = database.getCollection("recipes");

        Document query = new Document()
                .append("name", recipeTitle)
                .append("userId", userId);
        Document recipeDocument = recipes.find(query).first();

        if (recipeDocument != null) {
            String name = recipeDocument.getString("name");
            String description = recipeDocument.getString("description");
            String mealType = recipeDocument.getString("mealType");
            // You may also want to retrieve other fields, if available
            recipe = new Recipe(name, description, mealType);
        }
        return recipe;
    }

    @Override
    public void saveRecipe(RecipeSchema rs) {
        MongoCollection<Document> recipes = database.getCollection("recipes");

        Document recipe = new Document("_id", new ObjectId())
                .append("title", rs.title)
                .append("description", rs.description)
                .append("mealType", rs.mealType)
                .append("timeCreated", LocalDateTime.now().toString())
                .append("userId", rs.userId);

        recipes.insertOne(recipe);
    }

    @Override
    public void updateRecipe(String recipeTitle, String updatedRecipeBody, String updatedRecipeMealType,
            String username, String password) {
        ObjectId userId = getUserId(username, password);

        MongoCollection<Document> recipes = database.getCollection("recipes");

        Document query = new Document()
                .append("name", recipeTitle)
                .append("userId", userId);
        Document update = new Document("$set", new Document("description", updatedRecipeBody)
                .append("mealType", updatedRecipeMealType)
                .append("timestamp", new BSONTimestamp()));

        UpdateOptions options = new UpdateOptions().upsert(false);
        recipes.updateOne(query, update, options);

    }

    @Override
    public void deleteRecipe(String recipeTitle, String username, String password) {
        ObjectId userId = getUserId(username, password);

        MongoCollection<Document> recipes = database.getCollection("recipes");

        Document query = new Document()
                .append("name", recipeTitle)
                .append("userId", userId);
        recipes.deleteOne(query);

    }

    @Override
    public void clearDB() {
        throw new UnsupportedOperationException("Unimplemented method 'clearDB'");
    }
}

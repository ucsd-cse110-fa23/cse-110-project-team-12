package edu.ucsd.cse110.server.services.mongodb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.schemas.UserSchema;

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

    @Override
    public UserSchema getUser(String username, String password) {
        MongoCollection<Document> collection = database.getCollection("users");
        
        Document query = new Document()
            .append("username", username)
            .append("password", password);
        Document foundDocument = collection.find(query).first();
        
        if (foundDocument == null)
            return null;
        
        UserSchema user = new UserSchema();
        user.username = username;
        user.password = password;
        user._id = foundDocument.getObjectId("_id").toString();
        return user;
    }

    @Override
    public UserSchema createUser(String username, String password) {
        MongoCollection<Document> users = database.getCollection("users");

        long count = users.countDocuments(Filters.eq("username", username));
        if (count > 0) {
            return null;
        }

        Document newUser = new Document("_id", new ObjectId())
                .append("username", username)
                .append("password", password);
        users.insertOne(newUser);

        return getUser(username, password);
    }

    // recipes
    @Override
    public List<RecipeSchema> getRecipeList(String userId) {
        List<RecipeSchema> recipes = new ArrayList<>();
        MongoCollection<Document> recipesCollection = database.getCollection("recipes");

        Document query = new Document()
                .append("userId", new ObjectId(userId));
        FindIterable<Document> iterable = recipesCollection.find(query);
        MongoCursor<Document> cursor = iterable.iterator();

        while (cursor.hasNext()) {
            Document recipeDocument = cursor.next();
            RecipeSchema recipe = new RecipeSchema();
            recipe._id = recipeDocument.getObjectId("_id").toString();
            recipe.userId = recipeDocument.getObjectId("userId").toString();
            recipe.title = recipeDocument.getString("title");
            recipe.description = recipeDocument.getString("description");
            recipe.mealType = recipeDocument.getString("mealType");
            recipe.ingredients = recipeDocument.getString("ingredients");
            recipe.timeCreated = recipeDocument.getString("timeCreated");
            recipes.add(recipe);
        }
        Collections.reverse(recipes);
        return recipes;
    }

    @Override
    public RecipeSchema getRecipe(String recipeId) {
        RecipeSchema recipe = null;
        try {

            if (ObjectId.isValid(recipeId) == false)
                return recipe;

            MongoCollection<Document> recipes = database.getCollection("recipes");

            Document query = new Document()
                    .append("_id", new ObjectId(recipeId));
            Document recipeDocument = recipes.find(query).first();

            if (recipeDocument != null) {
                recipe = new RecipeSchema();
                recipe._id = recipeId;
                recipe.userId = recipeDocument.getObjectId("userId").toString();
                recipe.title = recipeDocument.getString("title");
                recipe.description = recipeDocument.getString("description");
                recipe.mealType = recipeDocument.getString("mealType");
                recipe.ingredients = recipeDocument.getString("ingredients");
                recipe.timeCreated = recipeDocument.getString("timeCreated");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipe;
    }

    @Override
    public RecipeSchema saveRecipe(RecipeSchema rs) {
        MongoCollection<Document> recipes = database.getCollection("recipes");

        ObjectId objId = new ObjectId();
        String timeCreated = LocalDateTime.now().toString();
        Document recipe = new Document("_id", objId)
                .append("title", rs.title)
                .append("description", rs.description)
                .append("mealType", rs.mealType)
                .append("ingredients", rs.ingredients)
                .append("timeCreated", LocalDateTime.now().toString())
                .append("userId", new ObjectId(rs.userId));

        recipes.insertOne(recipe);

        rs._id = objId.toString();
        rs.timeCreated = timeCreated;
        return rs;
    }

    @Override
    public void updateRecipe(String recipeId, String newTitle, String newDescription) {
        try {
            MongoCollection<Document> recipes = database.getCollection("recipes");

            Document query = new Document()
                    .append("_id", new ObjectId(recipeId));
            Document update = new Document("$set", new Document("description", newDescription)
                    .append("title", newTitle)
                    .append("timeCreated", LocalDateTime.now().toString()));

            UpdateOptions options = new UpdateOptions().upsert(false);
            recipes.updateOne(query, update, options);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteRecipe(String recipeId) {
        MongoCollection<Document> recipes = database.getCollection("recipes");

        Document query = new Document()
                .append("_id", new ObjectId(recipeId));
        recipes.deleteOne(query);
    }

    @Override
    public void dropCollection(String collectionName) {
        database.getCollection(collectionName).drop();
    }
}

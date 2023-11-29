package edu.ucsd.cse110.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
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

public class MongoDB implements MongoDBInterface{
    // users
    private ObjectId getUserId(String username, String password) {
        ObjectId id = null;
    
        try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
            MongoDatabase database = mongoClient.getDatabase("PantryPal");
            MongoCollection<Document> collection = database.getCollection("users");
    
            Document query = new Document()
                    .append("username", username)
                    .append("password", password);
            Document foundDocument = collection.find(query).first();
    
            if (foundDocument != null) {
                id = foundDocument.getObjectId("_id");
            } else {
                System.out.println("No matching user found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return id;
    }
    @Override
    public boolean isValidUser(String username, String password){
        try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
            MongoDatabase database = mongoClient.getDatabase("PantryPal");
            MongoCollection<Document> collection = database.getCollection("users");

            Document query = new Document()
                    .append("username", username)
                    .append("password", password);
            Document foundDocument = collection.find(query).first();

            if (foundDocument != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createUser(String username, String password){
        try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
            MongoDatabase database = mongoClient.getDatabase("PantryPal");
            MongoCollection<Document> users = database.getCollection("users");

            long count = users.countDocuments(Filters.eq("username", username));
            if (count > 0) {
                System.out.println("Username already exists.");
                return false;
            }

            Document newUser = new Document("_id", new ObjectId())
                    .append("username", username)
                    .append("password", password);
            users.insertOne(newUser);

            System.out.println("User created successfully.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // recipes
    @Override
    public List<Recipe> getRecipeList(String username, String password) {
        ObjectId userId = getUserId(username, password);

        List<Recipe> recipes = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
            MongoDatabase database = mongoClient.getDatabase("PantryPal");
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
                System.out.println("Received recipe \'" + name + "\' from MongoDB.");
                recipes.add(new Recipe(name, description, mealType));
            }
            Collections.reverse(recipes);
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return recipes;
    }
    @Override
    public Recipe getRecipe(String recipeTitle, String username, String password) {
        ObjectId userId = getUserId(username, password);

        Recipe recipe = null;
        try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
            MongoDatabase database = mongoClient.getDatabase("PantryPal");
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
            } else {
                System.out.println("Recipe with title '" + recipeTitle + "' not found.");
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return recipe;
    }
    
    @Override
    public void saveRecipe(String recipeTitle, String recipeBody, String recipeMealType, String username, String password) {
        ObjectId userId = getUserId(username, password);

        try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
            
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("PantryPal");
            MongoCollection<Document> recipes = sampleTrainingDB.getCollection("recipes");
            
            Document recipe = new Document("_id", new ObjectId());
            recipe.append("name", recipeTitle)
            .append("description", recipeBody)
            .append("mealType", recipeMealType)
            .append("timestamp", new BSONTimestamp())
            .append("userId", userId);
            
            recipes.insertOne(recipe);
            System.out.println("Recipe with title '" + recipeTitle + "' added to MongoDB.");
        }        
    }

    @Override
    public void updateRecipe(String recipeTitle, String updatedRecipeBody, String updatedRecipeMealType, String username, String password) {
        ObjectId userId = getUserId(username, password);

        try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
            
            MongoDatabase database = mongoClient.getDatabase("PantryPal");
            MongoCollection<Document> recipes = database.getCollection("recipes");
            
            Document query = new Document()
                    .append("name", recipeTitle)
                    .append("userId", userId);
            Document update = new Document("$set", new Document("description", updatedRecipeBody)
            .append("mealType", updatedRecipeMealType)
            .append("timestamp", new BSONTimestamp()));
            
            
            UpdateOptions options = new UpdateOptions().upsert(false);
            UpdateResult updateResult = recipes.updateOne(query, update, options);
            
            if (updateResult.getModifiedCount() == 0) {
                System.out.println("Recipe with title '" + recipeTitle + "' not found.");
            } else {
                System.out.println("Recipe with title '" + recipeTitle + "' updated successfully.");
            }
            
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRecipe(String recipeTitle, String username, String password) {
        ObjectId userId = getUserId(username, password);

        try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
            
            MongoDatabase database = mongoClient.getDatabase("PantryPal");
            MongoCollection<Document> recipes = database.getCollection("recipes");
            
            Document query = new Document()
                    .append("name", recipeTitle)
                    .append("userId", userId);
            DeleteResult deleteResult = recipes.deleteOne(query);
            
            if (deleteResult.getDeletedCount() == 0) {
                System.out.println("Recipe with title '" + recipeTitle + "' not found.");
            } else {
                System.out.println("Recipe with title '" + recipeTitle + "' deleted successfully.");
            }
            
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearDB() {
        throw new UnsupportedOperationException("Unimplemented method 'clearDB'");
    }
    
}

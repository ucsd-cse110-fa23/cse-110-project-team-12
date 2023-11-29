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
    @Override
    public List<Recipe> getRecipeList() {
        List<Recipe> recipes = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
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
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return recipes;
    }
    @Override
    public Recipe getRecipe(String recipeTitle) {
        Recipe recipe = null;
    
        try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
            MongoDatabase database = mongoClient.getDatabase("PantryPal");
            MongoCollection<Document> recipes = database.getCollection("recipes");
    
            Bson filter = Filters.eq("name", recipeTitle);
            Document recipeDocument = recipes.find(filter).first();
    
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
    public void saveRecipe(String recipeTitle, String recipeBody, String recipeMealType) {
        try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
            
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("PantryPal");
            MongoCollection<Document> recipes = sampleTrainingDB.getCollection("recipes");
            
            Document recipe = new Document("_id", new ObjectId());
            recipe.append("name", recipeTitle)
            .append("description", recipeBody)
            .append("mealType", recipeMealType).append("timestamp", new BSONTimestamp());
            
            recipes.insertOne(recipe);
            System.out.println("Recipe with title '" + recipeTitle + "' added to MongoDB.");
        }        
    }
    
    public void updateRecipe(String recipeTitle, String updatedRecipeBody, String updatedRecipeMealType) {
        try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
            
            MongoDatabase database = mongoClient.getDatabase("PantryPal");
            MongoCollection<Document> recipes = database.getCollection("recipes");
            
            Bson filter = Filters.eq("name", recipeTitle);
            Document update = new Document("$set", new Document("description", updatedRecipeBody)
            .append("mealType", updatedRecipeMealType)
            .append("timestamp", new BSONTimestamp()));
            
            UpdateOptions options = new UpdateOptions().upsert(false);
            UpdateResult updateResult = recipes.updateOne(filter, update, options);
            
            if (updateResult.getModifiedCount() == 0) {
                System.out.println("Recipe with title '" + recipeTitle + "' not found.");
            } else {
                System.out.println("Recipe with title '" + recipeTitle + "' updated successfully.");
            }
            
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteRecipe(String recipeTitle) {
        try (MongoClient mongoClient = MongoClients.create(Controller.mongoURI)) {
            
            MongoDatabase database = mongoClient.getDatabase("PantryPal");
            MongoCollection<Document> recipes = database.getCollection("recipes");
            
            Bson filter = Filters.eq("name", recipeTitle);
            DeleteResult deleteResult = recipes.deleteOne(filter);
            
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

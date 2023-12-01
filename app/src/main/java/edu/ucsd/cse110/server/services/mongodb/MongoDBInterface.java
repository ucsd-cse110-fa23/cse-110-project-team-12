package edu.ucsd.cse110.server.services.mongodb;

import java.util.List;

import edu.ucsd.cse110.client.Recipe;
import edu.ucsd.cse110.server.schemas.RecipeSchema;

public interface MongoDBInterface {
    //users schema
    public boolean isValidUser(String username, String password);
    public boolean createUser(String username, String password); //return false is username already exists

    //recipes schema
    public List<RecipeSchema> getRecipeList(String userId);
    public RecipeSchema getRecipe(String recipeId);
    public void saveRecipe(RecipeSchema recipe);
    public void updateRecipe(String recipeTitle, String updatedRecipeBody, String updatedRecipeMealType, String username, String password);
    public void deleteRecipe(String recipeId);

    // testing use
    public void dropCollection(String collectionName);
}

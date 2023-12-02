package edu.ucsd.cse110.server.services.mongodb;

import java.util.List;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.schemas.UserSchema;

public interface MongoDBInterface {
    //users schema
    public UserSchema getUser(String username, String password);
    public UserSchema createUser(String username, String password); //return false is username already exists

    //recipes schema
    public List<RecipeSchema> getRecipeList(String userId);
    public RecipeSchema getRecipe(String recipeId);
    public void saveRecipe(RecipeSchema recipe);
    public void updateRecipe(String userId, String newTitle, String newDescription);
    public void deleteRecipe(String recipeId);

    // testing use
    public void dropCollection(String collectionName);
}

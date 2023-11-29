package edu.ucsd.cse110.api;

import java.util.List;

import edu.ucsd.cse110.client.Recipe;

public interface MongoDBInterface {
    //users schema
    public boolean isValidUser(String username, String password);
    public boolean createUser(String username, String password); //return false is username already exists

    //recipes schema
    public List<Recipe> getRecipeList(String username, String password);
    public Recipe getRecipe(String recipeTitle, String username, String password);
    public void saveRecipe(String recipeTitle, String recipeBody, String recipeMealType, String username, String password);
    public void updateRecipe(String recipeTitle, String updatedRecipeBody, String updatedRecipeMealType, String username, String password);
    public void deleteRecipe(String recipeTitle, String username, String password);

    // testing use
    public void clearDB();
}
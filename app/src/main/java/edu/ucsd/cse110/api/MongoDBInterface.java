package edu.ucsd.cse110.api;

import java.util.List;
import edu.ucsd.cse110.client.Recipe;

public interface MongoDBInterface {
    public List<Recipe> getRecipeList();
    public Recipe getRecipe(String recipeTitle);
    public void saveRecipe(String recipeTitle, String recipeBody, String recipeMealType);
    public void updateRecipe(String recipeTitle, String updatedRecipeBody, String updatedRecipeMealType);
    public void deleteRecipe(String recipeTitle);

    // testing use
    public void clearDB();
}

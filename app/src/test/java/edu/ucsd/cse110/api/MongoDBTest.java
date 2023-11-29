package edu.ucsd.cse110.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import edu.ucsd.cse110.client.Recipe;

public class MongoDBTest {
    @Test
    public void testMongoDBMockConstructor() {
        MongoDBInterface mongoDB = new MongoDBMock();
        assertNotNull(mongoDB);
    }
    @Test
    public void testMongoDBAddRecipe() {
        MongoDBInterface mongoDB = new MongoDBMock();
        mongoDB.clearDB();
        String recipeTitle = "Apple Pie";
        String recipeBody = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        String recipeMealType = "Lunch";
        mongoDB.saveRecipe(recipeTitle, recipeBody, recipeMealType);

        List<Recipe> recipes = mongoDB.getRecipeList();
        assertEquals(1, recipes.size());
    }
    @Test
    public void testMongoDBGetRecipe() {
        MongoDBInterface mongoDB = new MongoDBMock();
        mongoDB.clearDB();
        String recipeTitle = "Apple Pie";
        String recipeBody = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        String recipeMealType = "Lunch";
        mongoDB.saveRecipe(recipeTitle, recipeBody, recipeMealType);
        
        Recipe recipe = mongoDB.getRecipe(recipeTitle);
        assertEquals(recipeTitle, recipe.getName());
        assertEquals(recipeBody, recipe.getInformation());
        assertEquals(recipeMealType, recipe.getMealType());
    }

    @Test
    public void testMongoDBUpdateRecipe() {
        MongoDBInterface mongoDB = new MongoDBMock();
        mongoDB.clearDB();
        String recipeTitle = "Apple Pie";
        String recipeBody = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        String recipeMealType = "Lunch";
        mongoDB.saveRecipe(recipeTitle, recipeBody, recipeMealType);

        String updatedRecipeBody = "1. Buy Apple Pie. \n 2. You have an Apple Pie.";
        String updatedRecipeMealType =  "Dinner";
        mongoDB.updateRecipe(recipeTitle, updatedRecipeBody, updatedRecipeMealType);
        
        Recipe recipe = mongoDB.getRecipe(recipeTitle);
        assertEquals(recipeTitle, recipe.getName());
        assertEquals(updatedRecipeBody, recipe.getInformation());
        assertEquals(updatedRecipeMealType, recipe.getMealType());
    }

    @Test
    public void testMongoDBDeleteRecipe() {
        MongoDBInterface mongoDB = new MongoDBMock();
        mongoDB.clearDB();
        String recipeTitle = "Apple Pie";
        String recipeBody = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        String recipeMealType = "Lunch";
        mongoDB.saveRecipe(recipeTitle, recipeBody, recipeMealType);

        mongoDB.deleteRecipe("Apple Pie");
        List<Recipe> recipes = mongoDB.getRecipeList();
        assertEquals(0, recipes.size());
    }

    @Test
    public void testMongoDBGetRecipeList() {
        MongoDBInterface mongoDB = new MongoDBMock();
        mongoDB.clearDB();
        String recipeTitle = "Apple Pie";
        String recipeBody = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        String recipeMealType = "Lunch";
        mongoDB.saveRecipe(recipeTitle, recipeBody, recipeMealType);

        String recipeTitle2 = "Banana Pie";
        String recipeBody2 = "1. Add Banana. \n 2. Make it to a Pie shape. \n 3. You have an Banana Pie.";
        String recipeMealType2 = "Dinner";
        mongoDB.saveRecipe(recipeTitle2, recipeBody2, recipeMealType2);

        List<Recipe> recipes = mongoDB.getRecipeList();
        assertEquals(2, recipes.size());

        assertEquals(recipeTitle, recipes.get(0).getName());
        assertEquals(recipeBody, recipes.get(0).getInformation());
        assertEquals(recipeMealType, recipes.get(0).getMealType());

        assertEquals(recipeTitle2, recipes.get(1).getName());
        assertEquals(recipeBody2, recipes.get(1).getInformation());
        assertEquals(recipeMealType2, recipes.get(1).getMealType());
    }
}

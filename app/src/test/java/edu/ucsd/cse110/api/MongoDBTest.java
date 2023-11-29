package edu.ucsd.cse110.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
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
    public void testMongoDBCreateUseranme() {
        MongoDBInterface mongoDB = new MongoDBMock();
        mongoDB.clearDB();
        String username = "test", password = "test";
        assertTrue(mongoDB.createUser(username, password));
    }
    @Test
    public void testMongoDBNoDuplicateUsername() {
        MongoDBInterface mongoDB = new MongoDBMock();
        mongoDB.clearDB();
        String username = "test", password = "test";
        assertTrue(mongoDB.createUser(username, password));
        assertFalse(mongoDB.createUser(username, password));
    }
    
    @Test
    public void testMongoDBisValidUser() {
        MongoDBInterface mongoDB = new MongoDBMock();
        mongoDB.clearDB();
        String username = "test", password = "test";
        mongoDB.createUser(username, password);
        assertTrue(mongoDB.isValidUser(username, password));
        assertFalse(mongoDB.isValidUser("hello", "world"));
    }

    @Test
    public void testMongoDBAddRecipe() {
        MongoDBInterface mongoDB = new MongoDBMock();
        mongoDB.clearDB();
        String username = "test", password = "test";
        assertTrue(mongoDB.createUser(username, password));

        String recipeTitle = "Apple Pie";
        String recipeBody = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        String recipeMealType = "Lunch";
        mongoDB.saveRecipe(recipeTitle, recipeBody, recipeMealType, username, password);

        List<Recipe> recipes = mongoDB.getRecipeList(username, password);
        assertEquals(1, recipes.size());
    }
    @Test
    public void testMongoDBGetRecipe() {
        MongoDBInterface mongoDB = new MongoDBMock();
        mongoDB.clearDB();
        String username = "test", password = "test";
        assertTrue(mongoDB.createUser(username, password));

        String recipeTitle = "Apple Pie";
        String recipeBody = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        String recipeMealType = "Lunch";
        mongoDB.saveRecipe(recipeTitle, recipeBody, recipeMealType, username, password);
        
        Recipe recipe = mongoDB.getRecipe(recipeTitle, username, password);
        assertEquals(recipeTitle, recipe.getName());
        assertEquals(recipeBody, recipe.getInformation());
        assertEquals(recipeMealType, recipe.getMealType());
    }

    @Test
    public void testMongoDBUpdateRecipe() {
        MongoDBInterface mongoDB = new MongoDBMock();
        mongoDB.clearDB();
        String username = "test", password = "test";
        assertTrue(mongoDB.createUser(username, password));
        
        String recipeTitle = "Apple Pie";
        String recipeBody = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        String recipeMealType = "Lunch";
        mongoDB.saveRecipe(recipeTitle, recipeBody, recipeMealType, username, password);

        String updatedRecipeBody = "1. Buy Apple Pie. \n 2. You have an Apple Pie.";
        String updatedRecipeMealType =  "Dinner";
        mongoDB.updateRecipe(recipeTitle, updatedRecipeBody, updatedRecipeMealType, username, password);
        
        Recipe recipe = mongoDB.getRecipe(recipeTitle, username, password);
        assertEquals(recipeTitle, recipe.getName());
        assertEquals(updatedRecipeBody, recipe.getInformation());
        assertEquals(updatedRecipeMealType, recipe.getMealType());
    }

    @Test
    public void testMongoDBDeleteRecipe() {
        MongoDBInterface mongoDB = new MongoDBMock();
        mongoDB.clearDB();
        String username = "test", password = "test";
        assertTrue(mongoDB.createUser(username, password));

        String recipeTitle = "Apple Pie";
        String recipeBody = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        String recipeMealType = "Lunch";
        mongoDB.saveRecipe(recipeTitle, recipeBody, recipeMealType, username, password);

        mongoDB.deleteRecipe("Apple Pie", username, password);
        List<Recipe> recipes = mongoDB.getRecipeList(username, password);
        assertEquals(0, recipes.size());
    }

    @Test
    public void testMongoDBGetRecipeList() {
        MongoDBInterface mongoDB = new MongoDBMock();
        mongoDB.clearDB();
        String username = "test", password = "test";
        assertTrue(mongoDB.createUser(username, password));

        String recipeTitle = "Apple Pie";
        String recipeBody = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        String recipeMealType = "Lunch";
        mongoDB.saveRecipe(recipeTitle, recipeBody, recipeMealType, username, password);

        String recipeTitle2 = "Banana Pie";
        String recipeBody2 = "1. Add Banana. \n 2. Make it to a Pie shape. \n 3. You have an Banana Pie.";
        String recipeMealType2 = "Dinner";
        mongoDB.saveRecipe(recipeTitle2, recipeBody2, recipeMealType2, username, password);

        List<Recipe> recipes = mongoDB.getRecipeList(username, password);
        assertEquals(2, recipes.size());

        assertEquals(recipeTitle, recipes.get(0).getName());
        assertEquals(recipeBody, recipes.get(0).getInformation());
        assertEquals(recipeMealType, recipes.get(0).getMealType());

        assertEquals(recipeTitle2, recipes.get(1).getName());
        assertEquals(recipeBody2, recipes.get(1).getInformation());
        assertEquals(recipeMealType2, recipes.get(1).getMealType());
    }
}

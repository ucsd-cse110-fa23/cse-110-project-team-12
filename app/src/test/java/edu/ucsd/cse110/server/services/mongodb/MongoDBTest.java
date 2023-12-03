package edu.ucsd.cse110.server.services.mongodb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.schemas.UserSchema;


public class MongoDBTest {
    MongoDBInterface mongoDB;
    @Before
    public void init() {
        mongoDB = new MongoDBMock();
        mongoDB.dropCollection("users");
        mongoDB.dropCollection("recipes");
    }
    @Test
    public void testMongoDBMockConstructor() {
        assertNotNull(mongoDB);
    }
    @Test
    public void testMongoDBCreateUseranme() {
        String username = "test", password = "test";
        assertNotNull(mongoDB.createUser(username, password));
    }
    @Test
    public void testMongoDBNoDuplicateUsername() {
        String username = "test", password = "test";
        assertNotNull(mongoDB.createUser(username, password));
        assertNull(mongoDB.createUser(username, password));
    }
    
    @Test
    public void testMongoDBgetUser() {
        String username = "test", password = "test";
        UserSchema gt = mongoDB.createUser(username, password);
        UserSchema get = mongoDB.getUser(username, password);
        assertNotNull(get);
        assertTrue(gt._id.equals(get._id));
        assertTrue(gt.username.equals(get.username));
        assertTrue(gt.password.equals(get.password));
        assertNull(mongoDB.getUser("hello", "world"));
    }

    @Test
    public void testMongoDBSaveRecipe() {
        String username = "test", password = "test";
        UserSchema us = mongoDB.createUser(username, password);

        RecipeSchema rs = new RecipeSchema();
        rs.title = "Apple Pie";
        rs.description = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        rs.ingredients = "Apple, Sugar, Flour";
        rs.mealType = "Lunch";
        rs.userId = us._id;
        RecipeSchema rs_ret = mongoDB.saveRecipe(rs);
        assertTrue(rs.title.equals(rs_ret.title));
        assertTrue(rs.description.equals(rs_ret.description));
        assertTrue(rs.ingredients.equals(rs_ret.ingredients));
        assertTrue(rs.mealType.equals(rs_ret.mealType));
        assertTrue(rs.userId.equals(rs_ret.userId));
        assertNotNull(rs_ret._id);
        assertNotNull(rs_ret.timeCreated);
    }

    @Test
    public void testMongoDBGetRecipe() {
        String username = "test", password = "test";
        UserSchema us = mongoDB.createUser(username, password);

        RecipeSchema rs = new RecipeSchema();
        rs.title = "Apple Pie";
        rs.description = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        rs.ingredients = "Apple, Sugar, Flour";
        rs.mealType = "Lunch";
        rs.userId = us._id;
        rs = mongoDB.saveRecipe(rs);
        
        RecipeSchema rs_get = mongoDB.getRecipe(rs._id);
        assertTrue(rs._id.equals(rs_get._id));
        assertTrue(rs.title.equals(rs_get.title));
        assertTrue(rs.description.equals(rs_get.description));
        assertTrue(rs.ingredients.equals(rs_get.ingredients));
        assertTrue(rs.mealType.equals(rs_get.mealType));
        assertTrue(rs.userId.equals(rs_get.userId));
        assertTrue(rs.timeCreated.equals(rs_get.timeCreated));
    }

    @Test
    public void testMongoDBUpdateRecipe() {
        String username = "test", password = "test";
        UserSchema us = mongoDB.createUser(username, password);

        RecipeSchema rs = new RecipeSchema();
        rs.title = "Apple Pie";
        rs.description = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        rs.ingredients = "Apple, Sugar, Flour";
        rs.mealType = "Lunch";
        rs.userId = us._id;
        rs = mongoDB.saveRecipe(rs);

        String newTitle = "Buy Apple Pie";
        String newDescription = "1. Buy Apple Pie. \n 2. You have an Apple Pie.";
        mongoDB.updateRecipe(rs._id, newTitle, newDescription);
        
        RecipeSchema rs_get = mongoDB.getRecipe(rs._id);
        assertTrue(rs._id.equals(rs_get._id));
        assertTrue(newTitle.equals(rs_get.title));
        assertTrue(newDescription.equals(rs_get.description));
        assertTrue(rs.ingredients.equals(rs_get.ingredients));
        assertTrue(rs.mealType.equals(rs_get.mealType));
        assertTrue(rs.userId.equals(rs_get.userId));
        assertTrue(rs.timeCreated.equals(rs_get.timeCreated));
    }

    @Test
    public void testMongoDBDeleteRecipe() {
        String username = "test", password = "test";
        UserSchema us = mongoDB.createUser(username, password);

        RecipeSchema rs = new RecipeSchema();
        rs.title = "Apple Pie";
        rs.description = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        rs.ingredients = "Apple, Sugar, Flour";
        rs.mealType = "Lunch";
        rs.userId = us._id;
        rs = mongoDB.saveRecipe(rs);

        mongoDB.deleteRecipe(rs._id);
        assertNull(mongoDB.getRecipe(rs._id));
    }

    @Test
    public void testMongoDBGetRecipeList() {
        String username = "test", password = "test";
        UserSchema us = mongoDB.createUser(username, password);

        List<RecipeSchema> recipes = mongoDB.getRecipeList(us._id);
        assertNotNull(recipes);
        assertEquals(0, recipes.size());

        RecipeSchema rs = new RecipeSchema();
        rs.title = "Apple Pie";
        rs.description = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        rs.ingredients = "Apple, Sugar, Flour";
        rs.mealType = "Lunch";
        rs.userId = us._id;
        rs = mongoDB.saveRecipe(rs);

        recipes = mongoDB.getRecipeList(us._id);
        assertEquals(1, recipes.size());

        RecipeSchema rs2 = new RecipeSchema();
        rs2.title = "Banana Pie";
        rs2.description = "1. Add Banana. \n 2. Make it to a Pie shape. \n 3. You have an Banana Pie.";
        rs2.ingredients = "Banana, Sugar, Flour";
        rs2.mealType = "Dinner";
        rs2.userId = us._id;
        rs2 = mongoDB.saveRecipe(rs2);

        recipes = mongoDB.getRecipeList(us._id);
        assertEquals(2, recipes.size());

        assertTrue(rs._id.equals(recipes.get(0)._id));
        assertTrue(rs.title.equals(recipes.get(0).title));
        assertTrue(rs.description.equals(recipes.get(0).description));
        assertTrue(rs.mealType.equals(recipes.get(0).mealType));
        assertTrue(rs.timeCreated.equals(recipes.get(0).timeCreated));
        assertTrue(rs.userId.equals(recipes.get(0).userId));

        assertTrue(rs2._id.equals(recipes.get(1)._id));
        assertTrue(rs2.title.equals(recipes.get(1).title));
        assertTrue(rs2.description.equals(recipes.get(1).description));
        assertTrue(rs2.mealType.equals(recipes.get(1).mealType));
        assertTrue(rs2.timeCreated.equals(recipes.get(1).timeCreated));
        assertTrue(rs2.userId.equals(recipes.get(1).userId));
    }

    @Test
    public void testMongoDBGetRecipeListDifferentUser() {
        String username = "test", password = "test";
        UserSchema us = mongoDB.createUser(username, password);

        List<RecipeSchema> recipes = mongoDB.getRecipeList(us._id);
        assertNotNull(recipes);
        assertEquals(0, recipes.size());

        RecipeSchema rs = new RecipeSchema();
        rs.title = "Apple Pie";
        rs.description = "1. Add Apple. \n 2. Make it to a Pie shape. \n 3. You have an Apple Pie.";
        rs.ingredients = "Apple, Sugar, Flour";
        rs.mealType = "Lunch";
        rs.userId = us._id;
        rs = mongoDB.saveRecipe(rs);

        recipes = mongoDB.getRecipeList(us._id);
        assertEquals(1, recipes.size());

        String username2 = "test2", password2 = "test2";
        UserSchema us2 = mongoDB.createUser(username2, password2);

        List<RecipeSchema> recipes2 = mongoDB.getRecipeList(us2._id);
        assertEquals(0, recipes2.size());

        RecipeSchema rs2 = new RecipeSchema();
        rs2.title = "Banana Pie";
        rs2.description = "1. Add Banana. \n 2. Make it to a Pie shape. \n 3. You have an Banana Pie.";
        rs2.ingredients = "Banana, Sugar, Flour";
        rs2.mealType = "Dinner";
        rs2.userId = us2._id;
        rs2 = mongoDB.saveRecipe(rs2);

        recipes = mongoDB.getRecipeList(us._id);
        assertEquals(1, recipes.size());

        recipes2 = mongoDB.getRecipeList(us2._id);
        assertEquals(1, recipes2.size());

        assertTrue(rs._id.equals(recipes.get(0)._id));
        assertTrue(rs.title.equals(recipes.get(0).title));
        assertTrue(rs.description.equals(recipes.get(0).description));
        assertTrue(rs.mealType.equals(recipes.get(0).mealType));
        assertTrue(rs.timeCreated.equals(recipes.get(0).timeCreated));
        assertTrue(rs.userId.equals(recipes.get(0).userId));

        assertTrue(rs2._id.equals(recipes2.get(0)._id));
        assertTrue(rs2.title.equals(recipes2.get(0).title));
        assertTrue(rs2.description.equals(recipes2.get(0).description));
        assertTrue(rs2.mealType.equals(recipes2.get(0).mealType));
        assertTrue(rs2.timeCreated.equals(recipes2.get(0).timeCreated));
        assertTrue(rs2.userId.equals(recipes2.get(0).userId));
    }
}

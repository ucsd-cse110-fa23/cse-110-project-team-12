package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.HomeModel;
import edu.ucsd.cse110.api.HomeModel.FilterOption;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.ModelFactory;
import edu.ucsd.cse110.api.VoicePromptMock;
import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.schemas.UserSchema;
import edu.ucsd.cse110.server.services.mongodb.MongoDBInterface;
import edu.ucsd.cse110.server.services.mongodb.MongoDBMock;

public class MS2US8Test {
    
    Controller controller;
    MongoDBInterface mongo;

    @Before
    public void init() {
        mongo = new MongoDBMock();
        mongo.dropCollection("users");
        mongo.dropCollection("recipes");
    }

    /*
     * Given I am in the recipe list
     * And There are 3 “Breakfast” recipes
     * And There are 2 “Dinner” recipes
     * When I click on the “Breakfast” radio-button in the “Filter” box
     * Then I exclusively see the 3 recipes in the recipe list that are of the meal type “Breakfast”
     */
    @Test
    public void testUS8BDD1() {

        controller = new Controller(false, new VoicePromptMock(new ArrayList<>()));

        UserSchema user = mongo.createUser("test", "test");
        List<RecipeSchema> recipeList;

        RecipeSchema recipe1 = new RecipeSchema();
        recipe1.userId = user._id;
        recipe1.title = "Scrambled Eggs";
        recipe1.description = "delicious";
        recipe1.mealType = "Breakfast";
        recipe1.ingredients = "Egg";
        mongo.saveRecipe(recipe1);

        RecipeSchema recipe2 = new RecipeSchema();
        recipe2.userId = user._id;
        recipe2.title = "Cold Pizza";
        recipe2.description = "Pizza";
        recipe2.mealType = "Dinner";
        recipe2.ingredients = "Cheese, Dough";
        mongo.saveRecipe(recipe2);

        RecipeSchema recipe3 = new RecipeSchema();
        recipe3.userId = user._id;
        recipe3.title = "Pancakes";
        recipe3.description = "warm pancakes";
        recipe3.mealType = "Breakfast";
        recipe3.ingredients = "Flour";
        mongo.saveRecipe(recipe3);

        RecipeSchema recipe4 = new RecipeSchema();
        recipe4.userId = user._id;
        recipe4.title = "Meatloaf";
        recipe4.description = "log of meat";
        recipe4.mealType = "Dinner";
        recipe4.ingredients = "Meat";
        mongo.saveRecipe(recipe4);

        RecipeSchema recipe5 = new RecipeSchema();
        recipe5.userId = user._id;
        recipe5.title = "Hash Browns";
        recipe5.description = "crispy";
        recipe5.mealType = "Breakfast";
        recipe5.ingredients = "Potatoes";
        mongo.saveRecipe(recipe5);

        controller.receiveMessageFromModel(new Message(Message.LogInModel.SetUser, "User", user));
        controller.receiveMessageFromModel(new Message(Message.LogInModel.StartHomeView));

        HomeModel hm = (HomeModel)controller.getState(ModelFactory.Type.HomePage);

        recipeList = hm.getRecipes();

        // recipe list contains all 5 recipies: dinner and breakfast
        assertEquals(5, recipeList.size());

        // click on the “Breakfast” radio-button in the “Filter” box
        controller.receiveMessageFromUI(new Message(Message.HomeView.FilterRecipeButton, "FilterOption", FilterOption.Breakfast));

        recipeList = hm.getRecipes();

        assertEquals(3, recipeList.size());
        
        for (int i = 0; i < recipeList.size(); i++)
            assertEquals("Breakfast", recipeList.get(i).mealType);
    }
}

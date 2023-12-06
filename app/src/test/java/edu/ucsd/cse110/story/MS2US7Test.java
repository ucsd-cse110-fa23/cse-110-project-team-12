package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.HomeModel;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.ModelFactory;
import edu.ucsd.cse110.api.UIFactory;
import edu.ucsd.cse110.api.VoicePromptMock;
import edu.ucsd.cse110.api.HomeModel.SortOption;
import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.schemas.UserSchema;
import edu.ucsd.cse110.server.services.mongodb.MongoDBInterface;
import edu.ucsd.cse110.server.services.mongodb.MongoDBMock;

public class MS2US7Test {
    
    Controller controller;
    MongoDBInterface mongo;

    @Before
    public void init() {
        mongo = new MongoDBMock();
        mongo.dropCollection("users");
        mongo.dropCollection("recipes");
    }

    /*
     * Given I am on the recipe list
     * And There are many recipes that are not in alphabetical order
     * When I click on the “Alphabetical” radio-button in the “Sort-by” box
     * Then I see the recipes in the recipe list sorted in alphabetical order based on their title
     */
    @Test
    public void testUS7BDD1() {

        controller = new Controller(false, new VoicePromptMock(new ArrayList<>()));

        UserSchema user = mongo.createUser("test", "test");
        List<RecipeSchema> recipeList;

        RecipeSchema recipe1 = new RecipeSchema();
        recipe1.userId = user._id;
        recipe1.title = "Fiery Chicken";
        mongo.saveRecipe(recipe1);

        RecipeSchema recipe2 = new RecipeSchema();
        recipe2.userId = user._id;
        recipe2.title = "Yuzu Cakes";
        mongo.saveRecipe(recipe2);

        RecipeSchema recipe3 = new RecipeSchema();
        recipe3.userId = user._id;
        recipe3.title = "Apple Pie Bites";
        mongo.saveRecipe(recipe3);

        controller.receiveMessageFromModel(new Message(Message.LogInModel.SetUser, "User", user));
        controller.receiveMessageFromModel(new Message(Message.LogInModel.StartHomeView));

        HomeModel hm = (HomeModel)controller.getState(ModelFactory.Type.HomePage);
        
        recipeList = hm.getRecipes();

        // recipe list not in alphabetical order
        assertNotEquals(recipe3, recipeList.get(0));
        assertNotEquals(recipe1, recipeList.get(1));
        assertNotEquals(recipe2, recipeList.get(2));

        // click on the “Alphabetical” radio-button in the “Sort-by” box
        controller.receiveMessageFromUI(new Message(Message.HomeView.SortRecipeButton, "SortOption", SortOption.TitleAsc));

        recipeList = hm.getRecipes();

        assertEquals(recipe3, recipeList.get(0));
        assertEquals(recipe1, recipeList.get(1));
        assertEquals(recipe2, recipeList.get(2));
    }
}

package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.HomeModel;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.VoicePromptMock;
import edu.ucsd.cse110.api.ModelFactory;
import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.schemas.UserSchema;
import edu.ucsd.cse110.server.services.mongodb.MongoDBInterface;
import edu.ucsd.cse110.server.services.mongodb.MongoDBMock;

public class MS2US1Test {

    MongoDBInterface mongo;

    @Before
    public void init() {
        mongo = new MongoDBMock();
        mongo.dropCollection("users");
        mongo.dropCollection("recipes");
    }

    // Given I am in the Sign In interface
    // And I have an existing account with the username “david03”
    // And The strong password for my existing account is “mathIsCool03”
    // And I have two existing recipes, for “Chicken Noodle Soup” and “Lasagna”
    // When I fill in “david03” as the username
    // And I fill in “mathIsCool03$$%” as the password
    // And I click on the sign-in button
    // Then I am brought to the home screen with my recipe list
    // And My two recipes “Chicken Noodle Soup” and “Lasagna” are displayed
    @Test
    public void testUS1BDD1() {
        Controller controller = new Controller(false, new VoicePromptMock(null));

        String username = "david03";
        String password = "mathIsCool03";

        UserSchema user = mongo.createUser(username, password);

        String description = "recipe description";
        String mealType = "Breakfast";
        String ingredients = "recipe ingredients";

        RecipeSchema recipe = new RecipeSchema();

        recipe.title = "Chicken Noodle Soup";
        recipe.description = description;
        recipe.mealType = mealType;
        recipe.ingredients = ingredients;
        recipe.userId = user._id;

        RecipeSchema recipe2 = new RecipeSchema();

        recipe2.title = "Lasagna";
        recipe2.description = description;
        recipe2.mealType = mealType;
        recipe2.ingredients = ingredients;
        recipe2.userId = user._id;

        mongo.saveRecipe(recipe);
        mongo.saveRecipe(recipe2);

        controller.receiveMessageFromModel(
                new Message(Message.LogInView.LogInButton,
                        "Username", username,
                        "Password", password,
                        "AutomaticLogIn", true));

        // Then I am brought to the home screen with my recipe list
        assertNotNull(controller.getState(ModelFactory.Type.HomePage));

        HomeModel hm = (HomeModel) controller.getState(ModelFactory.Type.HomePage);

        // And My two recipes “Chicken Noodle Soup” and “Lasagna” are displayed
        assertEquals("Lasagna", hm.getRecipes().get(0).title);
        assertEquals("Chicken Noodle Soup", hm.getRecipes().get(1).title);

    }
}

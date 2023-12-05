package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.RecipeDetailedModel;
import edu.ucsd.cse110.api.VoicePromptMock;
import edu.ucsd.cse110.api.ModelFactory;
import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.schemas.UserSchema;

public class MS2US4Test {
    // Given I started on the create new recipe page of PantryPal
    // And I have selected Breakfast as my meal type
    // And I have provided the ingredients: oats, milk, blueberries, bread, peanut butter
    // And I do not like the first generated recipe
    // When I click the refresh button
    // Then I am shown a new recipe that uses the same ingredients and meal type.
    @Test
    public void testUS4BDD1() {
        Controller controller = new Controller(false, new VoicePromptMock(null));
        UserSchema user = new UserSchema();
        user._id = "123";
        user.username = "Joe";
        user.password = "Mama";
        controller.receiveMessageFromModel(new Message(Message.LogInModel.SetUser, "User", user));
        controller.receiveMessageFromModel(new Message(Message.LogInModel.StartHomeView));

        controller.receiveMessageFromModel(new Message(Message.HomeModel.StartRecipeDetailedView));
        RecipeSchema recipe = new RecipeSchema();
        recipe.mealType = "Breakfast";
        recipe.ingredients = "oats, milk, blueberries, bread, peanut butter";
        recipe.title = "123";
        recipe.description = "abc";
        controller.receiveMessageFromModel(new Message(Message.CreateRecipeModel.SendRecipe, "Recipe", recipe));

        RecipeDetailedModel detailedModel = (RecipeDetailedModel) controller.getState(ModelFactory.Type.DetailedView);
        assertNotNull(detailedModel);
        assertEquals(RecipeDetailedModel.PageType.UnsavedLayout, detailedModel.getCurrentPage());
        assertEquals("Breakfast", detailedModel.getRecipe().mealType);
        assertEquals("oats, milk, blueberries, bread, peanut butter", detailedModel.getRecipe().ingredients);
        assertEquals("123", detailedModel.getRecipe().title);
        assertEquals("abc", detailedModel.getRecipe().description);

        controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.RefreshButton));
        detailedModel = (RecipeDetailedModel) controller.getState(ModelFactory.Type.DetailedView);
        assertNotNull(detailedModel);
        assertNotEquals("123", detailedModel.getRecipe().title);
        assertNotEquals("abc", detailedModel.getRecipe().description);
        // Response 1
        assertTrue(detailedModel.getRecipe().title.contains("Avocado Taco Bowls"));
    }
}

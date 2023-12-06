package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.ModelFactory;
import edu.ucsd.cse110.api.SharePopupModel;
import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.HomeModel;
import edu.ucsd.cse110.api.VoicePromptMock;
import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.schemas.UserSchema;
import edu.ucsd.cse110.server.services.mongodb.MongoDBInterface;
import edu.ucsd.cse110.server.services.mongodb.MongoDBMock;

public class MS2US9Test {

    MongoDBInterface mongo;

    @Before
    public void init() {
        mongo = new MongoDBMock();
        mongo.dropCollection("users");
        mongo.dropCollection("recipes");
    }

    // Given I am on the detailed view of the recipe “Avocado Taco Bowls”
    // When I click on the share button
    // Then I see a generated PantryPal link of the recipe on the detailed view
    // When I open the valid generated PantryPal link in my browser
    // Then I see a webpage with the recipe title “Avocado Taco Bowls”, meal type,
    // recipe information, and image
    @Test
    public void testUS9BDD1() {
        Controller controller = new Controller(false, new VoicePromptMock(null));

        String username = "david03";
        String password = "mathIsCool03";

        UserSchema user = mongo.createUser(username, password);

        String description = "recipe description";
        String mealType = "Breakfast";
        String ingredients = "recipe ingredients";

        RecipeSchema recipe = new RecipeSchema();

        recipe.title = "Avocado Taco Bowls";
        recipe.description = description;
        recipe.mealType = mealType;
        recipe.ingredients = ingredients;
        recipe.userId = user._id;

        mongo.saveRecipe(recipe);


        controller.receiveMessageFromModel(
                new Message(Message.LogInView.LogInButton,
                        "Username", username,
                        "Password", password,
                        "AutomaticLogIn", true));

        // Then I am brought to the home screen with my recipe list
        assertNotNull(controller.getState(ModelFactory.Type.HomePage));

        HomeModel hm = (HomeModel) controller.getState(ModelFactory.Type.HomePage);

        assertEquals("Avocado Taco Bowls", hm.getRecipes().get(0).title);

        controller.receiveMessageFromUI(new Message(Message.HomeView.OpenRecipe, "Recipe", recipe));
        controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.ShareButton));

        SharePopupModel spm = (SharePopupModel) controller.getState(ModelFactory.Type.SharePopup);
        assertNotNull(spm);
        assertNotNull(spm.getSharelink());
    }
}

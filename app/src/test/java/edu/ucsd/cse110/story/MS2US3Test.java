package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.ModelFactory;
import edu.ucsd.cse110.api.RecipeDetailedModel;
import edu.ucsd.cse110.api.VoicePromptMock;
import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.schemas.UserSchema;
import edu.ucsd.cse110.server.services.Utils;
import edu.ucsd.cse110.server.services.dalle.DallEInterface;
import edu.ucsd.cse110.server.services.dalle.DallEMock;
import edu.ucsd.cse110.server.services.mongodb.MongoDBInterface;
import edu.ucsd.cse110.server.services.mongodb.MongoDBMock;

public class MS2US3Test {
    
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
     * When I click on “Avocado Taco Bowls”
     * Then I see a generated image of “Avocado Taco Bowls” on the detailed view pop-up
     */
    @Test
    public void testUS3BBD1() {

        controller = new Controller(false, new VoicePromptMock(new ArrayList<>()));

        DallEInterface dalle = new DallEMock();

        UserSchema user = mongo.createUser("test", "test");

        RecipeSchema recipe = new RecipeSchema();
        recipe.userId = user._id;
        recipe.title = "Avocado Taco Bowls";
        recipe.base64ImageEncoding = Utils.encodeBufferedImageToBase64(dalle.promptDallE(recipe.title));

        mongo.saveRecipe(recipe);

        controller.receiveMessageFromModel(new Message(Message.LogInModel.SetUser, "User", user));
        controller.receiveMessageFromModel(new Message(Message.LogInModel.StartHomeView));

        // click on “Avocado Taco Bowls”
        controller.receiveMessageFromUI(new Message(Message.HomeView.OpenRecipe, "Recipe", recipe));

        RecipeDetailedModel rdm = (RecipeDetailedModel)controller.getState(ModelFactory.Type.DetailedView);

        assertNotNull(rdm);
        assertEquals(RecipeDetailedModel.PageType.SavedLayout, rdm.getCurrentPage());
        assertEquals(recipe.base64ImageEncoding, rdm.getRecipe().base64ImageEncoding);
    }
}
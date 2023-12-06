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

public class MS2US6Test {

    MongoDBInterface mongo;

    @Before
    public void init() {
        mongo = new MongoDBMock();
        mongo.dropCollection("users");
        mongo.dropCollection("recipes");
    }

    // Given I have a PantryPal account with username “user” and password “password”
    // And I have PantryPal downloaded on my laptop
    // When I open the PantryPal app on my laptop
    // And I am connected to the server
    // Then I am on the entry page
    // When I click on the “Sign In” button
    // Then I am taken to the Sign In interface
    // When I enter “user” and “password” as my username and password respectively
    // And I click the “Sign In” button
    // Then I am logged into my account and taken to the homepage.

    // Testing sign in functionality
    @Test
    public void testUS6BDD1() {
        Controller controller = new Controller(false, new VoicePromptMock(null));

        String username = "user";
        String password = "password";

        UserSchema user = mongo.createUser(username, password);

        controller.receiveMessageFromModel(
                new Message(Message.LogInView.LogInButton,
                        "Username", username,
                        "Password", password,
                        "AutomaticLogIn", true));

        // Then I am logged into my account and taken to the homepage.
        assertNotNull(controller.getState(ModelFactory.Type.HomePage));

    }
}

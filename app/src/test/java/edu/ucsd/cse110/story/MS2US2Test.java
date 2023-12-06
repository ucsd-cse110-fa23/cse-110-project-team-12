package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.ModelFactory;
import edu.ucsd.cse110.api.UIFactory;
import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.CreateAccountModel;
import edu.ucsd.cse110.api.HomeModel;
import edu.ucsd.cse110.api.HttpUtils;
import edu.ucsd.cse110.api.VoicePromptMock;
import edu.ucsd.cse110.server.services.mongodb.MongoDBInterface;
import edu.ucsd.cse110.server.services.mongodb.MongoDBMock;

public class MS2US2Test {

    Controller controller;
    MongoDBInterface mongo;

    @Before
    public void init() {
        mongo = new MongoDBMock();
        mongo.dropCollection("users");
        mongo.dropCollection("recipes");
    }

    /*
     * Given I am in the Sign In interface
     * When I click the “Sign Up” button
     * Then I see the Create Account interface
     * And the Username and Password fields are empty
     * And the automatic login button is checked off.
     */
    @Test
    public void testUS2BDD1() {
        
        controller = new Controller(false, new VoicePromptMock(new ArrayList<>()));

        // When I click the “Sign Up” button
        controller.receiveMessageFromUI(new Message(Message.LogInView.SignUpButton));

        // Then I see the Create Account interface
        assertTrue(controller.getState(ModelFactory.Type.CreateAccount) instanceof CreateAccountModel);
        assertNull(controller.getState(ModelFactory.Type.LogIn));
    }

    /*
     * Given I am in the Create Account interface
     * When I fill in “Alice” as the username
     * And fill in “A&^je39hdI” as the password and confirm password input
     * And check the Automatic login check box
     * And click on the “Create Account” button
     * Then I am brought to the home screen with 0 recipes in the recipe list.
     */
    @Test
    public void testUS2BDD2() {

        controller = new Controller(false, new VoicePromptMock(new ArrayList<>()));
        controller.receiveMessageFromModel(new Message(Message.LogInModel.StartCreateAccountView));

        String username = "Alice";
        String password = "A&^je39hdI";
        boolean autoLogin = true;

        // click the create account button
        controller.receiveMessageFromUI(new Message(Message.CreateAccountView.SignUpButton,
            "Username", username,
            "Password", password,
            "AutomaticLogIn", autoLogin));

        HomeModel hm = (HomeModel)controller.getState(ModelFactory.Type.HomePage);
        
        assertNull(controller.getState(ModelFactory.Type.CreateAccount));
        assertNotNull(hm);
        assertEquals(UIFactory.Type.HomePage, hm.getCurrentView());
        assertEquals(0, hm.getRecipes().size());
        assertEquals(username, URLDecoder.decode(controller.getCurrentUser().username, StandardCharsets.UTF_8));
        assertEquals(password, URLDecoder.decode(controller.getCurrentUser().password, StandardCharsets.UTF_8));
    }

    /*
     * Given I am in the Create Account interface
     * And a user named “Bob” already registered an account
     * When I fill in the “Bob” as the username
     * And I fill in “IJD3de0**#d” as the password and confirm password input
     * And click on the “Create Account” button
     * Then I will stay in the Create Account interface
     * And a “Username already exists” message will appear above the username input.
     */
    @Test
    public void testUS2BDD3() {

        controller = new Controller(false, new VoicePromptMock(new ArrayList<>()));
        controller.receiveMessageFromModel(new Message(Message.LogInModel.StartCreateAccountView));

        // Account with Username = Bob exists
        String username = "Bob";
        String password1 = "password";

        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
        String encodedPassword = URLEncoder.encode(password1, StandardCharsets.UTF_8);
        String urlString = Controller.serverUrl + "/user?username=" + encodedUsername + "&password=" + encodedPassword;
        HttpUtils.makeHttpRequest(urlString, "POST", "");

        String password2 = "IJD3de0**#d";
        boolean autoLogin = false;

        // click create account button with Username = Bob
        controller.receiveMessageFromUI(new Message(Message.CreateAccountView.SignUpButton,
            "Username", username,
            "Password", password2,
            "AutomaticLogIn", autoLogin));

        assertNotNull(controller.getState(ModelFactory.Type.CreateAccount));
        assertTrue(controller.getState(ModelFactory.Type.CreateAccount) instanceof CreateAccountModel);
    }

    /*
     * Given I am in the Create Account interface
     * When I leave the username blank
     * And I leave the password blank
     * And check the Automatic login check box
     * And click on the “Create Account” button
     * Then I will stay in the Create Account interface
     * And a message saying “Invalid Username/Password” is displayed
     */
    @Test
    public void testUS2BDD5() {

        controller = new Controller(false, new VoicePromptMock(new ArrayList<>()));
        controller.receiveMessageFromModel(new Message(Message.LogInModel.StartCreateAccountView));

        String username = "";
        String password = "";
        boolean autoLogin = true;

        // click the create account button
        controller.receiveMessageFromUI(new Message(Message.CreateAccountView.SignUpButton,
            "Username", username,
            "Password", password,
            "AutomaticLogIn", autoLogin));

        assertNotNull(controller.getState(ModelFactory.Type.CreateAccount));
        assertTrue(controller.getState(ModelFactory.Type.CreateAccount) instanceof CreateAccountModel);
    }
}

package edu.ucsd.cse110.story.MS2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;

import org.checkerframework.checker.guieffect.qual.UIType;
import org.junit.Test;

import edu.ucsd.cse110.api.*;

public class MS2US1Test {
    Controller controller;

    // Given I am in the Sign In interface
    // And I have an existing account with the username “david03”
    // And The strong password for my existing account is “mathIsCool03$$%”
    // And I have two existing recipes, for “Chicken Noodle Soup” and “Lasagna”
    // When I fill in “david03” as the username
    // And I fill in “mathIsCool03$$%” as the password
    // And I click on the sign-in button
    // Then I am brought to the home screen with my recipe list
    // And My two recipes “Chicken Noodle Soup” and “Lasagna” are displayed
    @Test
    public void TestMS2US1BDD1() {
        HttpRequesterMock mockHttpRequester = new HttpRequesterMock();
        controller = new Controller(false, mockHttpRequester, new VoicePromptMock(new ArrayList<>()));

        // Add user with username "david03" and password

        // Add two exising recipes "Chicken Noodle Soup" and "Lasagna"

        controller.receiveMessageFromUI(new Message(Message.CreateAccountModel.StartLogInView));

        mockHttpRequester.addResponse(
                new ServerResponse(200, "{{\"username\": \"david03\", \"password\": \"mathIsCool03$$%\"}}"));

        

        controller.receiveMessageFromUI(
                new Message(Message.LogInView.LogInButton,
                        "Username", "david03",
                        "Password", "mathIsCool03$$%",
                        "AutomaticLogIn", true));

        HomeModel hm = (HomeModel)controller.getState(ModelFactory.Type.HomePage);
        assertEquals(UIFactory.Type.HomePage,  hm.getCurrentView());

        // Fill in "david03 as the username"
        // Fill in mathIsCool03$$%" as the password

        // Sign in

        // Assert to see if home screen is the view
        // Assert that recipes contain "Chicken Noodle Soup" and "Lasagna"

    }

}

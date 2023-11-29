package edu.ucsd.cse110.story.MS2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;

import org.junit.Test;

import edu.ucsd.cse110.api.HomeModel;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.MongoDBMock;
import edu.ucsd.cse110.api.ChatGPTMock;
import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.VoicePromptMock;
import edu.ucsd.cse110.api.WhisperMock;
import edu.ucsd.cse110.api.Controller.ModelType;
import edu.ucsd.cse110.api.Controller.UIType;

public class MS2US2Test {
    Controller controller;

    // Given I am in the Sign In interface
    // When I click the “Sign Up” button
    // Then I see the Create Account interface
    // And the Username and Password fields are empty
    // And the automatic login button is checked off.
    @Test
    public void TestMS2US2BDD1() {
        // controller = new Controller(false, new VoicePromptMock(new ArrayList<>()), new WhisperMock(), new ChatGPTMock(), new MongoDBMock());

        // controller.receiveMessageFromUI(new Message(Message.LogInModel.StartCreateAccountView)); // When I click on the add button
        
    }

}

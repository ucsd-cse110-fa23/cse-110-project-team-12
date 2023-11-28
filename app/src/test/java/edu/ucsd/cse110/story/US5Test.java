package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;

import org.junit.Test;

import edu.ucsd.cse110.api.HomeModel;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.ChatGPTMock;
import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.VoicePromptMock;
import edu.ucsd.cse110.api.WhisperMock;
import edu.ucsd.cse110.api.Controller.ModelType;
import edu.ucsd.cse110.api.Controller.UIType;

public class US5Test {
    Controller controller;

    @Test
    public void TestUS5BDD1() {
        controller = new Controller(false, new VoicePromptMock(new ArrayList<>()), new WhisperMock(), new ChatGPTMock(), false);

        controller.receiveMessageFromUI(new Message(Message.HomeView.CreateRecipeButton)); // When I click on the add button
        
        assertEquals(UIType.CreateRecipe, ((HomeModel) controller.getState(ModelType.HomePage)).getCurrentView()); // Then I will see the input meal type page in a pop-up window..
    }

    @Test
    public void TestUS5BDD2() {
        // Open recipe.
        controller = new Controller(false, new VoicePromptMock(new ArrayList<>()), new WhisperMock(), new ChatGPTMock(), false);
        
        controller.receiveMessageFromUI(new Message(Message.HomeView.CreateRecipeButton)); // When I click on the add button
        assertEquals(UIType.CreateRecipe, ((HomeModel) controller.getState(ModelType.HomePage)).getCurrentView());

        // Close Recipe.
        controller.receiveMessageFromUI(new Message(Message.CreateRecipeModel.CloseCreateRecipeView));
        assertNotEquals(UIType.CreateRecipe, ((HomeModel) controller.getState(ModelType.HomePage)).getCurrentView());

    }
}

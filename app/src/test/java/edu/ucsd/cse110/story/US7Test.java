package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ucsd.cse110.api.*;

import java.util.List;
import java.util.ArrayList;

public class US7Test {
    
    private Controller controller;
    private VoicePromptInterface voice;

    /*
     * Given I am in the input recipe ingredients page
     * When I click on the record voice button
     * And say out loud a list of ingredients
     * And I click on the stop record voice button
     * Then the app will transcribe the recording into text.
     */
    @Test
    public void testUS7BDD1() {
        List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>();
        promptTypes.add(VoicePromptMock.PromptType.MealType);
        promptTypes.add(VoicePromptMock.PromptType.IngredientsList);
        voice = new VoicePromptMock(promptTypes);
        controller = new Controller(false, voice, new WhisperMock(), new ChatGPTMock());
        controller.receiveMessageFromUI(new Message(Message.HomeView.CreateRecipeButton));
        controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton));
        controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton));

        assertEquals(CreateRecipeModel.PageType.IngredientsInput, ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getCurrentPage()); // Given I am in the input recipe ingredients page
        controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton)); // When I click on the record voice button
        assertTrue(((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getIsRecording());
        controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton)); // And say out loud a list of ingredients And click on the stop record voice button
        assertFalse(((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getIsRecording());
        
        String transcribedText = ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getSelectedIngredients();
        assertTrue(transcribedText,transcribedText.contains("banana") 
                    && transcribedText.contains("pepper") 
                    && transcribedText.contains("onion") 
                    && transcribedText.contains("green onion") 
                    && transcribedText.contains("salt") 
                    && transcribedText.contains("carrot")); // Then the app will transcribe the recording into text.
    }

    /*
     * Given I am in the input recipe ingredients page
     * When I click on the back button
     * Then my preferences on meal type are discarded
     * And I go back to the input meal type page.
     * */
    
     @Test
    public void testUS7BDD2() {
        List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>();
        promptTypes.add(VoicePromptMock.PromptType.MealType);
        voice = new VoicePromptMock(promptTypes);
        controller = new Controller(false, voice, new WhisperMock(), new ChatGPTMock());
        controller.receiveMessageFromUI(new Message(Message.HomeView.CreateRecipeButton));
        controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton));
        controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton));

        assertEquals(CreateRecipeModel.PageType.IngredientsInput, ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getCurrentPage()); // Given I am in the input recipe ingredients page
        controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.BackButton)); // When I click on the back button
        assertEquals(CreateRecipeModel.MealType.None, ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getSelectedMealType()); // Then my preferences on meal type are discarded (By design, reinputting will override the old preference).
        assertEquals(CreateRecipeModel.PageType.MealTypeInput, ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getCurrentPage()); // And I go back to the input meal type page.
    }
}

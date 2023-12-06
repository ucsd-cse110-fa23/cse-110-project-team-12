package edu.ucsd.cse110.story.MS1Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import edu.ucsd.cse110.api.*;

import java.util.List;
import java.util.ArrayList;

public class US6Test {
    
    private Controller controller;
    private VoicePromptInterface voicePrompt;

    /*
     * Given I am on the input meal type page
     * When I click on the record voice button
     * And say out loud a valid recipe type
     * And click on the stop record voice button
     * Then I will be taken to the input recipe ingredients page in the pop-up window
     * And my preference will be displayed.
     */
  
    // @Test
    // public void testUS6BDD1() {
    //     List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>();
    //     promptTypes.add(VoicePromptMock.PromptType.MealType);
    //     voicePrompt = new VoicePromptMock(promptTypes);
    //     controller = new Controller(false, voicePrompt, new WhisperMock(), new ChatGPTMock(), false);

    //     controller.receiveMessageFromUI(new Message(Message.HomeView.CreateRecipeButton));

    //     assertEquals(CreateRecipeModel.PageType.MealTypeInput, ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getCurrentPage()); // Given I am on input meal type page
    //     controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton)); // When I click on the record voice button
    //     assertTrue(((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getIsRecording());
    //     controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton)); // And say out loud a valid recipe type And click on the stop record voice button
    //     assertFalse(((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getIsRecording());
        
    //     assertEquals(CreateRecipeModel.PageType.IngredientsInput, ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getCurrentPage()); // Then I will be taken to the input recipe ingredients page in the pop-up window
    //     assertEquals(CreateRecipeModel.MealType.Lunch, ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getSelectedMealType()); // And my preference will be displayed.
    // }
    // /* 
    //  * Given I am on the input meal type page
    //  * When I click on the record voice button
    //  * And say an invalid recipe type
    //  * And click on the stop record voice button
    //  * Then I will be shown an error message
    //  * And I can try to input the meal type again.
    //  */
    
    // @Test
    // public void testUS6BDD2() {
    //     List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>();
    //     promptTypes.add(VoicePromptMock.PromptType.InvalidNotMealType);
    //     voicePrompt = new VoicePromptMock(promptTypes);
    //     controller = new Controller(false, voicePrompt, new WhisperMock(), new ChatGPTMock(), false);

    //     controller.receiveMessageFromUI(new Message(Message.HomeView.CreateRecipeButton));

    //     assertEquals(CreateRecipeModel.PageType.MealTypeInput, ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getCurrentPage()); // Given I am on input meal type page
    //     controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton)); // When I click on the record voice button
    //     assertTrue(((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getIsRecording());
    //     controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton)); // And say an invalid recipe type And click on the stop record voice button
    //     assertFalse(((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getIsRecording());
        
    //     assertEquals(CreateRecipeModel.MealType.None, ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getSelectedMealType()); // Then I will be shown an error message
    //     assertEquals(CreateRecipeModel.PageType.MealTypeInput, ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getCurrentPage()); // And I can try to input the meal type again.
    // }

    // /*
    //  * Given I am on the input meal type page
    //  * When I click on the back button
    //  * Then my preferences made so far are discarded
    //  * And the popup window closes
    //  * And I am back in the existing recipe list. 
    //  * */
    
    //  @Test
    // public void testUS6BDD3() {
    //     List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>();
    //     voicePrompt = new VoicePromptMock(promptTypes);
    //     controller = new Controller(false, voicePrompt, new WhisperMock(), new ChatGPTMock(), false);

    //     controller.receiveMessageFromUI(new Message(Message.HomeView.CreateRecipeButton));

    //     assertEquals(CreateRecipeModel.PageType.MealTypeInput, ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getCurrentPage()); // Given I am on input meal type page
    //     controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.BackButton)); // When I click on the back button
    //     assertEquals(CreateRecipeModel.MealType.None, ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getSelectedMealType()); // Then my preferences made so far are discarded
    //     assertNull(((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getSelectedIngredients());
    //     assertEquals(Controller.UIType.HomePage, ((HomeModel) controller.getState(Controller.ModelType.HomePage)).getCurrentView()); // And the popup window closes And I am back in the existing recipe list.
    // }
}

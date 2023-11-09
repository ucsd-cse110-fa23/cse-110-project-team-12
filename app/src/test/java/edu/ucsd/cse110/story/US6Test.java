package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ucsd.cse110.api.*;

import java.util.List;
import java.util.ArrayList;

public class US6Test {
    
    private CreateRecipeManager manager;
    private WhisperInterface whisper;
    private VoicePromptInterface voice;

    /*
     * Given I am on the input meal type page
     * When I click on the record voice button
     * And say out loud a valid recipe type
     * And click on the stop record voice button
     * Then I will be taken to the input recipe ingredients page in the pop-up window
     * And my preference will be displayed.
     */
    @Test
    public void testUS6BDD1(){
        whisper = new WhisperMock();
        List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>();
        promptTypes.add(VoicePromptMock.PromptType.MealType);
        voice = new VoicePromptMock(promptTypes);
        manager = new CreateRecipeManager(false, voice, whisper, new ChatGPTMock());

        assertEquals(CreateRecipeManager.PageType.MealTypeInput, manager.getPage()); // Given I am on input meal type page
        manager.startRecording(); // When I click on the record voice button
        assertEquals(true, manager.getIsRecording());
        manager.stopRecording(); // And say out loud a valid recipe type And click on the stop record voice button
        assertEquals(false, manager.getIsRecording());
        
        assertEquals(CreateRecipeManager.PageType.IngredientsInput, manager.getPage()); // Then I will be taken to the input recipe ingredients page in the pop-up window
        assertEquals(CreateRecipeManager.MealType.Lunch, manager.getMealType()); // And my preference will be displayed.
    }
    /* 
     * Given I am on the input meal type page
     * When I click on the record voice button
     * And say an invalid recipe type
     * And click on the stop record voice button
     * Then I will be shown an error message
     * And I can try to input the meal type again.
     */
    
    @Test
    public void testUS6BDD2(){
        whisper = new WhisperMock();
        List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>();
        promptTypes.add(VoicePromptMock.PromptType.InvalidNotMealType);
        voice = new VoicePromptMock(promptTypes);
        manager = new CreateRecipeManager(false, voice, whisper, new ChatGPTMock());

        assertEquals(CreateRecipeManager.PageType.MealTypeInput, manager.getPage()); // Given I am on input meal type page
        manager.startRecording(); // When I click on the record voice button
        assertEquals(true, manager.getIsRecording());
        manager.stopRecording(); // And say an invalid recipe type And click on the stop record voice button
        assertEquals(false, manager.getIsRecording());
        
        assertEquals(CreateRecipeManager.MealType.Invalid, manager.getMealType()); // Then I will be shown an error message
        assertEquals(CreateRecipeManager.PageType.MealTypeInput, manager.getPage()); // And I can try to input the meal type again.
    }

    /*
     * Given I am on the input meal type page
     * When I click on the back button
     * Then my preferences made so far are discarded
     * And the popup window closes
     * And I am back in the existing recipe list. 
     * */
    
     @Test
    public void testUS6BDD3(){
        whisper = new WhisperMock();
        List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>();
        voice = new VoicePromptMock(promptTypes);
        manager = new CreateRecipeManager(false, voice, whisper, new ChatGPTMock());

        assertEquals(CreateRecipeManager.PageType.MealTypeInput, manager.getPage()); // Given I am on input meal type page
        manager.goToPreviousPage(); // When I click on the back button
        // Then my preferences made so far are discarded (By design, next time will override it).
         assertEquals(CreateRecipeManager.PageType.MealTypeInput, manager.getPage()); // And the popup window closes (By design, popup window closes, appframe stop casting but manager is still in mealtypeinput)
        // And I am back in the existing recipe list. (By design in CreateRecipe.java, popup window closes, appframe stop casting)
    }
}

package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ucsd.cse110.api.*;

import java.util.List;
import java.util.ArrayList;

public class US7Test {
    
    private CreateRecipeManager manager;
    private WhisperInterface whisper;
    private VoicePromptInterface voice;

    /*
     * Given I am in the input recipe ingredients page
     * When I click on the record voice button
     * And say out loud a list of ingredients
     * And I click on the stop record voice button
     * Then the app will transcribe the recording into text.
     */
    @Test
    public void testUS7BDD1(){
        whisper = new WhisperMock();
        List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>();
        promptTypes.add(VoicePromptMock.PromptType.IngredientsList);
        voice = new VoicePromptMock(promptTypes);
        manager = new CreateRecipeManager(voice, whisper, new ChatGPTMock());

        manager.goToNextPage();
        assertEquals(CreateRecipeManager.PageType.IngredientsInput, manager.getPage()); // Given I am in the input recipe ingredients page
        manager.startRecording(); // When I click on the record voice button
        assertEquals(true, manager.getIsRecording());
        manager.stopRecording(); // And say out loud a list of ingredients And click on the stop record voice button
        assertEquals(false, manager.getIsRecording());
        
        String transcribedText = manager.getSelectedIngredients();
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
    public void testUS7BDD2(){
        whisper = new WhisperMock();
        List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>();
        voice = new VoicePromptMock(promptTypes);
        manager = new CreateRecipeManager(voice, whisper, new ChatGPTMock());

        manager.goToNextPage();
        assertEquals(CreateRecipeManager.PageType.IngredientsInput, manager.getPage()); // Given I am in the input recipe ingredients page
        manager.goToPreviousPage(); // When I click on the back button
        // Then my preferences on meal type are discarded (By design, reinputting will override the old preference).
         assertEquals(CreateRecipeManager.PageType.MealTypeInput, manager.getPage()); // And I go back to the input meal type page.
    }
}

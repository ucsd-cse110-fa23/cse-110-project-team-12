package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import edu.ucsd.cse110.api.ChatGPTInterface;
import edu.ucsd.cse110.api.ChatGPTMock;
import edu.ucsd.cse110.api.CreateRecipeManager;
import edu.ucsd.cse110.api.VoicePromptInterface;
import edu.ucsd.cse110.api.VoicePromptMock;
import edu.ucsd.cse110.api.WhisperInterface;
import edu.ucsd.cse110.api.WhisperMock;

public class US8Test {
    private CreateRecipeManager manager;
    private ChatGPTInterface chatGPT;
    private WhisperInterface whisper;
    private VoicePromptInterface voice;

    // Tests the BDD Scenario listed for US8
    @Test
    public void testIfDetailedViewIsDisplayedAfterRecipeCreation() {

        // Set up manager
        chatGPT = new ChatGPTMock();
        whisper = new WhisperMock();
        voice = new VoicePromptMock(VoicePromptMock.PromptType.IngredientsList);
        
        manager = new CreateRecipeManager(voice, whisper, chatGPT);

        assertEquals("", manager.getRecipe().getName());
        assertEquals("", manager.getRecipe().getInformation());  

        // Part of set up, get to ingredients input page
        assertEquals(CreateRecipeManager.PageType.MealTypeInput, manager.getPage());
        manager.goToNextPage();
        assertEquals(CreateRecipeManager.PageType.IngredientsInput, manager.getPage());


        // Given I am waiting for the recipe to be created -> When the Recipe is created
        manager.processTranscript("Chicken, rice, broccoli");


        // Then I am taken to the detailed view for the recipe
        assertEquals(CreateRecipeManager.PageType.DetailedView, manager.getPage());

        assertNotEquals("", manager.getRecipe().getName());
        assertNotEquals("", manager.getRecipe().getInformation());   
    }

    

}
package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import edu.ucsd.cse110.api.ChatGPTInterface;
import edu.ucsd.cse110.api.ChatGPTMock;
import edu.ucsd.cse110.api.CreateRecipeModel;
import edu.ucsd.cse110.api.VoicePromptInterface;
import edu.ucsd.cse110.api.VoicePromptMock;
import edu.ucsd.cse110.api.WhisperInterface;
import edu.ucsd.cse110.api.WhisperMock;

import java.util.List;
import java.util.ArrayList;

public class US8Test {
    private CreateRecipeModel manager;
    private ChatGPTInterface chatGPT;
    private WhisperInterface whisper;
    private VoicePromptInterface voice;

    // Tests the BDD Scenario listed for US8
    /*
     * Given I am waiting for the recipe to be created
     * When the recipe is created
     * Then I am taken to the detailed view for the recipe
     */
    @Test
    public void testUS8BDD1() {

        // Set up manager
        chatGPT = new ChatGPTMock();
        whisper = new WhisperMock();
        List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>(); 
        promptTypes.add(VoicePromptMock.PromptType.IngredientsList);
        voice = new VoicePromptMock(promptTypes);
        
        manager = new CreateRecipeModel(false, voice, whisper, chatGPT);

        assertEquals("", manager.getRecipe().getName());
        assertEquals("", manager.getRecipe().getInformation());  

        // Part of set up, get to ingredients input page
        assertEquals(CreateRecipeModel.PageType.MealTypeInput, manager.getPage());
        manager.goToNextPage();
        assertEquals(CreateRecipeModel.PageType.IngredientsInput, manager.getPage());


        // Given I am waiting for the recipe to be created -> When the Recipe is created
        manager.processTranscript("Chicken, rice, broccoli");


        // Then I am taken to the detailed view for the recipe
        assertEquals(CreateRecipeModel.PageType.DetailedView, manager.getPage());

        assertNotEquals("", manager.getRecipe().getName());
        assertNotEquals("", manager.getRecipe().getInformation());   
    }

    

}
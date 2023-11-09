package edu.ucsd.cse110.story;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import edu.ucsd.cse110.api.HomeManager;
import edu.ucsd.cse110.api.ChatGPTMock;
import edu.ucsd.cse110.api.VoicePromptMock;
import edu.ucsd.cse110.api.WhisperMock;
import edu.ucsd.cse110.api.HomeManager.UpdateType;
import edu.ucsd.cse110.api.HomeManager.ViewType;

public class US5Test {
    @Test
    public void TestUS5BDD1() {
        HomeManager appManager = new HomeManager(false, new VoicePromptMock(new ArrayList<>()), new WhisperMock(), new ChatGPTMock());

        appManager.updateView(ViewType.CreateRecipeView, UpdateType.Start); // When I click on the add button

        assertTrue(appManager.getIsCreatingRecipe()); // Then I will see the input meal type page in a pop-up window..
    }

    @Test
    public void TestUS5BDD2() {
        // Open recipe.
        HomeManager appManager = new HomeManager(false, new VoicePromptMock(new ArrayList<>()), new WhisperMock(), new ChatGPTMock());

        appManager.updateView(ViewType.CreateRecipeView, UpdateType.Start); 
        assertTrue(appManager.getIsCreatingRecipe());

        // Close Recipe.
        appManager.updateView(ViewType.CreateRecipeView, UpdateType.Close);
        assertFalse(appManager.getIsCreatingRecipe());

    }
}

package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.api.AppFrameManager;
import edu.ucsd.cse110.api.CreateRecipeManager;
import edu.ucsd.cse110.client.AppFrame;

public class US5Test {
    @Test
    public void TestCreateRecipe() {
        AppFrameManager appFrameManager = new AppFrameManager(); // Given I am in the home page of the app

        appFrameManager.createRecipe(); // When I click on the add button

        assertTrue(appFrameManager.getIsCreatingRecipe()); // Then I will see the input meal type page in a pop-up window..
    }

    @Test
    public void TestDeleteRecipe() {
        // Open recipe.
        AppFrameManager appFrameManager = new AppFrameManager(); // Given I am in the home page of the app
        appFrameManager.createRecipe(); 
        assertTrue(appFrameManager.getIsCreatingRecipe());

        // Close Recipe.
        appFrameManager.stopCreating();
        assertFalse(appFrameManager.getIsCreatingRecipe());

    }
}

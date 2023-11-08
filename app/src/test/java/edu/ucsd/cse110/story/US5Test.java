package edu.ucsd.cse110.story;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ucsd.cse110.api.AppFrameManager;

public class US5Test {
    @Test
    public void TestUS5BDD1() {
        AppFrameManager appFrameManager = new AppFrameManager(); // Given I am in the home page of the app

        appFrameManager.createRecipe(); // When I click on the add button

        assertTrue(appFrameManager.getIsCreatingRecipe()); // Then I will see the input meal type page in a pop-up window..
    }

    @Test
    public void TestUS5BackButton() {
        // Open recipe.
        AppFrameManager appFrameManager = new AppFrameManager(); // Given I am in the home page of the app
        appFrameManager.createRecipe(); 
        assertTrue(appFrameManager.getIsCreatingRecipe());

        // Close Recipe.
        appFrameManager.stopCreating();
        assertFalse(appFrameManager.getIsCreatingRecipe());

    }
}

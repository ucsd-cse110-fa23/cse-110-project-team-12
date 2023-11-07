package edu.ucsd.cse110.client;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.ucsd.cse110.api.CreateRecipeManager;

public class US5Test {

    private AppFrameMock mockFrame;

    @Before
    public void setUp() {

        mockFrame = new AppFrameMock();
    }

    @Test
    public void testOnHomePage() {

        // on home page --> no recipe being created
        assertEquals(null, mockFrame.getRecipeManager());
        assertEquals(false, mockFrame.getCreatingRecipe());
    }
    
    @Test
    public void testHomePageToMealTypePage() {

        mockFrame.clickAddButton();

        // test the page is MealTypeInput and selected meal type is null
        assertEquals(CreateRecipeManager.PageType.MealTypeInput, mockFrame.getRecipeManager().getPage());
        assertEquals(null, mockFrame.getRecipeManager().getMealType());

        // test that creating recipe is true
        assertEquals(true, mockFrame.getCreatingRecipe());
    }

    @Test
    public void testMealTypePageToHomePage() {
        
        mockFrame.stopCreating();

        // when back button is clicked from MealTypePage remove create recipe page
        assertEquals(null, mockFrame.getRecipeManager());
        assertEquals(false, mockFrame.getCreatingRecipe());
    }
}

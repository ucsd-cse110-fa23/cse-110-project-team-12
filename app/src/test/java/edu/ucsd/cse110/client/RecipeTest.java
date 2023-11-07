package edu.ucsd.cse110.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class RecipeTest {
    private Recipe recipe;

    @Test
    public void testRecipeConstructor() {
        recipe = new Recipe("Banana Bread", "Bananas, Bread");
        assertNotNull(recipe);
        assertEquals(recipe.getName(), "Banana Bread");
        assertEquals(recipe.getInformation(), "Bananas, Bread");
    }

    @Test
    public void thisRecipeSetMethods() {
        recipe = new Recipe();
        assertNotNull(recipe);
        recipe.setName("Icecream Sandwich");
        recipe.setInformation("Ice Cream, Sandwich");
        assertEquals(recipe.getName(), "Icecream Sandwich");
        assertEquals(recipe.getInformation(), "Ice Cream, Sandwich");
    }

    @Test
    public void testRecipeSetMethodsWithNonDefaultConstructor() {
        recipe = new Recipe("Banana Bread", "Bananas, Bread");
        assertNotNull(recipe);
        recipe.setName("Icecream Sandwich");
        recipe.setInformation("Ice Cream, Sandwich");
        assertEquals(recipe.getName(), "Icecream Sandwich");
        assertEquals(recipe.getInformation(), "Ice Cream, Sandwich");
    }

}
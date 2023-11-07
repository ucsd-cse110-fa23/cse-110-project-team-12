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
        assertEquals("Banana Bread", recipe.getName());
        assertEquals("Bananas, Bread", recipe.getInformation());
    }

    @Test
    public void thisRecipeSetMethods() {
        recipe = new Recipe();
        assertNotNull(recipe);

        recipe.setName("Icecream Sandwich");
        recipe.setInformation("Ice Cream, Sandwich");

        assertEquals("Icecream Sandwich", recipe.getName());
        assertEquals("Ice Cream, Sandwich", recipe.getInformation());
    }

    @Test
    public void testRecipeSetMethodsWithNonDefaultConstructor() {
        recipe = new Recipe("Banana Bread", "Bananas, Bread");
        assertNotNull(recipe);

        recipe.setName("Icecream Sandwich");
        recipe.setInformation("Ice Cream, Sandwich");

        assertEquals("Icecream Sandwich", recipe.getName());
        assertEquals("Ice Cream, Sandwich", recipe.getInformation());
    }

}
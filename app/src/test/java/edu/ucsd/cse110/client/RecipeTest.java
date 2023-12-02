package edu.ucsd.cse110.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.ucsd.cse110.server.schemas.RecipeSchema;

public class RecipeTest {
    private RecipeSchema recipe;

    @Test
    public void testRecipeConstructor() {
        recipe = new RecipeSchema();
        recipe.title = "Banana Bread";
        recipe.description = "Bananas, Bread";
        recipe.mealType = "Breakfast";

        assertNotNull(recipe);
        assertEquals("Banana Bread", recipe.title);
        assertEquals("Bananas, Bread", recipe.description);
    }

    @Test
    public void testRecipeChangeWithNonDefaultConstructor() {
        recipe = new RecipeSchema();
        recipe.title = "Banana Bread";
        recipe.description = "Bananas, Bread";
        assertNotNull(recipe);

        recipe.title = "Icecream Sandwich";
        recipe.description = "Ice Cream, Sandwich";
        recipe.mealType = "Breakfast";

        assertEquals("Icecream Sandwich", recipe.title);
        assertEquals("Ice Cream, Sandwich", recipe.description);
    }

}
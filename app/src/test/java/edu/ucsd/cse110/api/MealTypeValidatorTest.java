package edu.ucsd.cse110.api;

import static org.junit.Assert.*;
import org.junit.Test;

public class MealTypeValidatorTest {

    @Test
    public void testMealValidate() {
        // Contains good string.
        String mealText = "I would like to have breakfast.";
        assertTrue(MealTypeValidator.validateMealType(mealText));

        // Contains good string.
        mealText = "please give me uhhhh DINNer, and uhhhh nevermind :)";
        assertTrue(MealTypeValidator.validateMealType(mealText));

        // Contains no good string.
        mealText = "I would like uhhhhhhh i forgot";
        assertFalse(MealTypeValidator.validateMealType(mealText));

        // Contains duplicate.
        mealText = "I would like breakfast and not lunch";
        assertFalse(MealTypeValidator.validateMealType(mealText));

        mealText = "I would like uhhhhhhh nevermind";
        assertFalse(MealTypeValidator.validateMealType(mealText));
    }

    @Test
    public void testMealTypeParse() {
        // Contains good string.
        String mealText = "I would like to have Breakfast.";
        assertTrue("Breakfast".equals(MealTypeValidator.parseMealType(mealText)));

        // Contains good string.
        mealText = "please give me uhhhh dinnER, and uhhhh nevermind :)";
        assertTrue("Dinner".equals(MealTypeValidator.parseMealType(mealText)));
    }
}

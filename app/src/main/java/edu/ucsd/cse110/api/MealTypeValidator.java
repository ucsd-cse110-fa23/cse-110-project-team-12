package edu.ucsd.cse110.api;

public class MealTypeValidator {

    public static boolean validateMealType(String mealTypeText) {
        String[] validMealTypes = new String[]{"breakfast", "lunch", "dinner"};
        int containsCount = 0;
        mealTypeText = mealTypeText.toLowerCase();
        for (String validMealType : validMealTypes) {
            if (mealTypeText.contains(validMealType)) {
                containsCount++;
            }
        }
        return containsCount == 1;
    }

    public static CreateRecipeModel.MealType parseMealType(String validMealTypeText) {
        validMealTypeText = validMealTypeText.toLowerCase();
        if (validMealTypeText.contains("breakfast"))
            return CreateRecipeModel.MealType.Breakfast;
        else if (validMealTypeText.contains("lunch"))
            return CreateRecipeModel.MealType.Lunch;
        else if (validMealTypeText.contains("dinner"))
            return CreateRecipeModel.MealType.Dinner;
        return null;
    }
}

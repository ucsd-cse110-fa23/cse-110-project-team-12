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

    public static String parseMealType(String validMealTypeText) {
        validMealTypeText = validMealTypeText.toLowerCase();
        if (validMealTypeText.contains("breakfast"))
            return "breakfast";
        else if (validMealTypeText.contains("lunch"))
            return "lunch";
        else if (validMealTypeText.contains("dinner"))
            return "dinner";
        return "";
    }
}

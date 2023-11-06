package edu.ucsd.cse110.api;

import java.io.File;

public class VoicePromptMock implements VoicePromptInterface {
    public enum PromptType {
        MealType,
        IngredientsList,
    }
    PromptType pt;

    public VoicePromptMock(PromptType pt) {
        this.pt = pt;
    }

    public void startRecording() {
    }

    public File stopRecording() {
        System.out.println(System.getProperty("user.dir"));
        if (pt == PromptType.MealType)
            return new File("./src/main/java/edu/ucsd/cse110/api/assets/iwantlunch.wav");
        if (pt == PromptType.IngredientsList)
            return new File("./src/main/java/edu/ucsd/cse110/api//assets/ingredients.wav");
        return null;
    }
}

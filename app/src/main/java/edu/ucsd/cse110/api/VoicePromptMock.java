package edu.ucsd.cse110.api;

import java.io.File;
import java.util.List;

public class VoicePromptMock implements VoicePromptInterface {
    public enum PromptType {
        MealType,
        InvalidNotMealType,
        InvalidMultipleMealType,
        IngredientsList,
    }
    List<PromptType> pt;
    int promptIdx;

    public VoicePromptMock(List<PromptType> pt) {
        this.pt = pt;
        this.promptIdx = 0;
    }

    public void startRecording() {
    }

    public File stopRecording() {
        System.out.println(System.getProperty("user.dir"));
        File f = null;
        if (pt.get(promptIdx) == PromptType.MealType)
            f = new File("./src/main/java/edu/ucsd/cse110/api/assets/iwantlunch.wav");
        else if(pt.get(promptIdx) == PromptType.InvalidNotMealType)
            f = new File("./src/main/java/edu/ucsd/cse110/api/assets/helloiwouldlikesupperplease.wav");
        else if(pt.get(promptIdx) == PromptType.InvalidMultipleMealType)
            f = new File("./src/main/java/edu/ucsd/cse110/api/assets/iwantlunchanddinner.wav");
        else if (pt.get(promptIdx) == PromptType.IngredientsList)
            f = new File("./src/main/java/edu/ucsd/cse110/api/assets/ingredients.wav");
        promptIdx++;
        return f;
    }
}

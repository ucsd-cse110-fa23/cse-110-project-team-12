package edu.ucsd.cse110.api;

import java.io.File;

import edu.ucsd.cse110.api.AppManager.UpdateType;
import edu.ucsd.cse110.api.AppManager.ViewType;
import edu.ucsd.cse110.client.Recipe;

public class CreateRecipeManager extends ManagerInterface{
    public enum PageType {
        MealTypeInput,
        IngredientsInput,
        DetailedView,
    };
    public enum MealType {
        Breakfast,
        Lunch,
        Dinner,
        Invalid,
    };
    
    // Which page we are on.
    private PageType page;
    private boolean isRecording;

    private MealType selectedMealType;
    private String selectedIngredients;
    private Recipe generatedRecipe;

    private VoicePromptInterface voicePrompt;
    private WhisperInterface whisper;
    private ChatGPTInterface chatGPT;

    private AppManager appManager;

    public CreateRecipeManager(VoicePromptInterface voicePrompt, WhisperInterface whisper, ChatGPTInterface chatGPT) {
        super();
        this.voicePrompt = voicePrompt;
        this.whisper = whisper;
        this.chatGPT = chatGPT;

        page = PageType.MealTypeInput;
        isRecording = false;
        generatedRecipe = new Recipe();
    }

    public void addAppManager(AppManager appManager){
        this.appManager = appManager;
    }


    public boolean getIsRecording() {
        return isRecording;
    }

    public PageType getPage() {
        return page;
    }

    public void createNewChatGPTRecipe() {
        String mealTypeString = "";
        if (selectedMealType == MealType.Breakfast)
            mealTypeString = "Breakfast";
        if (selectedMealType == MealType.Lunch)
            mealTypeString = "Lunch";
        if (selectedMealType == MealType.Dinner) 
            mealTypeString = "Dinner";

        try {
            String[] gptResult = chatGPT.promptGPT(mealTypeString, selectedIngredients);
            generatedRecipe = new Recipe(gptResult[0], gptResult[1]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Recipe getRecipe() {
        return generatedRecipe;
    }

    public MealType getMealType() {
        return selectedMealType;
    }

    public String getSelectedIngredients(){
        return selectedIngredients;
    }

    public void startRecording() {
        if (isRecording)
            return;
        isRecording = true;

        voicePrompt.startRecording();
    }

    public void stopRecording() {
        if (!isRecording)
            return;
        isRecording = false;

        File recordingFile = voicePrompt.stopRecording();
        try {
            String transcript = whisper.transcribe(recordingFile);
            processTranscript(transcript);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processTranscript(String transcript) {
        if (page == PageType.MealTypeInput) {
            if (MealTypeValidator.validateMealType(transcript)) {
                selectedMealType = MealTypeValidator.parseMealType(transcript);
                goToNextPage();
            }
            else {
                selectedMealType = MealType.Invalid;
            }
        }
        else if (page == PageType.IngredientsInput) {
            selectedIngredients = transcript;
            createNewChatGPTRecipe();
            goToNextPage();
        }
    }

    public void goToPreviousPage() {
        // Don't work if recording.
        if (isRecording)
            return;

        if (page == PageType.MealTypeInput);
        else if (page == PageType.IngredientsInput)
            page = PageType.MealTypeInput;
        else if (page == PageType.DetailedView)
            page = PageType.IngredientsInput;
    }

    public void goToNextPage() {
        // Don't work if recording;
        if (isRecording)
            return;

        if (page == PageType.MealTypeInput)
            page = PageType.IngredientsInput;
        else if (page == PageType.IngredientsInput)
            page = PageType.DetailedView;
        else if (page == PageType.DetailedView);
    }

    public void closeView(){
        appManager.updateView(ViewType.CreateRecipeView, UpdateType.Close);
    }
}

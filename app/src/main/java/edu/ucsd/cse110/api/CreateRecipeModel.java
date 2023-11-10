package edu.ucsd.cse110.api;

import java.io.File;
import java.util.Map;

import edu.ucsd.cse110.client.Recipe;

public class CreateRecipeModel implements ModelInterface {
    public enum PageType {
        MealTypeInput,
        IngredientsInput
    };
    public enum MealType {
        Breakfast,
        Lunch,
        Dinner,
    };
    private Controller controller;

    // Which page we are on.
    private PageType currentPage;
    private boolean isRecording;

    private MealType selectedMealType;
    private String selectedIngredients;
    private Recipe generatedRecipe;

    private VoicePromptInterface voicePrompt;
    private WhisperInterface whisper;
    private ChatGPTInterface chatGPT;

    public CreateRecipeModel(Controller c, VoicePromptInterface voicePrompt, WhisperInterface whisper, ChatGPTInterface chatGPT) {
        controller = c;
        this.voicePrompt = voicePrompt;
        this.whisper = whisper;
        this.chatGPT = chatGPT;

        currentPage = PageType.MealTypeInput;
        isRecording = false;
        generatedRecipe = new Recipe();
    }

    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.Type.CreateRecipeBackButton && !isRecording) {
            if (currentPage == PageType.MealTypeInput) {
                controller.receiveMessageFromModel(new Message(Message.Type.ButtonCloseCreateRecipe));
            }
            else if (currentPage == PageType.IngredientsInput) {
                currentPage = PageType.MealTypeInput;
                controller.receiveMessageFromModel(
                    new Message(Message.Type.CreateRecipeGotoPage,
                    Map.ofEntries(Map.entry(Message.Key.PageType, currentPage)))
                );
            }
        }
        else if (m.getMessageType() == Message.Type.ButtonRecord) {
            handleRecord();
        }
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
    
    private void handleRecord() {
        if (isRecording) {
            File recordingFile = voicePrompt.stopRecording();
            try {
                String transcript = whisper.transcribe(recordingFile);
                processTranscript(transcript);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            controller.receiveMessageFromModel(new Message(Message.Type.StopRecording));
            isRecording = false;
        }
        else {
            voicePrompt.startRecording();
            controller.receiveMessageFromModel(new Message(Message.Type.StartRecording));
            isRecording = true;
        }
    }

    public void processTranscript(String transcript) {
        if (currentPage == PageType.MealTypeInput) {
            if (MealTypeValidator.validateMealType(transcript)) {
                currentPage = PageType.IngredientsInput;
                selectedMealType = MealTypeValidator.parseMealType(transcript);
                controller.receiveMessageFromModel(
                    new Message(Message.Type.CreateRecipeGotoPage,
                    Map.ofEntries(Map.entry(Message.Key.PageType, currentPage),
                                  Map.entry(Message.Key.MealType, selectedMealType)))
                );
            }
            else {
                controller.receiveMessageFromModel(new Message(Message.Type.CreateRecipeInvalidMealType));
            }
        }
        else if (currentPage == PageType.IngredientsInput) {
            selectedIngredients = transcript;
            createNewChatGPTRecipe();
            controller.receiveMessageFromModel(
                new Message(Message.Type.FinishedCreatingRecipe,
                Map.ofEntries(Map.entry(Message.Key.RecipeTitle, generatedRecipe.getName()),
                                Map.entry(Message.Key.RecipeBody, generatedRecipe.getInformation())))
            );
        }
    }
}

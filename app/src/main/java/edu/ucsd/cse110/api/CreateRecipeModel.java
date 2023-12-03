package edu.ucsd.cse110.api;

import java.io.File;
import java.util.Map;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.services.Utils;
import javafx.application.Platform;

public class CreateRecipeModel implements ModelInterface {
    public enum PageType {
        MealTypeInput,
        IngredientsInput,
        Waiting,
    };

    public enum MealType {
        None,
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
    private RecipeSchema generatedRecipe;

    public CreateRecipeModel(Controller c) {
        controller = c;

        currentPage = PageType.MealTypeInput;
        selectedMealType = MealType.None;
        isRecording = false;
        generatedRecipe = new RecipeSchema();
    }

    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.CreateRecipeView.BackButton && !isRecording) {
            if (currentPage == PageType.MealTypeInput) {
                selectedMealType = MealType.None;
                selectedIngredients = null;
                controller.receiveMessageFromUI(new Message(Message.CreateRecipeModel.CloseCreateRecipeView));
            } else if (currentPage == PageType.IngredientsInput) {
                currentPage = PageType.MealTypeInput;
                selectedMealType = MealType.None;
                controller.receiveMessageFromModel(
                        new Message(Message.CreateRecipeModel.CreateRecipeGotoPage,
                                Map.ofEntries(Map.entry("PageType", currentPage.name()),
                                        Map.entry("MealType", selectedMealType.name()))));
            }
        } else if (m.getMessageType() == Message.CreateRecipeView.RecordButton) {
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
            generatedRecipe.mealType = mealTypeString;
            generatedRecipe.ingredients = selectedIngredients;
            String urlString = Controller.serverUrl + "/chatgpt";
            ServerResponse response = HttpUtils.makeHttpRequest(urlString, "POST", Utils.marshalJson(generatedRecipe));
            
            if (response.getStatusCode() == 200)
                generatedRecipe = Utils.unmarshalJson(response.getResponseBody(), RecipeSchema.class);
            else
                System.out.println("Failed to generate chatgpt recipe");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRecord() {
        if (isRecording) {
            controller.receiveMessageFromModel(new Message(Message.CreateRecipeModel.StopRecording));
            File recordingFile = controller.voicePrompt.stopRecording();
            if (controller.useUI) {
                new Thread(() -> {
                    try {
                        String transcript = controller.whisper.transcribe(recordingFile);
                        Platform.runLater(() -> {
                            processTranscript(transcript);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }).start();
                isRecording = false;
            } else {
                try {
                    String transcript = controller.whisper.transcribe(recordingFile);
                    processTranscript(transcript);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isRecording = false;
            }
        } else {
            controller.receiveMessageFromModel(new Message(Message.CreateRecipeModel.StartRecording));
            controller.voicePrompt.startRecording();
            isRecording = true;
        }
    }

    public void processTranscript(String transcript) {
        if (currentPage == PageType.MealTypeInput) {
            if (MealTypeValidator.validateMealType(transcript)) {
                currentPage = PageType.IngredientsInput;
                selectedMealType = MealTypeValidator.parseMealType(transcript);
                controller.receiveMessageFromModel(
                        new Message(Message.CreateRecipeModel.CreateRecipeGotoPage,
                                Map.ofEntries(Map.entry("PageType", currentPage.name()),
                                        Map.entry("MealType", selectedMealType.name()))));
            } else {
                controller.receiveMessageFromModel(new Message(Message.CreateRecipeModel.CreateRecipeInvalidMealType));
            }
        } else if (currentPage == PageType.IngredientsInput) {
            currentPage = PageType.Waiting;
            controller.receiveMessageFromModel(new Message(Message.CreateRecipeModel.CreateRecipeGotoPage,
                    Map.ofEntries(Map.entry("PageType", currentPage.name()),
                            Map.entry("MealType", selectedMealType.name()))));
            selectedIngredients = transcript;
            if (controller.useUI) {
                new Thread(() -> {
                    createNewChatGPTRecipe();
                    Platform.runLater(() -> {
                        controller.receiveMessageFromModel(
                                new Message(Message.CreateRecipeModel.CloseCreateRecipeView));
                        controller.receiveMessageFromModel(
                                new Message(Message.CreateRecipeModel.StartRecipeDetailedView));
                        controller.receiveMessageFromModel(
                                new Message(Message.CreateRecipeModel.SendRecipe,
                                        Map.ofEntries(Map.entry("Recipe", generatedRecipe))));
                    });
                }).start();
            } else {
                createNewChatGPTRecipe();
                controller.receiveMessageFromModel(
                        new Message(Message.CreateRecipeModel.CloseCreateRecipeView));
                controller.receiveMessageFromModel(new Message(Message.CreateRecipeModel.StartRecipeDetailedView));
                controller.receiveMessageFromModel(
                        new Message(Message.CreateRecipeModel.SendRecipe,
                                Map.ofEntries(Map.entry("Recipe", generatedRecipe))));
            }
        }
    }

    @Override
    public Object getState() {
        return this;
    }

    public PageType getCurrentPage() {
        return currentPage;
    }

    public MealType getSelectedMealType() {
        return selectedMealType;
    }

    public String getSelectedIngredients() {
        return selectedIngredients;
    }

    public boolean getIsRecording() {
        return isRecording;
    }
}

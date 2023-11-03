package edu.ucsd.cse110.client;

import java.io.File;

import api.ChatGPT;
import api.ChatGPTInterface;
import api.MockChatGPT;
import api.MockWhisper;
import api.WhisperInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;


public class CreateRecipe extends VBox{
	private int page;
	private boolean recording;
    private Button backButton;
	private Button recordButton;
    private String selectedMealType;
    private String selectedIngredients;
	private Recipe generatedRecipe;

	private WhisperInterface whisper;
	private ChatGPTInterface chatGPT;
	
    public CreateRecipe() {
        this.setPrefSize(400, 400);
        this.setStyle("-fx-background-color: #000;");
        this.page = 0;

		this.whisper = new MockWhisper();
		this.chatGPT = new MockChatGPT(0);

        this.backButton = new Button("Back");
        this.backButton.setOnAction(
            e -> {
                if (this.page == 0) {
                    Stage stage = (Stage) this.backButton.getScene().getWindow();
                    stage.close();
                } else {
                    this.page--;
                    this.update();
                }
            }
        );

		this.recordButton = new Button("Record");
        this.recordButton.setOnAction(
            e -> {
				if (page == 0) {
					if (!this.recording) {
						this.recording = true;
						VoicePrompt.startRecording();
					}
					else {
						this.recording = false;
						File mealTypeRecording = VoicePrompt.stopRecording();
						this.selectedMealType = this.whisper.transcribe(mealTypeRecording);
					}
				}
				if (page == 1) {
					if (!this.recording) {
						this.recording = true;
						VoicePrompt.startRecording();
					}
					else {
						this.recording = false;
						File ingredientsRecording = VoicePrompt.stopRecording();
						this.selectedIngredients = this.whisper.transcribe(ingredientsRecording);
						String[] gptResult = new String[2];
						try {
							gptResult = this.chatGPT.promptGPT(this.selectedMealType, this.selectedIngredients);
						}
						catch (Exception exception) {
							System.err.println(exception);
						}
						this.generatedRecipe = new Recipe(gptResult[0], gptResult[1]);
					}
				}
            }
        );
    }

    public void update() {
        this.getChildren().clear();
        if (this.page == 0) {
            Text mealOptionHeading = new Text(20, 10, "Select Meal Type");
            Text mealOptions = new Text(16, 10, "Breakfast\nLunch\nDinner");

            this.getChildren().clear();
            this.getChildren().addAll(this.backButton, mealOptionHeading, mealOptions);
        } else if (this.page == 1) {
            Text mealType = new Text(20, 10, "Meal Type: \n" + this.selectedMealType);
            Text ingredientPrompt = new Text(20, 10, "What is in your pantry?");

            this.getChildren().clear();
            this.getChildren().addAll(this.backButton, mealType, ingredientPrompt);
        } else {
            // this.getChildren().add(generatedRecipe);
        }
    }

    public void nextPage() {
        this.page++;
        this.update();
    }

}

class CreateRecipeButton extends Button {
    private Button createRecipeButton;
    
    public CreateRecipeButton() {
        createRecipeButton = new Button("+");
        createRecipeButton.setOnAction(
            e -> {
                Scene scene = new Scene(new CreateRecipe(), 400, 400);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
        );
    }
}

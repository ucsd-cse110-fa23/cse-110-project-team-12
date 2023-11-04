package edu.ucsd.cse110.client;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.io.File;

import api.ChatGPTInterface;
import api.MockChatGPT;
import api.MockWhisper;
import api.WhisperInterface;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;

// takes the user through the recipe creation process
public class CreateRecipe extends StackPane {
	private AppFrame appFrame; // helps call stopCreating when the creation ends

	private int page;
	private boolean recording;
	private VBox content;
	private Label backArrow;
    private Button backButton;
	private Button recordButton;
	private RecordButtonModule recordButtonModule; // handles record button UI elements

	private WhisperInterface whisper;
	private ChatGPTInterface chatGPT;
	private String selectedMealType;
	private String selectedIngredients;
	private Recipe generatedRecipe;

	
    public CreateRecipe(AppFrame appFrame) {
		this.appFrame = appFrame;
        this.setPrefSize(280, 290);
		this.setMaxHeight(290);
		this.setAlignment(Pos.TOP_LEFT);
        this.setStyle("-fx-background-color: rgba(91, 91, 91, 0.95);");
		DropShadow dropShadow = new DropShadow(5, Color.BLACK);
		this.setEffect(dropShadow);
        
		whisper = new MockWhisper();
		chatGPT = new MockChatGPT(1);

		content = new VBox();
		content.setPrefSize(280, 290);
		content.setAlignment(Pos.TOP_CENTER);
		content.setStyle("-fx-background-color: transparent;");

		backArrow = new Label("⟨");
		backArrow.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		backArrow.setFont(new Font("Helvetica Bold", 20));
		backArrow.setPadding(new Insets(5, 0, 0, 5));
	
		backButton = new Button();
		backButton.setPrefSize(20, 30);
		backButton.setStyle("-fx-background-color: transparent;");
		
		recordButton = new Button("");
		recordButton.setShape(new Circle(31));
		recordButton.setMinSize(31, 31);
		recordButton.setStyle("-fx-background-color: #BC8B8B; -fx-border-width: 0;");
		
		this.getChildren().addAll(content, backArrow, backButton);
		addListeners();
		updateUI();
    }

	private void addListeners() {
		backButton.setOnAction(
            e -> {
				if (!recording) {
					if (page == 0) {
						appFrame.stopCreating();
					} 
					else {
						page--;
						updateUI();
					}
				}
            }
        );

		recordButton.setOnAction(
            e -> {
				if (!recording) {
					recording = true;
					recordButtonModule.switchColor();
					VoicePrompt.startRecording();
				}
				else {
					recording = false;
					recordButtonModule.switchColor();

					// page 0 is the meal type selection
					if (page == 0) {
						File mealTypeRecording = VoicePrompt.stopRecording();
						selectedMealType = whisper.transcribe(mealTypeRecording);
					}

					// page 1 is the ingredient selection
					if (page == 1) {
						File ingredientsRecording = VoicePrompt.stopRecording();
						selectedIngredients = whisper.transcribe(ingredientsRecording);
						
						String[] gptResult = new String[2];
						try {
							gptResult = chatGPT.promptGPT(selectedMealType, selectedIngredients);
						}
						catch (Exception exception) {
							System.err.println(exception);
						}
						generatedRecipe = new Recipe(gptResult[0], gptResult[1]);
					}

					page++;
        			updateUI();
				}
            }
        );
	}

    private void updateUI() {
        content.getChildren().clear();

		// meal selection page
        if (this.page == 0) {
            Label mealOptionHeading = new Label("Select Meal Type:");
			mealOptionHeading.setFont(new Font("Helvetica Bold", 20));
			mealOptionHeading.setPadding(new Insets(70, 0, 0, 0));
			mealOptionHeading.setTextFill(Color.WHITE);

			Label mealOptions = new Label("Breakfast\nLunch\nDinner");
			mealOptions.setFont(new Font("Helvetica Bold", 15));
			mealOptions.setPadding(new Insets(10, 0, 0, 0));
			mealOptions.setTextFill(Color.WHITE);

			recordButtonModule = new RecordButtonModule(recordButton, 55);
            content.getChildren().addAll(mealOptionHeading, mealOptions, recordButtonModule);
        } 

		// ingredient selection page
		else if (this.page == 1) {
            Label mealTypeHeading = new Label("Meal Type:");
			mealTypeHeading.setFont(new Font("Helvetica Bold", 20));
			mealTypeHeading.setPadding(new Insets(70, 0, 0, 0));
			mealTypeHeading.setTextFill(Color.WHITE);

			Label mealTypeText = new Label(selectedMealType);
			mealTypeText.setFont(new Font("Helvetica Bold", 15));
			mealTypeText.setPadding(new Insets(10, 0, 0, 0));
			mealTypeText.setTextFill(Color.WHITE);

			Label pantryPrompt = new Label("What's in your pantry?");
			pantryPrompt.setFont(new Font("Helvetica Bold", 20));
			pantryPrompt.setPadding(new Insets(55, 0, 0, 0));
			pantryPrompt.setTextFill(Color.WHITE);

			recordButtonModule.setTopPadding(13);
            content.getChildren().addAll(mealTypeHeading, mealTypeText, pantryPrompt, recordButtonModule);
        } 

		// generated recipe view
		else {
			Label recipeTitle = new Label(generatedRecipe.getName());
			recipeTitle.setFont(new Font("Helvetica Bold", 20));
			recipeTitle.setPadding(new Insets(70, 0, 0, 0));
			recipeTitle.setTextFill(Color.WHITE);
			content.getChildren().addAll(recipeTitle);
        }
    }
}

// handles the UI for the record button
class RecordButtonModule extends StackPane {
	private Button recordButton;
	private Circle background;
	private Circle midground;
	private boolean recording;

	public RecordButtonModule(Button recordButton, int topPadding) {
		background = new Circle(28.5);
		background.setFill(Color.web("#BC8B8B"));
		midground = new Circle(24.5);
		midground.setFill(Color.web("#5B5B5B"));

		this.recordButton = recordButton;
		this.setPadding(new Insets(topPadding, 0, 0, 0));
		this.getChildren().addAll(background, midground, recordButton);
	}

	public void switchColor() {
		if (!recording) {
			recording = true;
			midground.setFill(Color.web("#BC8B8B"));
			recordButton.setStyle("-fx-background-color: #5B5B5B;");
		}
		else {
			recording = false;
			midground.setFill(Color.web("#5B5B5B"));
			recordButton.setStyle("-fx-background-color: #BC8B8B;");
		}
	}

	public void setTopPadding(int topPadding) {
		this.setPadding(new Insets(topPadding, 0, 0, 0));
	}
}
package edu.ucsd.cse110.client;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import edu.ucsd.cse110.api.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;

// takes the user through the recipe creation process
public class CreateRecipe extends StackPane {
	private AppFrame appFrame; // helps call stopCreating when the creation ends
	private Spacer spacer;

	private CreateRecipeManager createRecipeManager;

	private VBox content;
	private Label backArrow;
    private Button backButton;
	private Button recordButton;
	private RecordButtonModule recordButtonModule; // handles record button UI elements
	
    public CreateRecipe(AppFrame appFrame, CreateRecipeManager createRecipeManager) {
		this.appFrame = appFrame;
		this.createRecipeManager = createRecipeManager;

		DropShadow dropShadow = new DropShadow(5, Color.BLACK);
		this.setEffect(dropShadow);
		this.setId("create-recipe");

		content = new VBox();
		content.setId("content");

		spacer = new Spacer(this, new Insets(30, 0, 0, 0), Pos.TOP_CENTER);

		backArrow = new Label("⟨");
		backArrow.setFont(new Font("Helvetica Bold", 20));
		backArrow.setId("back-arrow");
	
		backButton = new Button();
		backButton.setId("back-button");
		
		recordButton = new Button();
		recordButton.setShape(new Circle(31));
		recordButton.setId("record-button");
		
		this.getChildren().addAll(content, backArrow, backButton);
		addListeners();
		updateUI();
    }

	private void addListeners() {
		backButton.setOnAction(
            e -> {
				if (createRecipeManager.getPage() == CreateRecipeManager.PageType.MealTypeInput) {
					appFrame.stopCreating();
				}
				else {
					createRecipeManager.goToPreviousPage();
					updateUI();
				}
            }
        );

		recordButton.setOnAction(
			e -> {
				if (createRecipeManager.getIsRecording() == false)
					createRecipeManager.startRecording();
				else
					createRecipeManager.stopRecording();
				updateUI();	
			}
        );
	}

    private void updateUI() {
        content.getChildren().clear();

		// meal selection page
        if (createRecipeManager.getPage() == CreateRecipeManager.PageType.MealTypeInput) {
            Label mealOptionsHeading = new Label("Select Meal Type:");
			mealOptionsHeading.setFont(new Font("Helvetica Bold", 20));
			mealOptionsHeading.setId("meal-options-heading");

			Label mealOptions = new Label("Breakfast\nLunch\nDinner");
			mealOptions.setFont(new Font("Helvetica Bold", 15));
			mealOptions.setId("meal-options");

			if (createRecipeManager.getMealType() == CreateRecipeManager.MealType.Invalid) {
				Label invalidMealTypeWarning = new Label("Invalid Meal Type");
				invalidMealTypeWarning.setFont(new Font("Helvetica Bold", 18));
				invalidMealTypeWarning.setId("invalid-meal-type-warning");
				
				recordButtonModule = new RecordButtonModule(recordButton, 16, createRecipeManager.getIsRecording());
            	content.getChildren().addAll(mealOptionsHeading, mealOptions, invalidMealTypeWarning, recordButtonModule);
			}
			else {
				recordButtonModule = new RecordButtonModule(recordButton, 55, createRecipeManager.getIsRecording());
				content.getChildren().addAll(mealOptionsHeading, mealOptions, recordButtonModule);
			}
        } 

		// ingredient selection page
		else if (createRecipeManager.getPage() == CreateRecipeManager.PageType.IngredientsInput) {
            Label mealTypeHeading = new Label("Meal Type:");
			mealTypeHeading.setFont(new Font("Helvetica Bold", 20));
			mealTypeHeading.setId("meal-type-heading");

			Label mealTypeText = new Label(createRecipeManager.getMealType().name());
			mealTypeText.setFont(new Font("Helvetica Bold", 15));
			mealTypeText.setId("meal-type-text");

			Label pantryPrompt = new Label("What's in your pantry?");
			pantryPrompt.setFont(new Font("Helvetica Bold", 20));
			pantryPrompt.setId("pantry-prompt");

			recordButtonModule = new RecordButtonModule(recordButton, 13, createRecipeManager.getIsRecording());
            content.getChildren().addAll(mealTypeHeading, mealTypeText, pantryPrompt, recordButtonModule);
        } 

		// generated recipe view
		else if (createRecipeManager.getPage() == CreateRecipeManager.PageType.DetailedView) {
			Label recipeTitle = new Label(createRecipeManager.getRecipe().getName());
			recipeTitle.setFont(new Font("Helvetica Bold", 13));
			recipeTitle.setId("recipe-title");

			Label information = new Label(createRecipeManager.getRecipe().getInformation());
			information.setFont(new Font("Helvetica", 11));
			information.setId("information");
			
			ScrollPane scrollPane = new ScrollPane(information);

			Button cancelButton = new Button("Cancel");	
			cancelButton.setFont(new Font("Helvetica Bold", 10));
			cancelButton.setId("cancel-button");
			cancelButton.setOnAction(
				e -> {
					appFrame.stopCreating();
				}
			);

			Button saveButton = new Button("Save");
			saveButton.setFont(new Font("Helvetica Bold", 10));
			saveButton.setId("save-button");
			saveButton.setOnAction(
				e -> {
					appFrame.stopCreating();
				}
			);

			HBox buttonBox = new HBox(cancelButton, saveButton);
			buttonBox.setId("button-box");

			content.getChildren().addAll(recipeTitle, scrollPane, buttonBox);
			this.getChildren().removeAll(backArrow, backButton);
        }
    }

	public Spacer getSpacer() {
		return spacer;
	}
}

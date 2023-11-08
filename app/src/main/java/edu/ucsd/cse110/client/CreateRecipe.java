package edu.ucsd.cse110.client;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;

import edu.ucsd.cse110.api.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// takes the user through the recipe creation process
public class CreateRecipe extends StackPane {
	private AppFrame appFrame; // helps call stopCreating when the creation ends
	private Spacer spacer;

	private CreateRecipeManager createRecipeManager;

	private VBox content;
	private HBox backArrowBox;
    private Button backButton;
	private Button recordButton;
	private RecordButtonModule recordButtonModule;
	private MealOptionsModule mealOptionsModule;
	
    public CreateRecipe(AppFrame appFrame, CreateRecipeManager createRecipeManager) {
		this.appFrame = appFrame;
		this.createRecipeManager = createRecipeManager;
		this.setId("create-recipe");

		spacer = new Spacer(this, new Insets(35, 0, 0, 0), Pos.TOP_CENTER);

		content = new VBox();
		content.setId("content");

		Image backArrow = null;
		try {
			backArrow = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/resources/backArrow.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		ImageView backArrowView = new ImageView(backArrow);
		backArrowView.setFitWidth(20);
		backArrowView.setFitHeight(20);
		
		backArrowBox = new HBox(backArrowView);
		backArrowBox.setId("back-arrow-box");
	
		backButton = new Button();
		backButton.setId("back-button");
		
		recordButton = new Button();
		recordButton.setId("record-button");

		this.getChildren().addAll(content, backArrowBox, backButton);
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
			mealOptionsHeading.setId("meal-options-heading");
			
			mealOptionsModule = new MealOptionsModule();
            
			if (createRecipeManager.getMealType() == CreateRecipeManager.MealType.Invalid) {
				Label invalidMealTypeWarning = new Label("Invalid Meal Type");
				invalidMealTypeWarning.setId("invalid-meal-type-warning");
			
				recordButtonModule = new RecordButtonModule(recordButton, 8, createRecipeManager.getIsRecording());
				content.getChildren().addAll(mealOptionsHeading, mealOptionsModule, invalidMealTypeWarning, recordButtonModule);
			}
			else {
				recordButtonModule = new RecordButtonModule(recordButton, 51, createRecipeManager.getIsRecording());
				content.getChildren().addAll(mealOptionsHeading, mealOptionsModule, recordButtonModule);
			}
		} 

		// ingredient selection page
		else if (createRecipeManager.getPage() == CreateRecipeManager.PageType.IngredientsInput) {
            Label mealTypeHeading = new Label("Meal Type:");
			mealTypeHeading.setId("meal-type-heading");

			HBox mealTypeContainer = new HBox(mealOptionsModule.getMealTypeBox(createRecipeManager.getMealType().name()));
			mealTypeContainer.setId("meal-type-container");

			Label pantryPrompt = new Label("What's in your pantry?");
			pantryPrompt.setId("pantry-prompt");

			recordButtonModule = new RecordButtonModule(recordButton, 11, createRecipeManager.getIsRecording());
            content.getChildren().addAll(mealTypeHeading, mealTypeContainer, pantryPrompt, recordButtonModule);
        } 

		// // generated recipe loading screen
		// else if (this.page == -1) {
		// 	Label generatingRecipeText = new Label("Generating Recipe...");
		// 	generatingRecipeText.setId("generating-recipe-text");
		// 	content.getChildren().add(generatingRecipeText);
		// 	this.getChildren().removeAll(backArrowBox, backButton);
		// }

		// generated recipe view
		else if (createRecipeManager.getPage() == CreateRecipeManager.PageType.DetailedView) {
			Text recipeTitle = new Text(createRecipeManager.getRecipe().getName());
			setTitleFont(recipeTitle, 19);
			recipeTitle.setId("recipe-title");

			Label information = new Label(createRecipeManager.getRecipe().getInformation());
			information.setId("information");
			
			ScrollPane scrollPane = new ScrollPane(information);

			Button cancelButton = new Button("Cancel");	
			cancelButton.setId("cancel-button");
			cancelButton.setOnAction(
				e -> {
					appFrame.stopCreating();
				}
			);

			Button saveButton = new Button("Save");
			saveButton.setId("save-button");
			saveButton.setOnAction(
				e -> {
					appFrame.stopCreating();
				}
			);

			HBox buttonBox = new HBox(cancelButton, saveButton);
			buttonBox.setId("button-box");

			content.getChildren().addAll(recipeTitle, scrollPane, buttonBox);
			this.getChildren().removeAll(backArrowBox, backButton);
        }
    }

	public Spacer getSpacer() {
		return spacer;
	}

	private void setTitleFont(Text title, double size){
		title.setFont(new Font("Helvetica Bold", size));
		if (size == 11) { return; }

        double width = title.getLayoutBounds().getWidth();
        if (width > 266){
            size -= 0.25;
            setTitleFont(title, size);
        }
    }
}

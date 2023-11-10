package edu.ucsd.cse110.client;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;

import edu.ucsd.cse110.api.*;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// takes the user through the recipe creation process
public class CreateRecipeView extends StackPane implements UIInterface {
	private Controller controller;

	private VBox content;
	private HBox backArrowBox;
    private Button backButton;
	private Button recordButton;
	private RecordButtonModule recordButtonModule;
	private MealOptionsModule mealOptionsModule;
	private Label mealOptionsHeading;

	private Label mealTypeHeading;
	private HBox mealTypeContainer;
	private Label pantryPrompt;
	
    public CreateRecipeView(Controller c) {
		controller = c;

		this.setId("create-recipe");

		content = new VBox();
		content.setId("content");

		Image backArrow = null;
		try {
			backArrow = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/backArrow.png"));
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
		
		mealOptionsHeading = new Label();
		mealTypeHeading = new Label();
		mealOptionsModule = new MealOptionsModule();
		recordButtonModule = new RecordButtonModule(recordButton, 0);
		mealTypeContainer = new HBox();
		pantryPrompt = new Label();

		addChild(content);
		addChild(backArrowBox);
		addChild(backButton);
		addListeners();
		setInputView(CreateRecipeModel.PageType.MealTypeInput, null);
    }

	public void receiveMessage(Message m) {
		if (m.getMessageType() == Message.Type.CreateRecipeGotoPage) {
			CreateRecipeModel.PageType pageType = (CreateRecipeModel.PageType) m.getKey(Message.Key.PageType);
			CreateRecipeModel.MealType mealType = (CreateRecipeModel.MealType) m.getKey(Message.Key.MealType);
			setInputView(pageType, mealType);
		}
		else if (m.getMessageType() == Message.Type.CreateRecipeInvalidMealType) {
			setInvalidMealType();
		}
		else if (m.getMessageType() == Message.Type.StartRecording) {
			recordButtonModule.setRecording(true);
		}
		else if (m.getMessageType() == Message.Type.StopRecording) {
			recordButtonModule.setRecording(false);
		}
	}

	public void addChild(Node ui) {
		getChildren().add(ui);
	}

	public void removeChild(Node ui) {
		getChildren().remove(ui);
	}

	private void addListeners() {
		backButton.setOnAction(
            e -> {
				controller.receiveMessageFromUI(new Message(Message.Type.CreateRecipeBackButton));
            }
        );

		recordButton.setOnAction(
			e -> {
				controller.receiveMessageFromUI(new Message(Message.Type.ButtonRecord));
			}
        );
	}

	private void setInvalidMealType() {
		content.getChildren().clear();
		
		Label invalidMealTypeWarning = new Label("Invalid Meal Type");
		invalidMealTypeWarning.setId("invalid-meal-type-warning");
		
		// Need to create a new one or else when delete mealTypeBox, it will be deleted from module as well.
		mealOptionsModule = new MealOptionsModule();
		content.getChildren().addAll(mealOptionsHeading, mealOptionsModule, invalidMealTypeWarning, recordButtonModule);
	}

	private void setInputView(CreateRecipeModel.PageType pagetype, CreateRecipeModel.MealType mealType) {
		content.getChildren().clear();
		mealOptionsModule = new MealOptionsModule();
		if (pagetype == CreateRecipeModel.PageType.MealTypeInput) {
			mealOptionsHeading.setText("Select Meal Type:");
			mealOptionsHeading.setId("meal-options-heading");
			content.getChildren().addAll(mealOptionsHeading, mealOptionsModule, recordButtonModule);
		}
		else if (pagetype == CreateRecipeModel.PageType.IngredientsInput) {
            mealTypeHeading.setText("Meal Type:");
			mealTypeHeading.setId("meal-type-heading");
			
			mealTypeContainer.getChildren().clear();
			mealTypeContainer.getChildren().add(mealOptionsModule.getMealTypeBox(mealType.name()));
			mealTypeContainer.setId("meal-type-container");

			pantryPrompt.setText("What's in your pantry?");
			pantryPrompt.setId("pantry-prompt");
			content.getChildren().addAll(mealTypeHeading, mealTypeContainer, pantryPrompt, recordButtonModule);
		}
	} 
}

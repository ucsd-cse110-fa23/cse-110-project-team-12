package edu.ucsd.cse110.client;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;

import edu.ucsd.cse110.api.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// takes the user through the recipe creation process
public class CreateRecipeView extends StackPane implements UIInterface {
	private Controller controller;

	private Spacer spacer;
	private VBox content;
	private HBox backArrowBox;
    private Button backButton;
	private Button recordButton;
	private RecordButtonModule recordButtonModule;
	private MealOptionsModule mealOptionsModule;
	private Label mealOptionsHeading;

	private Label mealTypeHeading;
	private Label pantryPrompt;

	private Spacer waitingPage;
	
    public CreateRecipeView(Controller c) {
		controller = c;

		this.setId("create-recipe");

		spacer = new Spacer(this, new Insets(35, 0, 0, 0), Pos.TOP_CENTER);

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

		mealOptionsHeading = new Label("Select Meal Type:");
		mealOptionsHeading.setId("meal-options-heading");

		mealTypeHeading = new Label("Meal Type:");
		mealTypeHeading.setId("meal-type-heading");

		mealOptionsModule = new MealOptionsModule();

		pantryPrompt = new Label("What's in your pantry?");
		pantryPrompt.setId("pantry-prompt");
		recordButtonModule = new RecordButtonModule(recordButton);

		Label waitingPrompt = new Label("Generating Recipe...");
		waitingPrompt.setId("waiting-prompt");
		waitingPage = new Spacer(waitingPrompt, new Insets(60, 0, 0, 0), Pos.TOP_CENTER);

		this.getChildren().addAll(content, backArrowBox, backButton);
		addListeners();
		setInputView("MealTypeInput", null);
    }
	@Override
	public void receiveMessage(Message m) {
		if (m.getMessageType() == Message.CreateRecipeModel.CreateRecipeGotoPage) {
			setInputView((String) m.getKey("PageType"), (String) m.getKey("MealType"));
		}
		else if (m.getMessageType() == Message.CreateRecipeModel.CreateRecipeInvalidMealType) {
			setInvalidMealType();
		}
		else if (m.getMessageType() == Message.CreateRecipeModel.StartRecording) {
			recordButtonModule.setRecording(true);
		}
		else if (m.getMessageType() == Message.CreateRecipeModel.StopRecording) {
			recordButtonModule.setRecording(false);
		}
	}
	@Override
	public void addChild(Node ui) {
		content.getChildren().add(ui);
	}
	@Override
	public void removeChild(Node ui) {
		content.getChildren().remove(ui);
	}

	private void addListeners() {
		backButton.setOnAction(
            e -> {
				controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.BackButton));
            }
        );

		recordButton.setOnAction(
			e -> {
				controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton));
			}
        );
	}

	private void setInvalidMealType() {
		content.getChildren().clear();
		
		Label invalidMealTypeWarning = new Label("Invalid Meal Type");
		invalidMealTypeWarning.setId("invalid-meal-type-warning");
		
		// Need to create a new one or else when delete mealTypeBox, it will be deleted from module as well.
		recordButtonModule.setTopPadding(10);
		content.getChildren().addAll(mealOptionsHeading, mealOptionsModule, invalidMealTypeWarning, recordButtonModule);
	}

	private void setInputView(String pagetype, String mealType) {
		content.getChildren().clear();
		if (pagetype == "MealTypeInput") {
			recordButtonModule.setTopPadding(53);
			content.getChildren().addAll(mealOptionsHeading, mealOptionsModule, recordButtonModule);
		}
		else if (pagetype == "IngredientsInput") {
			HBox mealTypeContainer = new HBox();
			mealTypeContainer.getChildren().add(mealOptionsModule.createMealTypeBox(mealType));
			mealTypeContainer.setId("meal-type-container");

			recordButtonModule.setTopPadding(13);
			content.getChildren().addAll(mealTypeHeading, mealTypeContainer, pantryPrompt, recordButtonModule);
		}else if (pagetype == "Waiting") {
			this.getChildren().removeAll(backArrowBox, backButton);
			addChild(waitingPage);
		}
	}

	@Override
	public Parent getUI() {
		return spacer;
	} 
}

package edu.ucsd.cse110.client;

import java.io.FileInputStream;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.services.Utils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;

public class RecipeDetailedView extends StackPane implements UIInterface {
    private Controller controller;

    private boolean inEditMode;

    private Spacer spacer;
	private VBox content;

    private RecipeSchema recipe;

    private Text recipeTitle;
    private double titleDefaultSize;
    private double titleWidthLimit;
    private HBox recipeTitleSpacer;
	private HBox recipeImageBox;
    private TextArea information;
    private TextArea informationEdit;

    private Button cancelButton;
    private Button refreshButton;
    private Button saveButton;
	private HBox middleButton;
    private HBox unsavedButtonBox;

    private Button deleteButton;
    private Button shareButton;
    private Button editButton;
    private HBox savedButtonBox;


    private HBox backArrowBox;
    private Button backButton;

    private HBox deleteConfirmationPage;
    private ConfirmDeleteButtonModule confirmDeleteModule;
    private Button confirmDeleteButton;

    public RecipeDetailedView(Controller c) {
        controller = c;

        inEditMode = false;

        this.setId("recipe-detailed");

		spacer = new Spacer(this, new Insets(35, 0, 0, 0), Pos.TOP_CENTER);

		content = new VBox();
		content.setId("content");

        //recipe contents
        recipeTitle = new Text();
        recipeTitle.setId("recipe-title");
        titleDefaultSize = 19;
        titleWidthLimit = 220;
        setTitleFont(recipeTitle, titleDefaultSize, titleWidthLimit);

        recipeTitleSpacer = new Spacer(recipeTitle, new Insets(0, 30, 0, 30), Pos.CENTER);
		recipeTitleSpacer.setId("recipe-title-spacer");

		recipeImageBox = new HBox();
		recipeImageBox.setId("recipe-image-box");

        information = new TextArea();
		information.setWrapText(true);
		information.setEditable(false);
		information.setFocusTraversable(false);

        //  UnsavedLayout
        cancelButton = new Button("Cancel");
        cancelButton.setId("cancel-button");

		refreshButton = new Button("Refresh");
        refreshButton.setId("refresh-button");

		middleButton = new HBox(refreshButton);
		middleButton.setId("middle-button");

        saveButton = new Button("Save");
        saveButton.setId("save-button");

        unsavedButtonBox = new HBox(cancelButton, middleButton, saveButton);
        unsavedButtonBox.setId("unsaved-button-box");

        // SavedLayout
        deleteButton = new Button("Delete");	
        deleteButton.setId("cancel-button");

		shareButton = new Button("Share");
        shareButton.setId("share-button");

        editButton = new Button("Edit");
        editButton.setId("edit-button");

        savedButtonBox = new HBox(deleteButton, shareButton, editButton);
        savedButtonBox.setId("saved-button-box");

        // Edit Layout
        informationEdit = new TextArea();
        informationEdit.setWrapText(true);
        informationEdit.setId("information-edit");

        // Back button for SavedLayout
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

        // Delete Confirmation

        Label deleteConfirmationPrompt = new Label("Are you sure you \n want to delete \n this recipe?");
		deleteConfirmationPrompt.setId("delete-confirmation-prompt");
		deleteConfirmationPage = new Spacer(deleteConfirmationPrompt, new Insets(20, 0, 0, 0), Pos.TOP_CENTER);

        confirmDeleteModule = new ConfirmDeleteButtonModule();
        confirmDeleteButton = confirmDeleteModule.getDeleteButton();

        this.getChildren().addAll(content);
        addListeners();

    }

    private void addListeners() {
		cancelButton.setOnAction(
            e -> {
                if (this.inEditMode) this.inEditMode = false;
                controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.CancelButton));
            }
        );

		refreshButton.setOnAction(
            e -> {
                controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.RefreshButton));
            }
        );

        saveButton.setOnAction(
            e -> {
                if (this.inEditMode) {
                    controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.UpdateInformation,
                        "RecipeBody", this.informationEdit.getText()));
                    this.inEditMode = false;
                }
                controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.SaveButton));
            }
        );

        deleteButton.setOnAction(
            e -> {
                controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.DeleteButton));
            }
        );

		shareButton.setOnAction(
            e -> {
                controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.ShareButton));
            }
        );

        editButton.setOnAction(
            e -> {
                controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.EditButton));
                this.inEditMode = true;
            }
        );

        backButton.setOnAction(
            e -> {
                controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.BackButton));   
            }
        );

        confirmDeleteButton.setOnAction(
            e -> {
                controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.ConfirmDeleteButton));
            }
        );
	}

    @Override
    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.RecipeDetailedModel.SetRecipe) {
            recipe = m.getKey("Recipe");
            recipeTitle.setText(recipe.title);
            setTitleFont(recipeTitle, titleDefaultSize, titleWidthLimit);

            removeChild(recipeTitleSpacer);
            addChild(recipeTitleSpacer);

			removeChild(recipeImageBox);
			addChild(recipeImageBox);

            information.setEditable(true);
			information.setFocusTraversable(true);
			information.setText(recipe.description.trim());
			information.setEditable(false);
			information.setFocusTraversable(false);
            setImage(recipe.base64ImageEncoding);
            addChild(information);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.UseUnsavedLayout) {
            addChild(unsavedButtonBox);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.RemoveUnsavedLayout) {
            removeChild(unsavedButtonBox);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.AddBackButton) {
            this.getChildren().addAll(backArrowBox, backButton);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.RemoveBackButton) {
            this.getChildren().removeAll(backArrowBox, backButton);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.UseSavedLayout) {
            addChild(savedButtonBox);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.SaveConfirmation) {
            displaySaveConfirmation();
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.GoToDeleteConfirmationPage) {
            content.getChildren().clear();
            addChild(deleteConfirmationPage);
            addChild(confirmDeleteModule.getSpacer());
        }
        if(m.getMessageType() == Message.RecipeDetailedModel.RemoveDeleteConfirmation) {
            content.getChildren().clear();
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.EditRecipe) {
            removeChild(information);
            removeChild(savedButtonBox);
			removeRefreshButton();

            informationEdit.setText(recipe.description.trim());
            addChild(informationEdit);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.ExitEditRecipe) {
            removeChild(unsavedButtonBox);
            removeChild(informationEdit);

            this.getChildren().removeAll(backArrowBox, backButton);
        }
    }

    private void setImage(String base64ImageEncoding) {
        try {
            BufferedImage bufImg = Utils.decodeBase64ToBufferedImage(base64ImageEncoding);
            Image recipeImage = SwingFXUtils.toFXImage(bufImg, null);
			ImageView recipeImageView = new ImageView(recipeImage);
            recipeImageView.setFitWidth(132);
            recipeImageView.setFitHeight(132);

			Image recipeImageResize = recipeImageView.snapshot(null, null);

			PixelReader pixelReader = recipeImageResize.getPixelReader();
			Image recipeImageCrop = new WritableImage(pixelReader, 0, 29, 132, 74);
    
            recipeImageView = new ImageView(recipeImageCrop);
            recipeImageView.setFitWidth(132);
            recipeImageView.setFitHeight(74);
    
            recipeImageBox.getChildren().clear();
            recipeImageBox.getChildren().add(recipeImageView);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void displaySaveConfirmation() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Recipe Saved");
        alert.setHeaderText(null);
        alert.setContentText("Recipe saved!");

        alert.showAndWait();
    }

    
    @Override
    public void addChild(Node ui) {
        content.getChildren().add(ui);
    }
    @Override
    public void removeChild(Node ui) {
        content.getChildren().remove(ui);
    }
    @Override
    public Parent getUI() {
        return this.spacer;
    }

	private void setTitleFont(Text title, double size, double widthLimit) {
        title.setFont(new Font("Arial Bold", size));
        double width = title.getLayoutBounds().getWidth();
        if (width >= widthLimit) {
            size -= 0.25;
            setTitleFont(title, size, widthLimit);
        }
    }

	private void removeRefreshButton() {
		middleButton.getChildren().remove(refreshButton);
	}
}

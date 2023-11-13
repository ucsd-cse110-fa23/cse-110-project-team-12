package edu.ucsd.cse110.client;

import java.io.FileInputStream;
import java.util.Map;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class RecipeDetailedView extends StackPane implements UIInterface {
    private Controller controller;

    private Spacer spacer;
	private VBox content;

    private Text recipeTitle;
    private double titleDefaultSize;
    private double titleWidthLimit;
    private HBox recipeTitleSpacer;
    private Label information;
    private TextArea informationEdit;
    private ScrollPane scrollPaneInfo;
    private ScrollPane scrollPaneEdit;

    private Button cancelButton;
    private Button saveButton;
    private HBox unsavedButtonBox;

    private Button deleteButton;
    private Button editButton;
    private HBox savedButtonBox;

    private Button saveEditButton;
    private HBox saveEditButtonBox;

    private HBox backArrowBox;
    private Button backButton;

    public RecipeDetailedView(Controller c) {
        controller = c;

        this.setId("recipe-detailed");
        //this.setMaxWidth(264);
        //this.setPrefWidth(264);

		spacer = new Spacer(this, new Insets(35, 0, 0, 0), Pos.TOP_CENTER);

		content = new VBox();
		content.setId("content");

        //recipe contents
        recipeTitle = new Text();
        recipeTitle.setId("recipe-title");
        titleDefaultSize = 19;
        titleWidthLimit = 240;
        setTitleFont(recipeTitle, titleDefaultSize, titleWidthLimit);
        recipeTitleSpacer = new Spacer(recipeTitle, new Insets(0, 11, 0, 11), Pos.TOP_CENTER);

        information = new Label();
        information.setWrapText(true);
        information.setId("information");
        scrollPaneInfo = new ScrollPane(information);

        // UnsavedLayout
        cancelButton = new Button("Cancel");
        cancelButton.setId("cancel-button");

        saveButton = new Button("Save");
        saveButton.setId("save-button");
        unsavedButtonBox = new HBox(cancelButton, saveButton);
        unsavedButtonBox.setId("unsaved-button-box");

        //SavedLayout
        deleteButton = new Button("Delete");	
        deleteButton.setId("cancel-button");

        editButton = new Button("Edit");
        editButton.setId("edit-button");
        savedButtonBox = new HBox(deleteButton, editButton);
        savedButtonBox.setId("saved-button-box");

        // Edit Layout
        informationEdit = new TextArea();
        informationEdit.setWrapText(true);
        informationEdit.setId("information-edit");
        scrollPaneEdit = new ScrollPane(informationEdit);

        saveEditButton = new Button("Save");
        saveEditButton.setId("save-edit-button");
        saveEditButtonBox = new HBox(saveEditButton);
        saveEditButtonBox.setId("save-edit-button-box");

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

        this.getChildren().addAll(content);
        addListeners();
    }

    private void addListeners() {
		cancelButton.setOnAction(
            e -> {
                controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.CancelButton));
            }
        );

        saveButton.setOnAction(
            e -> {
                controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.SaveButton));
            }
        );

        deleteButton.setOnAction(
            e -> {
                controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.DeleteButton));
            }
        );

        editButton.setOnAction(
            e -> {
                controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.EditButton));
            }
        );

        backButton.setOnAction(
            e -> {
				controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.BackButton));
            }
        );
        saveEditButton.setOnAction(
            e -> {
                controller.receiveMessageFromUI(new Message(Message.RecipeDetailedView.SaveEditButton,
                    Map.ofEntries(Map.entry("RecipeBody", this.informationEdit.getText()))));
            }
        );
	}

    private void setTitleFont(Text title, double size, double widthLimit) {
        title.setFont(new Font("Helvetica Bold", size));
        double width = title.getLayoutBounds().getWidth();
        if (width >= widthLimit) {
            size -= 0.25;
            setTitleFont(title, size, widthLimit);
        }
    }

    @Override
    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.RecipeDetailedModel.SetTitleBody) {
            information.setText("\n" + (String) m.getKey("RecipeBody"));

            if ((String) m.getKey("RecipeTitle") != "") {
                recipeTitle.setText((String) m.getKey("RecipeTitle"));

                setTitleFont(recipeTitle, titleDefaultSize, titleWidthLimit);
                addChild(recipeTitleSpacer);
            }

            addChild(scrollPaneInfo);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.UseUnsavedLayout) {
            addChild(unsavedButtonBox);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.SaveConfirmation) {
            displaySaveConfirmation();
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.RemoveUnsavedLayout) {
            removeChild(unsavedButtonBox);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.UseSavedLayout) {
            addChild(savedButtonBox);
            this.getChildren().addAll(backArrowBox, backButton);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.EditRecipe) {
               
            this.getChildren().removeAll(backArrowBox, backButton);
            removeChild(scrollPaneInfo);
            removeChild(savedButtonBox);

            informationEdit.setText((String) m.getKey("RecipeBody"));
            addChild(scrollPaneEdit);

            addChild(saveEditButtonBox);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.RemoveEditView) {
            removeChild(scrollPaneEdit);
            removeChild(saveEditButtonBox);
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
}

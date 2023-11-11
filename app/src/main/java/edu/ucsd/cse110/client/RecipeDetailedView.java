package edu.ucsd.cse110.client;

import java.io.FileInputStream;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class RecipeDetailedView extends StackPane implements UIInterface {
    private Controller controller;

    private Spacer spacer;
	private VBox content;

    private Text recipeTitle;
    private Label information;
    private ScrollPane scrollPane;

    private Button cancelButton;
    private Button saveButton;
    private HBox unsavedButtonBox;

    private Button deleteButton;
    private Button editButton;
    private HBox savedButtonBox;

    private HBox backArrowBox;
    private Button backButton;

    public RecipeDetailedView(Controller c) {
        controller = c;

		this.setId("recipe-detailed");

		spacer = new Spacer(this, new Insets(35, 0, 0, 0), Pos.TOP_CENTER);

		content = new VBox();
		content.setId("content");

        //recipe contents

        recipeTitle = new Text();
        recipeTitle.setId("recipe-title");
        setTitleFont(recipeTitle, 19);

        information = new Label();
        information.setId("information");
        scrollPane = new ScrollPane(information);

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
	}

    private void setTitleFont(Text title, double size){
		title.setFont(new Font("Helvetica Bold", size));
		if (size == 11) { return; }
        double width = title.getLayoutBounds().getWidth();
        if (width >= 266){
            size -= 0.25;
            setTitleFont(title, size);
        }
    }

    @Override
    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.RecipeDetailedModel.SetTitleBody) {
            recipeTitle.setText((String) m.getKey("RecipeTitle"));
            information.setText((String) m.getKey("RecipeBody"));
            
            addChild(recipeTitle);
            addChild(scrollPane);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.UseUnsavedLayout) {
            addChild(unsavedButtonBox); 
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.RemoveUnsavedLayout){
            removeChild(unsavedButtonBox);
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.UseSavedLayout) {
            addChild(savedButtonBox); 
            this.getChildren().addAll(backArrowBox, backButton);
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
    @Override
    public Parent getUI() {
        return this.spacer;
    }
}

package edu.ucsd.cse110.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;


// AppFrame that holds the RecipeList, createRecipeButton, createRecipe and more!
public class AppFrame extends BorderPane {
	private Header header;
	private Button createRecipeButton;
	private Spacer createRecipeSpacer;
	private StackPane content;
	
	private CreateRecipe createRecipe;
	private boolean creatingRecipe;

    public AppFrame() {
		this.header = new Header();
		this.createRecipeButton = new Button();	
		
		// transparent createRecipeButton
		createRecipeButton.setShape(new Circle(37));
		createRecipeButton.setMinSize(37, 37);
		createRecipeButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
		
		// text and coloring that goes behind the createRecipeButton
		Circle circle = new Circle(18.5);
		circle.setFill(Color.web("#98D38E"));
		Label plus = new Label("+");
		plus.setTextFill(Color.WHITE);
		plus.setFont(new Font("Helvetica Bold", 36));

		// spacers to put createRecipeButton in the bottom right
		Spacer circleSpacer = new Spacer(circle, new Insets(0, 10, 10, 0), Pos.BOTTOM_RIGHT);
		Spacer plusSpacer = new Spacer(plus, new Insets(0, 17.5, 10, 0), Pos.BOTTOM_RIGHT);
		Spacer buttonSpacer = new Spacer(createRecipeButton, new Insets(0, 10, 10, 0), Pos.BOTTOM_RIGHT);

		this.content = new StackPane(circleSpacer, plusSpacer, buttonSpacer);
		this.setStyle("-fx-background-color: #FAF9F6;");
        this.setTop(header);
        this.setCenter(content);
		addListeners();
    }

	// adds functionality to the createRecipeButton
	void addListeners() {
		createRecipeButton.setOnAction(
            e -> {
				if (!creatingRecipe) {
					creatingRecipe = true;
					createRecipe = new CreateRecipe(this);
					createRecipeSpacer = new Spacer(createRecipe, new Insets(30, 0, 0, 0), Pos.TOP_CENTER);
					content.getChildren().add(createRecipeSpacer);
				}
            }
        );
	}

	// reset AppFrame when the recipe creation stops
	public void stopCreating() {
		creatingRecipe = false;
		content.getChildren().remove(createRecipeSpacer);
	}
}
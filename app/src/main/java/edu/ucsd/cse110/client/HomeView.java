package edu.ucsd.cse110.client;

import java.util.*;
import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.Parent;

// AppFrame that holds the RecipeList, createRecipeButton, createRecipe and more!
public class HomeView extends BorderPane implements UIInterface {
	private Controller controller;
	private StackPane content;
	private ScrollPane recipeList;
	private VBox recipeInfo;
	private Button createButton;

    public HomeView(Controller c) {
		this.controller = c;
		this.content = new StackPane();
		this.recipeList = new ScrollPane();
		this.recipeInfo = new VBox();
		
		recipeList.setContent(recipeInfo);
		recipeList.setId("recipe-list");

        this.setTop(new Header());
        this.setCenter(content);
		CreateButtonModule createButtonModule = new CreateButtonModule();	
		
		// Important that put in spacer before recipeList, or else button won't be clickale
		addChild(createButtonModule.getSpacer());
		addChild(recipeList);
		this.setId("app-frame");

		// adds functionality to the createRecipeButton
		this.createButton = createButtonModule.getCreateButton();
		createButton.setOnAction(
            e -> {
				controller.receiveMessageFromUI(new Message(Message.HomeView.CreateRecipeButton));
            }
        );
    }

	@Override
	public void receiveMessage(Message m) {
		if (m.getMessageType() == Message.HomeModel.UpdateRecipeList) {
			List<Recipe> recipes = (List<Recipe>) m.getKey("Recipes");
			populateRecipes(recipes);
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
		return this;
	}

	private void populateRecipes(List<Recipe> recipes) {
		recipeInfo.getChildren().clear();
		for (Recipe r : recipes) {
			recipeInfo.getChildren().add(new RecipeTitleView(controller, r));
		}
	}
}
package edu.ucsd.cse110.client;

import java.util.*;
import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import edu.ucsd.cse110.server.schemas.RecipeSchema;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
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

		content.setAlignment(Pos.TOP_CENTER);

		Header header = new Header();
		header.setEffect(new DropShadow(BlurType.GAUSSIAN, new Color(0, 0, 0, 0.5), 20, 0.2, 0, 0));
		Button logOut = new Button("Log Out");
		logOut.setId("log-out");
		logOut.setOnAction(
            e -> {
				controller.receiveMessageFromModel(new Message(Message.HomeView.LogOut));
            }
        );

		StackPane topPane = new StackPane(header, logOut);
		topPane.setId("top-pane");

        this.setTop(topPane);
        this.setCenter(content);
		CreateButtonModule createButtonModule = new CreateButtonModule();	
		
		// Important that put in spacer before recipeList, or else button won't be clickale
		addChild(recipeList);
		addChild(createButtonModule.getSpacer());
		this.setId("app-frame");

		// adds functionality to the createRecipeButton
		this.createButton = createButtonModule.getCreateButton();
		createButton.setOnAction(
            e -> {
				controller.receiveMessageFromUI(new Message(Message.HomeView.CreateRecipeButton));
            }
        );
    }
	@SuppressWarnings("unchecked")
	@Override
	public void receiveMessage(Message m) {
		if (m.getMessageType() == Message.HomeModel.UpdateRecipeList) {
			List<RecipeSchema> recipes = (List<RecipeSchema>) m.getKey("Recipes");
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

	private void populateRecipes(List<RecipeSchema> recipes) {
		recipeInfo.getChildren().clear();
		for (RecipeSchema r : recipes) {
			recipeInfo.getChildren().add(new RecipeTitleView(controller, r));
		}
	}
}
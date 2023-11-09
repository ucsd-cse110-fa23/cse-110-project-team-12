package edu.ucsd.cse110.client;

import edu.ucsd.cse110.api.AppManager;
import edu.ucsd.cse110.api.AppManager.UpdateType;
import edu.ucsd.cse110.api.AppManager.ViewType;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.Node;
import javafx.scene.Parent;

// AppFrame that holds the RecipeList, createRecipeButton, createRecipe and more!
public class AppFrame extends BorderPane implements UIInterface{
	
	private StackPane content;
	private Button createButton;
	private AppManager appManager;

    public AppFrame(AppManager appManager) {
		this.appManager = appManager;

		this.content = new StackPane();

        this.setTop(new Header());
        this.setCenter(content);
		this.setId("app-frame");

		// adds functionality to the createRecipeButton
		CreateButtonModule createButtonModule = new CreateButtonModule();	
		this.createButton = createButtonModule.getCreateButton();
		this.content.getChildren().addAll(createButtonModule.getComponents());
		createButton.setOnAction(
            e -> {
				this.appManager.updateView(ViewType.CreateRecipeView, UpdateType.Start);
            }
        );
    }
	
	@Override
	public void addNode(Node node){
		this.content.getChildren().add(node);
	}

	@Override
	public void removeNode(Node node){
		this.content.getChildren().remove(node);
	}

	@Override
	public Parent getUI() {
		return this;
	}
}
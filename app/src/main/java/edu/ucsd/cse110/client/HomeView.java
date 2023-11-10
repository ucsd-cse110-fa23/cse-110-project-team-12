package edu.ucsd.cse110.client;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.Node;

// AppFrame that holds the RecipeList, createRecipeButton, createRecipe and more!
public class HomeView extends BorderPane implements UIInterface {
	private Controller controller;
	private StackPane content;
	private Button createButton;

    public HomeView(Controller c) {
		this.controller = c;
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
				controller.receiveMessageFromUI(new Message(Message.Type.ButtonCreateRecipe));
            }
        );
    }
	
	public void receiveMessage(Message m) {

	}

	public void addChild(Node ui) {
		getChildren().add(ui);
	}

	public void removeChild(Node ui) {
		getChildren().remove(ui);
	}
}
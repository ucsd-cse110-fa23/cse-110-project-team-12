package edu.ucsd.cse110.client;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CreateButtonModule {
	private Button createButton;
	private Label createPlus;

	public CreateButtonModule() {
		createButton = new Button();
		
		// text and coloring that goes behind the createRecipeButton
		createPlus = new Label("+");
		
		createButton.setGraphic(createPlus);
		
		createButton.setId("create-button");
		createPlus.setId("create-plus");
	}
	
	public Button getCreateButton() {
		return createButton;
	}

	public Node getNode() {
		return createButton;
	}
}

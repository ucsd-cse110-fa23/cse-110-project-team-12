package edu.ucsd.cse110.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CreateButtonModule {
	private Button createButton;
	private Label createPlus;
	private Spacer createSpacer;

	public CreateButtonModule() {
		createButton = new Button();
		
		// text and coloring that goes behind the createRecipeButton
		createPlus = new Label("+");
		
		createButton.setGraphic(createPlus);
		createSpacer = new Spacer(createButton, new Insets(0, 10, 10, 0), Pos.BOTTOM_RIGHT);
		createSpacer.setPickOnBounds(false);
		
		createButton.setId("create-button");
		createPlus.setId("create-plus");
	}
	
	public Button getCreateButton() {
		return createButton;
	}

	public Spacer getSpacer() {
		return createSpacer;
	}
}

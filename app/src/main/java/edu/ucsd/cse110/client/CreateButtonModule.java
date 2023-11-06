package edu.ucsd.cse110.client;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class CreateButtonModule {
	private Button createButton;
	private Circle createCircle;
	private Label createPlus;
	private List<Spacer> components;

	public CreateButtonModule() {
		createButton = new Button();
		createButton.setShape(new Circle(37));
		
		// text and coloring that goes behind the createRecipeButton
		createCircle = new Circle(18.5);
		createPlus = new Label("+");
		createPlus.setFont(new Font("Helvetica Bold", 36));
		
		components = new ArrayList<>();
		components.add(new Spacer(createCircle, new Insets(0, 10, 10, 0), Pos.BOTTOM_RIGHT));
		components.add(new Spacer(createPlus, new Insets(0, 17.5, 10, 0), Pos.BOTTOM_RIGHT));
		components.add(new Spacer(createButton, new Insets(0, 10, 10, 0), Pos.BOTTOM_RIGHT));
		
		createButton.setId("create-button");
		createCircle.setId("create-circle");
		createPlus.setId("create-plus");
	}
	
	public Button getCreateButton() {
		return createButton;
	}

	public List<Spacer> getComponents() {
		return components;
	}
}

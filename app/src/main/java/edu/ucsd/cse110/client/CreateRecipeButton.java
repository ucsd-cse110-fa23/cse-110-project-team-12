package edu.ucsd.cse110.client;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class CreateRecipeButton {
	private Button createButton;
	private Circle circle;
	private Label plus;

	public CreateRecipeButton() {
		createButton = new Button();

		// transparent createRecipeButton
		createButton.setShape(new Circle(37));
		createButton.setMinSize(37, 37);
		createButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");

		// text and coloring that goes behind the createRecipeButton
		circle = new Circle(18.5);
		circle.setFill(Color.web("#98D38E"));
		plus = new Label("+");
		plus.setTextFill(Color.WHITE);
		plus.setFont(new Font("Helvetica Bold", 36));
	}
	
	public Button getCreateButton() {
		return createButton;
	}

	public Circle getCircle() {
		return circle;
	}

	public Label getPlus() {
		return plus;
	}
}

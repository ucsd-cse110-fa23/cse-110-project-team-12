package edu.ucsd.cse110.client;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

// Consistent Header for the PantryPal app
class Header extends VBox {
	private Label title;

	Header() {
		this.setPrefSize(325, 68);
		this.setStyle("-fx-background-color: #98D38E;");
		this.setAlignment(Pos.CENTER);
		
		DropShadow dropShadow = new DropShadow(15, Color.BLACK); 
        this.setEffect(dropShadow);

		title = new Label("PantryPal");
		title.setFont(new Font("Helvetica Bold", 40));
		title.setTextFill(Color.WHITE);
		this.getChildren().add(title);
	}
}
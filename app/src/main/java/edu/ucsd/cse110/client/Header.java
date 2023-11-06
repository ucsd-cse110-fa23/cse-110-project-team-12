package edu.ucsd.cse110.client;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

// Consistent Header for the PantryPal app
class Header extends VBox {
	private Label headerTitle;

	Header() {
		headerTitle = new Label("PantryPal");
		headerTitle.setFont(new Font("Helvetica Bold", 40));
		headerTitle.setId("header-title");

		DropShadow dropShadow = new DropShadow(15, Color.BLACK); 
        this.setEffect(dropShadow);
		this.getChildren().add(headerTitle);
		this.setId("header");
	}
}
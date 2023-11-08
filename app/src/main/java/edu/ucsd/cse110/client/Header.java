package edu.ucsd.cse110.client;

import java.io.FileInputStream;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

// Consistent Header for the PantryPal app
class Header extends HBox {
	Header() {
		Label headerTitle = new Label("PantryPal");
		headerTitle.setId("header-title");
		
		Image PPIcon = null;
		try {
			PPIcon = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/PPIcon.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		ImageView PPIconView = new ImageView(PPIcon);
		PPIconView.setFitWidth(20);
		PPIconView.setFitHeight(20);

		this.getChildren().addAll(headerTitle, PPIconView);
		this.setId("header");
	}
}
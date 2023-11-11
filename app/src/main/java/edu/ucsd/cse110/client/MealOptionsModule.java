package edu.ucsd.cse110.client;

import java.io.FileInputStream;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MealOptionsModule extends VBox {

	public MealOptionsModule() {
		HBox breakfastBox = createMealTypeBox("Breakfast");
		HBox lunchBox = createMealTypeBox("Lunch");
		HBox dinnerBox = createMealTypeBox("Dinner");

		this.getChildren().addAll(breakfastBox, lunchBox, dinnerBox);
		this.setId("meal-options-module");
	}

	public HBox createMealTypeBox(String selectedMealType) {
		if (selectedMealType.equals("Breakfast")) {
			Label breakfast = new Label("Breakfast");
			breakfast.setId("breakfast");
			Image sun = null;
			try {
				sun = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/sun.png"));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			ImageView sunView = new ImageView(sun);
			sunView.setFitWidth(19);
			sunView.setFitHeight(19);
			return new HBox(sunView, breakfast);
		}
		else if (selectedMealType.equals("Lunch")) {
			Label lunch = new Label("Lunch");
			lunch.setId("lunch");
			Image cloud = null;
			try {
				cloud = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/cloud.png"));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			ImageView cloudView = new ImageView(cloud);
			cloudView.setFitWidth(19);
			cloudView.setFitHeight(19);
			return new HBox(cloudView, lunch);
		}
		else if (selectedMealType.equals("Dinner")) {
			Label dinner = new Label("Dinner");
			dinner.setId("dinner");
			Image moon = null;
			try {
				moon = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/moon.png"));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			ImageView moonView = new ImageView(moon);
			moonView.setFitWidth(19);
			moonView.setFitHeight(19);
			return new HBox(moonView, dinner);
		}
		else {
			return null;
		}
	}
}

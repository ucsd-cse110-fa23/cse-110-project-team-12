package edu.ucsd.cse110.client;

import java.io.FileInputStream;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MealOptionsModule extends VBox {
	HBox breakfastBox;
	HBox lunchBox;
	HBox dinnerBox;

	public MealOptionsModule() {
		Label breakfast = new Label("Breakfast");
		Label lunch = new Label("Lunch");
		Label dinner = new Label("Dinner");

		breakfast.setId("breakfast");
		lunch.setId("lunch");
		dinner.setId("dinner");
	
		Image sun = null;
		Image cloud = null;
		Image moon = null;
		try {
			sun = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/resources/sun.png"));
			cloud = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/resources/cloud.png"));
			moon = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/resources/moon.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		ImageView sunView = new ImageView(sun);
		sunView.setFitWidth(19);
		sunView.setFitHeight(19);

		ImageView cloudView = new ImageView(cloud);
		cloudView.setFitWidth(19);
		cloudView.setFitHeight(19);

		ImageView moonView = new ImageView(moon);
		moonView.setFitWidth(19);
		moonView.setFitHeight(19);

		breakfastBox = new HBox(sunView, breakfast);
		lunchBox = new HBox(cloudView, lunch);
		dinnerBox = new HBox(moonView, dinner);

		this.getChildren().addAll(breakfastBox, lunchBox, dinnerBox);
		this.setId("meal-options-module");
	}

	public HBox getMealTypeBox(String selectedMealType) {
		if (selectedMealType.equals("Breakfast")) {
			return breakfastBox;
		}
		else if (selectedMealType.equals("Lunch")) {
			return lunchBox;
		}
		else if (selectedMealType.equals("Dinner")) {
			return dinnerBox;
		}
		else {
			return null;
		}
	}
}

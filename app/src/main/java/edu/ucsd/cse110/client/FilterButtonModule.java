package edu.ucsd.cse110.client;

import java.io.FileInputStream;
import java.util.Map;

import edu.ucsd.cse110.api.HomeModel.FilterOption;
import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class FilterButtonModule {
	HBox content;
	StackPane buttonBackingClosed;
	StackPane buttonBackingOpen;

	boolean isOpen;
	FilterOption filterOption;

	Controller controller;
	
	public FilterButtonModule(Controller controller) {
		this.controller = controller;
		filterOption = FilterOption.All;

		content = new HBox();
		content.setPickOnBounds(false);
		content.setId("filter-content");

		closedSetup();
		openSetup();
		close();
	}

	public void closedSetup() {
		Image upArrow = null;
		try {
			upArrow = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/upArrow.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ImageView upArrowView = new ImageView(upArrow);
		upArrowView.setFitWidth(7);
		upArrowView.setFitHeight(7);
		HBox upArrowBox = new HBox(upArrowView);
		
		Label filter = new Label("Filter");
		HBox textLabel = new HBox(upArrowBox, filter);

		Button openButton = new Button();
		openButton.setOnAction(e -> { open(); });
		buttonBackingClosed = new StackPane(textLabel, openButton);
		
		upArrowBox.setId("up-arrow-box");
		filter.setId("filter");
		textLabel.setId("text-label");

		openButton.setId("open-button");
		buttonBackingClosed.setId("button-backing-closed");
	}

	public void openSetup() {
		Image upArrow = null;
		try {
			upArrow = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/upArrow.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ImageView upArrowView = new ImageView(upArrow);
		upArrowView.setFitWidth(7);
		upArrowView.setFitHeight(7);
		HBox upArrowBox = new HBox(upArrowView);
		
		Label filter = new Label("Filter");
		HBox textLabel = new HBox(upArrowBox, filter);

		
		Image check = null;
		try {
			check = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/check.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ImageView checkView = new ImageView(check);
		checkView.setFitWidth(7);
		checkView.setFitHeight(7);
		

		Image moon = null;
		try {
			moon = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/moon.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ImageView moonView = new ImageView(moon);
		moonView.setFitWidth(12);
		moonView.setFitHeight(12);
		HBox moonBox = new HBox(moonView);
		
		HBox moonCheck = filterOption == FilterOption.Dinner ? new HBox(checkView) : new HBox();
		HBox moonLabel = new HBox(moonCheck, moonBox);

		Image cloud = null;
		try {
			cloud = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/cloud.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ImageView cloudView = new ImageView(cloud);
		cloudView.setFitWidth(12);
		cloudView.setFitHeight(12);
		HBox cloudBox = new HBox(cloudView);
		
		HBox cloudCheck = filterOption == FilterOption.Lunch ? new HBox(checkView) : new HBox();
		HBox cloudLabel = new HBox(cloudCheck, cloudBox);


		Image sun = null;
		try {
			sun = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/sun.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ImageView sunView = new ImageView(sun);
		sunView.setFitWidth(12);
		sunView.setFitHeight(12);
		HBox sunBox = new HBox(sunView);
		
		HBox sunCheck = filterOption == FilterOption.Breakfast ? new HBox(checkView) : new HBox();
		HBox sunLabel = new HBox(sunCheck, sunBox);

		VBox labelBox = new VBox(sunLabel, cloudLabel, moonLabel, textLabel);

		Button closeButton = new Button();
		Button moonButton = new Button();
		Button cloudButton = new Button();
		Button sunButton = new Button();

		closeButton.setOnAction(e -> { close(); });
		moonButton.setOnAction(
			e -> { 
				filterOption = filterOption == FilterOption.Dinner ? FilterOption.All : FilterOption.Dinner; 
				openSetup(); 
				open(); 
				controller.receiveMessageFromUI(new Message(Message.HomeView.FilterRecipeButton,
                        Map.ofEntries(Map.entry("FilterOption", filterOption))));
			}
		);
		cloudButton.setOnAction(
			e -> { 
				filterOption = filterOption == FilterOption.Lunch ? FilterOption.All : FilterOption.Lunch; 
				openSetup(); 
				open(); 
				controller.receiveMessageFromUI(new Message(Message.HomeView.FilterRecipeButton,
                        Map.ofEntries(Map.entry("FilterOption", filterOption))));
			}
		);
		sunButton.setOnAction(
			e -> { 
				filterOption = filterOption == FilterOption.Breakfast ? FilterOption.All : FilterOption.Breakfast;
				openSetup(); 
				open(); 
				controller.receiveMessageFromUI(new Message(Message.HomeView.FilterRecipeButton,
                        Map.ofEntries(Map.entry("FilterOption", filterOption))));
			}
		);

		VBox openButtonBox = new VBox(sunButton, cloudButton, moonButton, closeButton);
		buttonBackingOpen = new StackPane(labelBox, openButtonBox);

		upArrowBox.setId("up-arrow-box");
		filter.setId("filter");
		textLabel.setId("text-label");

		moonCheck.setId("moon-check");
		moonBox.setId("moon-box");
		moonLabel.setId("moon-label");

		cloudCheck.setId("cloud-check");
		cloudBox.setId("cloud-box");
		cloudLabel.setId("cloud-label");

		sunCheck.setId("sun-check");
		sunBox.setId("sun-box");
		sunLabel.setId("sun-label");

		closeButton.setId("close-button");
		moonButton.setId("moon-button");
		cloudButton.setId("cloud-button");
		sunButton.setId("sun-button");

		labelBox.setId("filter-label-box");
		openButtonBox.setId("filter-open-button-box");
		buttonBackingOpen.setId("filter-button-backing-open");
	}
	
	public void close() {
		isOpen = false;
		content.getChildren().clear();
		content.getChildren().addAll(buttonBackingClosed);
	}
	
	public void open() {
		isOpen = true;
		content.getChildren().clear();
		content.getChildren().addAll(buttonBackingOpen);
	}

	public Node getNode() {
		return content;
	}
}

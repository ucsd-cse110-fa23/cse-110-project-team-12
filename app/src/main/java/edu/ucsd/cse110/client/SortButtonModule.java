package edu.ucsd.cse110.client;

import java.io.FileInputStream;
import java.util.Map;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.HomeModel.SortOption;
import edu.ucsd.cse110.api.Message;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SortButtonModule {
	Controller controller;
	HBox content;
	StackPane buttonBackingClosed;
	StackPane buttonBackingOpen;

	boolean isOpen;
	SortOption sortOption;
	
	public SortButtonModule(Controller controller) {
		this.controller = controller;
		sortOption = SortOption.DateDes;

		content = new HBox();
		content.setPickOnBounds(false);
		content.setId("sort-content");

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
		
		Label sort = new Label("Sort");
		HBox textLabel = new HBox(upArrowBox, sort);

		Button openButton = new Button();
		openButton.setOnAction(e -> { open(); });
		buttonBackingClosed = new StackPane(textLabel, openButton);
		
		upArrowBox.setId("up-arrow-box");
		sort.setId("sort");
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
		
		Label sort = new Label("Sort");
		HBox textLabel = new HBox(upArrowBox, sort);

		
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
		

		Image upTriangle = null;
		try {
			upTriangle = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/upTriangle.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ImageView upTriangleView = new ImageView(upTriangle);
		upTriangleView.setFitWidth(6);
		upTriangleView.setFitHeight(6);
		HBox upTriangleBox = new HBox(upTriangleView);
		
		HBox dateUpCheck = sortOption == SortOption.DateDes ? new HBox(checkView) : new HBox();
		Label dateUpText = new Label("Date");
		HBox dateUpLabel = new HBox(dateUpCheck, dateUpText, upTriangleBox);


		Image downTriangle = null;
		try {
			downTriangle = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/downTriangle.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ImageView downTriangleView = new ImageView(downTriangle);
		downTriangleView.setFitWidth(6);
		downTriangleView.setFitHeight(6);
		HBox downTriangleBox = new HBox(downTriangleView);
		
		HBox dateDownCheck = sortOption == SortOption.DateAsc ? new HBox(checkView) : new HBox();
		Label dateDownText = new Label("Date");
		HBox dateDownLabel = new HBox(dateDownCheck, dateDownText, downTriangleBox);


		HBox zACheck = sortOption == SortOption.TitleDes ? new HBox(checkView) : new HBox();
		Label zAText = new Label("Z – A");
		HBox zALabel = new HBox(zACheck, zAText);


		HBox aZCheck = sortOption == SortOption.TitleAsc ? new HBox(checkView) : new HBox();
		Label aZText = new Label("A – Z");
		HBox aZLabel = new HBox(aZCheck, aZText);

		VBox labelBox = new VBox(aZLabel, zALabel, dateDownLabel, dateUpLabel, textLabel);

		Button closeButton = new Button();
		Button dateUpButton = new Button();
		Button dateDownButton = new Button();
		Button zAButton = new Button();
		Button aZButton = new Button();

		closeButton.setOnAction(e -> { close(); });
		dateUpButton.setOnAction(
			e -> { 
				sortOption = SortOption.DateDes;
				openSetup(); 
				open(); 
				controller.receiveMessageFromUI(new Message(Message.HomeView.SortRecipeButton,
                        Map.ofEntries(Map.entry("SortOption", sortOption))));
			}
		);
		dateDownButton.setOnAction(
			e -> { 
				sortOption = SortOption.DateAsc;
				openSetup(); 
				open(); 
				controller.receiveMessageFromUI(new Message(Message.HomeView.SortRecipeButton,
                        Map.ofEntries(Map.entry("SortOption", sortOption))));
			}
		);
		zAButton.setOnAction(
			e -> { 
				sortOption = SortOption.TitleDes;
				openSetup(); 
				open(); 
				controller.receiveMessageFromUI(new Message(Message.HomeView.SortRecipeButton,
                        Map.ofEntries(Map.entry("SortOption", sortOption))));
			}
		);
		aZButton.setOnAction(
			e -> { 
				sortOption = SortOption.TitleAsc;
				openSetup(); 
				open(); 
				controller.receiveMessageFromUI(new Message(Message.HomeView.SortRecipeButton,
                        Map.ofEntries(Map.entry("SortOption", sortOption))));
			}
		);

		VBox openButtonBox = new VBox(aZButton, zAButton, dateDownButton, dateUpButton, closeButton);
		buttonBackingOpen = new StackPane(labelBox, openButtonBox);

		upArrowBox.setId("up-arrow-box");
		sort.setId("sort");
		textLabel.setId("text-label");

		dateUpCheck.setId("date-up-check");
		dateUpText.setId("date-up-text");
		upTriangleBox.setId("up-triangle-box");
		dateUpLabel.setId("date-up-label");

		dateDownCheck.setId("date-down-check");
		dateDownText.setId("date-down-text");
		downTriangleBox.setId("down-triangle-box");
		dateDownLabel.setId("date-down-label");

		aZCheck.setId("az-check");
		aZText.setId("az-text");
		aZLabel.setId("az-label");

		zACheck.setId("za-check");
		zAText.setId("za-text");
		zALabel.setId("za-label");

		closeButton.setId("close-button");
		dateUpButton.setId("date-up-button");
		dateDownButton.setId("date-down-button");
		zAButton.setId("za-button");
		aZButton.setId("az-button");

		labelBox.setId("sort-label-box");
		openButtonBox.setId("sort-open-button-box");
		buttonBackingOpen.setId("sort-button-backing-open");
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

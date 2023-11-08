package edu.ucsd.cse110.client;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

// handles the UI for the record button
class RecordButtonModule extends StackPane {
	private static final String SECONDARY = "#BC8B8B";
	private static final String BACKGROUND = "#5B5B5B";

	public RecordButtonModule(Button recordButton, int topPadding, boolean recording) {
		Circle recordBackground = new Circle(28.5);
		Circle recordMidground = new Circle(24.5);

		recordBackground.setId("record-background");
		recordMidground.setId("record-midground");

		if (!recording) {
			recordMidground.setFill(Color.web(BACKGROUND));
			recordButton.setStyle("-fx-background-color: " + SECONDARY + ";");
		}
		else {
			recordMidground.setFill(Color.web(SECONDARY));
			recordButton.setStyle("-fx-background-color: " + BACKGROUND + ";");
		}

		this.setPadding(new Insets(topPadding, 0, 0, 0));
		this.getChildren().addAll(recordBackground, recordMidground, recordButton);
	}
}

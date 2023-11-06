package edu.ucsd.cse110.client;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

// handles the UI for the record button
class RecordButtonModule extends StackPane {
	private Button recordButton;
	private Circle background;
	private Circle midground;
	private boolean recording;

	public RecordButtonModule(Button recordButton, int topPadding) {
		background = new Circle(28.5);
		background.setFill(Color.web("#BC8B8B"));
		midground = new Circle(24.5);
		midground.setFill(Color.web("#5B5B5B"));

		this.recordButton = recordButton;
		this.setPadding(new Insets(topPadding, 0, 0, 0));
		this.getChildren().addAll(background, midground, recordButton);
	}

	public void switchColor() {
		if (!recording) {
			recording = true;
			midground.setFill(Color.web("#BC8B8B"));
			recordButton.setStyle("-fx-background-color: #5B5B5B;");
		}
		else {
			recording = false;
			midground.setFill(Color.web("#5B5B5B"));
			recordButton.setStyle("-fx-background-color: #BC8B8B;");
		}
	}

	public void setTopPadding(int topPadding) {
		this.setPadding(new Insets(topPadding, 0, 0, 0));
	}
}

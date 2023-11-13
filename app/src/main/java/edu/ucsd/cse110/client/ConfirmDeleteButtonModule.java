package edu.ucsd.cse110.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;


public class ConfirmDeleteButtonModule {
	private Button deleteButton;
	private ImageView trashcanView;
	private Spacer deleteButtonSpacer;

	public ConfirmDeleteButtonModule() {
		deleteButton = new Button();
		deleteButton.setId("delete-confirmation-button");

		Image trashcan = null;
        try {
            trashcan = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/trashcan.png"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        trashcanView = new ImageView(trashcan);
        trashcanView.setFitWidth(25);
        trashcanView.setFitHeight(25);

		deleteButton.setGraphic(trashcanView);

		deleteButtonSpacer = new Spacer(deleteButton, new Insets(40, 0, 0, 0), Pos.TOP_CENTER);
	}
	
	public Button getDeleteButton() {
		return deleteButton;
	}

	public Spacer getSpacer() {
		return deleteButtonSpacer;
	}
}

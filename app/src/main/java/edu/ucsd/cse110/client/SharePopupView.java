package edu.ucsd.cse110.client;

import java.io.FileInputStream;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class SharePopupView extends StackPane {
	private Button closeButton;
	private Label linkLabel;
	
	public SharePopupView() {
		closeButton = new Button();
		
		linkLabel = new Label();
		Image clipboardImage = null;
		try {
			clipboardImage = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/clipboard.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ImageView clipboardView = new ImageView(clipboardImage);
		clipboardView.setFitWidth(10);
		clipboardView.setFitHeight(10);
		Button clipboardButton = new Button();
		clipboardButton.setGraphic(clipboardView);
		clipboardButton.setOnAction(
			e -> {
				Clipboard clipboard = Clipboard.getSystemClipboard();
				ClipboardContent content = new ClipboardContent();
				content.putString(linkLabel.getText());
				clipboard.setContent(content);
			}
		);

		HBox popupBackground = new HBox(linkLabel, clipboardButton);

		this.getChildren().add(closeButton);
		this.getChildren().add(popupBackground);

		closeButton.setId("close-button");
		linkLabel.setId("link-label");
		clipboardButton.setId("clipboard-button");
		popupBackground.setId("share-popup-background");
		this.setId("share-link-popup");
		this.setPickOnBounds(false);
	}

	public void setLink(String link) {
		linkLabel.setText(link);
	}

	public Button getCloseButton(){
		return closeButton;
	}
}

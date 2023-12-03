package edu.ucsd.cse110.client;

import java.io.FileInputStream;

import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class SharePopupView extends StackPane implements UIInterface {
	
	public SharePopupView(String link) {
		Button closeButton = new Button();
		closeButton.setOnAction(
			e -> {
				// remove share link popup from root
			}
		);
		
		Label linkLabel = new Label(link);
		Image clipboard = null;
		try {
			clipboard = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/clipboard.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		ImageView clipboardView = new ImageView(clipboard);
		clipboardView.setFitWidth(10);
		clipboardView.setFitHeight(10);
		Button clipboardButton = new Button();
		clipboardButton.setGraphic(clipboardView);
		clipboardButton.setOnAction(
			e -> {
				// copy link to clipboard
			}
		);

		HBox popupBackground = new HBox(linkLabel, clipboardButton);

		addChild(closeButton);
		addChild(popupBackground);

		closeButton.setId("close-button");
		linkLabel.setId("link-label");
		clipboardButton.setId("clipboard-button");
		popupBackground.setId("share-popup-background");
		this.setId("share-link-popup");
		this.setPickOnBounds(false);
	}

	@Override
	public void receiveMessage(Message m) {
		// no messegaes to recieve yet
	}

	@Override
	public void addChild(Node ui) {
		this.getChildren().add(ui);
	}

	@Override
	public void removeChild(Node ui) {
		this.getChildren().remove(ui);
	}

	@Override
	public Parent getUI() {
		return this;
	}
}

package edu.ucsd.cse110.client;

import java.io.FileInputStream;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ErrorPopupView extends VBox implements UIInterface{
	private Controller controller;

	private Button refreshButton;
	private VBox errorSpacer;
	
	public ErrorPopupView(Controller c) {
		controller = c;

		this.setId("error-popup");

		Header header = new Header();

		Label errorMessage1 = new Label("Unable to connect to server,");
		Label errorMessage2 = new Label("please try again");
		errorMessage1.setId("error-message");
		errorMessage2.setId("error-message");

		VBox errorMessageBackground = new VBox(errorMessage1, errorMessage2);
		errorMessageBackground.setId("error-message-background");
		
		refreshButton = new Button();
		refreshButton.setId("error-refresh-button");
		Image refresh = null;
		try {
			refresh = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/refresh.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		ImageView refreshView = new ImageView(refresh);
		refreshView.setFitWidth(24);
		refreshView.setFitHeight(24);

		refreshButton.setGraphic(refreshView);
		refreshButton.setOnAction(
			e -> {
				controller.receiveMessageFromModel(new Message(Message.HttpRequest.CloseServerError));
			}
		);
			
		errorSpacer = new VBox();
		errorSpacer.setId("error-spacer");
		errorSpacer.getChildren().addAll(errorMessageBackground, refreshButton);
		this.getChildren().addAll(header, errorSpacer);
	}

	@Override
	public void receiveMessage(Message m) {
		// no messages to recieve yet
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

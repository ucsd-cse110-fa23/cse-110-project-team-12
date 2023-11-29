package edu.ucsd.cse110.client;

import java.io.FileInputStream;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;

public class CreateAccountView extends VBox implements UIInterface {
	private Controller controller;
	boolean rememberMe;

	public CreateAccountView(Controller c) {
		this.controller = c;
		this.setId("signup-view");
		this.setAlignment(Pos.TOP_CENTER);

		Header header = new Header();

		HBox createSpacer = new HBox();
		createSpacer.setId("create-spacer");

		Label username = new Label("Username");
		TextField userArea = new TextField();
		HBox userBox = new HBox(username, userArea);
		username.setId("username");
		userArea.setId("user-area");
		userBox.setId("user-box");

		Label password = new Label("Password");
		TextField passArea = new TextField();
		HBox passBox = new HBox(password, passArea);
		password.setId("password");
		passArea.setId("pass-area");
		passBox.setId("pass-box");

		Label confirmPassword = new Label("Confirm Password");
		confirmPassword.setWrapText(true);
		confirmPassword.setId("confirm-password");
		
		TextField confirmPassArea = new TextField();
		
		confirmPassArea.setId("confirm-pass-area");
		Label remember = new Label("Remember Me?");
		Button rememberToggle = new Button();
		HBox rememberBox = new HBox(remember, rememberToggle);
		rememberBox.setPadding(new Insets(10, 0, 0, 109));
		remember.setId("remember");
		rememberToggle.setId("remember-toggle");
		rememberBox.setId("remember-box");
		
		HBox confirmPassBox = new HBox(confirmPassword, new VBox(confirmPassArea, rememberBox));
		confirmPassBox.setId("confirm-pass-box");

		Button back = new Button("Back");
		Button signUp = new Button("Sign Up");
		HBox signupButtonBox = new HBox(back, signUp);
		back.setId("back");
		signUp.setId("sign-up");
		signupButtonBox.setId("signup-button-box");

		this.getChildren().addAll(header, createSpacer, userBox, passBox, confirmPassBox, signupButtonBox);

		// not to stay, simply to be able to change UI things
		signUp.setOnAction(
            e -> {
				createSpacer.getChildren().clear();
				Label invalidEntry = null;
				if (true) {
					invalidEntry = new Label("Passwords must match");
				}
				else {
					invalidEntry = new Label("\"taken_username\" is taken");
				}
				invalidEntry.setId("invalid-entry");
				createSpacer.getChildren().add(invalidEntry);
            }
        );

		rememberMe = false; 
		rememberToggle.setOnAction(
            e -> {
				if (rememberMe) {
					rememberMe = false;
					rememberToggle.setGraphic(null);
				}
				else {
					rememberMe = true;
					Image x = null;
					try {
						x = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/x.png"));
					}
					catch (Exception except) {
						except.printStackTrace();
					}
					ImageView xView = new ImageView(x);
					xView.setFitWidth(10);
					xView.setFitHeight(10);

					rememberToggle.setGraphic(xView);
				}
            }
        );

	}


	@Override
	public void receiveMessage(Message m) {
		// TODO Auto-generated method stub
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
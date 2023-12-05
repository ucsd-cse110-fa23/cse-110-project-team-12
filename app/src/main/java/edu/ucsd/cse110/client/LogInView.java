package edu.ucsd.cse110.client;

import java.io.FileInputStream;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LogInView extends VBox implements UIInterface {
	Controller controller;
	HBox loginSpacer;
	HBox loginButtonBox;
	Button signUp;
	boolean rememberMe;

	public LogInView(Controller c) {
		this.controller = c;
		this.setId("log-in-view");

		Header header = new Header();

		loginSpacer = new HBox();
		loginSpacer.setId("login-spacer");

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

		Label remember = new Label("Remember Me?");
		Button rememberToggle = new Button();
		HBox rememberBox = new HBox(remember, rememberToggle);
		rememberBox.setPadding(new Insets(10, 10, 0, 216));
		remember.setId("remember");
		rememberToggle.setId("remember-toggle");
		rememberBox.setId("remember-box");

		Button logIn = new Button("Log In");
		signUp = new Button("Sign Up");
		loginButtonBox = new HBox(signUp, logIn);
		logIn.setId("log-in");
		signUp.setId("sign-up");
		loginButtonBox.setId("login-button-box");

		this.getChildren().addAll(header, loginSpacer, userBox, passBox, rememberBox, loginButtonBox);

		signUp.setOnAction(
				e-> {
					controller.receiveMessageFromUI(new Message(Message.LogInView.SignUpButton));
				}
		);

		logIn.setOnAction(
				e -> {
					controller.receiveMessageFromModel(
							new Message(Message.LogInView.LogInButton,
								"Username", userArea.getText(),
								"Password", passArea.getText(),
								"AutomaticLogIn", rememberMe));
					loginSpacer.getChildren().clear();
					Label invalidEntry = new Label("Invalid username/password");
					invalidEntry.setId("invalid-entry");
					loginSpacer.getChildren().add(invalidEntry);
				});

		rememberMe = false;
		rememberToggle.setOnAction(
				e -> {
					if (rememberMe) {
						rememberMe = false;
						rememberToggle.setGraphic(null);
					} else {
						rememberMe = true;
						Image x = null;
						try {
							x = new Image(
									new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/x.png"));
						} catch (Exception except) {
							except.printStackTrace();
						}
						ImageView xView = new ImageView(x);
						xView.setFitWidth(10);
						xView.setFitHeight(10);

						rememberToggle.setGraphic(xView);
					}
				});

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

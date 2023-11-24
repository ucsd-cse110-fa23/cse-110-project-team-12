package edu.ucsd.cse110.client;

import java.io.FileInputStream;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class CreateAccountView extends VBox implements UIInterface {
	private Controller controller;
	boolean rememberMe;
	boolean invalid;

	public CreateAccountView(Controller c) {
		this.controller = c;
	}


	@Override
	public void receiveMessage(Message m) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addChild(Node ui) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeChild(Node ui) {
		// TODO Auto-generated method stub
	}

	@Override
	public Parent getUI() {
		return this;
	}

}

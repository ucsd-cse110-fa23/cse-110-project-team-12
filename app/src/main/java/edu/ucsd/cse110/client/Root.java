package edu.ucsd.cse110.client;

import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class Root extends StackPane implements UIInterface {
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

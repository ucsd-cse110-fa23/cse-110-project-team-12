package edu.ucsd.cse110.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

// Helps place buttons and boxs where they need to be in the AppFrame
class Spacer extends HBox {
	Spacer(Node node, Insets insets, Pos pos) {
		super(node);
		this.setPadding(insets);
		this.setAlignment(pos);
	}
}
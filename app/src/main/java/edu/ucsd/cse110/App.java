package edu.ucsd.cse110;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import edu.ucsd.cse110.client.AppFrame;

// Main class that starts the application
public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AppFrame root = new AppFrame();
		Scene scene = new Scene(root, 325, 450);

        primaryStage.setTitle("");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

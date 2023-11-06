package edu.ucsd.cse110;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import edu.ucsd.cse110.client.AppFrame;

// Main class that starts the application
public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AppFrame root = new AppFrame();
		Scene scene = new Scene(root, 325, 450);
		scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package edu.ucsd.cse110;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import edu.ucsd.cse110.api.ChatGPT;
import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.VoicePrompt;
import edu.ucsd.cse110.api.Whisper;

// Main class that starts the application
public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Controller controller = new Controller(true, new VoicePrompt("voice.wav"), new Whisper(), new ChatGPT());

        Scene scene = new Scene(controller.getUIRoot(), 325, 450);
        
		scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package edu.ucsd.cse110;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

class Header extends VBox {
	Label title;
	Header() {
		this.setPrefSize(325, 68);
		this.setStyle("-fx-background-color: #98D38E;");
		this.setAlignment(Pos.CENTER);
		
		DropShadow dropShadow = new DropShadow(15, Color.BLACK); 
        this.setEffect(dropShadow);

		title = new Label("PantryPal");
		title.setFont(new Font("Helvetica Bold", 40));
		title.setTextFill(Color.WHITE);
		this.getChildren().add(title);
	}
}

class Spacer extends HBox {
	Spacer(Node node, Insets insets, Pos pos) {
		super(node);
		this.setPadding(insets);
		this.setAlignment(pos);
	}
}

class AppFrame extends BorderPane {
	private Header header;
	private CreateRecipeButton createRecipeButton;
	private CreateRecipe createRecipe;
	private StackPane stackPane;

	boolean creatingRecipe;

    AppFrame() {
		this.header = new Header();
		this.createRecipeButton = new CreateRecipeButton();		
		this.createRecipe = new CreateRecipe();

		Spacer buttonSpacer = new Spacer(createRecipeButton, new Insets(0, 10, 10, 0), Pos.BOTTOM_RIGHT);
		Spacer newRecipeSpacer = new Spacer(createRecipe, new Insets(30, 0, 0, 0), Pos.TOP_CENTER);
		this.stackPane = new StackPane(buttonSpacer, newRecipeSpacer);

		this.setStyle("-fx-background-color: #FAF9F6;");
        this.setTop(header);
        this.setCenter(stackPane);
    }

	void addListeners() {
		createRecipeButton.setOnAction(
            e -> {
				if (!creatingRecipe) {
					creatingRecipe = true;
					createRecipe = new CreateRecipe();
					stackPane.getChildren().add(createRecipe);
				}
            }
        );
	}
}

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AppFrame root = new AppFrame();
		Scene scene = new Scene(root, 325, 450);

        primaryStage.setTitle("PantryPal");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

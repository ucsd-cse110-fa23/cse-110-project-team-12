package edu.ucsd.cse110.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;


public class CreateRecipe extends VBox{
    private VoicePrompt mealtypePrompt;
    private VoicePrompt ingredientPrompt;
    private Button backButton;
    private int page;
    private String selectedMealType;

    public CreateRecipe() {
        this.setPrefSize(400, 400);
        this.setStyle("-fx-background-color: #000;");
        this.page = 0;

        this.backButton = new Button("Back");
        this.backButton.setOnAction(
            e -> {
                if (this.page == 0) {
                    Stage stage = (Stage) this.backButton.getScene().getWindow();
                    stage.close();
                } else {
                    this.page--;
                    this.update();
                }
            }
        );
    }

    public void update() {
        this.getChildren().clear();
        if (this.page == 0) {

            Text mealOptionHeading = new Text(20, 10, "Select Meal Type");
            Text mealOptions = new Text(16, 10, "Breakfast\nLunch\nDinner");

            this.getChildren().clear();
            this.getChildren().addAll(this.backButton, mealOptionHeading, mealOptions, this.mealtypePrompt);
        } else if (this.page == 1) {

            Text mealType = new Text(20, 10, "Meal Type: \n" + this.selectedMealType);
            Text ingredientPrompt = new Text(20, 10, "What is in your pantry?");

            this.getChildren().clear();
            this.getChildren().addAll(this.backButton, mealType, ingredientPrompt, this.ingredientPrompt);
        } else {
            this.getChildren().addAll(new Recipe());
        }
    }

    public void nextPage() {
        this.page++;
        this.update();
    }

}

class CreateRecipeButton extends Button {
    private Button createRecipeButton;
    
    public CreateRecipeButton() {
        createRecipeButton = new Button("+");
        createRecipeButton.setOnAction(
            e -> {
                Scene scene = new Scene(new CreateRecipe(), 400, 400);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
        );
    }
}

package edu.ucsd.cse110.client;

import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.UIInterface;
import edu.ucsd.cse110.server.schemas.RecipeSchema;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;

public class RecipeTitleView extends StackPane implements UIInterface {
    private Controller controller;
    private Button titleButton;
	private Text titleText;
    private RecipeSchema recipe;

	private double titleDefaultSize;
    private double titleWidthLimit;

    public RecipeTitleView(Controller c, RecipeSchema r) {
        controller = c;
        this.recipe = r;

        titleButton = new Button();
		titleText = new Text(r.title);
		titleDefaultSize = 20;
        titleWidthLimit = 257;
        setTitleFont(titleText, titleDefaultSize, titleWidthLimit);		
		
		Image icon = null;
		try {
			if (r.mealType.equals("Breakfast")) {
				icon = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/sun.png"));
			}
			else if (r.mealType.equals("Lunch")) {
				icon = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/cloud.png"));
			}
			else if (r.mealType.equals("Dinner")) {
				icon = new Image(new FileInputStream("./src/main/java/edu/ucsd/cse110/client/resources/moon.png"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		ImageView iconView = new ImageView(icon);
		iconView.setFitWidth(28);
		iconView.setFitHeight(28);
		HBox iconAndTitle = new HBox(iconView, titleText);
		iconAndTitle.setId("icon-and-title");

        titleButton.setId("recipe-title-button");
		titleText.setId("recipe-title-text");
		this.setId("recipe-title-view");
        this.getChildren().addAll(iconAndTitle, titleButton);

        titleButton.setOnAction(e -> {
            controller.receiveMessageFromUI(new Message(Message.HomeView.OpenRecipe, "Recipe", recipe));
        });
    }

    @Override
    public void receiveMessage(Message m) {
    }

    @Override
    public void addChild(Node n) {
    }

    @Override
    public void removeChild(Node n) {
    }

    @Override
    public Parent getUI() {
        return this;
    }

	private void setTitleFont(Text title, double size, double widthLimit) {
        title.setFont(new Font("Arial Bold", size));
        double width = title.getLayoutBounds().getWidth();
        if (width >= widthLimit) {
            size -= 0.25;
            setTitleFont(title, size, widthLimit);
        }
    }
}

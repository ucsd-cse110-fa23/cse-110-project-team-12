package edu.ucsd.cse110.client;

import edu.ucsd.cse110.api.ChatGPT;
import edu.ucsd.cse110.api.ChatGPTInterface;
import edu.ucsd.cse110.api.ChatGPTMock;
import edu.ucsd.cse110.api.VoicePrompt;
import edu.ucsd.cse110.api.VoicePromptInterface;
import edu.ucsd.cse110.api.Whisper;
import edu.ucsd.cse110.api.WhisperInterface;
import edu.ucsd.cse110.api.WhisperMock;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

// AppFrame that holds the RecipeList, createRecipeButton, createRecipe and more!
public class AppFrame extends BorderPane {
	private Header header;
	private CreateRecipeButton createRecipeButton;
	private Spacer createRecipeSpacer;
	private Button createButton;
	private StackPane content;

	private WhisperInterface whisper;
	private ChatGPTInterface chatGPT;
	private VoicePromptInterface voicePrompt;
	
	private CreateRecipe createRecipe;
	private boolean creatingRecipe;

    public AppFrame() {
		this.header = new Header();

		whisper = new Whisper();
		chatGPT = new ChatGPTMock(1);
		voicePrompt = new VoicePrompt("./voice.wav");

		this.createRecipeButton = new CreateRecipeButton();	
		this.createButton = createRecipeButton.getCreateButton();

		// spacers to put createRecipeButton in the bottom right
		Spacer circleSpacer = new Spacer(createRecipeButton.getCircle(), new Insets(0, 10, 10, 0), Pos.BOTTOM_RIGHT);
		Spacer plusSpacer = new Spacer(createRecipeButton.getPlus(), new Insets(0, 17.5, 10, 0), Pos.BOTTOM_RIGHT);
		Spacer buttonSpacer = new Spacer(createButton, new Insets(0, 10, 10, 0), Pos.BOTTOM_RIGHT);

		this.content = new StackPane(circleSpacer, plusSpacer, buttonSpacer);
		this.setStyle("-fx-background-color: #FAF9F6;");
        this.setTop(header);
        this.setCenter(content);
		addListeners();
    }

	// adds functionality to the createRecipeButton
	void addListeners() {
		createButton.setOnAction(
            e -> {
				if (!creatingRecipe) {
					creatingRecipe = true;
					createRecipe = new CreateRecipe(this, whisper, chatGPT, voicePrompt);
					createRecipeSpacer = new Spacer(createRecipe, new Insets(30, 0, 0, 0), Pos.TOP_CENTER);
					content.getChildren().add(createRecipeSpacer);
				}
            }
        );
	}

	// reset AppFrame when the recipe creation stops
	public void stopCreating() {
		creatingRecipe = false;
		content.getChildren().remove(createRecipeSpacer);
	}
}
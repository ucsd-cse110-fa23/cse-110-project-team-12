package edu.ucsd.cse110.client;

import edu.ucsd.cse110.api.ChatGPT;
import edu.ucsd.cse110.api.ChatGPTInterface;
import edu.ucsd.cse110.api.CreateRecipeManager;
import edu.ucsd.cse110.api.VoicePrompt;
import edu.ucsd.cse110.api.VoicePromptInterface;
import edu.ucsd.cse110.api.Whisper;
import edu.ucsd.cse110.api.WhisperInterface;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

// AppFrame that holds the RecipeList, createRecipeButton, createRecipe and more!
public class AppFrame extends BorderPane implements AppFrameInterface {
	private Header header;
	private StackPane content;
	private CreateButtonModule createButtonModule;
	private Button createButton;
	
	private WhisperInterface whisper;
	private ChatGPTInterface chatGPT;
	private VoicePromptInterface voicePrompt;
	
	private CreateRecipe createRecipe;
	private boolean creatingRecipe;

    public AppFrame() {
		this.header = new Header();

		whisper = new Whisper();
		chatGPT = new ChatGPT();
		voicePrompt = new VoicePrompt("./voice.wav");

		this.createButtonModule = new CreateButtonModule();	
		this.createButton = createButtonModule.getCreateButton();

		this.content = new StackPane();
		this.content.getChildren().addAll(createButtonModule.getComponents());

        this.setTop(header);
        this.setCenter(content);
		this.setId("app-frame");

		addListeners();
    }

	// adds functionality to the createRecipeButton
	private void addListeners() {
		createButton.setOnAction(
            e -> {
				if (!creatingRecipe) {
					creatingRecipe = true;
					CreateRecipeManager manager = new CreateRecipeManager(voicePrompt, whisper, chatGPT);
					createRecipe = new CreateRecipe(this, manager);
					content.getChildren().add(createRecipe.getSpacer());
				}
            }
        );
	}

	// reset AppFrame when the recipe creation stops
	public void stopCreating() {
		creatingRecipe = false;
		content.getChildren().remove(createRecipe.getSpacer());
	}
}
package edu.ucsd.cse110.api;

import edu.ucsd.cse110.api.CreateRecipeManager;
import edu.ucsd.cse110.client.HomeView;

public class HomeManager extends ManagerInterface {
    public enum ViewType {
        CreateRecipeView,
        DetailRecipeView,
    }
    public enum UpdateType {
        Start,
        Close,
    }
    
    private boolean creatingRecipe;

    private boolean useUI;
    private CreateRecipeManager createRecipeManager;
    private VoicePromptInterface voicePrompt;
    private WhisperInterface whisper;
    private ChatGPTInterface chatGPT;

    public HomeManager(boolean useUI, VoicePromptInterface voicePrompt, WhisperInterface whisper, ChatGPTInterface chatGPT) {
        super();
        this.useUI = useUI;
        this.voicePrompt = voicePrompt;
        this.whisper = whisper;
        this.chatGPT = chatGPT;

        if(useUI) this.ui = new HomeView(this);
    }

    public void updateView(ViewType vt, UpdateType ut) {
        switch (vt) {
            case CreateRecipeView:
                switch (ut) {
                    case Start:
                        startCreateRecipeView();
                        break;
                    case Close:
                        closeCreateRecipeView();
                        break;
                    default:
                        break;
                }
            case DetailRecipeView:
                switch (ut) {
                    case Start:
                        startDetailRecipeView();
                        break;
                    case Close:
                        closeDetailRecipeView();
                        break;
                    default:
                        break;
                }
            default:
                break;
        }
    }

    public boolean getIsCreatingRecipe() {
        return creatingRecipe;
    }

    private void startCreateRecipeView() {
		if (!creatingRecipe) {
			creatingRecipe = true;
            createRecipeManager = new CreateRecipeManager(useUI, voicePrompt, whisper, chatGPT);
            createRecipeManager.addHomeManager(this);
            ui.addNode(createRecipeManager.getUI());
		}
	}

    private void closeCreateRecipeView() {
        creatingRecipe = false;
        ui.removeNode(createRecipeManager.getUI());
    }

    private void startDetailRecipeView() {
        
    }

    private void closeDetailRecipeView() {
        
    }
}

package edu.ucsd.cse110.client;

import edu.ucsd.cse110.api.*;

public class AppFrameMock implements AppFrameInterface {

    private CreateRecipeManager manager;

    private WhisperInterface whisper;
	private ChatGPTInterface chatGPT;
	private VoicePromptInterface voicePrompt;

    private boolean creatingRecipe;
    
    public AppFrameMock() {

        whisper = new WhisperMock();
        chatGPT = new ChatGPTMock();
        voicePrompt = new VoicePromptMock(null);
        creatingRecipe = false;
    }
    
    public void clickAddButton() {

        if (!creatingRecipe) {
            manager = new CreateRecipeManager(voicePrompt, whisper, chatGPT);
            creatingRecipe = true;
        }
    }

    public void stopCreating() {

        creatingRecipe = false;
        manager = null;
    }

    public CreateRecipeManager getRecipeManager() {

        return this.manager;
    }

    public boolean getCreatingRecipe() {

        return this.creatingRecipe;
    }
}

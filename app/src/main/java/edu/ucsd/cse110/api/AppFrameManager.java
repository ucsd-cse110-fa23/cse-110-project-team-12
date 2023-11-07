package edu.ucsd.cse110.api;

public class AppFrameManager {
    private boolean creatingRecipe;

    public AppFrameManager() {
        creatingRecipe = false;
    }

    public boolean getIsCreatingRecipe() {
        return creatingRecipe;
    }

    public void createRecipe() {
        creatingRecipe = true;
    }

    public void stopCreating() {
        creatingRecipe = false;
    }
}

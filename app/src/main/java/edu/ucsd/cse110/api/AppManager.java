package edu.ucsd.cse110.api;


public class AppManager extends ManagerInterface {
    public enum ViewType {
        CreateRecipeView,
        DetailRecipeView,
    }
    public enum UpdateType {
        Start,
        Close,
    }
    
    private boolean creatingRecipe;

    private CreateRecipeManager createRecipeManager;

    public AppManager(CreateRecipeManager createRecipeManager) {
        super();
        this.createRecipeManager = createRecipeManager;
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

package edu.ucsd.cse110.api;


public class RecipeDetailedModel implements ModelInterface {
    Controller controller;
    private String recipeName;
    private String recipeBody;
    public RecipeDetailedModel(Controller c) {
        controller = c;
    }
    @Override
    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.CreateRecipeModel.SendTitleBody) {
            this.recipeName = (String) m.getKey("RecipeTitle");
            this.recipeBody = (String) m.getKey("RecipeBody");
        }
    }
    @Override
    public Object getState() {
        return this;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public String getRecipeBody() {
        return recipeBody;
    }
}

package edu.ucsd.cse110.api;

import java.util.Map;

public class RecipeDetailedModel implements ModelInterface {
    Controller controller;
    private String recipeTitle;
    private String recipeBody;
    public RecipeDetailedModel(Controller c) {
        controller = c;
    }
    @Override
    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.CreateRecipeModel.SendTitleBody) {
            this.recipeTitle = (String) m.getKey("RecipeTitle");
            this.recipeBody = (String) m.getKey("RecipeBody");
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetTitleBody,
                                                            Map.ofEntries(Map.entry("RecipeTitle", this.recipeTitle),
                                                            Map.entry("RecipeBody", this.recipeBody))));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseUnsavedLayout));
        }
        if (m.getMessageType() == Message.RecipeDetailedView.CancelButton || m.getMessageType() == Message.RecipeDetailedView.BackButton) {
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
        }
        if (m.getMessageType() == Message.RecipeDetailedView.SaveButton) {
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveUnsavedLayout));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
            // TODO: further actions to add database
        }
        if (m.getMessageType() == Message.RecipeDetailedView.DeleteButton) {
            // TODO: further actions to add delete confirmation page
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
        }
        if (m.getMessageType() == Message.RecipeDetailedView.EditButton) {
            // TODO: further actions to add edit functionality
        }
    }
    @Override
    public Object getState() {
        return this;
    }
    public String getRecipeTitle() {
        return recipeTitle;
    }
    public String getRecipeBody() {
        return recipeBody;
    }
}

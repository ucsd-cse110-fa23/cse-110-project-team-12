package edu.ucsd.cse110.api;

import java.util.Map;

import edu.ucsd.cse110.client.Recipe;

public class RecipeDetailedModel implements ModelInterface {
    public enum PageType {
        UnsavedLayout,
        SavedLayout,
        EditLayout,
        DeleteConfirmation;
    }

    private PageType currentPage;
    private Controller controller;
    private Recipe recipe;

    public RecipeDetailedModel(Controller c) {
        controller = c;
    }

    @Override
    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.HomeModel.SendTitleBody) {
            recipe = (Recipe) m.getKey("Recipe");
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetTitle,
                    Map.ofEntries(Map.entry("Recipe", recipe))));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetBody,
                    Map.ofEntries(Map.entry("Recipe", recipe))));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.AddBackButton));
            currentPage = PageType.SavedLayout;
        }
        if (m.getMessageType() == Message.CreateRecipeModel.SendTitleBody) {
            recipe = (Recipe) m.getKey("Recipe");
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetTitle,
                    Map.ofEntries(Map.entry("Recipe", recipe))));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetBody,
                    Map.ofEntries(Map.entry("Recipe", recipe))));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseUnsavedLayout));
            currentPage = PageType.UnsavedLayout;
        }
        if (m.getMessageType() == Message.RecipeDetailedView.CancelButton) {
            if (currentPage == PageType.UnsavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
            } else if (currentPage == PageType.EditLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveEditRecipe));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveUnsavedLayout));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetBody,
                        Map.ofEntries(Map.entry("Recipe", recipe))));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.AddBackButton));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
                currentPage = PageType.SavedLayout;
            }
        }
        if (m.getMessageType() == Message.RecipeDetailedView.BackButton) {
            if (currentPage == PageType.SavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
            } else if (currentPage == PageType.DeleteConfirmation) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveDeleteConfirmation));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetTitle,
                        Map.ofEntries(Map.entry("Recipe", recipe))));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetBody,
                        Map.ofEntries(Map.entry("Recipe", recipe))));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
                currentPage = PageType.SavedLayout;
            }
        }
        if (m.getMessageType() == Message.RecipeDetailedView.UpdateInformation) {
            String updatedRecipeBody = (String) m.getKey("RecipeBody");
            recipe.setInformation(updatedRecipeBody);
            controller.mongoDB.updateRecipe(recipe.getName(), recipe.getInformation(), recipe.getMealType());
        }
        if (m.getMessageType() == Message.RecipeDetailedView.SaveButton) {
            if (currentPage == PageType.UnsavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveUnsavedLayout));
                controller.mongoDB.saveRecipe(recipe.getName(), recipe.getInformation(), recipe.getMealType());
                // controller.receiveMessageFromModel(new
                // Message(Message.RecipeDetailedModel.SaveConfirmation));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.AddBackButton));
                controller.receiveMessageFromModel(new Message(Message.HomeView.UpdateRecipeList));
                currentPage = PageType.SavedLayout;
            } else if (currentPage == PageType.EditLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveEditRecipe));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveUnsavedLayout));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetBody,
                        Map.ofEntries(Map.entry("Recipe", recipe))));
                // if (updatedRecipeBody != "")
                // controller.receiveMessageFromModel(new
                // Message(Message.RecipeDetailedModel.SaveConfirmation));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.AddBackButton));
                controller.receiveMessageFromModel(new Message(Message.HomeView.UpdateRecipeList));
                currentPage = PageType.SavedLayout;
            }
        }
        if (m.getMessageType() == Message.RecipeDetailedView.DeleteButton) {
            if (currentPage == PageType.SavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.GoToDeleteConfirmationPage));
                currentPage = PageType.DeleteConfirmation;
            }
        }
        if (m.getMessageType() == Message.RecipeDetailedView.ConfirmDeleteButton) {
            if (currentPage == PageType.DeleteConfirmation) {
                controller.mongoDB.deleteRecipe(recipe.getName());
                controller.receiveMessageFromModel(new Message(Message.HomeView.UpdateRecipeList));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
            }
        }
        if (m.getMessageType() == Message.RecipeDetailedView.EditButton) {
            if (currentPage == PageType.SavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.EditRecipe,
                        Map.ofEntries(Map.entry("RecipeBody", recipe.getInformation()))));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveBackButton));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseUnsavedLayout));
                currentPage = PageType.EditLayout;
            }
        }
    }

    

    @Override
    public Object getState() {
        return this;
    }

    public String getRecipeTitle() {
        return recipe.getName();
    }

    public String getRecipeBody() {
        return recipe.getInformation();
    }

    public PageType getCurrentPage() {
        return this.currentPage;
    }

    // private void saveToJSON(String recipeTitle, String recipeBody) {
    // try {
    // Path path = Paths.get(Controller.storagePath + "json");
    // Files.createDirectories(path.getParent());

    // String jsonString = "{\"RecipeTitle\":\"" + recipeTitle +
    // "\",\"RecipeBody\":\""
    // + recipeBody + "\"}\n";

    // try (Writer writer = new FileWriter(path.toFile(), true)) {
    // writer.write(jsonString);
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
}

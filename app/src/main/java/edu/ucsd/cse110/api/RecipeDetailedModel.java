package edu.ucsd.cse110.api;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.services.Utils;

public class RecipeDetailedModel implements ModelInterface {
    public enum PageType {
        UnsavedLayout,
        SavedLayout,
        EditLayout,
        DeleteConfirmation;
    }

    PageType currentPage;
    Controller controller;
    RecipeSchema recipe;

    public RecipeDetailedModel(Controller c) {
        controller = c;
    }

    @Override
    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.HomeModel.SendRecipe) {
            recipe = m.getKey("Recipe");
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetRecipe, "Recipe", recipe));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.AddBackButton));
            currentPage = PageType.SavedLayout;
        }
        if (m.getMessageType() == Message.CreateRecipeModel.SendRecipe) {
            recipe = m.getKey("Recipe");
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetRecipe, "Recipe", recipe));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseUnsavedLayout));
            currentPage = PageType.UnsavedLayout;
        }
        if (m.getMessageType() == Message.RecipeDetailedView.CancelButton) {
            if (currentPage == PageType.UnsavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
            } else if (currentPage == PageType.EditLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.ExitEditRecipe));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetRecipe, "Recipe", recipe));
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
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetRecipe, "Recipe", recipe));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
                currentPage = PageType.SavedLayout;
            }
        }
        if (m.getMessageType() == Message.RecipeDetailedView.UpdateInformation) {
            String updatedRecipeBody = m.getKey("RecipeBody");
            recipe.description = updatedRecipeBody;
            updateRecipe(recipe._id, recipe.title, updatedRecipeBody, recipe.base64ImageEncoding);
        }
        if (m.getMessageType() == Message.RecipeDetailedView.SaveButton) {
            if (currentPage == PageType.UnsavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveUnsavedLayout));
                recipe = saveRecipe(recipe);
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.AddBackButton));
                controller.receiveMessageFromModel(new Message(Message.HomeView.UpdateRecipeList));
                currentPage = PageType.SavedLayout;
            } else if (currentPage == PageType.EditLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.ExitEditRecipe));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetRecipe, "Recipe", recipe));
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
                deleteRecipe(recipe._id);
                controller.receiveMessageFromModel(new Message(Message.HomeView.UpdateRecipeList));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
            }
        }
        if (m.getMessageType() == Message.RecipeDetailedView.EditButton) {
            if (currentPage == PageType.SavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.EditRecipe));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveBackButton));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseUnsavedLayout));
                currentPage = PageType.EditLayout;
            }
        }
        if (m.getMessageType() == Message.RecipeDetailedView.ShareButton) {
            if (currentPage == PageType.SavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.StartSharePopupView));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetRecipeShareLink,
                    "RecipeShareLink", "localhost:8100/share?recipeId=" + recipe._id));
            }
        }
        if (m.getMessageType() == Message.RecipeDetailedView.RefreshButton) {
            // Will be refreshed.
            recipe.base64ImageEncoding = "";
            recipe.title = "";
            recipe.description = "";
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.Refresh, "RecipeBody", recipe));
        }
    }

    private void updateRecipe(String recipeId, String newTitle, String newDescription, String newImageEncoding) {
        String urlString = Controller.serverUrl + "/recipe";
        RecipeSchema changes = new RecipeSchema();
        changes._id = recipeId;
        changes.title = newTitle;
        changes.description = newDescription;
        changes.base64ImageEncoding = newImageEncoding;
        ServerResponse response = controller.server.makeHttpRequest(urlString, "PUT", Utils.marshalJson(changes));

        if (response.getStatusCode() != 200)
            System.out.println("Update recipe failed. 😭😭😭😭😭😭😭");
    }

    // Returns new recipe with recipe id added.
    private RecipeSchema saveRecipe(RecipeSchema recipe) {
        recipe.userId = controller.getCurrentUser()._id;
        String urlString = Controller.serverUrl + "/recipe";
        ServerResponse response = controller.server.makeHttpRequest(urlString, "POST", Utils.marshalJson(recipe));

        if (response.getStatusCode() == 201)
            return Utils.unmarshalJson(response.getResponseBody(), RecipeSchema.class);
        else
            return null;
    }

    private void deleteRecipe(String recipeId) {
        String urlString = Controller.serverUrl + "/recipe?recipeId=" + recipeId;

        ServerResponse response = controller.server.makeHttpRequest(urlString, "DELETE", "");
        if (response.getStatusCode() != 200)
            System.out.println("Error delete recipe 😭😭😭😭😭😭😭😭😭😭.");
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

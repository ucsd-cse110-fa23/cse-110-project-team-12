package edu.ucsd.cse110.api;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.services.Utils;
import java.io.*;
import java.net.*;
import java.util.*;

public class RecipeDetailedModel implements ModelInterface {
    public enum PageType {
        UnsavedLayout,
        SavedLayout,
        EditLayout,
        DeleteConfirmation;
    }

    private PageType currentPage;
    private Controller controller;
    private RecipeSchema recipe;

    public RecipeDetailedModel(Controller c) {
        controller = c;
    }

    @Override
    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.HomeModel.SendRecipe) {
            recipe = (RecipeSchema) m.getKey("Recipe");
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetRecipe,
                    Map.ofEntries(Map.entry("Recipe", recipe))));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.AddBackButton));
            currentPage = PageType.SavedLayout;
        }
        if (m.getMessageType() == Message.CreateRecipeModel.SendRecipe) {
            recipe = (RecipeSchema) m.getKey("Recipe");
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetRecipe,
                    Map.ofEntries(Map.entry("Recipe", recipe))));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseUnsavedLayout));
            currentPage = PageType.UnsavedLayout;
        }
        if (m.getMessageType() == Message.RecipeDetailedView.CancelButton) {
            if (currentPage == PageType.UnsavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
            } else if (currentPage == PageType.EditLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.ExitEditRecipe));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetRecipe,
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
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetRecipe,
                        Map.ofEntries(Map.entry("Recipe", recipe))));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
                currentPage = PageType.SavedLayout;
            }
        }
        if (m.getMessageType() == Message.RecipeDetailedView.UpdateInformation) {
            String updatedRecipeBody = (String) m.getKey("RecipeBody");
            recipe.description = updatedRecipeBody;
            updateRecipe(recipe._id, recipe.title, updatedRecipeBody);
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
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetRecipe,
                        Map.ofEntries(Map.entry("Recipe", recipe))));
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
    }

    private void updateRecipe(String recipeId, String newTitle, String newDescription) {
        try {
            String urlString = Controller.serverUrl + "/recipe";
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            RecipeSchema changes = new RecipeSchema();
            changes._id = recipeId;
            changes.title = newTitle;
            changes.description = newDescription;
            out.write(Utils.marshalJson(changes));
            out.flush();
            out.close();

            if (conn.getResponseCode() != 200)
                throw new Exception("Update Recipe Failed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Returns new recipe with recipe id added.
    private RecipeSchema saveRecipe(RecipeSchema recipe) {
        try {
            recipe.userId = controller.getCurrentUser()._id;

            String urlString = Controller.serverUrl + "/recipe";
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(Utils.marshalJson(recipe));
            out.flush();
            out.close();

            if (conn.getResponseCode() == 201) {
                Scanner in = new Scanner(conn.getInputStream());
                String jsonString = "";
                while (in.hasNext())
                    jsonString += in.nextLine();
                in.close();
                return Utils.unmarshalJson(jsonString, RecipeSchema.class);
            }
            else {
                throw new Exception("Save Recipe Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void deleteRecipe(String recipeId) {
        try {
            String urlString = Controller.serverUrl + "/recipe?recipeId=" + recipeId;
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("DELETE");
            conn.connect();

            if (conn.getResponseCode() != 200)
                throw new Exception("Delete Recipe Failed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getState() {
        return this;
    }

    public RecipeSchema getRecipeSchema() {
        return recipe;
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

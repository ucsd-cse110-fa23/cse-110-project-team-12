package edu.ucsd.cse110.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.client.Recipe;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RecipeDetailedModel implements ModelInterface {
    public enum PageType{
        UnsavedLayout,
        SavedLayout,
        EditLayout,
        DeleteConfirmation;
    }
    PageType currentPage;
    Controller controller;
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
            } else if(currentPage == PageType.EditLayout) {
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
            if(currentPage == PageType.SavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
            } else if(currentPage == PageType.DeleteConfirmation) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveDeleteConfirmation));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetTitle,
                    Map.ofEntries(Map.entry("Recipe", recipe))));
                    controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetBody,
                    Map.ofEntries(Map.entry("Recipe", recipe))));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
                currentPage = PageType.SavedLayout;
            }
        }
        if(m.getMessageType() == Message.RecipeDetailedView.UpdateInformation) {
            String updatedRecipeBody = (String) m.getKey("RecipeBody");
            recipe.setInformation(updatedRecipeBody);
            this.updateCSV(recipe.getName(), recipe.getInformation());
        }
        if (m.getMessageType() == Message.RecipeDetailedView.SaveButton) {
            if(currentPage == PageType.UnsavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveUnsavedLayout));
                this.saveToCSV(recipe.getName(), recipe.getInformation());
                //controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SaveConfirmation));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.AddBackButton));
                controller.receiveMessageFromModel(new Message(Message.HomeView.UpdateRecipeList));
                currentPage = PageType.SavedLayout;
            } else if(currentPage == PageType.EditLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveEditRecipe));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveUnsavedLayout));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetBody,
                    Map.ofEntries(Map.entry("Recipe", recipe))));
                // if (updatedRecipeBody != "")
                //     controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SaveConfirmation));
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
            if (currentPage == PageType.DeleteConfirmation){
                this.deleteFromCSV(recipe.getName());
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

    // private void saveToJSON(String recipeTitle, String recipeBody) {
    //     try {
    //         Path path = Paths.get(Controller.storagePath + "json");
    //         Files.createDirectories(path.getParent());

    //         String jsonString = "{\"RecipeTitle\":\"" + recipeTitle + "\",\"RecipeBody\":\""
    //                 + recipeBody + "\"}\n";

    //         try (Writer writer = new FileWriter(path.toFile(), true)) {
    //             writer.write(jsonString);
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    private String escapeField(String field) {
        return "\"" + field.replaceAll("\n", "{NEWLINE}") + "\"";
    }

    public void saveToCSV(String recipeTitle, String recipeBody) {
        try {
            Path path = Paths.get(Controller.storagePath + "csv");
            Files.createDirectories(path.getParent());
            try (Writer writer = new FileWriter(path.toFile(), true)) {
                writer.write(escapeField(recipeTitle) + "," + escapeField(recipeBody) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCSV(String recipeTitle, String recipeBody) {
        try {
            Path path = Paths.get(Controller.storagePath + "csv");
            List<String> csvContents = new ArrayList<>(Files.readAllLines(path));
            for (int i = 0; i < csvContents.size(); i++) {
                if (csvContents.get(i).contains(escapeField(recipeTitle) + ",")) {
                    csvContents.set(i, escapeField(recipeTitle) + "," + escapeField(recipeBody));
                    break;
                }
            }
            Files.write(path, csvContents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFromCSV(String recipeTitle) {

        try {
            Path path = Paths.get(Controller.storagePath + "csv");
            List<String> allLines = Files.readAllLines(path);
            List<String> updatedLines = new ArrayList<>();

            for (String line : allLines) {
                if (!line.contains(recipeTitle)) {
                    updatedLines.add(line);
                }
            }
            Files.write(path, updatedLines);
        } catch (IOException e) {
            e.printStackTrace();
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
}

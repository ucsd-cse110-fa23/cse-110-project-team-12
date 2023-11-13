package edu.ucsd.cse110.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.client.Recipe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
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
        if (m.getMessageType() == Message.CreateRecipeModel.SendTitleBody) {
            recipe = (Recipe) m.getKey("Recipe");
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetTitleBody,
                    Map.ofEntries(Map.entry("Recipe", new Recipe(recipe.getName(), recipe.getInformation())))));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseUnsavedLayout));
            currentPage = PageType.UnsavedLayout;
        }
        if (m.getMessageType() == Message.RecipeDetailedView.CancelButton) {
            if (currentPage == PageType.UnsavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
            }
        }
        if (m.getMessageType() == Message.RecipeDetailedView.BackButton) {
            if(currentPage == PageType.SavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
            } else if(currentPage == PageType.DeleteConfirmation) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveDeleteConfirmation));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetTitleBody,
                    Map.ofEntries(Map.entry("Recipe", new Recipe(recipe.getName(), recipe.getInformation())))));
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
                currentPage = PageType.SavedLayout;
            }
        }
        if (m.getMessageType() == Message.RecipeDetailedView.SaveButton) {
            if(currentPage == PageType.UnsavedLayout) {
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveUnsavedLayout));
                this.saveToCSV(recipe.getName(), recipe.getInformation());
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SaveConfirmation));
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
                this.deleteFromCSV(recipe.getName(), recipe.getInformation());
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
            }
        }
        if (m.getMessageType() == Message.RecipeDetailedView.EditButton) {
            // TODO: further actions to add edit functionality
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

    private void saveToCSV(String recipeTitle, String recipeBody) {
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

    private void deleteFromCSV(String recipeTitle, String recipeBody) {
        try {
            Path path = Paths.get(Controller.storagePath + "csv");
            List<String> allLines = Files.readAllLines(path);
            List<String> updatedLines = new ArrayList<>();

            for (String line : allLines) {
                if (!(escapeField(recipeTitle) + "," + escapeField(recipeBody)).equals(line)) {
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
}

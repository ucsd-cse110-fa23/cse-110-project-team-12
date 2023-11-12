package edu.ucsd.cse110.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        if (m.getMessageType() == Message.RecipeDetailedView.CancelButton
                || m.getMessageType() == Message.RecipeDetailedView.BackButton) {
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
        }
        if (m.getMessageType() == Message.RecipeDetailedView.SaveButton) {
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveUnsavedLayout));
            this.saveToCSV(recipeTitle, recipeBody);
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SaveConfirmation));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
        }
        if (m.getMessageType() == Message.RecipeDetailedView.DeleteButton) {
            // TODO: further actions to add delete confirmation page
            this.deleteFromCSV(recipeTitle, recipeBody);
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
        }
        if (m.getMessageType() == Message.RecipeDetailedView.EditButton) {
            // TODO: further actions to add edit functionality
        }
    }

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
        return recipeTitle;
    }

    public String getRecipeBody() {
        return recipeBody;
    }
}

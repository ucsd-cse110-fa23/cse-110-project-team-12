package edu.ucsd.cse110.api;

import java.util.Map;
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
            // controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
            this.saveToCSV(recipeTitle, recipeBody);
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SaveConfirmation));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
        }
        if (m.getMessageType() == Message.RecipeDetailedView.DeleteButton) {
            // TODO: further actions to add delete confirmation page
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
        }
        if (m.getMessageType() == Message.RecipeDetailedView.EditButton) {
            // TODO: further actions to add edit functionality
        }
    }

    private void saveToCSV(String recipeTitle, String recipeBody) {
        try {
            Path path = Paths.get(Controller.storagePath);
            Files.createDirectories(path.getParent()); // Create directories if they don't exist
            try (Writer writer = new FileWriter(path.toFile(), true)) {
                // Write the new recipe entry
                writer.write(escapeField(recipeTitle) + "," + escapeField(recipeBody) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String escapeField(String field) {
        return "\"" + field.replaceAll("\n", "{NEWLINE}") + "\"";
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

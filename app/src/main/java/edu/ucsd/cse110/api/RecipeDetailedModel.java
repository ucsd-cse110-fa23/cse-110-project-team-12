package edu.ucsd.cse110.api;

import java.util.List;
import java.util.ArrayList;
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
            this.saveToCSV(recipeTitle, recipeBody);
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SaveConfirmation));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
        }
        if (m.getMessageType() == Message.RecipeDetailedView.DeleteButton) {
            // TODO: further actions to add delete confirmation page
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.CloseRecipeDetailedView));
        }
        if (m.getMessageType() == Message.RecipeDetailedView.EditButton) {
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.EditRecipe,
                Map.ofEntries(Map.entry("RecipeBody", this.recipeBody))));

            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseUnsavedLayout));
        }
        if (m.getMessageType() == Message.RecipeDetailedView.ExitEditAction) {

            String updatedRecipeBody = (String) m.getKey("RecipeBody");

            if (updatedRecipeBody != "") {

                this.recipeBody = updatedRecipeBody;
                this.updateCSV(recipeTitle, recipeBody);
            }

            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveEditRecipe));
            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.RemoveUnsavedLayout));

            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SetTitleBody,
                Map.ofEntries(Map.entry("RecipeTitle", ""),
                Map.entry("RecipeBody", updatedRecipeBody))));

            if (updatedRecipeBody != "")
                controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.SaveConfirmation));

            controller.receiveMessageFromModel(new Message(Message.RecipeDetailedModel.UseSavedLayout));
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

    private void updateCSV(String recipeTitle, String recipeBody) {
        try {
            Path path = Paths.get(Controller.storagePath + "csv");

            if (Files.exists(path)) {

                List<String> csvContents = new ArrayList<>(Files.readAllLines(path));

                for (int i = 0; i < csvContents.size(); i++) {
                    if (csvContents.get(i).contains(escapeField(recipeTitle) + ",")) {

                        csvContents.set(i, escapeField(recipeTitle) + "," + escapeField(recipeBody));
                        break;
                    }
                }

                Files.write(path, csvContents);
            }
            else {
                // idk throw an error or something
            }
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

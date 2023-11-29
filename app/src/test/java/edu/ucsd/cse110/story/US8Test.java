package edu.ucsd.cse110.story;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import edu.ucsd.cse110.api.HomeModel;
import edu.ucsd.cse110.api.ChatGPTMock;
import edu.ucsd.cse110.api.Controller;
import edu.ucsd.cse110.api.CreateRecipeModel;
import edu.ucsd.cse110.api.Message;
import edu.ucsd.cse110.api.RecipeDetailedModel;
import edu.ucsd.cse110.api.VoicePromptInterface;
import edu.ucsd.cse110.api.VoicePromptMock;
import edu.ucsd.cse110.api.WhisperMock;

import java.util.List;
import java.util.ArrayList;

public class US8Test {
    private Controller controller;
    private VoicePromptInterface voice;

    // Tests the BDD Scenario listed for US8
    /*
     * Given I am waiting for the recipe to be created
     * When the recipe is created
     * Then I am taken to the detailed view for the recipe
     */
  
    // @Test
    // public void testUS8BDD1() {

    //     // Set up manager
    //     List<VoicePromptMock.PromptType> promptTypes = new ArrayList<>(); 
    //     promptTypes.add(VoicePromptMock.PromptType.MealType);
    //     promptTypes.add(VoicePromptMock.PromptType.IngredientsList);
    //     voice = new VoicePromptMock(promptTypes);
    //     controller = new Controller(false, voice, new WhisperMock(), new ChatGPTMock(), false);
    //     controller.receiveMessageFromUI(new Message(Message.HomeView.CreateRecipeButton));
    //     controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton));
    //     controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton));

    //     assertEquals(CreateRecipeModel.PageType.IngredientsInput, ((CreateRecipeModel) controller.getState(Controller.ModelType.CreateRecipe)).getCurrentPage());
    //     controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton));
    //     controller.receiveMessageFromUI(new Message(Message.CreateRecipeView.RecordButton)); // Given I am waiting for the recipe to be created -> When the Recipe is created

    //     // Then I am taken to the detailed view for the recipe
    //     assertEquals(Controller.UIType.DetailedView, ((HomeModel)controller.getState(Controller.ModelType.HomePage)).getCurrentView());

    //     assertNotEquals("", ((RecipeDetailedModel)controller.getState(Controller.ModelType.DetailedView)).getRecipeTitle());
    //     assertNotEquals("", ((RecipeDetailedModel)controller.getState(Controller.ModelType.DetailedView)).getRecipeBody());
    // }
}
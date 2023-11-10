package edu.ucsd.cse110.api;

import java.util.List;

import edu.ucsd.cse110.client.Recipe;

public class HomeModel implements ModelInterface {
    private Controller controller;

    private boolean creatingRecipe;

    // TODO: this is for our list of recipes
    private List<Recipe> savedRecipes;

    public HomeModel(Controller c) {
        this.controller = c;
    }

    public void receiveMessage(Message m) {
        
    }
}

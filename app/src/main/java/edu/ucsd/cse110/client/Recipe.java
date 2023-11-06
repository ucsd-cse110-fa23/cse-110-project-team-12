package edu.ucsd.cse110.client;

// This class represents a Recipe Object, that contains the name and information for a Recipe
public class Recipe {
    private String name;
    private String information;

    // Blank constructor for no fields
    public Recipe() {
        this.name = "";
        this.information = "";
    }

    // Constructor with given variables
    public Recipe(String recipeName, String recipeInformation) {
        this.name = recipeName;
        this.information = recipeInformation;
    }

    /**
     * 
     * @return String name of this Recipe
     */
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @return String information of this Recipe
     */
    public String getInformation() {
        return this.information;
    }

    /**
     * 
     * @param newName new name for this Recipe
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * 
     * @param newInformation new information for this Recipe
     */
    public void setInformation(String newInformation) {
        this.information = newInformation;
    }
}

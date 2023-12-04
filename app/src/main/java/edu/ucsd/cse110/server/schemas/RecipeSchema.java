package edu.ucsd.cse110.server.schemas;

public class RecipeSchema {
    // Must be mongodb objectId format: 24 characters.
    public String _id;
    // Must be mongodb objectId format: 24 characters. It is the "foreign key".
    public String userId;

    public String mealType;
    public String ingredients;
    public String timeCreated;

    public String title;
    public String description;
    public String base64ImageEncoding;

    public RecipeSchema() {}
}

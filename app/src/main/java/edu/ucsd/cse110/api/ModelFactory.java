package edu.ucsd.cse110.api;

public class ModelFactory {
    public enum Type {
        CreateRecipe,
        HomePage,
        DetailedView,
        SharePopup,
        CreateAccount,
        LogIn,
    }

    public static ModelInterface make(Type type, Controller c) {
        if (type == Type.CreateRecipe)
            return new CreateRecipeModel(c);
        else if (type == Type.HomePage)
            return new HomeModel(c);
        else if (type == Type.DetailedView)
            return new RecipeDetailedModel(c);
        else if (type == Type.SharePopup)
            return new SharePopupModel(c);
        else if (type == Type.CreateAccount)
            return new CreateAccountModel(c);
        else if (type == Type.LogIn)
            return new LogInModel(c);
        return null;
    }
}

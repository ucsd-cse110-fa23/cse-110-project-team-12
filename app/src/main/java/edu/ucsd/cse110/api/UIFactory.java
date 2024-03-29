package edu.ucsd.cse110.api;


import edu.ucsd.cse110.client.*;

public class UIFactory {
    public enum Type {
        CreateRecipe,
        HomePage,
        DetailedView,
        SharePopup,
		CreateAccount,
		LogIn,
        ServerErrorPopup,
    }

    public static UIInterface make(Type type, Controller c) {
        if(!c.useUI) return new NoUI();
        if (type == Type.CreateRecipe)
            return new CreateRecipeView(c);
        else if (type == Type.HomePage)
            return new HomeView(c);
        else if (type == Type.DetailedView)
            return new RecipeDetailedView(c);
        else if (type == Type.SharePopup)
            return new SharePopupView(c);
        else if (type == Type.CreateAccount)
            return new CreateAccountView(c);
        else if (type == Type.LogIn)
            return new LogInView(c);
        else if (type == Type.ServerErrorPopup)
            return new ErrorPopupView(c);
        return null;
    }
}
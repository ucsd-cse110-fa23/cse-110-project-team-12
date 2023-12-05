package edu.ucsd.cse110.api;

import java.util.*;

public class Message {

    // Message Type Interface
    public interface Type {
        default Set<String> allowedKeys() { return new HashSet<>();}
        default void checkPayload(Map<String, Object> payload) throws IllegalArgumentException {
            for(String key : allowedKeys())
                if(!payload.containsKey(key))
                    throw new IllegalArgumentException("Key not found: " + key);
        }
    }

    // Class Type Enum

    public enum LogInView implements Type {
        SignUpButton,
        LogInButton {
            Set<String> keys = new HashSet<>(Arrays.asList("Username", "Password", "AutomaticLogIn"));
            @Override public Set<String> allowedKeys() {return keys;}
        },
    }

    public enum LogInModel implements Type {
        CloseLogInView,
        StartCreateAccountView,
        StartHomeView,
        SetUser {
            Set<String> keys = new HashSet<>(Arrays.asList("User"));
            @Override public Set<String> allowedKeys() {return keys;}
        };
    }

    public enum CreateAccountView implements Type {
        BackButton,
        SignUpButton {
            Set<String> keys = new HashSet<>(Arrays.asList("Username", "Password","AutomaticLogIn"));
            @Override public Set<String> allowedKeys() {return keys;}
        },
    }

    public enum CreateAccountModel implements Type {
        CloseCreateAccountView,
        StartLogInView,
        StartHomeView,
        ErrorUsernameTaken,
        SetUser {
            Set<String> keys = new HashSet<>(Arrays.asList("User"));
            @Override public Set<String> allowedKeys() {return keys;}
        }
    }
    
    public enum HomeView implements Type {
        CreateRecipeButton, // HomeView.CreateRecipeButton
        FilterRecipeButton {
            Set<String> keys = new HashSet<>(Arrays.asList("FilterOption"));
            @Override public Set<String> allowedKeys() {return keys;}
        },
        SortRecipeButton {
            Set<String> keys = new HashSet<>(Arrays.asList("SortOption"));
            @Override public Set<String> allowedKeys() {return keys;}
        },
        UpdateRecipeList, 
        OpenRecipe {
            Set<String> keys = new HashSet<>(Arrays.asList("Recipe"));
            @Override public Set<String> allowedKeys() {return keys;}
        },
        LogOut
    }
    public enum HomeModel implements Type {
        StartCreateRecipeView,
        CloseCreateRecipeView,
        StartRecipeDetailedView,
        CloseRecipeDetailedView,
        UpdateRecipeList {
            Set<String> keys = new HashSet<>(Arrays.asList("Recipes"));
            @Override public Set<String> allowedKeys() {return keys;}
        }, 
        SendRecipe {
            Set<String> keys = new HashSet<>(Arrays.asList("Recipe"));
            @Override public Set<String> allowedKeys() {return keys;}
        },
        CloseHomeView,
        StartLogInView,
    }
    public enum CreateRecipeView implements Type {
        RecordButton,
        BackButton;
    }
    public enum CreateRecipeModel implements Type {
        CloseCreateRecipeView,
        StartRecording,
        StopRecording,
        SendRecipe {
            Set<String> keys = new HashSet<>(Arrays.asList("Recipe"));
            @Override public Set<String> allowedKeys() {return keys;}
        },
        CreateRecipeGotoPage {
            Set<String> keys = new HashSet<>(Arrays.asList("PageType", "MealType"));
            @Override public Set<String> allowedKeys() {return keys;}
        },
        CreateRecipeInvalidMealType,
        StartRecipeDetailedView;
    }
    public enum RecipeTitleView implements Type {
        RecipeTitleClick {
            Set<String> keys = new HashSet<>(Arrays.asList("Recipe"));
            @Override public Set<String> allowedKeys() {return keys;}
        }
    }
    public enum RecipeDetailedView implements Type {
        CancelButton, 
        SaveButton,
        BackButton,
        DeleteButton,
        EditButton, 
        ConfirmDeleteButton, 
        UpdateInformation {
            Set<String> keys = new HashSet<>(Arrays.asList("RecipeBody"));
            @Override public Set<String> allowedKeys() {return keys;}
        }, 
        ShareButton, 
        RefreshButton,
        CloseSharePopupViewButton;
    }
    public enum RecipeDetailedModel implements Type {
        CloseRecipeDetailedView,
        SetRecipe {
            Set<String> keys = new HashSet<>(Arrays.asList("Recipe"));
            @Override public Set<String> allowedKeys() {return keys;}
        },
        UseUnsavedLayout,
        UseSavedLayout,
        SaveConfirmation,
        RemoveUnsavedLayout,
        EditRecipe,
        ExitEditRecipe,
        GoToDeleteConfirmationPage,
        RemoveDeleteConfirmation, 
        AddBackButton, 
        RemoveBackButton,
        StartSharePopupView,
        SetRecipeShareLink {
            Set<String> keys = new HashSet<>(Arrays.asList("RecipeShareLink"));
            @Override public Set<String> allowedKeys() {return keys;}
        },
        Refresh {
            Set<String> keys = new HashSet<>(Arrays.asList("RecipeBody"));
            @Override public Set<String> allowedKeys() {return keys;}
        }
    }

    public enum SharePopupView implements Type {
        CloseButton,
        ClipboardButton,
    }

    public enum SharePopupModel implements Type {
        CloseSharePopupView, 
        SetRecipeShareLink {
            Set<String> keys = new HashSet<>(Arrays.asList("RecipeShareLink"));
            @Override public Set<String> allowedKeys() {return keys;}
        },
    }

    public enum HttpRequest implements Type {
        ServerError,
        CloseServerError,
    }
    
    private Type type;
    private Map<String, Object> payload;

    public Message(Type type, Object... kvs) {
        this.type = type;
        payload = new HashMap<>();
        if (kvs.length % 2 != 0)
            throw new IllegalArgumentException("Need even number of key-values.");
        for (int i=0; i<kvs.length; i+=2) {
            String key = (String) kvs[i];
            Object value = kvs[i+1];
            payload.put(key, value);
        }
        type.checkPayload(payload);
    }

    public Message(Type type) {
        if(!type.allowedKeys().isEmpty())
            throw new IllegalArgumentException("Loader not found for " + getClassEnum() + " " +  type);
        this.type = type;
        this.payload = null;
    } 

    public String getClassEnum() {
        return type.getClass().getEnclosingClass().getSimpleName();
    }

    public Type getMessageType() {
        return type;
    }

    private boolean keyValid(String key) {
        return type.allowedKeys().contains(key);
    }
    @SuppressWarnings("unchecked")
    public <T> T getKey(String key) {
        if (!keyValid(key))
            throw new IllegalArgumentException("Can't use key " + key + " for message type " + type);
        return (T) payload.get(key);
    }
}

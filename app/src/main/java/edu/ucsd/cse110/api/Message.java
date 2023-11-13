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
    
    public enum HomeView implements Type {
        CreateRecipeButton, // HomeView.CreateRecipeButton
        UpdateRecipeList,
    }
    public enum HomeModel implements Type {
        StartCreateRecipeView,
        CloseCreateRecipeView,
        StartRecipeDetailedView,
        CloseRecipeDetailedView,
        UpdateRecipeList {
            Set<String> keys = new HashSet<>(Arrays.asList("Recipes"));
            @Override public Set<String> allowedKeys() {return keys;}
        }
    }
    public enum CreateRecipeView implements Type {
        RecordButton,
        BackButton;
    }
    public enum CreateRecipeModel implements Type {
        CloseCreateRecipeView,
        StartRecording,
        StopRecording,
        SendTitleBody {
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
    public enum RecipeDetailedView implements Type {
        CancelButton, 
        SaveButton,
        BackButton,
        DeleteButton,
        ExitEditAction {
            Set<String> keys = new HashSet<>(Arrays.asList("RecipeBody"));
            @Override public Set<String> allowedKeys() {return keys;}
        };
        EditButton, 
        ConfirmDeleteButton;
    }
    public enum RecipeDetailedModel implements Type {
        CloseRecipeDetailedView,
        SetTitleBody {
            Set<String> keys = new HashSet<>(Arrays.asList("Recipe"));
            @Override public Set<String> allowedKeys() {return keys;}
        },
        UseUnsavedLayout,
        UseSavedLayout,
        SaveConfirmation,
        RemoveUnsavedLayout,
        EditRecipe {
            Set<String> keys = new HashSet<>(Arrays.asList("RecipeBody"));
            @Override public Set<String> allowedKeys() {return keys;}
        },
        RemoveEditRecipe;
        GoToDeleteConfirmationPage,
        RemoveUnsavedLayout, 
        RemoveDeleteConfirmation, 
        AddBackButton, 
        RemoveBackButton;
    }
    
    private Type type;
    private Map<String, Object> payload;

    public Message(Type type, Map<String, Object> payload) {
        this.type = type;
        this.payload = payload;
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

    public Object getKey(String key) {
        if (!keyValid(key))
            throw new IllegalArgumentException("Can't use key " + key + " for message type " + type);
        return payload.get(key);
    }
}

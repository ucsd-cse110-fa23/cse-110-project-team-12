package edu.ucsd.cse110.api;

import java.util.*;

import edu.ucsd.cse110.api.CreateRecipeModel.PageType;

public class Message {
    public enum Type {
        ButtonRecord,
        ButtonCreateRecipe,
        ButtonCloseCreateRecipe,
        CreateRecipeBackButton,
        StopRecording,
        StartRecording,
        FinishedCreatingRecipe,
        
        CreateRecipeGotoPage,
        CreateRecipeInvalidMealType,
    }
    public enum Key {
        PageType,
        MealType,
        RecipeTitle,
        RecipeBody,
    }

    
    private Type messageType;
    private Map<Key, Object> payload;

    private static Map<Type, List<Key>> allowedKeys = Map.ofEntries(
        Map.entry(Type.ButtonRecord, Arrays.asList()),
        Map.entry(Type.ButtonCreateRecipe, Arrays.asList()),
        Map.entry(Type.ButtonCloseCreateRecipe, Arrays.asList()),
        Map.entry(Type.CreateRecipeBackButton, Arrays.asList()),
        Map.entry(Type.CreateRecipeInvalidMealType, Arrays.asList()),
        Map.entry(Type.CreateRecipeGotoPage, Arrays.asList(Key.PageType, Key.MealType)),
        Map.entry(Type.FinishedCreatingRecipe, Arrays.asList(Key.RecipeTitle, Key.RecipeBody))
    );

    public Type getMessageType() {
        return messageType;
    }

    public Message(Type messageType, Map<Key, Object> payload) {
        this.messageType = messageType;
        this.payload = payload;
    }

    public Message(Type messageType) {
        this.messageType = messageType;
        this.payload = null;
    } 

    private boolean keyValid(Key key) {
        return allowedKeys.get(messageType).contains(key);
    }

    public Object getKey(Key key) {
        if (!keyValid(key))
            throw new IllegalArgumentException("Can't use key " + key + " for message type " + messageType);
        return payload.get(key);
    }

    public void setKey(Key key, Object value) {
        if (!keyValid(key))
            throw new IllegalArgumentException("Can't use key " + key + " for message type " + messageType);
        payload.put(key, value);
    }
}

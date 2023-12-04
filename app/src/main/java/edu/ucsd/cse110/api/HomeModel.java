package edu.ucsd.cse110.api;

import edu.ucsd.cse110.server.schemas.RecipeSchema;
import edu.ucsd.cse110.server.services.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

public class HomeModel implements ModelInterface {
    Controller controller;
    List<RecipeSchema> recipes;
    UIFactory.Type currentView;

    public enum FilterOption{
        Breakfast,
        Lunch,
        Dinner,
        All,
    }
    FilterOption filterOption;

    public enum SortOption{
        TitleAsc, // Ascending Title
        TitleDes, // Descending Title
        DateDes, // Descending DateCreated
        DateAsc // Ascending DateCreated
    }

    SortOption sortOption;

    public HomeModel(Controller c) {
        this.controller = c;
        currentView = UIFactory.Type.HomePage;
        filterOption = FilterOption.All;
        sortOption = SortOption.DateDes;
        updateRecipeList();
    }

    public void receiveMessage(Message m) {
        if (m.getMessageType() == Message.HomeView.CreateRecipeButton) {
            currentView = UIFactory.Type.CreateRecipe;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.StartCreateRecipeView));
        }
        if (m.getMessageType() == Message.HomeView.FilterRecipeButton) {
            FilterOption filterOption = m.getKey("FilterOption");
            updateFilterOption(filterOption);
            updateRecipeList();
        }
        if (m.getMessageType() == Message.HomeView.SortRecipeButton) {
            SortOption sortOption = m.getKey("SortOption");
            updateSortOption(sortOption);
            updateRecipeList();
        }
        if (m.getMessageType() == Message.CreateRecipeModel.CloseCreateRecipeView) {
            currentView = UIFactory.Type.HomePage;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.CloseCreateRecipeView));
        }
        if (m.getMessageType() == Message.CreateRecipeModel.StartRecipeDetailedView) {
            currentView = UIFactory.Type.DetailedView;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.StartRecipeDetailedView));
        }
        if (m.getMessageType() == Message.RecipeDetailedModel.CloseRecipeDetailedView) {
            currentView = UIFactory.Type.HomePage;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.CloseRecipeDetailedView));
        }
        if (m.getMessageType() == Message.HomeView.UpdateRecipeList) {
            updateRecipeList();
        }
        if (m.getMessageType() == Message.HomeView.OpenRecipe) {
            currentView = UIFactory.Type.DetailedView;
            controller.receiveMessageFromModel(new Message(Message.HomeModel.StartRecipeDetailedView));
            RecipeSchema openRecipe = m.getKey("Recipe");
            controller.receiveMessageFromModel(new Message(Message.HomeModel.SendRecipe, "Recipe", openRecipe));
        }
        if (m.getMessageType() == Message.HomeView.LogOut) {
            currentView = UIFactory.Type.HomePage;
            deleteLogInFile();

            controller.receiveMessageFromModel(new Message(Message.HomeModel.CloseHomeView));
            controller.receiveMessageFromModel(new Message(Message.HomeModel.StartLogInView));
        }
    }

    private String storagePath = "./src/main/java/edu/ucsd/cse110/api/assets/";

    private void deleteLogInFile() {
        try {
            Path path = Paths.get(storagePath + "savelogin.txt");
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UIFactory.Type getCurrentView() {
        return currentView;
    }

    private void updateRecipeList() {
        String userId = controller.getCurrentUser()._id;
        String urlString = Controller.serverUrl + "/recipe?userId=" + userId;
        ServerResponse response = HttpUtils.makeHttpRequest(urlString, "GET", "");
        
        if (response.getStatusCode() == 200) {
            recipes = Arrays.asList(Utils.unmarshalJson(response.getResponseBody(), RecipeSchema[].class));
            recipes = filterRecipeList(recipes);
            recipes = sortRecipeList(recipes);
        }
        controller.receiveMessageFromModel(
            new Message(Message.HomeModel.UpdateRecipeList, "Recipes", recipes));
    }

    public void updateFilterOption(FilterOption filterOption) {
        if(this.filterOption == filterOption) this.filterOption = FilterOption.All;
        else this.filterOption = filterOption;
    }

    private List<RecipeSchema> filterRecipeList(List<RecipeSchema> recipes) {
        if(filterOption == FilterOption.Breakfast) {
            List<RecipeSchema> filteredRecipes = new ArrayList<>();
            for(RecipeSchema rs : recipes) {
                if(rs.mealType.equals("Breakfast"))
                    filteredRecipes.add(rs);
            }
            return filteredRecipes;
        } else if(filterOption == FilterOption.Lunch) {
            List<RecipeSchema> filteredRecipes = new ArrayList<>();
            for(RecipeSchema rs : recipes) {
                if(rs.mealType.equals("Lunch"))
                    filteredRecipes.add(rs);
            }
            return filteredRecipes;
        } else if(filterOption == FilterOption.Dinner) {
            List<RecipeSchema> filteredRecipes = new ArrayList<>();
            for(RecipeSchema rs : recipes) {
                if(rs.mealType.equals("Dinner"))
                    filteredRecipes.add(rs);
            }
            return filteredRecipes;
        }else{
            return recipes;
        }
    }

    public void updateSortOption(SortOption sortOption) {
        this.sortOption = sortOption;
    }

    private List<RecipeSchema> sortRecipeList(List<RecipeSchema> recipes) {
        if(sortOption == SortOption.DateDes) {
            Collections.sort(recipes, (recipe1, recipe2) -> LocalDateTime.parse(recipe2.timeCreated)
                                                            .compareTo(LocalDateTime.parse(recipe1.timeCreated)));
        } else if(sortOption == SortOption.DateAsc) {
            Collections.sort(recipes, (recipe1, recipe2) -> LocalDateTime.parse(recipe1.timeCreated)
                                                            .compareTo(LocalDateTime.parse(recipe2.timeCreated)));
        } else if(sortOption == SortOption.TitleAsc) {
            Collections.sort(recipes, (recipe1, recipe2) -> recipe1.title
                                                            .compareTo(recipe2.title));
        } else if(sortOption == SortOption.TitleDes) {
            Collections.sort(recipes, (recipe1, recipe2) -> recipe2.title
                                                            .compareTo(recipe1.title));
        }
        return recipes;
    }

    public List<RecipeSchema> getRecipes() {
        return recipes;
    }
}

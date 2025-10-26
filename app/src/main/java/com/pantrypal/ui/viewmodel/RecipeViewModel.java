package com.pantrypal.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pantrypal.data.model.Recipe;
import com.pantrypal.data.repository.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private RecipeRepository repository;

    public RecipeViewModel(Application application) {
        super(application);
        repository = new RecipeRepository(application);
    }

    public void insert(Recipe recipe) {
        repository.insert(recipe);
    }

    public void update(Recipe recipe) {
        repository.update(recipe);
    }

    public void delete(Recipe recipe) {
        repository.delete(recipe);
    }

    public LiveData<Recipe> getRecipeById(int recipeId) {
        return repository.getRecipeById(recipeId);
    }

    public LiveData<List<Recipe>> getFavoriteRecipes() {
        return repository.getFavoriteRecipes();
    }

    public LiveData<List<Recipe>> getRecipesByCategory(String category, int limit) {
        return repository.getRecipesByCategory(category, limit);
    }

    public LiveData<List<Recipe>> getTopRecipes(int limit) {
        return repository.getTopRecipes(limit);
    }

    public LiveData<List<Recipe>> searchRecipes(String query) {
        return repository.searchRecipes(query);
    }

    public LiveData<List<Recipe>> getRecipesInCategory(String category) {
        return repository.getRecipesInCategory(category);
    }

    public void addToFavorites(int recipeId) {
        repository.addToFavorites(recipeId);
    }

    public void removeFromFavorites(int recipeId) {
        repository.removeFromFavorites(recipeId);
    }

    public LiveData<Integer> getFavoritesCount() {
        return repository.getFavoritesCount();
    }
}
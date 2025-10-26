package com.pantrypal.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pantrypal.data.dao.RecipeDao;
import com.pantrypal.data.database.PantrypalDatabase;
import com.pantrypal.data.model.Recipe;

import java.util.List;

public class RecipeRepository {
    private RecipeDao recipeDao;

    public RecipeRepository(Application application) {
        PantrypalDatabase db = PantrypalDatabase.getDatabase(application);
        recipeDao = db.recipeDao();
    }

    public void insert(Recipe recipe) {
        new Thread(() -> recipeDao.insert(recipe)).start();
    }

    public void update(Recipe recipe) {
        new Thread(() -> recipeDao.update(recipe)).start();
    }

    public void delete(Recipe recipe) {
        new Thread(() -> recipeDao.delete(recipe)).start();
    }

    public LiveData<Recipe> getRecipeById(int recipeId) {
        return recipeDao.getRecipeById(recipeId);
    }

    public LiveData<List<Recipe>> getFavoriteRecipes() {
        return recipeDao.getFavoriteRecipes();
    }

    public LiveData<List<Recipe>> getRecipesByCategory(String category, int limit) {
        return recipeDao.getRecipesByCategory(category, limit);
    }

    public LiveData<List<Recipe>> getTopRecipes(int limit) {
        return recipeDao.getTopRecipes(limit);
    }

    public LiveData<List<Recipe>> searchRecipes(String query) {
        return recipeDao.searchRecipes(query);
    }

    public LiveData<List<Recipe>> getRecipesInCategory(String category) {
        return recipeDao.getRecipesInCategory(category);
    }

    public void addToFavorites(int recipeId) {
        new Thread(() -> recipeDao.addToFavorites(recipeId)).start();
    }

    public void removeFromFavorites(int recipeId) {
        new Thread(() -> recipeDao.removeFromFavorites(recipeId)).start();
    }

    public LiveData<Integer> getFavoritesCount() {
        return recipeDao.getFavoritesCount();
    }
}
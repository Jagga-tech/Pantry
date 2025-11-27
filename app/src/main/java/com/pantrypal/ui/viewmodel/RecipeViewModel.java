package com.pantrypal.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pantrypal.data.model.Recipe;
import com.pantrypal.data.repository.FirebaseFavoritesRepository;
import com.pantrypal.data.repository.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private RecipeRepository recipeRepository;
    private FirebaseFavoritesRepository favoritesRepository;
    private String currentUserId;

    public RecipeViewModel(Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
        favoritesRepository = new FirebaseFavoritesRepository();

        // Get current user ID from FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }
    }

    public void insert(Recipe recipe) {
        recipeRepository.insert(recipe);
    }

    public void update(Recipe recipe) {
        recipeRepository.update(recipe);
    }

    public void delete(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    public LiveData<Recipe> getRecipeById(int recipeId) {
        return recipeRepository.getRecipeById(recipeId);
    }

    public LiveData<List<Recipe>> getFavoriteRecipes() {
        return recipeRepository.getFavoriteRecipes();
    }

    public LiveData<List<Recipe>> getRecipesByCategory(String category, int limit) {
        return recipeRepository.getRecipesByCategory(category, limit);
    }

    public LiveData<List<Recipe>> getTopRecipes(int limit) {
        return recipeRepository.getTopRecipes(limit);
    }

    public LiveData<List<Recipe>> searchRecipes(String query) {
        return recipeRepository.searchRecipes(query);
    }

    public LiveData<List<Recipe>> getRecipesInCategory(String category) {
        return recipeRepository.getRecipesInCategory(category);
    }

    // Firebase favorites methods
    public LiveData<List<String>> getFavoriteRecipeIds() {
        if (currentUserId == null) {
            return null;
        }
        return favoritesRepository.getFavoriteRecipeIds(currentUserId);
    }

    public void addFavorite(String recipeId, FirebaseFavoritesRepository.RepositoryCallback<Void> callback) {
        if (currentUserId == null) {
            callback.onFailure("User not authenticated");
            return;
        }
        favoritesRepository.addFavorite(currentUserId, recipeId, callback);
    }

    public void removeFavorite(String recipeId, FirebaseFavoritesRepository.RepositoryCallback<Void> callback) {
        if (currentUserId == null) {
            callback.onFailure("User not authenticated");
            return;
        }
        favoritesRepository.removeFavorite(currentUserId, recipeId, callback);
    }

    public void toggleFavorite(String recipeId, FirebaseFavoritesRepository.RepositoryCallback<Boolean> callback) {
        if (currentUserId == null) {
            callback.onFailure("User not authenticated");
            return;
        }
        favoritesRepository.toggleFavorite(currentUserId, recipeId, callback);
    }

    public void isFavorite(String recipeId, FirebaseFavoritesRepository.RepositoryCallback<Boolean> callback) {
        if (currentUserId == null) {
            callback.onFailure("User not authenticated");
            return;
        }
        favoritesRepository.isFavorite(currentUserId, recipeId, callback);
    }

    public LiveData<Boolean> isFavoriteLiveData(String recipeId) {
        if (currentUserId == null) {
            return null;
        }
        return favoritesRepository.isFavoriteLiveData(currentUserId, recipeId);
    }

    public LiveData<Integer> getFavoritesCount() {
        return recipeRepository.getFavoritesCount();
    }

    public void getFavoriteCount(FirebaseFavoritesRepository.RepositoryCallback<Integer> callback) {
        if (currentUserId == null) {
            callback.onFailure("User not authenticated");
            return;
        }
        favoritesRepository.getFavoriteCount(currentUserId, callback);
    }
}
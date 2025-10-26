package com.pantrypal.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pantrypal.data.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    void insert(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    LiveData<Recipe> getRecipeById(int recipeId);

    @Query("SELECT * FROM recipes WHERE isFavorite = 1 ORDER BY name ASC")
    LiveData<List<Recipe>> getFavoriteRecipes();

    @Query("SELECT * FROM recipes WHERE category = :category ORDER BY matchPercentage DESC LIMIT :limit")
    LiveData<List<Recipe>> getRecipesByCategory(String category, int limit);

    @Query("SELECT * FROM recipes ORDER BY matchPercentage DESC LIMIT :limit")
    LiveData<List<Recipe>> getTopRecipes(int limit);

    @Query("SELECT * FROM recipes WHERE name LIKE '%' || :query || '%'")
    LiveData<List<Recipe>> searchRecipes(String query);

    @Query("SELECT * FROM recipes WHERE category = :category")
    LiveData<List<Recipe>> getRecipesInCategory(String category);

    @Query("UPDATE recipes SET isFavorite = 1 WHERE id = :recipeId")
    void addToFavorites(int recipeId);

    @Query("UPDATE recipes SET isFavorite = 0 WHERE id = :recipeId")
    void removeFromFavorites(int recipeId);

    @Query("SELECT COUNT(*) FROM recipes WHERE isFavorite = 1")
    LiveData<Integer> getFavoritesCount();
}
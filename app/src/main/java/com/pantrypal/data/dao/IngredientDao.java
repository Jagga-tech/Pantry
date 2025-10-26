package com.pantrypal.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pantrypal.data.model.Ingredient;

import java.util.List;

@Dao
public interface IngredientDao {
    @Insert
    void insert(Ingredient ingredient);

    @Update
    void update(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);

    @Query("SELECT * FROM ingredients ORDER BY name ASC")
    List<Ingredient> getAllIngredients();

    @Query("SELECT * FROM ingredients WHERE category = :category ORDER BY name ASC")
    List<Ingredient> getIngredientsByCategory(String category);

    @Query("SELECT * FROM ingredients WHERE name LIKE '%' || :query || '%'")
    List<Ingredient> searchIngredients(String query);

    @Query("SELECT DISTINCT category FROM ingredients")
    List<String> getAllCategories();
}
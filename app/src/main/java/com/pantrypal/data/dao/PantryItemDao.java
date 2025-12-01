package com.pantrypal.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pantrypal.data.model.PantryItem;

import java.util.List;

@Dao
public interface PantryItemDao {
    @Insert
    void insert(PantryItem pantryItem);

    @Update
    void update(PantryItem pantryItem);

    @Delete
    void delete(PantryItem pantryItem);

    @Query("SELECT * FROM pantry_items WHERE userId = :userId ORDER BY ingredientName ASC")
    LiveData<List<PantryItem>> getAllItemsByUser(String userId);

    @Query("SELECT * FROM pantry_items WHERE userId = :userId AND category = :category ORDER BY ingredientName ASC")
    LiveData<List<PantryItem>> getItemsByCategory(String userId, String category);

    @Query("SELECT * FROM pantry_items WHERE userId = :userId AND ingredientName LIKE '%' || :query || '%'")
    LiveData<List<PantryItem>> searchItems(String userId, String query);

    @Query("SELECT * FROM pantry_items WHERE userId = :userId AND expirationDate <= :expiryThreshold")
    LiveData<List<PantryItem>> getExpiringItems(String userId, long expiryThreshold);

    @Query("SELECT * FROM pantry_items WHERE id = :itemId")
    LiveData<PantryItem> getItemById(String itemId);

    @Query("SELECT * FROM pantry_items WHERE id = :itemId")
    PantryItem getItemByIdSync(String itemId);

    @Query("DELETE FROM pantry_items WHERE userId = :userId AND id = :itemId")
    void deleteItemById(String userId, String itemId);

    @Query("DELETE FROM pantry_items")
    void deleteAll();
}
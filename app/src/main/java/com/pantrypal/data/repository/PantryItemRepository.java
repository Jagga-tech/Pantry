package com.pantrypal.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pantrypal.data.dao.PantryItemDao;
import com.pantrypal.data.database.PantrypalDatabase;
import com.pantrypal.data.model.PantryItem;

import java.util.List;

public class PantryItemRepository {
    private PantryItemDao pantryItemDao;

    public PantryItemRepository(Application application) {
        PantrypalDatabase db = PantrypalDatabase.getDatabase(application);
        pantryItemDao = db.pantryItemDao();
    }

    public void insert(PantryItem pantryItem) {
        new Thread(() -> pantryItemDao.insert(pantryItem)).start();
    }

    public void update(PantryItem pantryItem) {
        new Thread(() -> pantryItemDao.update(pantryItem)).start();
    }

    public void delete(PantryItem pantryItem) {
        new Thread(() -> pantryItemDao.delete(pantryItem)).start();
    }

    public LiveData<List<PantryItem>> getAllItemsByUser(int userId) {
        return pantryItemDao.getAllItemsByUser(userId);
    }

    public LiveData<List<PantryItem>> getItemsByCategory(int userId, String category) {
        return pantryItemDao.getItemsByCategory(userId, category);
    }

    public LiveData<List<PantryItem>> searchItems(int userId, String query) {
        return pantryItemDao.searchItems(userId, query);
    }

    public LiveData<List<PantryItem>> getExpiringItems(int userId, long expiryThreshold) {
        return pantryItemDao.getExpiringItems(userId, expiryThreshold);
    }

    public LiveData<PantryItem> getItemById(int itemId) {
        return pantryItemDao.getItemById(itemId);
    }

    public void deleteItemById(int userId, int itemId) {
        new Thread(() -> pantryItemDao.deleteItemById(userId, itemId)).start();
    }
}
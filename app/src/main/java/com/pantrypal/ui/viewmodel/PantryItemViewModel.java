package com.pantrypal.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pantrypal.data.model.PantryItem;
import com.pantrypal.data.repository.PantryItemRepository;

import java.util.List;

public class PantryItemViewModel extends AndroidViewModel {
    private PantryItemRepository repository;

    public PantryItemViewModel(Application application) {
        super(application);
        repository = new PantryItemRepository(application);
    }

    public void insert(PantryItem pantryItem) {
        repository.insert(pantryItem);
    }

    public void update(PantryItem pantryItem) {
        repository.update(pantryItem);
    }

    public void delete(PantryItem pantryItem) {
        repository.delete(pantryItem);
    }

    public LiveData<List<PantryItem>> getAllItemsByUser(int userId) {
        return repository.getAllItemsByUser(userId);
    }

    public LiveData<List<PantryItem>> getItemsByCategory(int userId, String category) {
        return repository.getItemsByCategory(userId, category);
    }

    public LiveData<List<PantryItem>> searchItems(int userId, String query) {
        return repository.searchItems(userId, query);
    }

    public LiveData<List<PantryItem>> getExpiringItems(int userId, long expiryThreshold) {
        return repository.getExpiringItems(userId, expiryThreshold);
    }

    public LiveData<PantryItem> getItemById(int itemId) {
        return repository.getItemById(itemId);
    }

    public void deleteItemById(int userId, int itemId) {
        repository.deleteItemById(userId, itemId);
    }
}
package com.pantrypal.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pantrypal.data.model.PantryItem;
import com.pantrypal.data.repository.FirebasePantryRepository;

import java.util.List;

public class PantryItemViewModel extends AndroidViewModel {
    private FirebasePantryRepository repository;
    private String currentUserId;

    public PantryItemViewModel(Application application) {
        super(application);
        repository = new FirebasePantryRepository();

        // Get current user ID from FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }
    }

    public LiveData<List<PantryItem>> getAllPantryItems() {
        if (currentUserId == null) {
            return null;
        }
        return repository.getAllPantryItems(currentUserId);
    }

    public LiveData<List<PantryItem>> getExpiringItems() {
        if (currentUserId == null) {
            return null;
        }
        return repository.getExpiringItems(currentUserId);
    }

    public LiveData<List<PantryItem>> getPantryItemsByCategory(String category) {
        if (currentUserId == null) {
            return null;
        }
        return repository.getPantryItemsByCategory(currentUserId, category);
    }

    public void addPantryItem(PantryItem item, FirebasePantryRepository.RepositoryCallback<String> callback) {
        if (currentUserId == null) {
            callback.onFailure("User not authenticated");
            return;
        }
        repository.addPantryItem(currentUserId, item, callback);
    }

    public void updatePantryItem(PantryItem item, FirebasePantryRepository.RepositoryCallback<Void> callback) {
        if (currentUserId == null) {
            callback.onFailure("User not authenticated");
            return;
        }
        repository.updatePantryItem(currentUserId, item, callback);
    }

    public void deletePantryItem(String itemId, FirebasePantryRepository.RepositoryCallback<Void> callback) {
        if (currentUserId == null) {
            callback.onFailure("User not authenticated");
            return;
        }
        repository.deletePantryItem(currentUserId, itemId, callback);
    }

    public void getPantryItemById(String itemId, FirebasePantryRepository.RepositoryCallback<PantryItem> callback) {
        if (currentUserId == null) {
            callback.onFailure("User not authenticated");
            return;
        }
        repository.getPantryItemById(currentUserId, itemId, callback);
    }

    public void searchByName(String searchQuery, FirebasePantryRepository.RepositoryCallback<List<PantryItem>> callback) {
        if (currentUserId == null) {
            callback.onFailure("User not authenticated");
            return;
        }
        repository.searchByName(currentUserId, searchQuery, callback);
    }
}
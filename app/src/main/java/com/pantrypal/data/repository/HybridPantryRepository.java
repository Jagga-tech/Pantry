package com.pantrypal.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.pantrypal.data.dao.PantryItemDao;
import com.pantrypal.data.database.PantrypalDatabase;
import com.pantrypal.data.model.PantryItem;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Hybrid repository that syncs pantry items between Firebase (cloud) and Room (local)
 * Offline-first architecture: reads from Room, writes to both Room and Firebase
 */
public class HybridPantryRepository {
    private static final String TAG = "HybridPantryRepo";

    private final PantryItemDao pantryItemDao;
    private final FirebasePantryRepository firebaseRepo;

    public HybridPantryRepository(Application application) {
        PantrypalDatabase db = PantrypalDatabase.getDatabase(application);
        this.pantryItemDao = db.pantryItemDao();
        this.firebaseRepo = new FirebasePantryRepository();
    }

    /**
     * Get all pantry items for a user with real-time sync
     */
    public LiveData<List<PantryItem>> getAllItemsByUser(String userId) {
        // Start listening to Firebase updates
        syncItemsFromFirebase(userId);

        // Return Room LiveData (offline-first)
        return pantryItemDao.getAllItemsByUser(userId);
    }

    /**
     * Get items by category
     */
    public LiveData<List<PantryItem>> getItemsByCategory(String userId, String category) {
        return pantryItemDao.getItemsByCategory(userId, category);
    }

    /**
     * Search items
     */
    public LiveData<List<PantryItem>> searchItems(String userId, String query) {
        return pantryItemDao.searchItems(userId, query);
    }

    /**
     * Get expiring items
     */
    public LiveData<List<PantryItem>> getExpiringItems(String userId, long expiryThreshold) {
        return pantryItemDao.getExpiringItems(userId, expiryThreshold);
    }

    /**
     * Get item by ID
     */
    public LiveData<PantryItem> getItemById(String itemId) {
        return pantryItemDao.getItemById(itemId);
    }

    /**
     * Add or update pantry item (writes to both Room and Firebase)
     */
    public void saveItem(PantryItem item, RepositoryCallback<Void> callback) {
        // Save to Room first (offline support)
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                PantryItem existingItem = pantryItemDao.getItemByIdSync(item.getId());
                if (existingItem != null) {
                    pantryItemDao.update(item);
                } else {
                    pantryItemDao.insert(item);
                }
                Log.d(TAG, "✅ Item saved to Room: " + item.getId());

                // Then sync to Firebase (if online)
                firebaseRepo.createPantryItem(item, new FirebasePantryRepository.RepositoryCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Log.d(TAG, "✅ Item synced to Firebase: " + item.getId());
                        if (callback != null) callback.onSuccess(null);
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.w(TAG, "⚠️ Firebase sync failed (offline?): " + error);
                        // Still report success since Room save succeeded
                        if (callback != null) callback.onSuccess(null);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "❌ Error saving item: " + e.getMessage());
                if (callback != null) callback.onFailure(e.getMessage());
            }
        });
    }

    /**
     * Delete pantry item from both Room and Firebase
     */
    public void deleteItem(String userId, String itemId, RepositoryCallback<Void> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                pantryItemDao.deleteItemById(userId, itemId);
                Log.d(TAG, "✅ Item deleted from Room: " + itemId);

                // Then delete from Firebase
                firebaseRepo.deletePantryItem(userId, itemId, new FirebasePantryRepository.RepositoryCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Log.d(TAG, "✅ Item deleted from Firebase: " + itemId);
                        if (callback != null) callback.onSuccess(null);
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.w(TAG, "⚠️ Firebase delete failed: " + error);
                        if (callback != null) callback.onSuccess(null);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "❌ Error deleting item: " + e.getMessage());
                if (callback != null) callback.onFailure(e.getMessage());
            }
        });
    }

    /**
     * Sync pantry items from Firebase to Room (real-time updates)
     */
    private void syncItemsFromFirebase(String userId) {
        firebaseRepo.getPantryItemsByUser(userId).observeForever(firebaseItems -> {
            if (firebaseItems != null && !firebaseItems.isEmpty()) {
                // Update Room with latest Firebase data
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        for (PantryItem item : firebaseItems) {
                            PantryItem existingItem = pantryItemDao.getItemByIdSync(item.getId());
                            if (existingItem != null) {
                                pantryItemDao.update(item);
                            } else {
                                pantryItemDao.insert(item);
                            }
                        }
                        Log.d(TAG, "✅ Synced " + firebaseItems.size() + " items from Firebase to Room");
                    } catch (Exception e) {
                        Log.e(TAG, "❌ Error syncing items from Firebase: " + e.getMessage());
                    }
                });
            }
        });
    }

    /**
     * Callback interface
     */
    public interface RepositoryCallback<T> {
        void onSuccess(T data);
        void onFailure(String error);
    }
}

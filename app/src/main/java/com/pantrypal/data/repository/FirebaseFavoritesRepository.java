package com.pantrypal.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseFavoritesRepository {

    private final FirebaseFirestore firestore;
    private static final String COLLECTION_USERS = "users";
    private static final String COLLECTION_FAVORITES = "favorites";

    public FirebaseFavoritesRepository() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    /**
     * Callback interface for async operations
     */
    public interface RepositoryCallback<T> {
        void onSuccess(T data);
        void onFailure(String error);
    }

    /**
     * Get collection reference for user's favorites
     */
    private CollectionReference getFavoritesCollection(String userId) {
        return firestore.collection(COLLECTION_USERS)
                .document(userId)
                .collection(COLLECTION_FAVORITES);
    }

    /**
     * Get all favorite recipe IDs with real-time updates
     */
    public LiveData<List<String>> getFavoriteRecipeIds(String userId) {
        MutableLiveData<List<String>> liveData = new MutableLiveData<>();

        getFavoritesCollection(userId)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        liveData.setValue(new ArrayList<>());
                        return;
                    }

                    if (snapshots != null) {
                        List<String> recipeIds = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            String recipeId = doc.getId();
                            recipeIds.add(recipeId);
                        }
                        liveData.setValue(recipeIds);
                    }
                });

        return liveData;
    }

    /**
     * Add recipe to favorites
     */
    public void addFavorite(String userId, String recipeId, RepositoryCallback<Void> callback) {
        if (recipeId == null || recipeId.isEmpty()) {
            callback.onFailure("Recipe ID is required");
            return;
        }

        Map<String, Object> favoriteData = new HashMap<>();
        favoriteData.put("recipeId", recipeId);
        favoriteData.put("addedAt", new Date());

        getFavoritesCollection(userId)
                .document(recipeId)
                .set(favoriteData)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to add favorite");
                });
    }

    /**
     * Remove recipe from favorites
     */
    public void removeFavorite(String userId, String recipeId, RepositoryCallback<Void> callback) {
        if (recipeId == null || recipeId.isEmpty()) {
            callback.onFailure("Recipe ID is required");
            return;
        }

        getFavoritesCollection(userId)
                .document(recipeId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to remove favorite");
                });
    }

    /**
     * Toggle favorite status
     */
    public void toggleFavorite(String userId, String recipeId, RepositoryCallback<Boolean> callback) {
        if (recipeId == null || recipeId.isEmpty()) {
            callback.onFailure("Recipe ID is required");
            return;
        }

        getFavoritesCollection(userId)
                .document(recipeId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Recipe is favorited, remove it
                        removeFavorite(userId, recipeId, new RepositoryCallback<Void>() {
                            @Override
                            public void onSuccess(Void data) {
                                callback.onSuccess(false); // false means removed
                            }

                            @Override
                            public void onFailure(String error) {
                                callback.onFailure(error);
                            }
                        });
                    } else {
                        // Recipe is not favorited, add it
                        addFavorite(userId, recipeId, new RepositoryCallback<Void>() {
                            @Override
                            public void onSuccess(Void data) {
                                callback.onSuccess(true); // true means added
                            }

                            @Override
                            public void onFailure(String error) {
                                callback.onFailure(error);
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to toggle favorite");
                });
    }

    /**
     * Check if recipe is favorited
     */
    public void isFavorite(String userId, String recipeId, RepositoryCallback<Boolean> callback) {
        if (recipeId == null || recipeId.isEmpty()) {
            callback.onFailure("Recipe ID is required");
            return;
        }

        getFavoritesCollection(userId)
                .document(recipeId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    callback.onSuccess(documentSnapshot.exists());
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to check favorite status");
                });
    }

    /**
     * Get favorite status with real-time updates
     */
    public LiveData<Boolean> isFavoriteLiveData(String userId, String recipeId) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();

        getFavoritesCollection(userId)
                .document(recipeId)
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        liveData.setValue(false);
                        return;
                    }

                    liveData.setValue(documentSnapshot != null && documentSnapshot.exists());
                });

        return liveData;
    }

    /**
     * Clear all favorites
     */
    public void clearAllFavorites(String userId, RepositoryCallback<Void> callback) {
        getFavoritesCollection(userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Delete all documents in the collection
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        doc.getReference().delete();
                    }
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to clear favorites");
                });
    }

    /**
     * Get favorite count
     */
    public void getFavoriteCount(String userId, RepositoryCallback<Integer> callback) {
        getFavoritesCollection(userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    callback.onSuccess(queryDocumentSnapshots.size());
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to get favorite count");
                });
    }
}

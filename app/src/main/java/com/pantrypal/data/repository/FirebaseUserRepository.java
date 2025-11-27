package com.pantrypal.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pantrypal.data.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseUserRepository {

    private final FirebaseFirestore firestore;
    private static final String COLLECTION_USERS = "users";

    public FirebaseUserRepository() {
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
     * Get users collection reference
     */
    private CollectionReference getUsersCollection() {
        return firestore.collection(COLLECTION_USERS);
    }

    /**
     * Get user document reference
     */
    private DocumentReference getUserDocument(String userId) {
        return getUsersCollection().document(userId);
    }

    /**
     * Get user by ID with real-time updates
     */
    public LiveData<User> getUserById(String userId) {
        MutableLiveData<User> liveData = new MutableLiveData<>();

        getUserDocument(userId)
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        liveData.setValue(null);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            user.setId(documentSnapshot.getId());
                            liveData.setValue(user);
                        }
                    } else {
                        liveData.setValue(null);
                    }
                });

        return liveData;
    }

    /**
     * Create new user
     */
    public void createUser(User user, RepositoryCallback<Void> callback) {
        if (user.getId() == null || user.getId().isEmpty()) {
            callback.onFailure("User ID is required");
            return;
        }

        user.setCreatedAt(new Date());
        user.setLastLoginAt(new Date());

        getUserDocument(user.getId())
                .set(user.toFirestoreMap())
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to create user");
                });
    }

    /**
     * Update user
     */
    public void updateUser(User user, RepositoryCallback<Void> callback) {
        if (user.getId() == null || user.getId().isEmpty()) {
            callback.onFailure("User ID is required");
            return;
        }

        getUserDocument(user.getId())
                .update(user.toFirestoreMap())
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to update user");
                });
    }

    /**
     * Update last login timestamp
     */
    public void updateLastLoginAt(String userId, RepositoryCallback<Void> callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("lastLoginAt", new Date());

        getUserDocument(userId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to update last login");
                });
    }

    /**
     * Update profile image URL
     */
    public void updateProfileImageUrl(String userId, String profileImageUrl, RepositoryCallback<Void> callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("profilePicUrl", profileImageUrl != null ? profileImageUrl : "");

        getUserDocument(userId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to update profile image");
                });
    }

    /**
     * Update dietary preference
     */
    public void updateDietaryPreference(String userId, String dietaryPreference, RepositoryCallback<Void> callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("dietaryPreferences", dietaryPreference != null ? dietaryPreference : "");

        getUserDocument(userId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to update dietary preference");
                });
    }

    /**
     * Check if user exists
     */
    public void checkUserExists(String userId, RepositoryCallback<Boolean> callback) {
        getUserDocument(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    callback.onSuccess(documentSnapshot.exists());
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to check user existence");
                });
    }

    /**
     * Get user by ID (one-time fetch, not real-time)
     */
    public void getUserByIdOnce(String userId, RepositoryCallback<User> callback) {
        getUserDocument(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            user.setId(documentSnapshot.getId());
                            callback.onSuccess(user);
                        } else {
                            callback.onFailure("Failed to parse user data");
                        }
                    } else {
                        callback.onFailure("User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to get user");
                });
    }

    /**
     * Delete user
     */
    public void deleteUser(String userId, RepositoryCallback<Void> callback) {
        getUserDocument(userId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to delete user");
                });
    }

    /**
     * Update user field
     */
    public void updateUserField(String userId, String fieldName, Object value, RepositoryCallback<Void> callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(fieldName, value);

        getUserDocument(userId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage() != null ? e.getMessage() : "Failed to update field: " + fieldName);
                });
    }
}

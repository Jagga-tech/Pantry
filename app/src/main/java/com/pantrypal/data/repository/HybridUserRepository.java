package com.pantrypal.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.pantrypal.data.dao.UserDao;
import com.pantrypal.data.database.PantrypalDatabase;
import com.pantrypal.data.model.User;

import java.util.concurrent.Executors;

/**
 * Hybrid repository that syncs data between Firebase (cloud) and Room (local)
 * Offline-first architecture: reads from Room, writes to both Room and Firebase
 */
public class HybridUserRepository {
    private static final String TAG = "HybridUserRepo";

    private final UserDao userDao;
    private final FirebaseUserRepository firebaseRepo;

    public HybridUserRepository(Application application) {
        PantrypalDatabase db = PantrypalDatabase.getDatabase(application);
        this.userDao = db.userDao();
        this.firebaseRepo = new FirebaseUserRepository();
    }

    /**
     * Get user by ID with real-time sync from Firebase to Room
     */
    public LiveData<User> getUserById(String userId) {
        // Start listening to Firebase updates
        syncUserFromFirebase(userId);

        // Return Room LiveData (offline-first)
        return userDao.getUserById(userId);
    }

    /**
     * Get current user
     */
    public LiveData<User> getCurrentUser() {
        return userDao.getCurrentUser();
    }

    /**
     * Create or update user (writes to both Room and Firebase)
     */
    public void saveUser(User user, RepositoryCallback<Void> callback) {
        // Save to Room first (offline support)
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                User existingUser = userDao.getUserByIdSync(user.getId());
                if (existingUser != null) {
                    userDao.update(user);
                } else {
                    userDao.insert(user);
                }
                Log.d(TAG, "✅ User saved to Room: " + user.getId());

                // Then sync to Firebase (if online)
                firebaseRepo.createUser(user, new FirebaseUserRepository.RepositoryCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Log.d(TAG, "✅ User synced to Firebase: " + user.getId());
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
                Log.e(TAG, "❌ Error saving user: " + e.getMessage());
                if (callback != null) callback.onFailure(e.getMessage());
            }
        });
    }

    /**
     * Update user (writes to both Room and Firebase)
     */
    public void updateUser(User user, RepositoryCallback<Void> callback) {
        saveUser(user, callback);
    }

    /**
     * Delete user from both Room and Firebase
     */
    public void deleteUser(String userId, RepositoryCallback<Void> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                User user = userDao.getUserByIdSync(userId);
                if (user != null) {
                    userDao.delete(user);
                    Log.d(TAG, "✅ User deleted from Room: " + userId);
                }

                // Then delete from Firebase
                firebaseRepo.deleteUser(userId, new FirebaseUserRepository.RepositoryCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        Log.d(TAG, "✅ User deleted from Firebase: " + userId);
                        if (callback != null) callback.onSuccess(null);
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.w(TAG, "⚠️ Firebase delete failed: " + error);
                        if (callback != null) callback.onSuccess(null);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "❌ Error deleting user: " + e.getMessage());
                if (callback != null) callback.onFailure(e.getMessage());
            }
        });
    }

    /**
     * Sync user data from Firebase to Room (real-time updates)
     */
    private void syncUserFromFirebase(String userId) {
        firebaseRepo.getUserById(userId).observeForever(firebaseUser -> {
            if (firebaseUser != null) {
                // Update Room with latest Firebase data
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        User existingUser = userDao.getUserByIdSync(userId);
                        if (existingUser != null) {
                            userDao.update(firebaseUser);
                        } else {
                            userDao.insert(firebaseUser);
                        }
                        Log.d(TAG, "✅ User synced from Firebase to Room: " + userId);
                    } catch (Exception e) {
                        Log.e(TAG, "❌ Error syncing user from Firebase: " + e.getMessage());
                    }
                });
            }
        });
    }

    /**
     * Update specific field for user
     */
    public void updateUserField(String userId, String fieldName, Object value, RepositoryCallback<Void> callback) {
        firebaseRepo.updateUserField(userId, fieldName, value, new FirebaseUserRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                // Firebase will trigger real-time sync to Room
                if (callback != null) callback.onSuccess(null);
            }

            @Override
            public void onFailure(String error) {
                if (callback != null) callback.onFailure(error);
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
package com.pantrypal.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pantrypal.data.model.User;
import com.pantrypal.data.repository.FirebaseUserRepository;

public class UserViewModel extends AndroidViewModel {
    private FirebaseUserRepository repository;
    private LiveData<User> currentUser;
    private String currentUserId;

    public UserViewModel(Application application) {
        super(application);
        repository = new FirebaseUserRepository();

        // Get current user ID from FirebaseAuth
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            currentUserId = firebaseUser.getUid();
            currentUser = repository.getUserById(currentUserId);
        }
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public LiveData<User> getUserById(String userId) {
        return repository.getUserById(userId);
    }

    public void getUserByIdOnce(String userId, FirebaseUserRepository.RepositoryCallback<User> callback) {
        repository.getUserByIdOnce(userId, callback);
    }

    public void createUser(User user, FirebaseUserRepository.RepositoryCallback<Void> callback) {
        repository.createUser(user, callback);
    }

    public void updateUser(User user, FirebaseUserRepository.RepositoryCallback<Void> callback) {
        repository.updateUser(user, callback);
    }

    public void updateDietaryPreference(String userId, String dietaryPreference, FirebaseUserRepository.RepositoryCallback<Void> callback) {
        repository.updateDietaryPreference(userId, dietaryPreference, callback);
    }

    public void updateProfileImageUrl(String userId, String profileImageUrl, FirebaseUserRepository.RepositoryCallback<Void> callback) {
        repository.updateProfileImageUrl(userId, profileImageUrl, callback);
    }

    public void updateLastLoginAt(String userId, FirebaseUserRepository.RepositoryCallback<Void> callback) {
        repository.updateLastLoginAt(userId, callback);
    }

    public void deleteUser(String userId, FirebaseUserRepository.RepositoryCallback<Void> callback) {
        repository.deleteUser(userId, callback);
    }

    public void checkUserExists(String userId, FirebaseUserRepository.RepositoryCallback<Boolean> callback) {
        repository.checkUserExists(userId, callback);
    }
}
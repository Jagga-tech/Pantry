package com.pantrypal.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pantrypal.data.model.User;
import com.pantrypal.data.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<User> currentUser;

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
        currentUser = repository.getCurrentUser();
    }

    public void insert(User user) {
        repository.insert(user);
    }

    public void update(User user) {
        repository.update(user);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public LiveData<User> getUserById(int userId) {
        return repository.getUserById(userId);
    }

    public User getUserByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
}
package com.pantrypal.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pantrypal.data.dao.UserDao;
import com.pantrypal.data.database.PantrypalDatabase;
import com.pantrypal.data.model.User;

public class UserRepository {
    private UserDao userDao;
    private LiveData<User> currentUser;

    public UserRepository(Application application) {
        PantrypalDatabase db = PantrypalDatabase.getDatabase(application);
        userDao = db.userDao();
        currentUser = userDao.getCurrentUser();
    }

    public void insert(User user) {
        new Thread(() -> userDao.insert(user)).start();
    }

    public void update(User user) {
        new Thread(() -> userDao.update(user)).start();
    }

    public void delete(User user) {
        new Thread(() -> userDao.delete(user)).start();
    }

    public LiveData<User> getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public User getUserByEmail(String email) {
        User[] user = new User[1];
        Thread thread = new Thread(() -> user[0] = userDao.getUserByEmail(email));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return user[0];
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
}
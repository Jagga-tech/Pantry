package com.pantrypal.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pantrypal.data.model.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users WHERE id = :userId")
    LiveData<User> getUserById(String userId);

    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserByIdSync(String userId);

    @Query("SELECT * FROM users LIMIT 1")
    LiveData<User> getCurrentUser();

    @Query("DELETE FROM users")
    void deleteAll();
}
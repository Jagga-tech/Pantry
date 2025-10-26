package com.pantrypal.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    private int id;
    private String name;
    private String email;
    private String password;
    private String profilePicUrl;
    private String dietaryPreferences; // JSON string
    private long createdAt;

    public User(int id, String name, String email, String password, String profilePicUrl, String dietaryPreferences, long createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profilePicUrl = profilePicUrl;
        this.dietaryPreferences = dietaryPreferences;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

    public String getDietaryPreferences() { return dietaryPreferences; }
    public void setDietaryPreferences(String dietaryPreferences) { this.dietaryPreferences = dietaryPreferences; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
package com.pantrypal.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    @DocumentId
    private String id;

    private String name;
    private String email;

    @Exclude
    private String passwordHash;

    private String profilePicUrl;
    private String dietaryPreferences;
    private Date createdAt;
    private Date lastLoginAt;

    // Default constructor required for Firestore
    public User() {
        this.id = "";
        this.createdAt = new Date();
    }

    public User(String id, String name, String email, String passwordHash, String profilePicUrl, String dietaryPreferences, Date createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.profilePicUrl = profilePicUrl;
        this.dietaryPreferences = dietaryPreferences;
        this.createdAt = createdAt;
        this.lastLoginAt = new Date();
    }

    /**
     * Create User from FirebaseUser
     */
    public static User fromFirebaseUser(FirebaseUser firebaseUser) {
        if (firebaseUser == null) {
            return null;
        }

        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setName(firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "");
        user.setEmail(firebaseUser.getEmail() != null ? firebaseUser.getEmail() : "");
        user.setProfilePicUrl(firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : "");
        user.setCreatedAt(new Date());
        user.setLastLoginAt(new Date());
        user.setDietaryPreferences("");

        return user;
    }

    /**
     * Convert User to Firestore map
     */
    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name != null ? name : "");
        map.put("email", email != null ? email : "");
        map.put("profilePicUrl", profilePicUrl != null ? profilePicUrl : "");
        map.put("dietaryPreferences", dietaryPreferences != null ? dietaryPreferences : "");
        map.put("createdAt", createdAt != null ? createdAt : new Date());
        map.put("lastLoginAt", lastLoginAt != null ? lastLoginAt : new Date());
        // Note: passwordHash is excluded from Firestore
        return map;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Exclude
    public String getPasswordHash() { return passwordHash; }
    @Exclude
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getProfilePicUrl() { return profilePicUrl; }
    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

    public String getDietaryPreferences() { return dietaryPreferences; }
    public void setDietaryPreferences(String dietaryPreferences) { this.dietaryPreferences = dietaryPreferences; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(Date lastLoginAt) { this.lastLoginAt = lastLoginAt; }
}
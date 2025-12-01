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

    // Nutrition Goals
    private int dailyCalorieGoal;      // Target calories per day
    private int dailyProteinGoal;      // Target protein in grams
    private int dailyCarbsGoal;        // Target carbs in grams
    private int dailyFatGoal;          // Target fat in grams

    // Current nutrition tracking (resets daily)
    private int currentCalories;
    private int currentProtein;
    private int currentCarbs;
    private int currentFat;
    private Date lastNutritionReset;   // Date when nutrition was last reset

    // Meal Planning
    private String mealPlanPreference; // "weekly", "daily", "custom"
    private int mealsPerDay;           // Number of meals per day (default: 3)

    // Default constructor required for Firestore
    public User() {
        this.id = "";
        this.createdAt = new Date();
        this.dailyCalorieGoal = 2000;  // Default 2000 calories
        this.dailyProteinGoal = 50;    // Default 50g protein
        this.dailyCarbsGoal = 250;     // Default 250g carbs
        this.dailyFatGoal = 70;        // Default 70g fat
        this.currentCalories = 0;
        this.currentProtein = 0;
        this.currentCarbs = 0;
        this.currentFat = 0;
        this.lastNutritionReset = new Date();
        this.mealPlanPreference = "weekly";
        this.mealsPerDay = 3;
    }

    @Ignore
    public User(@NonNull String id, String name, String email, String passwordHash, String profilePicUrl, String dietaryPreferences, Date createdAt) {
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

        // Nutrition goals
        map.put("dailyCalorieGoal", dailyCalorieGoal);
        map.put("dailyProteinGoal", dailyProteinGoal);
        map.put("dailyCarbsGoal", dailyCarbsGoal);
        map.put("dailyFatGoal", dailyFatGoal);

        // Current nutrition tracking
        map.put("currentCalories", currentCalories);
        map.put("currentProtein", currentProtein);
        map.put("currentCarbs", currentCarbs);
        map.put("currentFat", currentFat);
        map.put("lastNutritionReset", lastNutritionReset != null ? lastNutritionReset : new Date());

        // Meal planning
        map.put("mealPlanPreference", mealPlanPreference != null ? mealPlanPreference : "weekly");
        map.put("mealsPerDay", mealsPerDay);

        // Note: passwordHash is excluded from Firestore
        return map;
    }

    // Getters and Setters
    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

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

    // Nutrition Goals Getters and Setters
    public int getDailyCalorieGoal() { return dailyCalorieGoal; }
    public void setDailyCalorieGoal(int dailyCalorieGoal) { this.dailyCalorieGoal = dailyCalorieGoal; }

    public int getDailyProteinGoal() { return dailyProteinGoal; }
    public void setDailyProteinGoal(int dailyProteinGoal) { this.dailyProteinGoal = dailyProteinGoal; }

    public int getDailyCarbsGoal() { return dailyCarbsGoal; }
    public void setDailyCarbsGoal(int dailyCarbsGoal) { this.dailyCarbsGoal = dailyCarbsGoal; }

    public int getDailyFatGoal() { return dailyFatGoal; }
    public void setDailyFatGoal(int dailyFatGoal) { this.dailyFatGoal = dailyFatGoal; }

    // Current Nutrition Tracking Getters and Setters
    public int getCurrentCalories() { return currentCalories; }
    @SuppressWarnings("unused")
    public void setCurrentCalories(int currentCalories) { this.currentCalories = currentCalories; }

    public int getCurrentProtein() { return currentProtein; }
    @SuppressWarnings("unused")
    public void setCurrentProtein(int currentProtein) { this.currentProtein = currentProtein; }

    public int getCurrentCarbs() { return currentCarbs; }
    @SuppressWarnings("unused")
    public void setCurrentCarbs(int currentCarbs) { this.currentCarbs = currentCarbs; }

    public int getCurrentFat() { return currentFat; }
    @SuppressWarnings("unused")
    public void setCurrentFat(int currentFat) { this.currentFat = currentFat; }

    public Date getLastNutritionReset() { return lastNutritionReset; }
    @SuppressWarnings("unused")
    public void setLastNutritionReset(Date lastNutritionReset) { this.lastNutritionReset = lastNutritionReset; }

    // Meal Planning Getters and Setters
    @SuppressWarnings("unused")
    public String getMealPlanPreference() { return mealPlanPreference; }
    @SuppressWarnings("unused")
    public void setMealPlanPreference(String mealPlanPreference) { this.mealPlanPreference = mealPlanPreference; }

    @SuppressWarnings("unused")
    public int getMealsPerDay() { return mealsPerDay; }
    @SuppressWarnings("unused")
    public void setMealsPerDay(int mealsPerDay) { this.mealsPerDay = mealsPerDay; }

    // Helper methods
    @SuppressWarnings("unused")
    public void addNutrition(int calories, int protein, int carbs, int fat) {
        this.currentCalories += calories;
        this.currentProtein += protein;
        this.currentCarbs += carbs;
        this.currentFat += fat;
    }

    public void resetDailyNutrition() {
        this.currentCalories = 0;
        this.currentProtein = 0;
        this.currentCarbs = 0;
        this.currentFat = 0;
        this.lastNutritionReset = new Date();
    }

    public int getRemainingCalories() {
        return Math.max(0, dailyCalorieGoal - currentCalories);
    }

    @SuppressWarnings("unused")
    public int getRemainingProtein() {
        return Math.max(0, dailyProteinGoal - currentProtein);
    }

    @SuppressWarnings("unused")
    public int getRemainingCarbs() {
        return Math.max(0, dailyCarbsGoal - currentCarbs);
    }

    @SuppressWarnings("unused")
    public int getRemainingFat() {
        return Math.max(0, dailyFatGoal - currentFat);
    }

    public int getCalorieProgress() {
        if (dailyCalorieGoal == 0) return 0;
        return Math.min(100, (currentCalories * 100) / dailyCalorieGoal);
    }
}
package com.pantrypal.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity(tableName = "meal_plans")
public class MealPlan {
    @PrimaryKey
    @NonNull
    @DocumentId
    private String id;

    private String userId;              // User who owns this meal plan
    private String name;                // e.g., "Week of Jan 15-21"
    private Date startDate;             // When the meal plan starts
    private Date endDate;               // When the meal plan ends
    private String planType;            // "daily", "weekly", "custom"
    private List<String> recipeIds;     // List of recipe IDs in the plan
    private Map<String, Integer> recipeDayMap;  // Map recipe ID to day number
    private Map<String, String> recipeMealMap;  // Map recipe ID to meal type (breakfast, lunch, dinner)
    private int totalCalories;          // Total calories for the plan
    private Date createdAt;
    private Date updatedAt;

    // Default constructor
    public MealPlan() {
        this.id = "";
        this.recipeIds = new ArrayList<>();
        this.recipeDayMap = new HashMap<>();
        this.recipeMealMap = new HashMap<>();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public MealPlan(String userId, String name, Date startDate, Date endDate, String planType) {
        this();
        this.userId = userId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.planType = planType;
    }

    // Convert to Firestore map
    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId != null ? userId : "");
        map.put("name", name != null ? name : "");
        map.put("startDate", startDate != null ? startDate : new Date());
        map.put("endDate", endDate != null ? endDate : new Date());
        map.put("planType", planType != null ? planType : "weekly");
        map.put("recipeIds", recipeIds != null ? recipeIds : new ArrayList<>());
        map.put("recipeDayMap", recipeDayMap != null ? recipeDayMap : new HashMap<>());
        map.put("recipeMealMap", recipeMealMap != null ? recipeMealMap : new HashMap<>());
        map.put("totalCalories", totalCalories);
        map.put("createdAt", createdAt != null ? createdAt : new Date());
        map.put("updatedAt", updatedAt != null ? updatedAt : new Date());
        return map;
    }

    // Helper methods
    public void addRecipe(String recipeId, int dayNumber, String mealType) {
        if (!recipeIds.contains(recipeId)) {
            recipeIds.add(recipeId);
        }
        recipeDayMap.put(recipeId, dayNumber);
        recipeMealMap.put(recipeId, mealType);
        this.updatedAt = new Date();
    }

    public void removeRecipe(String recipeId) {
        recipeIds.remove(recipeId);
        recipeDayMap.remove(recipeId);
        recipeMealMap.remove(recipeId);
        this.updatedAt = new Date();
    }

    public List<String> getRecipesForDay(int dayNumber) {
        List<String> recipes = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : recipeDayMap.entrySet()) {
            if (entry.getValue() == dayNumber) {
                recipes.add(entry.getKey());
            }
        }
        return recipes;
    }

    public List<String> getRecipesForMeal(String mealType) {
        List<String> recipes = new ArrayList<>();
        for (Map.Entry<String, String> entry : recipeMealMap.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(mealType)) {
                recipes.add(entry.getKey());
            }
        }
        return recipes;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getPlanType() { return planType; }
    public void setPlanType(String planType) { this.planType = planType; }

    public List<String> getRecipeIds() { return recipeIds; }
    public void setRecipeIds(List<String> recipeIds) { this.recipeIds = recipeIds; }

    public Map<String, Integer> getRecipeDayMap() { return recipeDayMap; }
    public void setRecipeDayMap(Map<String, Integer> recipeDayMap) { this.recipeDayMap = recipeDayMap; }

    public Map<String, String> getRecipeMealMap() { return recipeMealMap; }
    public void setRecipeMealMap(Map<String, String> recipeMealMap) { this.recipeMealMap = recipeMealMap; }

    public int getTotalCalories() { return totalCalories; }
    public void setTotalCalories(int totalCalories) { this.totalCalories = totalCalories; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}

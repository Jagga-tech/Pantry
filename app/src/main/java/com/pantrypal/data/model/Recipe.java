package com.pantrypal.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String imageUrl;
    private String category;
    private int cookingTime; // in minutes
    private String difficulty;
    private String ingredients; // JSON string
    private String instructions; // JSON string
    private String nutrition; // JSON string
    private int servings;
    private int calories;
    private boolean isFavorite;
    private double matchPercentage;

    public Recipe(int id, String name, String description, String imageUrl, String category, int cookingTime, String difficulty, String ingredients, String instructions, String nutrition, int servings, int calories, boolean isFavorite, double matchPercentage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.cookingTime = cookingTime;
        this.difficulty = difficulty;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.nutrition = nutrition;
        this.servings = servings;
        this.calories = calories;
        this.isFavorite = isFavorite;
        this.matchPercentage = matchPercentage;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getCookingTime() { return cookingTime; }
    public void setCookingTime(int cookingTime) { this.cookingTime = cookingTime; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getNutrition() { return nutrition; }
    public void setNutrition(String nutrition) { this.nutrition = nutrition; }

    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = servings; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public double getMatchPercentage() { return matchPercentage; }
    public void setMatchPercentage(double matchPercentage) { this.matchPercentage = matchPercentage; }
}
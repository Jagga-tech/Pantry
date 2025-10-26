package com.pantrypal.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pantry_items")
public class PantryItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String ingredientName;
    private String category;
    private String quantity;
    private String unit;
    private long expirationDate; // milliseconds
    private String notes;
    private long createdAt;
    private int userId;

    public PantryItem(String ingredientName, String category, String quantity, String unit, long expirationDate, String notes, long createdAt, int userId) {
        this.ingredientName = ingredientName;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.expirationDate = expirationDate;
        this.notes = notes;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public long getExpirationDate() { return expirationDate; }
    public void setExpirationDate(long expirationDate) { this.expirationDate = expirationDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
package com.pantrypal.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "pantry_items")
public class PantryItem {
    @PrimaryKey
    @NonNull
    @DocumentId
    private String id;

    private String userId;
    private String ingredientName;
    private String category;
    private String quantity;
    private String unit;
    private Date expirationDate;
    private String notes;
    private String barcode;

    @ServerTimestamp
    private Date createdAt;

    @ServerTimestamp
    private Date updatedAt;

    // Default constructor required for Firestore
    public PantryItem() {
        this.id = "";
        this.userId = "";
    }

    public PantryItem(String id, String userId, String ingredientName, String category, String quantity,
                      String unit, Date expirationDate, String notes, String barcode) {
        this.id = id;
        this.userId = userId;
        this.ingredientName = ingredientName;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.expirationDate = expirationDate;
        this.notes = notes;
        this.barcode = barcode;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    /**
     * Convert PantryItem to Firestore map
     */
    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId != null ? userId : "");
        map.put("ingredientName", ingredientName != null ? ingredientName : "");
        map.put("category", category != null ? category : "");
        map.put("quantity", quantity != null ? quantity : "");
        map.put("unit", unit != null ? unit : "");
        map.put("expirationDate", expirationDate != null ? expirationDate : new Date());
        map.put("notes", notes != null ? notes : "");
        map.put("barcode", barcode != null ? barcode : "");
        map.put("createdAt", createdAt != null ? createdAt : new Date());
        map.put("updatedAt", new Date()); // Always update timestamp
        return map;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Date getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
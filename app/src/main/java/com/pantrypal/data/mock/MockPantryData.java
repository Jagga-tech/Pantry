package com.pantrypal.data.mock;

import com.pantrypal.data.model.PantryItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Mock pantry items data - 18 Indian ingredients
 * Includes expiring items alert data
 */
public class MockPantryData {

    private static final int USER_ID = 1;
    private static final long NOW = System.currentTimeMillis();

    /**
     * Get date in milliseconds from date string (YYYY-MM-DD)
     */
    private static long parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr).getTime();
        } catch (Exception e) {
            return NOW;
        }
    }

    public static List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();

        // VEGETABLES (5 items)
        items.add(new PantryItem("Tomatoes", "Vegetables", "4", "pieces", 
                parseDate("2025-10-27"), "Expiring in 1 day!", NOW, USER_ID));
        items.add(new PantryItem("Onions", "Vegetables", "6", "pieces", 
                parseDate("2025-11-10"), "", NOW, USER_ID));
        items.add(new PantryItem("Potatoes", "Vegetables", "1", "kg", 
                parseDate("2025-11-15"), "", NOW, USER_ID));
        items.add(new PantryItem("Green Chilies", "Vegetables", "100", "grams", 
                parseDate("2025-10-28"), "Expiring in 2 days!", NOW, USER_ID));
        items.add(new PantryItem("Ginger", "Vegetables", "200", "grams", 
                parseDate("2025-11-05"), "", NOW, USER_ID));

        // PROTEINS (3 items)
        items.add(new PantryItem("Chicken Breast", "Proteins", "500", "grams", 
                parseDate("2025-10-27"), "Expiring in 1 day!", NOW, USER_ID));
        items.add(new PantryItem("Paneer", "Proteins", "250", "grams", 
                parseDate("2025-11-01"), "", NOW, USER_ID));
        items.add(new PantryItem("Chickpeas (Dried)", "Proteins", "500", "grams", 
                parseDate("2026-05-15"), "", NOW, USER_ID));

        // DAIRY (3 items)
        items.add(new PantryItem("Milk", "Dairy", "1", "liters", 
                parseDate("2025-10-29"), "Expiring in 3 days!", NOW, USER_ID));
        items.add(new PantryItem("Yogurt", "Dairy", "500", "grams", 
                parseDate("2025-10-30"), "", NOW, USER_ID));
        items.add(new PantryItem("Ghee", "Dairy", "250", "grams", 
                parseDate("2026-02-20"), "", NOW, USER_ID));

        // GRAINS & LENTILS (4 items)
        items.add(new PantryItem("Basmati Rice", "Grains", "2", "kg", 
                parseDate("2026-08-30"), "", NOW, USER_ID));
        items.add(new PantryItem("Red Lentils (Masoor Dal)", "Grains", "500", "grams", 
                parseDate("2026-03-15"), "", NOW, USER_ID));
        items.add(new PantryItem("Yellow Lentils (Toor Dal)", "Grains", "500", "grams", 
                parseDate("2026-04-20"), "", NOW, USER_ID));
        items.add(new PantryItem("Whole Wheat Flour", "Grains", "1", "kg", 
                parseDate("2025-12-10"), "", NOW, USER_ID));

        // SPICES & CONDIMENTS (3 items)
        items.add(new PantryItem("Turmeric Powder", "Spices", "100", "grams", 
                parseDate("2026-06-30"), "", NOW, USER_ID));
        items.add(new PantryItem("Garam Masala", "Spices", "50", "grams", 
                parseDate("2025-11-20"), "", NOW, USER_ID));
        items.add(new PantryItem("Cumin Seeds", "Spices", "100", "grams", 
                parseDate("2026-01-15"), "", NOW, USER_ID));

        return items;
    }

    /**
     * Get only expiring items (within 3 days)
     */
    public static List<PantryItem> getExpiringItems() {
        List<PantryItem> expiringItems = new ArrayList<>();
        List<PantryItem> allItems = getAllPantryItems();
        long threeDaysLater = NOW + (3 * 24 * 60 * 60 * 1000);

        for (PantryItem item : allItems) {
            if (item.getExpirationDate() <= threeDaysLater && item.getExpirationDate() >= NOW) {
                expiringItems.add(item);
            }
        }
        return expiringItems;
    }

    /**
     * Get items grouped by category
     */
    public static java.util.Map<String, List<PantryItem>> getItemsByCategory() {
        java.util.Map<String, List<PantryItem>> categorized = new java.util.LinkedHashMap<>();
        List<PantryItem> allItems = getAllPantryItems();

        for (PantryItem item : allItems) {
            String category = item.getCategory();
            categorized.putIfAbsent(category, new ArrayList<>());
            categorized.get(category).add(item);
        }
        return categorized;
    }
}
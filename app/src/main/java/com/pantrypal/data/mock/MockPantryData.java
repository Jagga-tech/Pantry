package com.pantrypal.data.mock;

import com.pantrypal.data.model.PantryItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Mock pantry items data - 18 Indian ingredients
 * Includes expiring items alert data
 */
public class MockPantryData {

    private static final String USER_ID = "mock-user-1";
    private static final long NOW = System.currentTimeMillis();

    /**
     * Get date from date string (YYYY-MM-DD)
     */
    private static Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return new Date(NOW);
        }
    }

    public static List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();

        // VEGETABLES (5 items)
        items.add(new PantryItem("item-1", USER_ID, "Tomatoes", "Vegetables", "4", "pieces",
                parseDate("2025-10-27"), "Expiring in 1 day!", ""));
        items.add(new PantryItem("item-2", USER_ID, "Onions", "Vegetables", "6", "pieces",
                parseDate("2025-11-10"), "", ""));
        items.add(new PantryItem("item-3", USER_ID, "Potatoes", "Vegetables", "1", "kg",
                parseDate("2025-11-15"), "", ""));
        items.add(new PantryItem("item-4", USER_ID, "Green Chilies", "Vegetables", "100", "grams",
                parseDate("2025-10-28"), "Expiring in 2 days!", ""));
        items.add(new PantryItem("item-5", USER_ID, "Ginger", "Vegetables", "200", "grams",
                parseDate("2025-11-05"), "", ""));

        // PROTEINS (3 items)
        items.add(new PantryItem("item-6", USER_ID, "Chicken Breast", "Proteins", "500", "grams",
                parseDate("2025-10-27"), "Expiring in 1 day!", ""));
        items.add(new PantryItem("item-7", USER_ID, "Paneer", "Proteins", "250", "grams",
                parseDate("2025-11-01"), "", ""));
        items.add(new PantryItem("item-8", USER_ID, "Chickpeas (Dried)", "Proteins", "500", "grams",
                parseDate("2026-05-15"), "", ""));

        // DAIRY (3 items)
        items.add(new PantryItem("item-9", USER_ID, "Milk", "Dairy", "1", "liters",
                parseDate("2025-10-29"), "Expiring in 3 days!", ""));
        items.add(new PantryItem("item-10", USER_ID, "Yogurt", "Dairy", "500", "grams",
                parseDate("2025-10-30"), "", ""));
        items.add(new PantryItem("item-11", USER_ID, "Ghee", "Dairy", "250", "grams",
                parseDate("2026-02-20"), "", ""));

        // GRAINS & LENTILS (4 items)
        items.add(new PantryItem("item-12", USER_ID, "Basmati Rice", "Grains", "2", "kg",
                parseDate("2026-08-30"), "", ""));
        items.add(new PantryItem("item-13", USER_ID, "Red Lentils (Masoor Dal)", "Grains", "500", "grams",
                parseDate("2026-03-15"), "", ""));
        items.add(new PantryItem("item-14", USER_ID, "Yellow Lentils (Toor Dal)", "Grains", "500", "grams",
                parseDate("2026-04-20"), "", ""));
        items.add(new PantryItem("item-15", USER_ID, "Whole Wheat Flour", "Grains", "1", "kg",
                parseDate("2025-12-10"), "", ""));

        // SPICES & CONDIMENTS (3 items)
        items.add(new PantryItem("item-16", USER_ID, "Turmeric Powder", "Spices", "100", "grams",
                parseDate("2026-06-30"), "", ""));
        items.add(new PantryItem("item-17", USER_ID, "Garam Masala", "Spices", "50", "grams",
                parseDate("2025-11-20"), "", ""));
        items.add(new PantryItem("item-18", USER_ID, "Cumin Seeds", "Spices", "100", "grams",
                parseDate("2026-01-15"), "", ""));

        return items;
    }

    /**
     * Get only expiring items (within 3 days)
     */
    public static List<PantryItem> getExpiringItems() {
        List<PantryItem> expiringItems = new ArrayList<>();
        List<PantryItem> allItems = getAllPantryItems();
        Date now = new Date();
        Date threeDaysLater = new Date(NOW + (3 * 24 * 60 * 60 * 1000));

        for (PantryItem item : allItems) {
            Date expDate = item.getExpirationDate();
            if (expDate != null && !expDate.after(threeDaysLater) && !expDate.before(now)) {
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
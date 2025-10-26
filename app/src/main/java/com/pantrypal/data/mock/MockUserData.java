package com.pantrypal.data.mock;

import com.pantrypal.data.model.User;

/**
 * Mock user data for testing
 */
public class MockUserData {

    public static User getMockUser() {
        return new User(
                1,                                      // id
                "Chan",                                 // name
                "chan@example.com",                     // email
                "password123",                          // password
                "",                                     // profilePicUrl (empty for placeholder)
                "[\"Vegetarian\"]",                     // dietaryPreferences (JSON)
                System.currentTimeMillis()              // createdAt
        );
    }

    public static class Statistics {
        public static int getTotalRecipes() { return 12; }
        public static int getPantryItemsCount() { return 18; }
        public static int getFavoriteRecipesCount() { return 12; }
    }
}
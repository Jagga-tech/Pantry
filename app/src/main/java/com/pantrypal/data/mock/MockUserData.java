package com.pantrypal.data.mock;

import com.pantrypal.data.model.User;
import java.util.Date;

/**
 * Mock user data for testing
 */
public class MockUserData {

    public static User getMockUser() {
        return new User(
                "mock-user-1",                          // id
                "Chan",                                 // name
                "chan@example.com",                     // email
                "hashed-password-123",                  // passwordHash
                "",                                     // profilePicUrl (empty for placeholder)
                "[\"Vegetarian\"]",                     // dietaryPreferences (JSON)
                new Date()                              // createdAt
        );
    }

    public static class Statistics {
        public static int getTotalRecipes() { return 12; }
        public static int getPantryItemsCount() { return 18; }
        public static int getFavoriteRecipesCount() { return 12; }
    }
}
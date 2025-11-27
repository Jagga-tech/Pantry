package com.pantrypal.data.service;

import com.pantrypal.data.model.PantryItem;
import com.pantrypal.data.model.Recipe;
import com.pantrypal.data.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for recommending recipes based on:
 * - Available pantry items
 * - User's dietary preferences
 * - User's nutrition goals
 */
public class RecipeRecommendationService {

    /**
     * Recommend recipes based on user's pantry and preferences
     */
    public static List<RecipeRecommendation> recommendRecipes(
            List<Recipe> allRecipes,
            List<PantryItem> pantryItems,
            User user,
            int maxResults) {

        List<RecipeRecommendation> recommendations = new ArrayList<>();

        // Get pantry ingredient names
        Set<String> pantryIngredients = pantryItems.stream()
                .map(item -> item.getIngredientName().toLowerCase())
                .collect(Collectors.toSet());

        for (Recipe recipe : allRecipes) {
            double score = calculateRecommendationScore(recipe, pantryIngredients, user);
            if (score > 0) {
                recommendations.add(new RecipeRecommendation(recipe, score));
            }
        }

        // Sort by score (highest first) and return top results
        return recommendations.stream()
                .sorted(Comparator.comparingDouble(RecipeRecommendation::getScore).reversed())
                .limit(maxResults)
                .collect(Collectors.toList());
    }

    /**
     * Calculate recommendation score for a recipe
     * Score components:
     * - Pantry match: 0-50 points
     * - Dietary match: 0-30 points
     * - Nutrition fit: 0-20 points
     */
    private static double calculateRecommendationScore(
            Recipe recipe,
            Set<String> pantryIngredients,
            User user) {

        double score = 0.0;

        // 1. Pantry Match Score (0-50 points)
        score += calculatePantryMatchScore(recipe, pantryIngredients);

        // 2. Dietary Preference Score (0-30 points)
        score += calculateDietaryScore(recipe, user);

        // 3. Nutrition Goal Score (0-20 points)
        score += calculateNutritionScore(recipe, user);

        return score;
    }

    /**
     * Calculate how well the recipe matches available pantry items
     */
    private static double calculatePantryMatchScore(Recipe recipe, Set<String> pantryIngredients) {
        if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            return 0.0;
        }

        try {
            JSONArray ingredients = new JSONArray(recipe.getIngredients());
            int totalIngredients = ingredients.length();
            int matchedIngredients = 0;

            for (int i = 0; i < totalIngredients; i++) {
                JSONObject ingredient = ingredients.getJSONObject(i);
                String name = ingredient.optString("name", "").toLowerCase();

                // Check for exact or partial match
                if (containsIngredient(pantryIngredients, name)) {
                    matchedIngredients++;
                }
            }

            if (totalIngredients == 0) return 0.0;

            // Calculate match percentage and convert to 0-50 point scale
            double matchPercentage = (double) matchedIngredients / totalIngredients;
            return matchPercentage * 50.0;

        } catch (JSONException e) {
            return 0.0;
        }
    }

    /**
     * Check if pantry contains an ingredient (handles partial matches)
     */
    private static boolean containsIngredient(Set<String> pantryIngredients, String recipeIngredient) {
        // Exact match
        if (pantryIngredients.contains(recipeIngredient)) {
            return true;
        }

        // Partial match (e.g., "chicken" matches "chicken breast")
        for (String pantryItem : pantryIngredients) {
            if (pantryItem.contains(recipeIngredient) || recipeIngredient.contains(pantryItem)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Calculate how well the recipe matches dietary preferences
     */
    private static double calculateDietaryScore(Recipe recipe, User user) {
        if (user == null || user.getDietaryPreferences() == null ||
                user.getDietaryPreferences().isEmpty()) {
            return 15.0; // Neutral score if no dietary preference
        }

        String dietPref = user.getDietaryPreferences().toLowerCase();
        String category = recipe.getCategory() != null ? recipe.getCategory().toLowerCase() : "";

        // Perfect match
        if (category.contains(dietPref) || category.equals(dietPref)) {
            return 30.0;
        }

        // Vegetarian/Vegan logic
        if (dietPref.equals("vegetarian")) {
            if (!category.contains("meat") && !category.contains("fish") &&
                    !category.contains("chicken") && !category.contains("beef") &&
                    !category.contains("pork")) {
                return 25.0;
            }
        }

        if (dietPref.equals("vegan")) {
            if (!category.contains("meat") && !category.contains("fish") &&
                    !category.contains("dairy") && !category.contains("egg") &&
                    !category.contains("chicken") && !category.contains("beef")) {
                return 25.0;
            }
        }

        // Gluten-free logic
        if (dietPref.equals("gluten-free")) {
            if (!category.contains("bread") && !category.contains("pasta") &&
                    !category.contains("wheat")) {
                return 25.0;
            }
        }

        // Keto logic
        if (dietPref.equals("keto")) {
            if (!category.contains("rice") && !category.contains("pasta") &&
                    !category.contains("bread") && !category.contains("potato")) {
                return 25.0;
            }
        }

        // No match
        return 5.0;
    }

    /**
     * Calculate how well the recipe fits nutrition goals
     */
    private static double calculateNutritionScore(Recipe recipe, User user) {
        if (user == null) return 10.0;

        int recipeCalories = recipe.getCalories();
        int dailyGoal = user.getDailyCalorieGoal();
        int remaining = user.getRemainingCalories();

        // If user has no calories left, penalize high-calorie recipes
        if (remaining <= 0) {
            return recipeCalories < 300 ? 15.0 : 0.0;
        }

        // Calculate how well the recipe fits remaining calorie budget
        // Ideal: recipe uses 20-40% of remaining calories
        double percentageOfRemaining = (double) recipeCalories / remaining;

        if (percentageOfRemaining >= 0.2 && percentageOfRemaining <= 0.4) {
            return 20.0; // Perfect fit
        } else if (percentageOfRemaining < 0.2) {
            return 15.0; // Light meal
        } else if (percentageOfRemaining <= 0.6) {
            return 10.0; // Acceptable
        } else if (percentageOfRemaining <= 0.8) {
            return 5.0;  // High calorie
        }

        return 0.0; // Too high calorie
    }

    /**
     * Get missing ingredients for a recipe
     */
    public static List<String> getMissingIngredients(Recipe recipe, List<PantryItem> pantryItems) {
        List<String> missing = new ArrayList<>();

        Set<String> pantryIngredients = pantryItems.stream()
                .map(item -> item.getIngredientName().toLowerCase())
                .collect(Collectors.toSet());

        try {
            JSONArray ingredients = new JSONArray(recipe.getIngredients());

            for (int i = 0; i < ingredients.length(); i++) {
                JSONObject ingredient = ingredients.getJSONObject(i);
                String name = ingredient.optString("name", "");

                if (!containsIngredient(pantryIngredients, name.toLowerCase())) {
                    missing.add(name);
                }
            }
        } catch (JSONException e) {
            // Return empty list on error
        }

        return missing;
    }

    /**
     * Filter recipes that can be made with current pantry (90%+ match)
     */
    public static List<Recipe> getRecipesYouCanMake(
            List<Recipe> allRecipes,
            List<PantryItem> pantryItems) {

        Set<String> pantryIngredients = pantryItems.stream()
                .map(item -> item.getIngredientName().toLowerCase())
                .collect(Collectors.toSet());

        List<Recipe> canMake = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            double matchScore = calculatePantryMatchScore(recipe, pantryIngredients);
            // If 90% or more ingredients are available (45/50 points)
            if (matchScore >= 45.0) {
                canMake.add(recipe);
            }
        }

        return canMake;
    }

    /**
     * Recommendation result wrapper
     */
    public static class RecipeRecommendation {
        private Recipe recipe;
        private double score;
        private String reason;

        public RecipeRecommendation(Recipe recipe, double score) {
            this.recipe = recipe;
            this.score = score;
            this.reason = generateReason(score);
        }

        private String generateReason(double score) {
            if (score >= 80) {
                return "Perfect match! You have most ingredients.";
            } else if (score >= 60) {
                return "Great match for your pantry and diet.";
            } else if (score >= 40) {
                return "Good option with available ingredients.";
            } else if (score >= 20) {
                return "Consider this recipe.";
            }
            return "New recipe to try.";
        }

        public Recipe getRecipe() { return recipe; }
        public double getScore() { return score; }
        public String getReason() { return reason; }
    }
}

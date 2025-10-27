package com.pantrypal.data.mock;

import com.pantrypal.data.model.Recipe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;

/**
 * Mock recipe data - 20 Indian recipes with full details
 */
public class MockRecipeData {

    public static List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        // RECIPE 1: Butter Chicken
        recipes.add(new Recipe(1, "Butter Chicken (Murgh Makhani)", 
                "Creamy tomato-based curry with tender chicken",
                "https://example.com/butter-chicken.jpg", "Dinner", 45, "Medium",
                toJsonArray("500g chicken breast, cubed", "4 tomatoes, pureed", "1 cup yogurt", 
                        "2 tbsp ginger-garlic paste", "1 tsp garam masala", "1 tsp turmeric", 
                        "2 tbsp butter", "1 cup heavy cream", "Salt to taste"),
                toJsonArray("Marinate chicken in yogurt, ginger-garlic paste, and spices for 30 minutes",
                        "Cook marinated chicken in butter until golden",
                        "Add tomato puree and simmer for 15 minutes",
                        "Add cream and garam masala",
                        "Simmer for 10 more minutes until thick",
                        "Serve hot with naan or rice"),
                toJsonObject("420", "32g", "18g", "26g"), 4, 420, true, 95.0));

        // RECIPE 2: Palak Paneer
        recipes.add(new Recipe(2, "Palak Paneer", 
                "Spinach and paneer cheese curry",
                "https://example.com/palak-paneer.jpg", "Dinner", 30, "Easy",
                toJsonArray("250g paneer, cubed", "500g fresh spinach", "2 tomatoes, chopped",
                        "1 onion, chopped", "2 tsp ginger-garlic paste", "1 tsp garam masala",
                        "1 tsp cumin seeds", "2 tbsp cream", "Salt to taste"),
                toJsonArray("Blanch spinach and puree it",
                        "Sauté cumin seeds, onions, and ginger-garlic paste",
                        "Add tomatoes and cook until soft",
                        "Add spinach puree and spices",
                        "Add paneer cubes and cream",
                        "Simmer for 5 minutes and serve"),
                toJsonObject("280", "16g", "14g", "18g"), 4, 280, true, 90.0));

        // RECIPE 3: Chicken Biryani
        recipes.add(new Recipe(3, "Chicken Biryani", 
                "Fragrant rice with layered chicken",
                "https://example.com/chicken-biryani.jpg", "Dinner", 60, "Hard",
                toJsonArray("500g chicken", "2 cups basmati rice", "2 onions, sliced",
                        "1 cup yogurt", "2 tbsp ginger-garlic paste", "2 tsp garam masala",
                        "1 tsp turmeric", "4 green chilies", "Fresh coriander", "Saffron soaked in milk"),
                toJsonArray("Marinate chicken in yogurt and spices",
                        "Parboil rice with whole spices",
                        "Fry onions until golden brown",
                        "Layer marinated chicken in pot",
                        "Layer rice and fried onions",
                        "Cover and cook on low heat for 30 minutes",
                        "Serve with raita"),
                toJsonObject("520", "28g", "65g", "16g"), 6, 520, true, 85.0));

        // RECIPE 4: Chana Masala
        recipes.add(new Recipe(4, "Chana Masala (Chickpea Curry)", 
                "Spiced chickpea curry",
                "https://example.com/chana-masala.jpg", "Dinner", 35, "Easy",
                toJsonArray("2 cups cooked chickpeas", "3 tomatoes, pureed", "2 onions, chopped",
                        "1 tbsp ginger-garlic paste", "2 tsp garam masala", "1 tsp cumin seeds",
                        "1 tsp turmeric", "2 green chilies", "Fresh coriander"),
                toJsonArray("Sauté cumin seeds and onions",
                        "Add ginger-garlic paste and tomatoes",
                        "Add spices and cook until oil separates",
                        "Add chickpeas and water",
                        "Simmer for 15 minutes",
                        "Garnish with coriander"),
                toJsonObject("240", "12g", "38g", "6g"), 4, 240, true, 92.0));

        // RECIPE 5: Dal Tadka
        recipes.add(new Recipe(5, "Dal Tadka", 
                "Lentil soup with tempering",
                "https://example.com/dal-tadka.jpg", "Dinner", 30, "Easy",
                toJsonArray("1 cup yellow lentils (toor dal)", "2 tomatoes, chopped", "1 onion, chopped",
                        "2 tsp cumin seeds", "1 tsp turmeric", "2 green chilies", "2 tbsp ghee",
                        "Garlic cloves", "Fresh coriander"),
                toJsonArray("Pressure cook lentils with turmeric until soft",
                        "Heat ghee and add cumin seeds",
                        "Add garlic, onions, and green chilies",
                        "Add tomatoes and cook until soft",
                        "Pour tadka over cooked dal",
                        "Garnish with coriander"),
                toJsonObject("180", "10g", "26g", "4g"), 4, 180, true, 90.0));

        // RECIPE 6: Aloo Gobi
        recipes.add(new Recipe(6, "Aloo Gobi (Potato Cauliflower)", 
                "Spiced potato and cauliflower stir-fry",
                "https://example.com/aloo-gobi.jpg", "Dinner", 25, "Easy",
                toJsonArray("3 potatoes, cubed", "1 small cauliflower", "2 tomatoes, chopped",
                        "1 tsp cumin seeds", "1 tsp turmeric", "1 tsp garam masala",
                        "2 green chilies", "Fresh coriander"),
                toJsonArray("Sauté cumin seeds",
                        "Add potatoes and cauliflower",
                        "Add tomatoes and spices",
                        "Cover and cook until vegetables are tender",
                        "Garnish with coriander"),
                toJsonObject("150", "4g", "28g", "3g"), 4, 150, true, 88.0));

        // RECIPE 7: Paneer Tikka
        recipes.add(new Recipe(7, "Paneer Tikka", 
                "Grilled paneer and vegetable skewers",
                "https://example.com/paneer-tikka.jpg", "Snacks", 20, "Easy",
                toJsonArray("250g paneer, cubed", "1/2 cup yogurt", "2 bell peppers, cubed",
                        "1 onion, cubed", "2 tbsp tikka masala", "1 tsp ginger-garlic paste",
                        "Lemon juice", "Oil for grilling"),
                toJsonArray("Marinate paneer and vegetables in yogurt and spices",
                        "Thread onto skewers",
                        "Grill or bake until golden",
                        "Serve hot with chutney"),
                toJsonObject("220", "14g", "12g", "14g"), 4, 220, true, 85.0));

        // RECIPE 8: Jeera Rice
        recipes.add(new Recipe(8, "Jeera Rice (Cumin Rice)", 
                "Fragrant rice with cumin",
                "https://example.com/jeera-rice.jpg", "Dinner", 20, "Easy",
                toJsonArray("2 cups basmati rice", "2 tsp cumin seeds", "2 tbsp ghee",
                        "1 bay leaf", "2 green chilies", "Salt to taste"),
                toJsonArray("Wash and soak rice for 20 minutes",
                        "Heat ghee and add cumin seeds",
                        "Add bay leaf and green chilies",
                        "Add rice and sauté for 2 minutes",
                        "Add water and cook until rice is done",
                        "Fluff and serve"),
                toJsonObject("280", "6g", "52g", "6g"), 4, 280, true, 95.0));

        // RECIPE 9: Chicken Curry
        recipes.add(new Recipe(9, "Chicken Curry", 
                "Classic Indian chicken curry",
                "https://example.com/chicken-curry.jpg", "Dinner", 40, "Medium",
                toJsonArray("500g chicken", "3 tomatoes, pureed", "2 onions, chopped",
                        "1 tbsp ginger-garlic paste", "2 tsp curry powder", "1 tsp turmeric",
                        "1 tsp garam masala", "2 green chilies", "Fresh coriander"),
                toJsonArray("Sauté onions until golden",
                        "Add ginger-garlic paste",
                        "Add tomato puree and spices",
                        "Add chicken and cook",
                        "Add water and simmer until chicken is tender",
                        "Garnish with coriander"),
                toJsonObject("320", "28g", "16g", "16g"), 4, 320, true, 92.0));

        // RECIPE 10: Masala Dosa
        recipes.add(new Recipe(10, "Masala Dosa", 
                "Crispy fermented rice crepe with potato filling",
                "https://example.com/masala-dosa.jpg", "Breakfast", 30, "Medium",
                toJsonArray("2 cups dosa batter", "4 potatoes, boiled and mashed",
                        "1 onion, chopped", "2 green chilies", "1 tsp mustard seeds",
                        "1 tsp turmeric", "Curry leaves", "Oil for dosa"),
                toJsonArray("Prepare potato filling with spices",
                        "Heat dosa pan",
                        "Spread dosa batter thin",
                        "Add potato filling in center",
                        "Fold and serve with chutney"),
                toJsonObject("280", "6g", "48g", "6g"), 4, 280, true, 75.0));

        // RECIPE 11: Cucumber Raita
        recipes.add(new Recipe(11, "Cucumber Raita", 
                "Yogurt-based side dish with cucumber",
                "https://example.com/raita.jpg", "Snacks", 10, "Easy",
                toJsonArray("2 cups yogurt", "1 cucumber, grated", "1 tsp cumin powder",
                        "Salt to taste", "Fresh coriander"),
                toJsonArray("Mix yogurt with grated cucumber",
                        "Add cumin powder and salt",
                        "Garnish with coriander",
                        "Chill and serve"),
                toJsonObject("120", "4g", "8g", "6g"), 4, 120, true, 85.0));

        // RECIPE 12: Samosas
        recipes.add(new Recipe(12, "Samosas", 
                "Crispy pastry with spiced potato filling",
                "https://example.com/samosa.jpg", "Snacks", 40, "Medium",
                toJsonArray("2 cups flour", "3 potatoes, boiled", "1 cup peas",
                        "2 green chilies", "1 tsp garam masala", "Oil for frying"),
                toJsonArray("Prepare dough with flour and oil",
                        "Roll and cut into triangles",
                        "Prepare potato-peas filling",
                        "Fill and seal samosas",
                        "Deep fry until golden"),
                toJsonObject("280", "5g", "35g", "12g"), 4, 280, true, 80.0));

        // RECIPE 13: Chole Bhature
        recipes.add(new Recipe(13, "Chole Bhature", 
                "Fluffy fried bread with chickpea curry",
                "https://example.com/chole-bhature.jpg", "Breakfast", 45, "Hard",
                toJsonArray("3 cups flour", "2 cups chickpeas", "2 onions",
                        "3 tomatoes", "Yogurt", "Ginger-garlic paste"),
                toJsonArray("Prepare dough with yogurt",
                        "Let it rest for 4 hours",
                        "Prepare chickpea curry",
                        "Deep fry bhature until puffed",
                        "Serve with curry and pickle"),
                toJsonObject("450", "15g", "68g", "14g"), 4, 450, false, 82.0));

        // RECIPE 14: Tandoori Chicken
        recipes.add(new Recipe(14, "Tandoori Chicken", 
                "Grilled chicken in yogurt marinade",
                "https://example.com/tandoori-chicken.jpg", "Dinner", 40, "Medium",
                toJsonArray("1 kg chicken", "1 cup yogurt", "2 tbsp tandoori masala",
                        "2 tbsp ginger-garlic paste", "Lemon juice", "Oil"),
                toJsonArray("Mix yogurt with tandoori masala and spices",
                        "Marinate chicken for 2-4 hours",
                        "Grill or bake at 400F",
                        "Baste with butter",
                        "Serve with lemon and onions"),
                toJsonObject("380", "45g", "8g", "18g"), 4, 380, true, 88.0));

        // RECIPE 15: Biryani Rice
        recipes.add(new Recipe(15, "Vegetable Biryani", 
                "Fragrant vegetable rice",
                "https://example.com/veg-biryani.jpg", "Dinner", 50, "Medium",
                toJsonArray("2 cups basmati rice", "2 cups mixed vegetables",
                        "2 onions", "2 tbsp ghee", "2 tsp garam masala"),
                toJsonArray("Boil rice with spices",
                        "Sauté vegetables with ghee",
                        "Layer rice and vegetables",
                        "Cook covered for 15 minutes"),
                toJsonObject("320", "8g", "58g", "8g"), 4, 320, false, 80.0));

        // RECIPE 16: Garlic Naan
        recipes.add(new Recipe(16, "Garlic Naan", 
                "Flatbread with garlic butter",
                "https://example.com/naan.jpg", "Dinner", 20, "Easy",
                toJsonArray("3 cups flour", "1 cup yogurt", "2 tbsp ghee",
                        "Garlic cloves", "Salt"),
                toJsonArray("Prepare dough with flour and yogurt",
                        "Rest for 2 hours",
                        "Roll into oval shapes",
                        "Cook on hot skillet",
                        "Brush with garlic butter"),
                toJsonObject("240", "6g", "40g", "8g"), 4, 240, false, 85.0));

        // RECIPE 17: Gulab Jamun
        recipes.add(new Recipe(17, "Gulab Jamun", 
                "Sweet milk ball dessert",
                "https://example.com/gulab-jamun.jpg", "Dessert", 30, "Medium",
                toJsonArray("1 cup milk powder", "1/2 cup flour", "4 tbsp ghee",
                        "2 cups sugar", "Water", "Cardamom"),
                toJsonArray("Mix milk powder and flour",
                        "Make into balls",
                        "Deep fry until golden",
                        "Soak in sugar syrup"),
                toJsonObject("180", "2g", "32g", "6g"), 4, 180, false, 75.0));

        // RECIPE 18: Kheer
        recipes.add(new Recipe(18, "Kheer", 
                "Rice pudding dessert",
                "https://example.com/kheer.jpg", "Dessert", 25, "Easy",
                toJsonArray("1 cup rice", "1 liter milk", "1/2 cup sugar",
                        "Cardamom", "Nuts"),
                toJsonArray("Fry rice in ghee",
                        "Add milk and sugar",
                        "Simmer until rice is soft",
                        "Garnish with nuts and cardamom"),
                toJsonObject("220", "6g", "35g", "8g"), 4, 220, false, 80.0));

        // RECIPE 19: Idli
        recipes.add(new Recipe(19, "Idli", 
                "Steamed rice cake",
                "https://example.com/idli.jpg", "Breakfast", 20, "Easy",
                toJsonArray("2 cups rice", "1 cup lentils", "Salt", "Water"),
                toJsonArray("Soak rice and lentils",
                        "Grind into batter",
                        "Ferment overnight",
                        "Pour into molds",
                        "Steam for 10 minutes"),
                toJsonObject("140", "4g", "28g", "2g"), 4, 140, false, 70.0));

        // RECIPE 20: Uttapam
        recipes.add(new Recipe(20, "Uttapam", 
                "Thick Indian pancake",
                "https://example.com/uttapam.jpg", "Breakfast", 25, "Easy",
                toJsonArray("Idli batter", "Onions", "Tomatoes", "Green chilies", "Oil"),
                toJsonArray("Heat griddle",
                        "Pour thick batter",
                        "Add toppings",
                        "Cook until golden",
                        "Serve with chutney"),
                toJsonObject("180", "5g", "32g", "5g"), 4, 180, false, 78.0));

        return recipes;
    }

    /**
     * Get recipes by category
     */
    public static List<Recipe> getRecipesByCategory(String category) {
        List<Recipe> filtered = new ArrayList<>();
        for (Recipe recipe : getAllRecipes()) {
            if (recipe.getCategory().equalsIgnoreCase(category)) {
                filtered.add(recipe);
            }
        }
        return filtered;
    }

    /**
     * Get favorite recipes
     */
    public static List<Recipe> getFavoriteRecipes() {
        List<Recipe> favorites = new ArrayList<>();
        for (Recipe recipe : getAllRecipes()) {
            if (recipe.isFavorite()) {
                favorites.add(recipe);
            }
        }
        return favorites;
    }

    /**
     * Convert list to JSON array string
     */
    private static String toJsonArray(String... items) {
        try {
            JSONArray array = new JSONArray();
            for (String item : items) {
                array.put(item);
            }
            return array.toString();
        } catch (Exception e) {
            return "[]";
        }
    }

    /**
     * Convert nutrition data to JSON object string
     */
    private static String toJsonObject(String calories, String protein, String carbs, String fat) {
        try {
            org.json.JSONObject obj = new org.json.JSONObject();
            obj.put("calories", calories);
            obj.put("protein", protein);
            obj.put("carbs", carbs);
            obj.put("fat", fat);
            return obj.toString();
        } catch (Exception e) {
            return "{}";
        }
    }
}
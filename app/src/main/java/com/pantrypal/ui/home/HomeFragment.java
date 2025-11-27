package com.pantrypal.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.card.MaterialCardView;
import com.pantrypal.databinding.FragmentHomeBinding;
import com.pantrypal.data.model.Recipe;
import com.pantrypal.data.model.User;
import com.pantrypal.data.model.PantryItem;
import com.pantrypal.data.service.RecipeRecommendationService;
import com.pantrypal.ui.viewmodel.RecipeViewModel;
import com.pantrypal.ui.viewmodel.UserViewModel;
import com.pantrypal.ui.viewmodel.PantryItemViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private RecipeViewModel recipeViewModel;
    private UserViewModel userViewModel;
    private PantryItemViewModel pantryItemViewModel;
    private RecipeGridAdapter recipeAdapter;
    private User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        pantryItemViewModel = new ViewModelProvider(this).get(PantryItemViewModel.class);

        // Setup user information and personalized content
        setupUserInfo();

        // Setup greeting based on time
        setupGreeting();

        // Setup stats cards with live data
        setupStatsCards();

        // Setup expiring items alert
        setupExpiringItemsAlert();

        // Setup recipe suggestions (will be filtered by dietary preference)
        setupRecipeSuggestions();

        // Setup FAB
        binding.fabAddPantry.setOnClickListener(v -> {
            // Navigate to Add Pantry Item
        });
    }
    
    private void setupUserInfo() {
        // Display user information and handle dietary preferences
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                currentUser = user;

                // Set user name with personalized welcome message
                binding.userName.setText(user.getName());

                // Update greeting to include dietary preference if set
                if (user.getDietaryPreferences() != null && !user.getDietaryPreferences().isEmpty()) {
                    String dietInfo = " (" + user.getDietaryPreferences() + ")";
                    // You could add a TextView to show dietary preference, or update existing elements
                }

                // Reload recipes with dietary filter if needed
                setupRecipeSuggestions();
            }
        });
    }

    private void setupGreeting() {
        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour < 12) {
            greeting = "Good Morning";
        } else if (hour < 18) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Evening";
        }
        binding.greetingText.setText(greeting);
    }

    private void setupStatsCards() {
        // Setup pantry items count
        if (pantryItemViewModel != null) {
            pantryItemViewModel.getAllPantryItems().observe(getViewLifecycleOwner(), pantryItems -> {
                if (pantryItems != null) {
                    binding.pantryCount.setText(String.valueOf(pantryItems.size()));
                }
            });
        }

        // Setup saved recipes count
        if (recipeViewModel != null) {
            recipeViewModel.getFavoritesCount().observe(getViewLifecycleOwner(), count -> {
                if (count != null) {
                    binding.recipesCount.setText(String.valueOf(count));
                }
            });
        }
    }

    private void setupExpiringItemsAlert() {
        if (pantryItemViewModel != null) {
            pantryItemViewModel.getExpiringItems().observe(getViewLifecycleOwner(), expiringItems -> {
                if (expiringItems != null && !expiringItems.isEmpty()) {
                    // Show the expiring alert card
                    binding.expiringAlertCard.setVisibility(View.VISIBLE);

                    // Set count badge
                    binding.expiringCountBadge.setText(String.valueOf(expiringItems.size()));

                    // Create personalized message with user's name
                    String itemsList;
                    if (expiringItems.size() <= 3) {
                        // Show up to 3 items by name
                        itemsList = expiringItems.stream()
                            .limit(3)
                            .map(PantryItem::getIngredientName)
                            .collect(Collectors.joining(", "));
                    } else {
                        // Show first 2 and indicate more
                        itemsList = expiringItems.stream()
                            .limit(2)
                            .map(PantryItem::getIngredientName)
                            .collect(Collectors.joining(", "))
                            + " and " + (expiringItems.size() - 2) + " more";
                    }

                    // Add user's name to the message if available
                    String message;
                    if (currentUser != null) {
                        message = currentUser.getName() + ", these items are expiring soon: " + itemsList;
                    } else {
                        message = "These items are expiring soon: " + itemsList;
                    }
                    binding.expiringItemsList.setText(message);

                    // Setup button to find recipes with expiring items
                    binding.expiringFindRecipesBtn.setOnClickListener(v -> {
                        // Navigate to browse recipes with expiring items filter
                        // TODO: Implement navigation
                    });
                } else {
                    // Hide the expiring alert card if no items are expiring
                    binding.expiringAlertCard.setVisibility(View.GONE);
                }
            });
        }
    }

    private void setupRecipeSuggestions() {
        // Initialize adapter if not already done
        if (recipeAdapter == null) {
            recipeAdapter = new RecipeGridAdapter(new ArrayList<>());
            binding.popularRecipesRecycler.setAdapter(recipeAdapter);
        }

        // Use intelligent recommendation algorithm
        recipeViewModel.getTopRecipes(30).observe(getViewLifecycleOwner(), allRecipes -> {
            if (allRecipes != null && !allRecipes.isEmpty()) {
                pantryItemViewModel.getAllPantryItems().observe(getViewLifecycleOwner(), pantryItems -> {
                    if (currentUser != null && pantryItems != null) {
                        // Use RecipeRecommendationService for intelligent recommendations
                        List<RecipeRecommendationService.RecipeRecommendation> recommendations =
                                RecipeRecommendationService.recommendRecipes(
                                        allRecipes,
                                        pantryItems,
                                        currentUser,
                                        10
                                );

                        // Extract recipes from recommendations
                        List<Recipe> recommendedRecipes = recommendations.stream()
                                .map(RecipeRecommendationService.RecipeRecommendation::getRecipe)
                                .collect(Collectors.toList());

                        recipeAdapter.setRecipes(recommendedRecipes);
                    } else {
                        // Fallback: show top recipes
                        recipeAdapter.setRecipes(allRecipes.stream()
                                .limit(10)
                                .collect(Collectors.toList()));
                    }
                });
            }
        });
    }

    private void loadAndFilterRecipes(String dietaryPreference) {
        recipeViewModel.getTopRecipes(20).observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null && !recipes.isEmpty()) {
                // Filter recipes that match dietary preference
                List<Recipe> filteredRecipes = recipes.stream()
                    .filter(recipe -> {
                        String category = recipe.getCategory();
                        return category != null &&
                               (category.equalsIgnoreCase(dietaryPreference) ||
                                category.toLowerCase().contains(dietaryPreference.toLowerCase()));
                    })
                    .limit(10)
                    .collect(Collectors.toList());

                if (!filteredRecipes.isEmpty()) {
                    recipeAdapter.setRecipes(filteredRecipes);
                } else {
                    // If no matches, show top recipes
                    recipeAdapter.setRecipes(recipes.stream().limit(10).collect(Collectors.toList()));
                }
            }
        });
    }

    // Simple RecyclerView adapter for recipe grid
    public static class RecipeGridAdapter extends Adapter<RecipeGridAdapter.RecipeViewHolder> {
        private List<Recipe> recipes;

        public RecipeGridAdapter(List<Recipe> recipes) {
            this.recipes = recipes;
        }

        public void setRecipes(List<Recipe> recipes) {
            this.recipes = recipes;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            MaterialCardView cardView = new MaterialCardView(parent.getContext());
            cardView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dpToPx(200)
            ));
            cardView.setCardBackgroundColor(parent.getContext().getResources().getColor(android.R.color.white));
            cardView.setRadius(12);
            cardView.setContentPadding(16, 16, 16, 16);
            cardView.setCardElevation(4);
            
            TextView tv = new TextView(parent.getContext());
            tv.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            tv.setTextSize(14);
            cardView.addView(tv);
            
            return new RecipeViewHolder(cardView, tv);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
            Recipe recipe = recipes.get(position);
            holder.recipeText.setText(recipe.getName() + "\n" + recipe.getCookingTime() + " min");
        }

        @Override
        public int getItemCount() {
            return recipes.size();
        }

        public static class RecipeViewHolder extends ViewHolder {
            MaterialCardView cardView;
            TextView recipeText;

            public RecipeViewHolder(@NonNull View itemView, TextView recipeText) {
                super(itemView);
                this.cardView = (MaterialCardView) itemView;
                this.recipeText = recipeText;
            }
        }
        
        private static int dpToPx(int dp) {
            return (int) (dp * android.content.res.Resources.getSystem().getDisplayMetrics().density);
        }
    }
}
package com.pantrypal.ui.browse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.pantrypal.R;
import com.pantrypal.databinding.FragmentBrowseRecipesBinding;
import com.pantrypal.data.model.Recipe;
import com.pantrypal.data.model.User;
import com.pantrypal.ui.viewmodel.RecipeViewModel;
import com.pantrypal.ui.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BrowseRecipesFragment extends Fragment {
    private FragmentBrowseRecipesBinding binding;
    private RecipeViewModel recipeViewModel;
    private UserViewModel userViewModel;
    private RecyclerView recipesRecyclerView;
    private User currentUser;
    private boolean isDietFilterEnabled = false;
    private String selectedCategory = "All";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBrowseRecipesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        recipesRecyclerView = binding.recipesRecyclerView;

        // Load user data for dietary preference
        setupUserInfo();

        // Setup recipes with filtering
        setupRecipes();

        // Setup category and dietary filters
        setupFilters();
    }

    private void setupUserInfo() {
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                currentUser = user;

                // Show dietary filter chip if user has dietary preference
                if (user.getDietaryPreferences() != null && !user.getDietaryPreferences().isEmpty()) {
                    String dietPref = user.getDietaryPreferences();
                    binding.myDietFilterChip.setText("Recipes for my " + dietPref + " diet");
                    binding.myDietFilterChip.setVisibility(View.VISIBLE);
                } else {
                    binding.myDietFilterChip.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupRecipes() {
        recipeViewModel.getTopRecipes(50).observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null) {
                List<Recipe> filteredRecipes = filterRecipes(recipes);
                updateRecipesDisplay(filteredRecipes);
            }
        });
    }

    private List<Recipe> filterRecipes(List<Recipe> allRecipes) {
        List<Recipe> filtered = new ArrayList<>(allRecipes);

        // Filter by category
        if (!selectedCategory.equals("All")) {
            filtered = filtered.stream()
                    .filter(recipe -> recipe.getCategory() != null &&
                            recipe.getCategory().equalsIgnoreCase(selectedCategory))
                    .collect(Collectors.toList());
        }

        // Filter by dietary preference if enabled
        if (isDietFilterEnabled && currentUser != null &&
                currentUser.getDietaryPreferences() != null &&
                !currentUser.getDietaryPreferences().isEmpty()) {

            String dietPref = currentUser.getDietaryPreferences();
            filtered = filtered.stream()
                    .filter(recipe -> matchesDietaryPreference(recipe, dietPref))
                    .collect(Collectors.toList());

            // Sort matching recipes higher
            filtered = filtered.stream()
                    .sorted(Comparator.comparingInt((Recipe r) ->
                            matchesDietaryPreference(r, dietPref) ? 0 : 1))
                    .collect(Collectors.toList());
        }

        return filtered;
    }

    private boolean matchesDietaryPreference(Recipe recipe, String dietaryPreference) {
        if (recipe.getCategory() == null) return false;

        String category = recipe.getCategory().toLowerCase();
        String dietPref = dietaryPreference.toLowerCase();

        // Check if recipe category matches or contains dietary preference
        return category.contains(dietPref) ||
               category.equals(dietPref) ||
               (dietPref.equals("vegetarian") && !category.contains("meat") && !category.contains("fish")) ||
               (dietPref.equals("vegan") && !category.contains("meat") && !category.contains("dairy") &&
                !category.contains("egg") && !category.contains("fish"));
    }

    private void updateRecipesDisplay(List<Recipe> recipes) {
        // Update the RecyclerView adapter with filtered recipes
        // TODO: Create and update adapter when RecipeAdapter is implemented

        // Update matching count if diet filter is enabled
        if (isDietFilterEnabled && binding.myDietFilterChip.getVisibility() == View.VISIBLE) {
            int count = recipes.size();
            binding.matchingRecipesCount.setText(count + " matching recipe" + (count != 1 ? "s" : ""));
            binding.matchingRecipesCount.setVisibility(View.VISIBLE);
        } else {
            binding.matchingRecipesCount.setVisibility(View.GONE);
        }
    }

    private void setupFilters() {
        // Dietary preference filter chip
        binding.myDietFilterChip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDietFilterEnabled = isChecked;
            setupRecipes(); // Reload recipes with filter
        });

        // Category filter chips
        View.OnClickListener categoryClickListener = v -> {
            Chip chip = (Chip) v;
            if (chip.isChecked()) {
                selectedCategory = chip.getText().toString();
                setupRecipes(); // Reload recipes with filter
            }
        };

        binding.chipAll.setOnClickListener(categoryClickListener);
        binding.chipBreakfast.setOnClickListener(categoryClickListener);
        binding.chipLunch.setOnClickListener(categoryClickListener);
        binding.chipDinner.setOnClickListener(categoryClickListener);
        binding.chipDessert.setOnClickListener(categoryClickListener);
    }
}
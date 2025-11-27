package com.pantrypal.ui.pantry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pantrypal.databinding.FragmentPantryBinding;
import com.pantrypal.data.model.PantryItem;
import com.pantrypal.data.model.User;
// import com.pantrypal.ui.adapter.PantryItemAdapter; // TODO: Create adapter
import com.pantrypal.ui.viewmodel.PantryItemViewModel;
import com.pantrypal.ui.viewmodel.UserViewModel;
import com.pantrypal.util.SharedPreferencesManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PantryFragment extends Fragment {
    private FragmentPantryBinding binding;
    private PantryItemViewModel pantryItemViewModel;
    private UserViewModel userViewModel;
    private RecyclerView pantryRecyclerView;
    private User currentUser;
    // private PantryItemAdapter adapter; // TODO: Create adapter

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPantryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pantryItemViewModel = new ViewModelProvider(this).get(PantryItemViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        pantryRecyclerView = binding.pantryRecyclerView;

        // Setup RecyclerView with LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        pantryRecyclerView.setLayoutManager(linearLayoutManager);

        // Create adapter
        // TODO: Create PantryItemAdapter
        // adapter = new PantryItemAdapter();
        // pantryRecyclerView.setAdapter(adapter);

        // Load user data for personalization
        setupUserInfo();

        // Setup pantry items with personalization
        setupPantryItems();

        // Setup quick stats
        setupQuickStats();

        // Setup expiring items alert
        setupExpiringItemsAlert();

        // Setup FAB for adding items
        binding.fabAddItem.setOnClickListener(v -> {
            // Navigate to Add Pantry Item
        });

        // Empty state button
        binding.emptyAddButton.setOnClickListener(v -> {
            // Navigate to Add Pantry Item
        });
    }

    private void setupUserInfo() {
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                currentUser = user;
                updatePersonalizedGreeting();
                updateDietarySuggestions();
            }
        });
    }

    private void updatePersonalizedGreeting() {
        if (currentUser != null) {
            pantryItemViewModel.getAllPantryItems().observe(getViewLifecycleOwner(), items -> {
                int itemCount = (items != null) ? items.size() : 0;
                String greeting = "Hi " + currentUser.getName() + ", you have " + itemCount + " item" +
                        (itemCount != 1 ? "s" : "") + " in your pantry";
                binding.pantryGreeting.setText(greeting);
            });
        } else {
            String userName = SharedPreferencesManager.getUserName(getContext());
            if (userName != null && !userName.isEmpty()) {
                binding.pantryGreeting.setText("Hi " + userName + ", you have 0 items in your pantry");
            }
        }
    }

    private void updateDietarySuggestions() {
        if (currentUser != null && currentUser.getDietaryPreferences() != null
                && !currentUser.getDietaryPreferences().isEmpty()) {
            String dietPref = currentUser.getDietaryPreferences();
            String suggestions = getSuggestedItemsForDiet(dietPref);

            if (!suggestions.isEmpty()) {
                binding.dietarySuggestion.setText("Suggested items for your " + dietPref + " diet: " + suggestions);
                binding.dietarySuggestion.setVisibility(View.VISIBLE);
            }
        } else {
            binding.dietarySuggestion.setVisibility(View.GONE);
        }
    }

    private String getSuggestedItemsForDiet(String dietaryPreference) {
        // Map dietary preferences to common suggested items
        Map<String, String> dietSuggestions = new HashMap<>();
        dietSuggestions.put("Vegetarian", "Tofu, Legumes, Vegetables, Dairy");
        dietSuggestions.put("Vegan", "Tofu, Tempeh, Nuts, Vegetables, Plant Milk");
        dietSuggestions.put("Gluten-Free", "Rice, Quinoa, Gluten-Free Bread, Fresh Produce");
        dietSuggestions.put("Keto", "Avocados, Eggs, Cheese, Leafy Greens, Nuts");
        dietSuggestions.put("Paleo", "Meat, Fish, Vegetables, Fruits, Nuts, Seeds");
        dietSuggestions.put("Halal", "Halal Meat, Fresh Vegetables, Rice, Legumes");
        dietSuggestions.put("Kosher", "Kosher Meat, Dairy, Vegetables, Grains");

        return dietSuggestions.getOrDefault(dietaryPreference, "");
    }

    private void setupQuickStats() {
        // Total items count
        pantryItemViewModel.getAllPantryItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                binding.totalItemsCount.setText(String.valueOf(items.size()));

                // Count categories
                long categoriesCount = items.stream()
                        .map(PantryItem::getCategory)
                        .distinct()
                        .count();
            }
        });

        // Expiring items count
        pantryItemViewModel.getExpiringItems().observe(getViewLifecycleOwner(), expiringItems -> {
            if (expiringItems != null) {
                binding.expiringItemsCount.setText(String.valueOf(expiringItems.size()));
            }
        });
    }

    private void setupExpiringItemsAlert() {
        pantryItemViewModel.getExpiringItems().observe(getViewLifecycleOwner(), expiringItems -> {
            if (expiringItems != null && !expiringItems.isEmpty()) {
                // Show expiring items card
                binding.expiringItemsCard.setVisibility(View.VISIBLE);

                // Create personalized message
                String userName = (currentUser != null) ? currentUser.getName() : "User";
                binding.expiringItemsMessage.setText(userName + ", these items are expiring soon:");

                // List expiring items
                String itemsList;
                if (expiringItems.size() <= 3) {
                    itemsList = expiringItems.stream()
                            .map(PantryItem::getIngredientName)
                            .collect(Collectors.joining(", "));
                } else {
                    itemsList = expiringItems.stream()
                            .limit(3)
                            .map(PantryItem::getIngredientName)
                            .collect(Collectors.joining(", "))
                            + " and " + (expiringItems.size() - 3) + " more";
                }
                binding.expiringItemsListText.setText(itemsList);
            } else {
                binding.expiringItemsCard.setVisibility(View.GONE);
            }
        });
    }

    private void setupPantryItems() {
        pantryItemViewModel.getAllPantryItems().observe(getViewLifecycleOwner(), items -> {
            if (items == null || items.isEmpty()) {
                binding.emptyState.setVisibility(View.VISIBLE);
                pantryRecyclerView.setVisibility(View.GONE);
            } else {
                binding.emptyState.setVisibility(View.GONE);
                pantryRecyclerView.setVisibility(View.VISIBLE);
                // TODO: Update adapter with items when PantryItemAdapter is created
                // adapter.submitList(items);
            }
        });
    }
}
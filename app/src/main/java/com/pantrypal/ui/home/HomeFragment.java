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
import com.pantrypal.ui.viewmodel.RecipeViewModel;
import com.pantrypal.ui.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private RecipeViewModel recipeViewModel;
    private UserViewModel userViewModel;
    private RecipeGridAdapter recipeAdapter;

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

        // Setup user name
        setupUserName();
        
        // Setup greeting based on time
        setupGreeting();

        // Setup stats cards
        setupStatsCards();

        // Setup recipe suggestions
        setupRecipeSuggestions();

        // Setup FAB
        binding.fabAddPantry.setOnClickListener(v -> {
            // Navigate to Add Pantry Item
        });
    }
    
    private void setupUserName() {
        // Display user name from database
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.userName.setText(user.getName());
            }
        });
    }

    private void setupGreeting() {
        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        String greeting;
        if (hour < 12) {
            greeting = "Good Morning!";
        } else if (hour < 18) {
            greeting = "Good Afternoon!";
        } else {
            greeting = "Good Evening!";
        }
        binding.greetingText.setText(greeting + " 👋");
    }

    private void setupStatsCards() {
        // TODO: Setup stats from LiveData
    }

    private void setupRecipeSuggestions() {
        // Initialize adapter
        recipeAdapter = new RecipeGridAdapter(new ArrayList<>());
        binding.popularRecipesRecycler.setAdapter(recipeAdapter);
        
        // Load top recipes
        recipeViewModel.getTopRecipes(10).observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null && !recipes.isEmpty()) {
                recipeAdapter.setRecipes(recipes);
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
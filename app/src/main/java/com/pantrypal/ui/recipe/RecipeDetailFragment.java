package com.pantrypal.ui.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pantrypal.databinding.FragmentRecipeDetailBinding;
import com.pantrypal.ui.viewmodel.RecipeViewModel;

public class RecipeDetailFragment extends Fragment {
    private FragmentRecipeDetailBinding binding;
    private RecipeViewModel recipeViewModel;
    private int recipeId;
    private boolean isFavorite;

    public RecipeDetailFragment() {
    }

    public static RecipeDetailFragment newInstance(int recipeId) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putInt("recipe_id", recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeId = getArguments().getInt("recipe_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        setupRecipeDetails();
        setupTabs();
        setupListeners();
    }

    private void setupRecipeDetails() {
        recipeViewModel.getRecipeById(recipeId).observe(getViewLifecycleOwner(), recipe -> {
            if (recipe != null) {
                binding.recipeName.setText(recipe.getName());
                binding.cookTime.setText(recipe.getCookingTime() + " min");
                binding.difficulty.setText(recipe.getDifficulty());
                binding.calories.setText(recipe.getCalories() + " kcal");
                isFavorite = recipe.isFavorite();
                updateFavoriteIcon();
            }
        });
    }

    private void setupTabs() {
        TabLayout tabLayout = binding.tabLayout;
        ViewPager2 viewPager = binding.viewPager;

        RecipeDetailPagerAdapter adapter = new RecipeDetailPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Ingredients");
                    break;
                case 1:
                    tab.setText("Instructions");
                    break;
                case 2:
                    tab.setText("Nutrition");
                    break;
            }
        }).attach();
    }

    private void setupListeners() {
        binding.favoriteIcon.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            if (isFavorite) {
                // TODO: Implement addToFavorites in RecipeViewModel
                // recipeViewModel.addToFavorites(recipeId);
                Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Implement removeFromFavorites in RecipeViewModel
                // recipeViewModel.removeFromFavorites(recipeId);
                Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
            }
            updateFavoriteIcon();
        });

        binding.shareButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Share feature coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateFavoriteIcon() {
        binding.favoriteIcon.setIcon(
                getContext().getDrawable(
                        isFavorite ? android.R.drawable.ic_dialog_info : android.R.drawable.ic_menu_delete
                )
        );
    }

    private static class RecipeDetailPagerAdapter extends FragmentStateAdapter {
        public RecipeDetailPagerAdapter(Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 1:
                    return new RecipeInstructionsFragment();
                case 2:
                    return new RecipeNutritionFragment();
                case 0:
                default:
                    return new RecipeIngredientsFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}
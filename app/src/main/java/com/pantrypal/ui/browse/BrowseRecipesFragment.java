package com.pantrypal.ui.browse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.pantrypal.databinding.FragmentBrowseRecipesBinding;
import com.pantrypal.ui.viewmodel.RecipeViewModel;

public class BrowseRecipesFragment extends Fragment {
    private FragmentBrowseRecipesBinding binding;
    private RecipeViewModel recipeViewModel;
    private RecyclerView recipesRecyclerView;

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
        recipesRecyclerView = binding.recipesRecyclerView;

        setupRecipes();
        setupFilters();
    }

    private void setupRecipes() {
        recipeViewModel.getTopRecipes(20).observe(getViewLifecycleOwner(), recipes -> {
            // Update recycler view with recipes
        });
    }

    private void setupFilters() {
        // Setup category filters
        binding.filterChips.setOnCheckedStateChangeListener((group, checkedIds) -> {
            // Handle filter changes
        });
    }
}
package com.pantrypal.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pantrypal.databinding.FragmentFavoritesBinding;
import com.pantrypal.ui.adapter.RecipeGridAdapter;
import com.pantrypal.ui.viewmodel.RecipeViewModel;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;
    private RecipeViewModel recipeViewModel;
    private RecyclerView favoritesRecyclerView;
    private RecipeGridAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        favoritesRecyclerView = binding.favoritesRecyclerView;
        
        // Setup RecyclerView with GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        favoritesRecyclerView.setLayoutManager(gridLayoutManager);
        
        // Create adapter
        adapter = new RecipeGridAdapter();
        favoritesRecyclerView.setAdapter(adapter);

        setupFavorites();
    }

    private void setupFavorites() {
        recipeViewModel.getFavoriteRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes == null || recipes.isEmpty()) {
                binding.emptyState.setVisibility(View.VISIBLE);
                favoritesRecyclerView.setVisibility(View.GONE);
            } else {
                binding.emptyState.setVisibility(View.GONE);
                favoritesRecyclerView.setVisibility(View.VISIBLE);
                // Update adapter with recipes
                adapter.submitList(recipes);
            }
        });
    }
}
package com.pantrypal.ui.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pantrypal.databinding.FragmentRecipeIngredientsBinding;

public class RecipeIngredientsFragment extends Fragment {
    private FragmentRecipeIngredientsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeIngredientsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
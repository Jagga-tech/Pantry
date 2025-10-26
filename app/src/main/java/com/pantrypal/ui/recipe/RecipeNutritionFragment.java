package com.pantrypal.ui.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pantrypal.databinding.FragmentRecipeNutritionBinding;

public class RecipeNutritionFragment extends Fragment {
    private FragmentRecipeNutritionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeNutritionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
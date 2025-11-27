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
// import com.pantrypal.ui.adapter.PantryItemAdapter; // TODO: Create adapter
import com.pantrypal.ui.viewmodel.PantryItemViewModel;
import com.pantrypal.util.SharedPreferencesManager;

public class PantryFragment extends Fragment {
    private FragmentPantryBinding binding;
    private PantryItemViewModel pantryItemViewModel;
    private RecyclerView pantryRecyclerView;
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
        pantryRecyclerView = binding.pantryRecyclerView;
        
        // Setup RecyclerView with LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        pantryRecyclerView.setLayoutManager(linearLayoutManager);

        // Create adapter
        // TODO: Create PantryItemAdapter
        // adapter = new PantryItemAdapter();
        // pantryRecyclerView.setAdapter(adapter);

        setupPantryItems();

        // Setup FAB for adding items
        binding.fabAddItem.setOnClickListener(v -> {
            // Navigate to Add Pantry Item
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
package com.pantrypal.ui.pantry;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pantrypal.data.model.PantryItem;
import com.pantrypal.data.repository.FirebasePantryRepository;
import com.pantrypal.databinding.FragmentAddPantryItemBinding;
import com.pantrypal.ui.viewmodel.PantryItemViewModel;
import com.pantrypal.util.SharedPreferencesManager;

import java.util.Calendar;
import java.util.Date;

public class AddPantryItemFragment extends Fragment {
    private FragmentAddPantryItemBinding binding;
    private PantryItemViewModel pantryItemViewModel;
    private String itemId = null; // null for new item, otherwise it's edit mode
    private Calendar selectedCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPantryItemBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pantryItemViewModel = new ViewModelProvider(this).get(PantryItemViewModel.class);

        if (getArguments() != null) {
            itemId = getArguments().getString("item_id");
            if (itemId != null && !itemId.isEmpty()) {
                loadItemData();
                binding.buttonAdd.setText("Save Changes");
            }
        }

        setupListeners();
    }

    private void loadItemData() {
        pantryItemViewModel.getPantryItemById(itemId, new FirebasePantryRepository.RepositoryCallback<PantryItem>() {
            @Override
            public void onSuccess(PantryItem item) {
                if (item != null) {
                    binding.ingredientInput.setText(item.getIngredientName());
                    binding.quantityInput.setText(item.getQuantity());
                    binding.notesInput.setText(item.getNotes());
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getContext(), "Error loading item: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        binding.expirationDateInput.setOnClickListener(v -> showDatePicker());

        binding.buttonAdd.setOnClickListener(v -> handleSave());

        binding.buttonCancel.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedCalendar.set(year, monthOfYear, dayOfMonth);
                    binding.expirationDateInput.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                },
                selectedCalendar.get(Calendar.YEAR),
                selectedCalendar.get(Calendar.MONTH),
                selectedCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void handleSave() {
        String ingredientName = binding.ingredientInput.getText().toString().trim();
        String quantity = binding.quantityInput.getText().toString().trim();
        String notes = binding.notesInput.getText().toString().trim();

        if (ingredientName.isEmpty()) {
            Toast.makeText(getContext(), "Please enter ingredient name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert to Firebase user ID (currently using mock data, will use Firebase Auth later)
        String userId = String.valueOf(SharedPreferencesManager.getUserId(getContext()));

        PantryItem item = new PantryItem(
                itemId,  // null for new item
                userId,
                ingredientName,
                "General",
                quantity,
                "units",
                selectedCalendar.getTime(),
                notes,
                ""  // barcode (empty for now)
        );

        if (itemId == null || itemId.isEmpty()) {
            // Add new item
            pantryItemViewModel.addPantryItem(item, new FirebasePantryRepository.RepositoryCallback<String>() {
                @Override
                public void onSuccess(String newItemId) {
                    Toast.makeText(getContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(getContext(), "Error adding item: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Update existing item
            item.setId(itemId);
            pantryItemViewModel.updatePantryItem(item, new FirebasePantryRepository.RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    Toast.makeText(getContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(getContext(), "Error updating item: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
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
import com.pantrypal.databinding.FragmentAddPantryItemBinding;
import com.pantrypal.ui.viewmodel.PantryItemViewModel;
import com.pantrypal.util.SharedPreferencesManager;

import java.util.Calendar;

public class AddPantryItemFragment extends Fragment {
    private FragmentAddPantryItemBinding binding;
    private PantryItemViewModel pantryItemViewModel;
    private int itemId = -1; // -1 for new item, otherwise it's edit mode
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
            itemId = getArguments().getInt("item_id", -1);
            if (itemId != -1) {
                loadItemData();
                binding.buttonAdd.setText("Save Changes");
            }
        }

        setupListeners();
    }

    private void loadItemData() {
        pantryItemViewModel.getItemById(itemId).observe(getViewLifecycleOwner(), item -> {
            if (item != null) {
                binding.ingredientInput.setText(item.getIngredientName());
                binding.quantityInput.setText(item.getQuantity());
                binding.notesInput.setText(item.getNotes());
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

        int userId = SharedPreferencesManager.getUserId(getContext());
        PantryItem item = new PantryItem(
                ingredientName,
                "General",
                quantity,
                "units",
                selectedCalendar.getTimeInMillis(),
                notes,
                System.currentTimeMillis(),
                userId
        );

        if (itemId == -1) {
            pantryItemViewModel.insert(item);
            Toast.makeText(getContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
        } else {
            item.setId(itemId);
            pantryItemViewModel.update(item);
            Toast.makeText(getContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
        }

        requireActivity().onBackPressed();
    }
}
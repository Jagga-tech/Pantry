package com.pantrypal.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pantrypal.R;
import com.pantrypal.data.firebase.FirebaseAuthManager;
import com.pantrypal.data.model.User;
import com.pantrypal.databinding.FragmentProfileBinding;
import com.pantrypal.ui.auth.LoginActivity;
import com.pantrypal.ui.viewmodel.UserViewModel;
import com.pantrypal.util.SharedPreferencesManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private UserViewModel userViewModel;
    private FirebaseAuthManager authManager;
    private User currentUser;
    private boolean isEditMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authManager = new FirebaseAuthManager();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        setupDietaryPreferenceSpinner();
        loadUserData();
        setupSettings();
        setupEditProfile();
        setupNutritionTracking();
    }

    private void setupDietaryPreferenceSpinner() {
        // Setup spinner with dietary preferences
        String[] dietaryOptions = {"None", "Vegetarian", "Vegan", "Gluten-Free", "Keto", "Paleo", "Halal", "Kosher"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                dietaryOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.dietaryPreferenceSpinner.setAdapter(adapter);
    }

    private void loadUserData() {
        // Get current Firebase user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            // Observe current user from Firestore
            userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    currentUser = user;

                    // Set user name
                    binding.userName.setText(user.getName() != null && !user.getName().isEmpty()
                            ? user.getName()
                            : "User");

                    // Set user email
                    binding.userEmail.setText(user.getEmail() != null && !user.getEmail().isEmpty()
                            ? user.getEmail()
                            : firebaseUser.getEmail());

                    // Set dietary preference badge
                    if (user.getDietaryPreferences() != null && !user.getDietaryPreferences().isEmpty()) {
                        binding.dietaryPreference.setText(user.getDietaryPreferences());
                        binding.dietaryPreference.setVisibility(View.VISIBLE);
                    } else {
                        binding.dietaryPreference.setVisibility(View.GONE);
                    }

                    // Set member since date
                    if (user.getCreatedAt() != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
                        String memberSince = "Member since " + sdf.format(user.getCreatedAt());
                        binding.memberSince.setText(memberSince);
                    }

                    // Also save to SharedPreferences for quick access
                    SharedPreferencesManager.setUserName(getContext(), user.getName());
                    SharedPreferencesManager.setUserEmail(getContext(), user.getEmail());
                } else {
                    // Fallback to Firebase user data if Firestore user is not found
                    String displayName = firebaseUser.getDisplayName();
                    binding.userName.setText(displayName != null && !displayName.isEmpty()
                            ? displayName
                            : "User");
                    binding.userEmail.setText(firebaseUser.getEmail());
                }
            });
        } else {
            // If no Firebase user, fallback to SharedPreferences
            String userName = SharedPreferencesManager.getUserName(getContext());
            String userEmail = SharedPreferencesManager.getUserEmail(getContext());
            binding.userName.setText(userName != null && !userName.isEmpty() ? userName : "User");
            binding.userEmail.setText(userEmail != null && !userEmail.isEmpty() ? userEmail : "");
        }
    }

    private void setupEditProfile() {
        // Edit Profile button
        binding.editProfileButton.setOnClickListener(v -> {
            showEditMode(true);
            if (currentUser != null) {
                // Pre-fill edit fields with current data
                binding.nameEditText.setText(currentUser.getName());

                // Set dietary preference spinner
                String dietPref = currentUser.getDietaryPreferences();
                if (dietPref != null && !dietPref.isEmpty()) {
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) binding.dietaryPreferenceSpinner.getAdapter();
                    int position = adapter.getPosition(dietPref);
                    if (position >= 0) {
                        binding.dietaryPreferenceSpinner.setSelection(position);
                    }
                }
            }
        });

        // Cancel edit button
        binding.cancelEditButton.setOnClickListener(v -> showEditMode(false));

        // Save changes button
        binding.saveChangesButton.setOnClickListener(v -> saveProfileChanges());
    }

    private void showEditMode(boolean show) {
        isEditMode = show;
        binding.editProfileCard.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void saveProfileChanges() {
        if (currentUser == null) {
            Toast.makeText(getContext(), "Unable to update profile", Toast.LENGTH_SHORT).show();
            return;
        }

        String newName = binding.nameEditText.getText().toString().trim();
        String newDietaryPref = binding.dietaryPreferenceSpinner.getSelectedItem().toString();

        if (newName.isEmpty()) {
            binding.nameInputLayout.setError("Name cannot be empty");
            return;
        }

        // Update user object
        currentUser.setName(newName);
        if (!newDietaryPref.equals("None")) {
            currentUser.setDietaryPreferences(newDietaryPref);
        } else {
            currentUser.setDietaryPreferences("");
        }

        // Save to Firestore
        userViewModel.updateUser(currentUser, new com.pantrypal.data.repository.FirebaseUserRepository.RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                showEditMode(false);
                loadUserData();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getContext(), "Failed to update profile: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSettings() {
        // Change Password button
        binding.changePasswordButton.setOnClickListener(v -> showChangePasswordDialog());

        // Sign Out button
        binding.signOutButton.setOnClickListener(v -> showSignOutDialog());

        // Email notifications switch
        binding.emailNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle email notifications preference
            Toast.makeText(getContext(), "Email notifications " + (isChecked ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
        });

        // Push notifications switch
        binding.pushNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle push notifications preference
            Toast.makeText(getContext(), "Push notifications " + (isChecked ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
        });
    }

    private void showChangePasswordDialog() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(getContext(), "Not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create dialog to get email for password reset
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Password");
        builder.setMessage("We'll send a password reset link to your email: " + firebaseUser.getEmail());

        builder.setPositiveButton("Send Email", (dialog, which) -> {
            authManager.sendPasswordResetEmail(firebaseUser.getEmail(), new FirebaseAuthManager.AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    Toast.makeText(getContext(), "Password reset email sent!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(getContext(), "Failed to send email: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showSignOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sign Out");
        builder.setMessage("Are you sure you want to sign out?");

        builder.setPositiveButton("Sign Out", (dialog, which) -> {
            // Sign out from Firebase
            authManager.signOut();

            // Clear SharedPreferences
            SharedPreferencesManager.clearUserData(getContext());

            // Navigate to LoginActivity
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            Toast.makeText(getContext(), "Signed out successfully", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void setupNutritionTracking() {
        // Display nutrition data
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                updateNutritionDisplay(user);
            }
        });

        // Set nutrition goals button
        binding.setNutritionGoalsButton.setOnClickListener(v -> showNutritionGoalsDialog());
    }

    private void updateNutritionDisplay(User user) {
        // Check if need to reset daily nutrition
        checkAndResetDailyNutrition(user);

        // Update summary
        binding.nutritionSummary.setText(user.getCurrentCalories() + "/" +
                user.getDailyCalorieGoal() + " calories");

        // Update calories
        binding.caloriesText.setText(user.getCurrentCalories() + "/" +
                user.getDailyCalorieGoal());
        binding.caloriesProgress.setProgress(user.getCalorieProgress());

        // Update protein
        int proteinProgress = calculateProgress(user.getCurrentProtein(), user.getDailyProteinGoal());
        binding.proteinText.setText(user.getCurrentProtein() + "/" +
                user.getDailyProteinGoal() + "g");
        binding.proteinProgress.setProgress(proteinProgress);

        // Update carbs
        binding.carbsText.setText(user.getCurrentCarbs() + "/" +
                user.getDailyCarbsGoal() + "g");

        // Update fat
        binding.fatText.setText(user.getCurrentFat() + "/" +
                user.getDailyFatGoal() + "g");
    }

    private void checkAndResetDailyNutrition(User user) {
        if (user.getLastNutritionReset() != null) {
            java.util.Calendar lastReset = java.util.Calendar.getInstance();
            lastReset.setTime(user.getLastNutritionReset());

            java.util.Calendar today = java.util.Calendar.getInstance();

            // Check if last reset was before today
            if (lastReset.get(java.util.Calendar.DAY_OF_YEAR) !=
                    today.get(java.util.Calendar.DAY_OF_YEAR) ||
                    lastReset.get(java.util.Calendar.YEAR) !=
                            today.get(java.util.Calendar.YEAR)) {

                // Reset nutrition for new day
                user.resetDailyNutrition();
                userViewModel.updateUser(user, new com.pantrypal.data.repository.FirebaseUserRepository.RepositoryCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        // Nutrition reset successful
                    }

                    @Override
                    public void onFailure(String error) {
                        // Handle error silently
                    }
                });
            }
        }
    }

    private int calculateProgress(int current, int goal) {
        if (goal == 0) return 0;
        return Math.min(100, (current * 100) / goal);
    }

    private void showNutritionGoalsDialog() {
        if (currentUser == null) {
            Toast.makeText(getContext(), "User data not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Set Nutrition Goals");

        // Create input layout
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        // Calorie goal input
        EditText calorieInput = new EditText(getContext());
        calorieInput.setHint("Daily Calorie Goal");
        calorieInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        calorieInput.setText(String.valueOf(currentUser.getDailyCalorieGoal()));
        layout.addView(calorieInput);

        // Protein goal input
        EditText proteinInput = new EditText(getContext());
        proteinInput.setHint("Daily Protein Goal (g)");
        proteinInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        proteinInput.setText(String.valueOf(currentUser.getDailyProteinGoal()));
        layout.addView(proteinInput);

        // Carbs goal input
        EditText carbsInput = new EditText(getContext());
        carbsInput.setHint("Daily Carbs Goal (g)");
        carbsInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        carbsInput.setText(String.valueOf(currentUser.getDailyCarbsGoal()));
        layout.addView(carbsInput);

        // Fat goal input
        EditText fatInput = new EditText(getContext());
        fatInput.setHint("Daily Fat Goal (g)");
        fatInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        fatInput.setText(String.valueOf(currentUser.getDailyFatGoal()));
        layout.addView(fatInput);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            try {
                int calories = Integer.parseInt(calorieInput.getText().toString());
                int protein = Integer.parseInt(proteinInput.getText().toString());
                int carbs = Integer.parseInt(carbsInput.getText().toString());
                int fat = Integer.parseInt(fatInput.getText().toString());

                currentUser.setDailyCalorieGoal(calories);
                currentUser.setDailyProteinGoal(protein);
                currentUser.setDailyCarbsGoal(carbs);
                currentUser.setDailyFatGoal(fat);

                userViewModel.updateUser(currentUser, new com.pantrypal.data.repository.FirebaseUserRepository.RepositoryCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(getContext(), "Nutrition goals updated!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(getContext(), "Failed to update goals: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
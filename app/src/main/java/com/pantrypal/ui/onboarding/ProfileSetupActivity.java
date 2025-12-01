package com.pantrypal.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pantrypal.R;
import com.pantrypal.data.model.User;
import com.pantrypal.data.repository.HybridUserRepository;
import com.pantrypal.ui.home.HomeActivity;
import com.pantrypal.util.SharedPreferencesManager;

public class ProfileSetupActivity extends AppCompatActivity {

    private ImageView profileImage;
    private MaterialButton uploadPhotoButton;
    private TextInputEditText caloriesInput, proteinInput, carbsInput, fatInput;
    private MaterialButton continueButton, skipButton;

    private HybridUserRepository userRepository;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        // Get current user ID
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            // No user logged in, go back to login
            navigateToLogin();
            return;
        }
        userId = firebaseUser.getUid();

        userRepository = new HybridUserRepository(getApplication());

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        profileImage = findViewById(R.id.profileImage);
        uploadPhotoButton = findViewById(R.id.uploadPhotoButton);
        caloriesInput = findViewById(R.id.caloriesInput);
        proteinInput = findViewById(R.id.proteinInput);
        carbsInput = findViewById(R.id.carbsInput);
        fatInput = findViewById(R.id.fatInput);
        continueButton = findViewById(R.id.continueButton);
        skipButton = findViewById(R.id.skipButton);
    }

    private void setupListeners() {
        uploadPhotoButton.setOnClickListener(v -> {
            Toast.makeText(this, "Photo upload coming soon!", Toast.LENGTH_SHORT).show();
        });

        continueButton.setOnClickListener(v -> saveProfileAndContinue());

        skipButton.setOnClickListener(v -> navigateToHome());
    }

    private void saveProfileAndContinue() {
        // Validate inputs
        String caloriesStr = caloriesInput.getText() != null ? caloriesInput.getText().toString() : "2000";
        String proteinStr = proteinInput.getText() != null ? proteinInput.getText().toString() : "50";
        String carbsStr = carbsInput.getText() != null ? carbsInput.getText().toString() : "250";
        String fatStr = fatInput.getText() != null ? fatInput.getText().toString() : "70";

        int calories = 2000, protein = 50, carbs = 250, fat = 70;
        try {
            calories = Integer.parseInt(caloriesStr);
            protein = Integer.parseInt(proteinStr);
            carbs = Integer.parseInt(carbsStr);
            fat = Integer.parseInt(fatStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading
        continueButton.setEnabled(false);
        continueButton.setText("Saving...");

        // Get existing user data and update goals
        int finalCalories = calories;
        int finalProtein = protein;
        int finalCarbs = carbs;
        int finalFat = fat;

        userRepository.getUserById(userId).observe(this, user -> {
            if (user != null) {
                // Update nutrition goals
                user.setDailyCalorieGoal(finalCalories);
                user.setDailyProteinGoal(finalProtein);
                user.setDailyCarbsGoal(finalCarbs);
                user.setDailyFatGoal(finalFat);

                // Save to both Room and Firebase
                userRepository.saveUser(user, new HybridUserRepository.RepositoryCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        runOnUiThread(() -> {
                            Toast.makeText(ProfileSetupActivity.this, "Profile saved!", Toast.LENGTH_SHORT).show();
                            navigateToHome();
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        runOnUiThread(() -> {
                            continueButton.setEnabled(true);
                            continueButton.setText("Continue to App");
                            Toast.makeText(ProfileSetupActivity.this, "Error saving profile: " + error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            } else {
                // User data not found, create new user
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    User newUser = User.fromFirebaseUser(firebaseUser);
                    newUser.setDailyCalorieGoal(finalCalories);
                    newUser.setDailyProteinGoal(finalProtein);
                    newUser.setDailyCarbsGoal(finalCarbs);
                    newUser.setDailyFatGoal(finalFat);

                    userRepository.saveUser(newUser, new HybridUserRepository.RepositoryCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            runOnUiThread(() -> {
                                Toast.makeText(ProfileSetupActivity.this, "Profile created!", Toast.LENGTH_SHORT).show();
                                navigateToHome();
                            });
                        }

                        @Override
                        public void onFailure(String error) {
                            runOnUiThread(() -> {
                                continueButton.setEnabled(true);
                                continueButton.setText("Continue to App");
                                Toast.makeText(ProfileSetupActivity.this, "Error creating profile: " + error, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                }
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(ProfileSetupActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        SharedPreferencesManager.logout(this);
        Intent intent = new Intent(ProfileSetupActivity.this, com.pantrypal.ui.auth.LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

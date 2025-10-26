package com.pantrypal.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pantrypal.R;
import com.pantrypal.databinding.ActivitySignupBinding;
import com.pantrypal.ui.home.HomeActivity;
import com.pantrypal.util.SharedPreferencesManager;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private TextInputLayout nameLayout, emailLayout, passwordLayout, confirmPasswordLayout;
    private TextInputEditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    private ChipGroup dietaryChipGroup;
    private MaterialCheckBox termsCheckbox;
    private MaterialButton signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        nameLayout = binding.nameLayout;
        emailLayout = binding.emailLayout;
        passwordLayout = binding.passwordLayout;
        confirmPasswordLayout = binding.confirmPasswordLayout;
        
        nameInput = binding.nameInput;
        emailInput = binding.emailInput;
        passwordInput = binding.passwordInput;
        confirmPasswordInput = binding.confirmPasswordInput;
        
        dietaryChipGroup = binding.dietaryChipGroup;
        termsCheckbox = binding.termsCheckbox;
        signupButton = binding.signupButton;
    }

    private void setupListeners() {
        signupButton.setOnClickListener(v -> handleSignUp());

        binding.loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void handleSignUp() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (!validateInputs(name, email, password, confirmPassword)) {
            return;
        }

        if (!termsCheckbox.isChecked()) {
            Toast.makeText(this, "Please agree to Terms & Conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simulate signup (in real app, save to database)
        SharedPreferencesManager.setLoggedIn(this, true);
        SharedPreferencesManager.setUserId(this, 1); // Simulated user ID
        SharedPreferencesManager.setUserEmail(this, email);
        SharedPreferencesManager.setUserName(this, name);

        Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show();

        // Navigate to Home
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateInputs(String name, String email, String password, String confirmPassword) {
        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            nameLayout.setError("Name is required");
            isValid = false;
        } else {
            nameLayout.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Enter a valid email");
            isValid = false;
        } else {
            emailLayout.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            passwordLayout.setError(null);
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Passwords do not match");
            isValid = false;
        } else {
            confirmPasswordLayout.setError(null);
        }

        return isValid;
    }
}
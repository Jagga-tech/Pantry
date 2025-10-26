package com.pantrypal.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pantrypal.R;
import com.pantrypal.databinding.ActivityLoginBinding;
import com.pantrypal.ui.home.HomeActivity;
import com.pantrypal.util.SharedPreferencesManager;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText emailInput, passwordInput;
    private MaterialButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        emailLayout = binding.emailLayout;
        passwordLayout = binding.passwordLayout;
        emailInput = binding.emailInput;
        passwordInput = binding.passwordInput;
        loginButton = binding.loginButton;
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> handleLogin());

        binding.signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        binding.forgotPasswordLink.setOnClickListener(v -> {
            Toast.makeText(this, "Forgot password feature coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Allow login without validation
        // Save login state
        SharedPreferencesManager.setLoggedIn(this, true);
        SharedPreferencesManager.setUserId(this, 1); // Simulated user ID
        SharedPreferencesManager.setUserEmail(this, email.isEmpty() ? "user@example.com" : email);

        // Navigate to Home
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateInputs(String email, String password) {
        boolean isValid = true;

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

        return isValid;
    }

    private boolean isValidCredentials(String email, String password) {
        // Temporary validation - in real app this would query the database
        return !email.isEmpty() && !password.isEmpty();
    }
}
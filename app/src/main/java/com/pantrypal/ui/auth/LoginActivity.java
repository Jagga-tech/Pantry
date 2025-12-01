package com.pantrypal.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.pantrypal.R;
import com.pantrypal.data.firebase.FirebaseAuthManager;
import com.pantrypal.data.model.User;
import com.pantrypal.data.repository.FirebaseUserRepository;
import com.pantrypal.databinding.ActivityLoginBinding;
import com.pantrypal.ui.home.HomeActivity;
import com.pantrypal.util.SharedPreferencesManager;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText emailInput, passwordInput;
    private MaterialButton loginButton;
    private FirebaseAuthManager authManager;
    private FirebaseUserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authManager = new FirebaseAuthManager();
        userRepository = new FirebaseUserRepository();

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

        if (!validateInputs(email, password)) {
            return;
        }

        // Show loading state
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        // Sign in with Firebase
        authManager.signInWithEmail(email, password, new FirebaseAuthManager.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    // Create or update user in Firestore
                    User user = User.fromFirebaseUser(firebaseUser);
                    userRepository.createUser(user, new FirebaseUserRepository.RepositoryCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            // Save user info in SharedPreferences
                            SharedPreferencesManager.setLoggedIn(LoginActivity.this, true);
                            SharedPreferencesManager.setUserId(LoginActivity.this, firebaseUser.getUid());
                            SharedPreferencesManager.setUserEmail(LoginActivity.this, firebaseUser.getEmail());

                            // Navigate to Home
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(String error) {
                            // Even if Firestore fails, still navigate if auth succeeded
                            SharedPreferencesManager.setLoggedIn(LoginActivity.this, true);
                            SharedPreferencesManager.setUserId(LoginActivity.this, firebaseUser.getUid());
                            SharedPreferencesManager.setUserEmail(LoginActivity.this, firebaseUser.getEmail());

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                // Reset button state
                loginButton.setEnabled(true);
                loginButton.setText("Login");

                Toast.makeText(LoginActivity.this, "Login failed: " + error, Toast.LENGTH_LONG).show();
            }
        });
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
}
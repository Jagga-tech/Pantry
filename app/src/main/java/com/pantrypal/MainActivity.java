package com.pantrypal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.pantrypal.databinding.ActivityMainBinding;
import com.pantrypal.ui.auth.LoginActivity;
import com.pantrypal.ui.home.HomeActivity;
import com.pantrypal.ui.onboarding.OnboardingActivity;
import com.pantrypal.util.SharedPreferencesManager;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final int SPLASH_SCREEN_DURATION = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Show splash screen and navigate based on user state
        new Handler(Looper.getMainLooper()).postDelayed(this::navigateToNextScreen, SPLASH_SCREEN_DURATION);
    }

    private void navigateToNextScreen() {
        boolean isFirstTime = SharedPreferencesManager.isFirstTime(this);
        boolean isLoggedIn = SharedPreferencesManager.isLoggedIn(this);

        Intent intent;
        if (isFirstTime) {
            intent = new Intent(MainActivity.this, OnboardingActivity.class);
        } else if (isLoggedIn) {
            intent = new Intent(MainActivity.this, HomeActivity.class);
        } else {
            intent = new Intent(MainActivity.this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
package com.pantrypal.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.pantrypal.R;
import com.pantrypal.databinding.ActivityOnboardingBinding;
import com.pantrypal.ui.auth.LoginActivity;
import com.pantrypal.util.SharedPreferencesManager;

public class OnboardingActivity extends AppCompatActivity {
    private ActivityOnboardingBinding binding;
    private OnboardingPagerAdapter adapter;
    private ViewPager2 viewPager;
    private DotsIndicator dotsIndicator;
    private MaterialButton nextButton;
    private MaterialButton skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();
        setupViewPager();
        setupListeners();
    }

    private void initializeViews() {
        viewPager = binding.viewPager;
        dotsIndicator = binding.dotsIndicator;
        nextButton = binding.nextButton;
        skipButton = binding.skipButton;
    }

    private void setupViewPager() {
        adapter = new OnboardingPagerAdapter(this);
        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager2(viewPager);
    }

    private void setupListeners() {
        skipButton.setOnClickListener(v -> navigateToLogin());

        nextButton.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(currentItem + 1, true);
            } else {
                navigateToLogin();
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == adapter.getItemCount() - 1) {
                    nextButton.setText("Get Started");
                } else {
                    nextButton.setText("Next");
                }
            }
        });
    }

    private void navigateToLogin() {
        SharedPreferencesManager.setFirstTime(this, false);
        Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
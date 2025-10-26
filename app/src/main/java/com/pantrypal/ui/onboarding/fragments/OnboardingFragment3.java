package com.pantrypal.ui.onboarding.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pantrypal.databinding.FragmentOnboarding3Binding;

public class OnboardingFragment3 extends Fragment {
    private FragmentOnboarding3Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOnboarding3Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
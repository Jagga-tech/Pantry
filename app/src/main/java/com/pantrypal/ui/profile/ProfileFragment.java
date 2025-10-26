package com.pantrypal.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pantrypal.databinding.FragmentProfileBinding;
import com.pantrypal.ui.viewmodel.UserViewModel;
import com.pantrypal.util.SharedPreferencesManager;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Observe current user from database
        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.userName.setText(user.getName());
                binding.userEmail.setText(user.getEmail());
                
                // Also save to SharedPreferences for quick access
                SharedPreferencesManager.setUserId(getContext(), user.getId());
                SharedPreferencesManager.setUserName(getContext(), user.getName());
                SharedPreferencesManager.setUserEmail(getContext(), user.getEmail());
            } else {
                // Fallback to SharedPreferences if database user is empty
                String userName = SharedPreferencesManager.getUserName(getContext());
                String userEmail = SharedPreferencesManager.getUserEmail(getContext());
                binding.userName.setText(userName);
                binding.userEmail.setText(userEmail);
            }
        });

        // Setup settings
        setupSettings();
    }

    private void setupSettings() {
        binding.changePasswordButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Change password feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        binding.emailNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle email notifications
        });

        binding.pushNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle push notifications
        });
    }
}
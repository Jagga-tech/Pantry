package com.pantrypal.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pantrypal.data.firebase.FirebaseAuthManager;
import com.pantrypal.databinding.FragmentProfileBinding;
import com.pantrypal.ui.auth.LoginActivity;
import com.pantrypal.ui.viewmodel.UserViewModel;
import com.pantrypal.util.SharedPreferencesManager;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private UserViewModel userViewModel;
    private FirebaseAuthManager authManager;

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

        loadUserData();
        setupSettings();
    }

    private void loadUserData() {
        // Get current Firebase user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            // Observe current user from Firestore
            userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    binding.userName.setText(user.getName() != null && !user.getName().isEmpty()
                            ? user.getName()
                            : "User");
                    binding.userEmail.setText(user.getEmail() != null && !user.getEmail().isEmpty()
                            ? user.getEmail()
                            : firebaseUser.getEmail());

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
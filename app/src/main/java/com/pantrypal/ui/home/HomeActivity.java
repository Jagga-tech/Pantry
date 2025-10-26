package com.pantrypal.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pantrypal.R;
import com.pantrypal.databinding.ActivityHomeBinding;
import com.pantrypal.ui.auth.LoginActivity;
import com.pantrypal.util.SharedPreferencesManager;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigation();
    }

    private void setupNavigation() {
        BottomNavigationView navView = binding.bottomNavigation;
        
        // Create NavHostFragment programmatically
        androidx.navigation.fragment.NavHostFragment navHostFragment =
                (androidx.navigation.fragment.NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
        
        if (navHostFragment == null) {
            navHostFragment = androidx.navigation.fragment.NavHostFragment.create(R.navigation.nav_graph);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.nav_host_fragment, navHostFragment)
                    .commitNow();
        }
        
        navController = navHostFragment.getNavController();

        AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_favorites, R.id.nav_pantry, R.id.nav_profile)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SharedPreferencesManager.logout(this);
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
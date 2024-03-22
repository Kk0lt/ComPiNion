package com.example.cumpinion.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.cumpinion.R;
import com.example.cumpinion.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NavHostFragment host =(NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.FragmentContainer);
        NavController navController = host.getNavController();
        navController.navigate(R.id.loginFragment);

        bottomNavigationBar(navController);


    }

    private void bottomNavigationBar(NavController navController) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.homeFragmentItem) {
                navController.navigate(R.id.homeFragment);
            }
            else if (item.getItemId() == R.id.leaderboardFragmentItem) {
                navController.navigate(R.id.leaderboardFragment);
            }
            else if (item.getItemId() == R.id.settingsFragmentItem) {
                navController.navigate(R.id.settingsFragment);
            }
            return true;
        });
    }


}
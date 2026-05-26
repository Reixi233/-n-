package com.example.lab6_23520536_21521202.avtivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.lab6_23520536_21521202.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // Xóa sạch các Listener rườm rà.
            // CHỈ CẦN DUY NHẤT 1 DÒNG NÀY ĐỂ THANH MENU TỰ ĐỘNG CHẠY 100%
            NavigationUI.setupWithNavController(bottomNav, navController);
        }
    }
}
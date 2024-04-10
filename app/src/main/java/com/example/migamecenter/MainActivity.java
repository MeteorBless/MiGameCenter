package com.example.migamecenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private HashMap<Integer, Fragment> fragmentCache = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // 初始时显示默认 fragment
        showFragment(HomeFragment.class);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    if (item.getItemId() == R.id.nav_home) {
                        selectedFragment = new HomeFragment();
                    } else if (item.getItemId() == R.id.nav_my) {
                        selectedFragment = new MyHomePageFragment();
                    }

                    // 切换到选择的 fragment
                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.fragment_container, selectedFragment)
                                .addToBackStack(selectedFragment.getClass().getSimpleName())
                                .commit();
                        return true;
                    } else {
                        return false;
                    }
                }
            };

    private <T extends Fragment> T getOrCreateFragment(Class<T> fragmentClass) {
        T fragment = (T) fragmentCache.get(fragmentClass.hashCode());

        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
                fragmentCache.put(fragmentClass.hashCode(), fragment);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate " + fragmentClass.getSimpleName(), e);
            }
        }

        return fragment;
    }

    private void showFragment(Class<? extends Fragment> fragmentClass) {
        try {
            Fragment fragment = fragmentClass.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        } catch (Exception e) {
            Log.e("MainActivity", "Failed to instantiate fragment", e);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Fragment fragment : fragmentCache.values()) {
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
            }
        }
        fragmentCache.clear();
    }

}
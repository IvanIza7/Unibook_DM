package com.navify.unibook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.navify.unibook.fragments.HomeFragment;
import com.navify.unibook.fragments.ActivityFragment;
import com.navify.unibook.fragments.PerfilFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        loadFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (id == R.id.nav_actividades) {
                fragment = new ActivityFragment();
            } else if (id == R.id.nav_perfil) {
                fragment = new PerfilFragment();
            }

            if (fragment != null) {
                loadFragment(fragment);
            }

            return true;
        });

    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}

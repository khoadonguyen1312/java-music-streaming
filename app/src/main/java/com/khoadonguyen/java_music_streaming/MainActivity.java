package com.khoadonguyen.java_music_streaming;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khoadonguyen.java_music_streaming.presentation.fragment.FolderFragment;
import com.khoadonguyen.java_music_streaming.presentation.fragment.HomeFragment;
import com.khoadonguyen.java_music_streaming.presentation.fragment.SearchFragment;
import com.khoadonguyen.java_music_streaming.presentation.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {
    //    Button button;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
            replaceFragment(new HomeFragment());

            handleBottomNavs();
    }

    private void handleBottomNavs() {
        bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.item_1) {
                replaceFragment(new HomeFragment());
                return true;
            }
            if (item.getItemId() == R.id.item_2) {
                replaceFragment(new SearchFragment());
                return true;
            }
            if (item.getItemId() == R.id.item_3) {
                replaceFragment(new FolderFragment());
                return true;
            }
            if (item.getItemId() == R.id.item_4) {
                replaceFragment(new UserFragment());
                return true;
            }
            return false;


        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .commit();
    }
}
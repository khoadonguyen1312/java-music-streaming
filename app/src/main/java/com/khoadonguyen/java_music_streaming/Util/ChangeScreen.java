package com.khoadonguyen.java_music_streaming.Util;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khoadonguyen.java_music_streaming.R;

public class ChangeScreen {
    public static void changeScreen(FragmentActivity activity, Fragment fragment) {

        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.navigation_bar);
        bottomNavigationView.setVisibility(GONE);

        LinearLayout bottom_audio = activity.findViewById(R.id.audio_bottomsheet);

        if (bottom_audio != null) {
            bottom_audio.setVisibility(GONE);
        }
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_fragment, fragment)
                .commit();

    }

    public static void popScreen(FragmentActivity fragmentActivity) {
        fragmentActivity.getSupportFragmentManager().popBackStack();
        if (fragmentActivity.findViewById(R.id.navigation_bar).getVisibility() == GONE) {
            fragmentActivity.findViewById(R.id.navigation_bar).setVisibility(VISIBLE);
        }
    }

}

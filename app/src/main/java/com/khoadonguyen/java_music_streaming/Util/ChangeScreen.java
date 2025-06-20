package com.khoadonguyen.java_music_streaming.Util;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.AnimRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khoadonguyen.java_music_streaming.R;
import com.khoadonguyen.java_music_streaming.Service.Playlist.Playlist;
import com.khoadonguyen.java_music_streaming.Service.manager.AudioPlayerManager;

public class ChangeScreen {
    public static void changeScreen(FragmentActivity activity, Fragment fragment, @Nullable @AnimRes Integer enter, @Nullable @AnimRes Integer exit, @Nullable @AnimRes Integer popEnter, @Nullable @AnimRes Integer popExit) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.navigation_bar);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(GONE);
        }

        LinearLayout bottom_audio = activity.findViewById(R.id.audio_bottomsheet);


        boolean isMainFragment = fragment instanceof com.khoadonguyen.java_music_streaming.presentation.fragment.HomeFragment || fragment instanceof com.khoadonguyen.java_music_streaming.presentation.fragment.SearchFragment || fragment instanceof com.khoadonguyen.java_music_streaming.presentation.fragment.FolderFragment || fragment instanceof com.khoadonguyen.java_music_streaming.presentation.fragment.UserFragment;

        if (!isMainFragment && bottom_audio != null) {
            bottom_audio.setVisibility(GONE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (enter != null && exit != null && popEnter != null && popExit != null) {
            transaction.setCustomAnimations(enter, exit, popEnter, popExit);
        }
        transaction.replace(R.id.main_fragment, fragment).addToBackStack(null).commit();
    }


    public static void popScreen(FragmentActivity fragmentActivity) {
        LinearLayout bottom_audio = fragmentActivity.findViewById(R.id.audio_bottomsheet);
        fragmentActivity.getSupportFragmentManager().popBackStack();
        if (fragmentActivity.findViewById(R.id.navigation_bar).getVisibility() == GONE) {
            fragmentActivity.findViewById(R.id.navigation_bar).setVisibility(VISIBLE);
        }
        if (!AudioPlayerManager.getAudioService().getPlaylist().getValue().isEmpty()) {
            bottom_audio.setVisibility(VISIBLE);
        }
    }

}

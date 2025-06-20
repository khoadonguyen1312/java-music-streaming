package com.khoadonguyen.java_music_streaming.presentation.fragment;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.R;
import com.khoadonguyen.java_music_streaming.Service.extractor.impl.DynamicYoutubeExtractor;
import com.khoadonguyen.java_music_streaming.Util.ChangeScreen;
import com.khoadonguyen.java_music_streaming.presentation.Adapter.RecomandAdapter;
import com.khoadonguyen.java_music_streaming.presentation.core.BottomSheet;

import org.jetbrains.annotations.Async;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());
    ImageButton playlist_button;
    TextView source_textview;
    GridView recomand_girdView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        source_textview = view.findViewById(R.id.source_textview);
        recomand_girdView = view.findViewById(R.id.home_fragment_recomand_girdview);

        playlist_button = view.findViewById(R.id.home_fragment_playlist_button);
        sourceChange();
        recomanSong();

        playlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animator scale = ObjectAnimator.ofPropertyValuesHolder(v,
                        PropertyValuesHolder.ofFloat(View.SCALE_X, 1, 1.5f, 1),
                        PropertyValuesHolder.ofFloat(View.SCALE_Y, 1, 1.5f, 1)
                );
                scale.setDuration(1000);
                scale.start();
                ChangeScreen.changeScreen(getActivity(), new CurrentPlaylist(), R.anim.slide_in_up, 0, 0, R.anim.slide_in_down);

            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragement, container, false);
    }

    private void sourceChange() {
        source_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet bottomSheet = new BottomSheet();
                bottomSheet.show(getActivity().getSupportFragmentManager(), bottomSheet.getTag());
            }
        });
    }

    private void recomanSong() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Song> songs = new DynamicYoutubeExtractor().search("remix tiktok").join();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isAdded()) {
                            Context context = requireContext();
                            recomand_girdView.setAdapter(new RecomandAdapter(context, songs.subList(0, 6)));
                        } else {

                        }
                    }
                });


            }
        });

    }
}

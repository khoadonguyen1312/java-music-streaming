package com.khoadonguyen.java_music_streaming.presentation.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.slider.Slider;
import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.R;
import com.khoadonguyen.java_music_streaming.Service.Playlist.Playlist;
import com.khoadonguyen.java_music_streaming.Service.manager.AudioPlayerManager;
import com.khoadonguyen.java_music_streaming.Util.ChangeScreen;
import com.khoadonguyen.java_music_streaming.presentation.core.CurrentSongFragmentBottomSheet;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;

public class CurrentSongFragment extends Fragment {
    MaterialToolbar materialToolbar;
    ImageView thumb;
    ImageButton play_pause, playlist, skip_next, skip_back, loop, more;
    Slider slider;
    TextView current_duration;
    TextView max_duration;
    TextView title;
    TextView author;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.current_song_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setview();

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeScreen.popScreen(requireActivity());
            }
        });

        AudioPlayerManager.getAudioService().getPlaylist().observe(getViewLifecycleOwner(), new Observer<Playlist>() {
            @Override
            public void onChanged(Playlist songs) {
                Song song = songs.get(AudioPlayerManager.getAudioService().getPlaylist().getValue().gIndex());
                Glide.with(requireContext()).load(song.gHighImage()).into(thumb);
                slider.setValue(0);
                slider.setValueTo(AudioPlayerManager.getAudioService().getAudioPlayer().getDuration());
                String title_str = song.getTitle();
                String author_str = song.getAuthor();
                if (title.length() > 22) {
                    title.setText(title_str.substring(0, 22) + "...");
                }
                if (author_str.length() > 12) {
                    author.setText(title_str.substring(0, 12) + "...");

                }
                title.setText(song.getTitle());
                author.setText(song.getAuthor());
                max_duration.setText(DurationFormatUtils.formatDuration(AudioPlayerManager.getAudioService().getAudioPlayer().getDuration(), "H:mm:ss"));
            }
        });
        AudioPlayerManager.getAudioService().getPlaying().observe(getActivity(), new Observer<Boolean>() {
            int play = R.drawable.play_arrow_24dp_e3e3e3_fill0_wght400_grad0_opsz24;
            int pause = R.drawable.pause_24dp_e3e3e3_fill0_wght400_grad0_opsz24;

            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    play_pause.setImageResource(pause);
                    thumb.animate().scaleX(1).scaleY(1).setDuration(300).start();
                } else {
                    play_pause.setImageResource(play);
                    thumb.animate().scaleX(0.9F).scaleY(0.9f).setDuration(300).start();
                }
            }
        });
        materialToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.current_song_fragment_appbar_more) {
                CurrentSongFragmentBottomSheet currentSongFragmentBottomSheet = new CurrentSongFragmentBottomSheet();
                currentSongFragmentBottomSheet.show(getParentFragmentManager(), currentSongFragmentBottomSheet.getTag());
                return true;
            }
            return false;
        });
        skip_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioPlayerManager.getAudioService().next();
            }
        });

        skip_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioPlayerManager.getAudioService().back();
            }
        });
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioPlayerManager.getAudioService().togglePausePlay();
            }
        });

        AudioPlayerManager.getAudioService().getCurrent_song_pos().observe(getActivity(), new Observer<Duration>() {
            @Override
            public void onChanged(Duration duration) {
                Log.d("duration", duration.toString());

                slider.setValue(duration.toMillis());
                current_duration.setText(DurationFormatUtils.formatDuration(duration.toMillis(), "H:mm:ss"));

            }
        });
        slider.addOnChangeListener((slider1, value, fromUser) -> {
            if (fromUser) {
                AudioPlayerManager.getAudioService().seekTo(Duration.ofMillis((long) value));

            }

        });

    }


    private void setview() {
        materialToolbar = getView().findViewById(R.id.current_song_fragment_toolbar);
        thumb = getView().findViewById(R.id.current_song_fragment_thumb);
        play_pause = getView().findViewById(R.id.current_song_fragment_pause_btn);
//
        skip_next = getView().findViewById(R.id.current_song_fragment_next_btn);
        skip_back = getView().findViewById(R.id.current_song_fragment_back_btn);

        slider = getView().findViewById(R.id.current_song_fragment_slider_duration);
//        test = getView().findViewById(R.id.test);
        current_duration = getView().findViewById(R.id.current_song_fragment_current_duration);

        max_duration = getView().findViewById(R.id.current_song_fragment_max_duration);
        title = getView().findViewById(R.id.current_song_fragment_title);
        author = getView().findViewById(R.id.current_song_fragment_author);

    }


}

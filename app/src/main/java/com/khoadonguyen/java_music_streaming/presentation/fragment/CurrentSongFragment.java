package com.khoadonguyen.java_music_streaming.presentation.fragment;

import static androidx.media3.common.Player.REPEAT_MODE_ONE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import android.text.format.DateUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.slider.Slider;
import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.R;
import com.khoadonguyen.java_music_streaming.Service.Playlist.Playlist;
import com.khoadonguyen.java_music_streaming.Service.manager.AudioPlayerManager;
import com.khoadonguyen.java_music_streaming.Util.ChangeScreen;
import com.khoadonguyen.java_music_streaming.presentation.bottomSheet.CurrentSongMoreBottomSheet;
import com.khoadonguyen.java_music_streaming.presentation.core.CurrentSongFragmentBottomSheet;


import java.time.Duration;

public class CurrentSongFragment extends Fragment {
    MaterialToolbar materialToolbar;
    ImageView thumb;
    ImageButton play_pause, playlist, skip_next, skip_back, loop, more,
            back_screen;
    Slider slider;
    TextView current_duration;
    TextView max_duration;
    TextView title;
    TextView author;
    PlayerView video_background;
    private ExoPlayer exoPlayer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.current_song_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setview();
        exoPlayer = new ExoPlayer.Builder(requireContext()).build();
        exoPlayer.setRepeatMode(REPEAT_MODE_ONE);
        /**
         * set khong focus chiem dung quyen am thanh
         */
        exoPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setUsage(C.USAGE_MEDIA)
                        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                        .build(),
                false
        );
        /**
         *
         */
        exoPlayer.setVolume(0f);
        video_background.setPlayer(exoPlayer);

        handle_btn();
        handleUiListen();

    }

    private void handle_btn() {

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentSongMoreBottomSheet currentSongFragmentBottomSheet = new CurrentSongMoreBottomSheet();
                currentSongFragmentBottomSheet.show(getFragmentManager(), currentSongFragmentBottomSheet.getTag());
            }
        });
        back_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeScreen.popScreen(getActivity());
            }
        });
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioPlayerManager.getAudioService().togglePausePlay();
            }
        });

        skip_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioPlayerManager.getAudioService().next();
            }
        });
        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider s) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider s) {

                long seekPosition = (long) s.getValue();
                AudioPlayerManager.getAudioService().seekTo(Duration.ofMillis(seekPosition));
            }
        });
        back_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioPlayerManager.getAudioService().back();
            }
        });

    }

    private void handleUiListen() {
        AudioPlayerManager.getAudioService().getPlaylist().observe(getViewLifecycleOwner(), new Observer<Playlist>() {
            @Override
            public void onChanged(Playlist songs) {
                Song song = songs.get(songs.gIndex());
                String songTitle = song.getTitle();

                MediaItem previewClip = song.getShortThumbVideo();

                exoPlayer.setMediaItem(previewClip);
                exoPlayer.prepare();
                exoPlayer.play();

                /**
                 * set view
                 */

                slider.setValue(0f);


                Duration maxDuration = AudioPlayerManager.getAudioService()
                        .getMax_pos()
                        .getValue();


                float maxMs = (float) maxDuration.toMillis();
                slider.setValueTo(maxMs);

                long maxSeconds = maxDuration.getSeconds();
                max_duration.setText(DateUtils.formatElapsedTime(maxSeconds));


                title.setText(songTitle);


            }
        });
        AudioPlayerManager.getAudioService().getPlaying().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            int playing_icon = R.drawable.play_arrow_24dp_e3e3e3_fill0_wght400_grad0_opsz24;
            int pause_icon = R.drawable.pause_24dp_e3e3e3_fill0_wght400_grad0_opsz24;

            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    play_pause.setImageResource(playing_icon);
                } else {
                    play_pause.setImageResource(pause_icon);
                }
            }
        });
//        AudioPlayerManager.getAudioService().getMax_pos().observe(getViewLifecycleOwner(), new Observer<Duration>() {
//            @Override
//            public void onChanged(Duration duration) {
//                max_duration.ma(DateUtils.formatElapsedTime(duration.getSeconds()));
//            }
//        });
        AudioPlayerManager.getAudioService().getCurrent_song_pos().observe(getViewLifecycleOwner(), new Observer<Duration>() {
            @Override
            public void onChanged(Duration duration) {
                current_duration.setText(DateUtils.formatElapsedTime(duration.getSeconds()));
                slider.setValue(duration.toMillis());
            }
        });

    }

    @Override
    public void onDestroy() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;

        }
        super.onDestroy();
    }

    private void setview() {
        more = getView().findViewById(R.id.more_btn);
        video_background = getView().findViewById(R.id.video_background);
        back_screen = getView().findViewById(R.id.back_screen);
        play_pause = getView().findViewById(R.id.play_pause);
        skip_back = getView().findViewById(R.id.back_song);
        skip_next = getView().findViewById(R.id.next_song);
        loop = getView().findViewById(R.id.loop);
        title = getView().findViewById(R.id.title_two);
        slider = getView().findViewById(R.id.slider);
        current_duration = getView().findViewById(R.id.current_time);
        max_duration = getView().findViewById(R.id.max_time);

    }


}

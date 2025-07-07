package com.khoadonguyen.java_music_streaming.presentation.fragment;

import static androidx.media3.common.Player.REPEAT_MODE_ONE;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.khoadonguyen.java_music_streaming.Service.extractor.SourceExtractor;
import com.khoadonguyen.java_music_streaming.Service.manager.AudioPlayerManager;
import com.khoadonguyen.java_music_streaming.Service.realtimedb.LoveSongRespository;
import com.khoadonguyen.java_music_streaming.Util.ChangeScreen;
import com.khoadonguyen.java_music_streaming.presentation.bottomSheet.CurrentSongMoreBottomSheet;
import com.khoadonguyen.java_music_streaming.presentation.core.CurrentSongFragmentBottomSheet;


import java.time.Duration;

public class CurrentSongFragment extends Fragment {
    private static String tag = "CurrentSongFragment";
    MaterialToolbar materialToolbar;
    private Handler handler = new Handler(Looper.getMainLooper());
    ImageView thumb;
    ImageButton play_pause, playlist, skip_next, skip_back, loop, more, back_screen, like;
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
        setview(view);
        exoPlayer = new ExoPlayer.Builder(requireContext()).build();
        exoPlayer.setRepeatMode(REPEAT_MODE_ONE);
        /**
         * set khong focus chiem dung quyen am thanh
         */
        exoPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(C.USAGE_MEDIA).setContentType(C.AUDIO_CONTENT_TYPE_MOVIE).build(), false);
        /**
         *
         */
        exoPlayer.setVolume(0f);
        video_background.setPlayer(exoPlayer);

        handle_btn();
        handleUiListen();

    }

    private void handle_btn() {

//        more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CurrentSongMoreBottomSheet currentSongFragmentBottomSheet = new CurrentSongMoreBottomSheet();
//                currentSongFragmentBottomSheet.show(getFragmentManager(), currentSongFragmentBottomSheet.getTag());
//            }
//        });
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
        skip_back.setOnClickListener(new View.OnClickListener() {
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

                int source_id = SourceExtractor.getInstance().getCurrent_source_id();
                if (source_id == 0) {
                    MediaItem previewClip = song.getShortThumbVideo();

                    exoPlayer.setMediaItem(previewClip);
                    exoPlayer.prepare();
                    exoPlayer.play();
                } else {
                    Glide.with(requireContext()).load(song.gHighImage()).into(thumb);
                }
                /**
                 * set view
                 */

                slider.setValue(0f);


                Duration maxDuration = AudioPlayerManager.getAudioService().getMax_pos().getValue();


                float maxMs = (float) maxDuration.toMillis();
                slider.setValueTo(maxMs);

                long maxSeconds = maxDuration.getSeconds();
                max_duration.setText(DateUtils.formatElapsedTime(maxSeconds));


                title.setText(songTitle);
                loveSongView(song);


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

                float currentMs = duration.toMillis();
                float maxMs = slider.getValueTo();

                if (currentMs > maxMs) {

                    slider.setValue(maxMs);
                } else {
                    slider.setValue(currentMs);
                }
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

    private void loveSongView(Song song) {
        int liked_icon = R.drawable.heart_fill;
        int unlike_icon = R.drawable.hear_not_fill;

        song.checkIfFacvorite(isFavorite -> {
            Log.d(tag, "isFavorite = " + isFavorite);


            like.setImageResource(isFavorite ? liked_icon : unlike_icon);


            like.setOnClickListener(v -> {
                if (isFavorite) {
                    Log.d("CurrentSong", "Removing from favorite...");
                    song.removefacvorite(() -> loveSongView(song));
                } else {
                    Log.d("CurrentSong", "Adding to favorite...");
                    song.addfacvorite(() -> loveSongView(song));
                }
            });
        });


    }


    private void setview(View view) {
        more = view.findViewById(R.id.more_btn);

        video_background = view.findViewById(R.id.video_background);
        back_screen = view.findViewById(R.id.back_screen);
        play_pause = view.findViewById(R.id.play_pause);
        skip_back = view.findViewById(R.id.back_song);
        skip_next = view.findViewById(R.id.next_song);
        loop = view.findViewById(R.id.loop);
        title = view.findViewById(R.id.title_two);
        slider = view.findViewById(R.id.slider);
        current_duration = view.findViewById(R.id.current_time);
        max_duration = view.findViewById(R.id.max_time);
        like = view.findViewById(R.id.like);
    }

}

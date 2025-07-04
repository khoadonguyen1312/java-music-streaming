package com.khoadonguyen.java_music_streaming.Service.AudioPlayer.impl;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import androidx.annotation.OptIn;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.media3.common.MediaItem;


import androidx.media3.common.MediaMetadata;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;

import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;
import androidx.media3.session.MediaStyleNotificationHelper;
import androidx.media3.session.SessionCommand;
import androidx.media3.session.SessionResult;

import com.google.common.util.concurrent.ListenableFuture;
import com.khoadonguyen.java_music_streaming.Model.Song;

import com.khoadonguyen.java_music_streaming.Service.AudioPlayer.DynamicAudioPlayer;
import com.khoadonguyen.java_music_streaming.Service.Playlist.Playlist;
import com.khoadonguyen.java_music_streaming.Service.extractor.Extractor;
import com.khoadonguyen.java_music_streaming.Service.extractor.SourceExtractor;
import com.khoadonguyen.java_music_streaming.Service.extractor.impl.DynamicSoundCloudExtractor;
import com.khoadonguyen.java_music_streaming.Service.extractor.impl.DynamicYoutubeExtractor;
import com.khoadonguyen.java_music_streaming.storage.SharePreferencesHelper;

import org.schabi.newpipe.extractor.stream.AudioStream;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class DynamicAudioPlayerImpl extends MediaSessionService implements DynamicAudioPlayer {
    private static final String tag = "DynamicAudioPlayerImpl";

    private MutableLiveData<Playlist> playlist = new MutableLiveData<>(new Playlist());

    private MutableLiveData<Boolean> playing = new MutableLiveData<>();
    private ExoPlayer audioPlayer;
    private Extractor extractor;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private MediaSession mediaSession;

    private MutableLiveData<Duration> current_song_pos = new MutableLiveData<>(Duration.ZERO);

    private MutableLiveData<Duration> max_pos = new MutableLiveData<>(Duration.ZERO);
    private final IBinder binder = new LocalBinder();

    public ExoPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public LiveData<Playlist> getPlaylist() {
        return playlist;
    }

    public Extractor getExtractor() {
        return extractor;
    }

    public class LocalBinder extends Binder {
        public DynamicAudioPlayerImpl getService() {
            return DynamicAudioPlayerImpl.this;
        }
    }

    public MutableLiveData<Duration> getCurrent_song_pos() {
        return current_song_pos;
    }

    private final Handler handler = new Handler(Looper.getMainLooper());

    private Runnable updateCurrent_pos = new Runnable() {
        @Override
        public void run() {
            if (audioPlayer != null && playing.getValue() == true) {
                current_song_pos.postValue(Duration.ofMillis(gcurrentpos()));
                max_pos.postValue(Duration.ofMillis(gmaxpost()));

                handler.postDelayed(this, 500);
            }
        }
    };

    public MutableLiveData<Duration> getMax_pos() {
        return max_pos;
    }

    public void observePlayer() {
        audioPlayer.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                playing.setValue(isPlaying);
                if (isPlaying) {
                    handler.post(updateCurrent_pos);
                } else {
                    handler.removeCallbacks(updateCurrent_pos);
                }

            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);

                if (playbackState == Player.STATE_ENDED) {
                    next();
                }
            }


        });

    }

    private Long gcurrentpos() {

        return audioPlayer.getCurrentPosition();
    }

    private Long gmaxpost() {
        return audioPlayer.getDuration();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        super.onBind(intent);
        return binder;
    }

    public List<Song> gplaylist() {
        return this.playlist.getValue();
    }

    @Override
    public void start(Song song) {
        executorService.execute(() -> {
            Playlist current = playlist.getValue();
            if (current == null) current = new Playlist();
            else current.clear();

            Log.d(tag, "Bắt đầu tải bài hát đầu tiên...");

            Playlist finalCurrent = current;
            extractor.gsong(song.getUrl())
                    .thenAccept(firstSong -> {
                        Log.d(tag, "Đã tải xong bài đầu tiên: " + firstSong.getTitle());

                        finalCurrent.add(firstSong);
                        playlist.postValue(finalCurrent);


                        new Handler(Looper.getMainLooper()).post(() -> {
                            playSong(firstSong);
                        });


                        addSongsToplaylist(firstSong);
                    })
                    .exceptionally(e -> {
                        Log.e(tag, "Lỗi khi tải bài hát đầu tiên", e);
                        return null;
                    });
        });
    }

    private void addSongsToplaylist(Song song) {
        extractor.recomandSong(song)
                .thenAccept(songs -> {

                    Playlist playlistCache = new Playlist();

                    Playlist current = playlist.getValue();
                    if (current != null) {
                        playlistCache.addAll(current);
                    }


                    playlistCache.addAll(songs);
                    playlist.postValue(playlistCache);

                    Log.d(tag, "Đã thêm bài gợi ý. Tổng bài: " + playlistCache.size());
                })
                .exceptionally(e -> {
                    Log.e(tag, "Lỗi khi lấy bài gợi ý", e);
                    return null;
                });
    }


    @Override
    public void next() {
        Playlist current = playlist.getValue();
        if (current != null && current.listIterator().hasNext()) {
            current.next();
            playlist.setValue(current);
            playSong(null);
        } else {
            Log.d(tag, "Không có bài kế tiếp");
        }
    }

    @Override
    public void back() {
        Playlist current = playlist.getValue();
        if (current != null && current.gIndex() > 0) {
            current.back();
            playlist.setValue(current);
            playSong(null);
        } else {
            Log.d(tag, "Không có bài trước đó");
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (audioPlayer == null) {
            audioPlayer = new ExoPlayer.Builder(this).build();
        }
        if (mediaSession == null) {
            mediaSession = new MediaSession.Builder(this, audioPlayer).setCallback(new MediaSession.Callback() {
                @Override
                public ListenableFuture<SessionResult> onCustomCommand(MediaSession session, MediaSession.ControllerInfo controller, SessionCommand customCommand, Bundle args) {

                    switch (customCommand.customAction) {
                        case "PLAY":

                            break;
                        case "PAUSE":
                            pause();
                            break;
                        case "NEXT":
                            next();
                            break;
                        case "PREVIOUS":
                            back();
                            break;
                    }
                    return MediaSession.Callback.super.onCustomCommand(session, controller, customCommand, args);

                }
            }).build();
        }
        if (extractor == null) {
            int source_id = SourceExtractor.getInstance().gSource(this);
            Log.d(tag, "source id được lấy ra là " + String.valueOf(source_id));
            if (source_id == 0) {
                extractor = new DynamicYoutubeExtractor();
            } else {
                extractor = new DynamicSoundCloudExtractor();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("audio", "Audio Playback", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }

    public MutableLiveData<Boolean> getPlaying() {
        return playing;
    }


    @Override
    public void pause() {
        try {
            audioPlayer.pause();
            playing.setValue(false);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void play() {
        try {
            audioPlayer.play();
            playing.setValue(true);
        } catch (RuntimeException exception) {

        }
    }

    public void togglePausePlay() {
        try {
            if (audioPlayer.isPlaying()) {
                pause();
            } else {
                play();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void seekTo(Duration duration) {
        if (!audioPlayer.isPlaying()) {
            audioPlayer.seekTo(duration.toMillis());
            audioPlayer.play();
        } else {
            audioPlayer.seekTo(duration.toMillis());
        }
    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void playSong(Song song) {
        try {
            Song finalSong = playlist.getValue().gCurrentSong();
            Log.d("sub", String.valueOf(finalSong.getSubtitlesStreams().size()));
            for (var item : finalSong.getSubtitlesStreams()) {
                Log.d("sub", item.getUrl());
                Log.d("sub", item.getFormat().name);
                Log.d("sub", item.getDisplayLanguageName());

            }
            // Sắp xếp audioLink từ bitrate cao đến thấp
            List<AudioStream> sortedAudioLinks = new ArrayList<>(finalSong.getAudioLink());
            sortedAudioLinks.sort((a, b) -> Integer.compare(b.getAverageBitrate(), a.getAverageBitrate()));

            // Khởi tạo chỉ số và listener
            int[] index = {0};

            Player.Listener retryListener = new Player.Listener() {
                @Override
                public void onPlayerError(PlaybackException error) {
                    Log.e("Player", "Playback error: " + error.getMessage());
                    index[0]++;
                    if (index[0] < sortedAudioLinks.size()) {
                        AudioStream nextAudio = sortedAudioLinks.get(index[0]);
                        MediaItem nextItem = new MediaItem.Builder()
                                .setUri(nextAudio.getUrl())
                                .setMediaMetadata(new MediaMetadata.Builder()
                                        .setTitle(finalSong.getTitle())
                                        .setArtworkUri(Uri.parse(finalSong.getImages().get(0).getUrl()))
                                        .build())
                                .build();
                        audioPlayer.setMediaItem(nextItem);
                        audioPlayer.prepare();
                        audioPlayer.play();
                    } else {
                        Log.e("Player", "No valid audio links to play.");
                    }
                }
            };


            AudioStream firstAudio = sortedAudioLinks.get(0);
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(firstAudio.getUrl())
                    .setMediaMetadata(new MediaMetadata.Builder()
                            .setTitle(finalSong.getTitle())
                            .setArtworkUri(Uri.parse(finalSong.getImages().get(0).getUrl()))
                            .build())
                    .build();

            audioPlayer.setMediaItem(mediaItem);
            audioPlayer.addListener(retryListener);
            audioPlayer.prepare();
            audioPlayer.play();


            mediaSession.setPlayer(audioPlayer);
            Notification noti = new NotificationCompat.Builder(this, "audio")

                    .setContentTitle(finalSong.getTitle())
                    .setContentText("Playing audio")
                    .setSmallIcon(android.R.drawable.ic_media_play)
                    .setStyle(new MediaStyleNotificationHelper.MediaStyle(mediaSession))
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            startForeground(1, noti);

        } catch (Exception e) {
            Log.e("Player", "Error in playSong", e);
        }
    }


    @Nullable
    @Override
    public MediaSession onGetSession(MediaSession.ControllerInfo controllerInfo) {

        return mediaSession;
    }


    //    @Override
//    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
//        String start_key = intent.getDataString();
//
//        return super.onStartCommand(intent, flags, startId);
//    }


}

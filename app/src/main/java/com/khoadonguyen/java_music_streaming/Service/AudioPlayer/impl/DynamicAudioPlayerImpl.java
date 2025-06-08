package com.khoadonguyen.java_music_streaming.Service.AudioPlayer.impl;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import androidx.annotation.OptIn;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.media3.common.MediaItem;


import androidx.media3.common.MediaMetadata;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;

import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;
import androidx.media3.session.MediaStyleNotificationHelper;

import com.khoadonguyen.java_music_streaming.Model.Song;

import com.khoadonguyen.java_music_streaming.R;
import com.khoadonguyen.java_music_streaming.Service.AudioPlayer.DynamicAudioPlayer;
import com.khoadonguyen.java_music_streaming.Service.Playlist.Playlist;
import com.khoadonguyen.java_music_streaming.Service.extractor.impl.DynamicSoundCloudExtractor;
import com.khoadonguyen.java_music_streaming.Service.extractor.impl.DynamicYoutubeExtractor;

import java.time.Duration;


public class DynamicAudioPlayerImpl extends MediaSessionService implements DynamicAudioPlayer {
    private static final String tag = "DynamicAudioPlayerImpl";

    private MutableLiveData<Playlist> playlist = new MutableLiveData<>(new Playlist());

    private ExoPlayer audioPlayer;
    private DynamicSoundCloudExtractor dynamicYoutubeExtractor;
    private MediaSession mediaSession;

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public DynamicAudioPlayerImpl getService() {
            return DynamicAudioPlayerImpl.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        super.onBind(intent);
        return binder;
    }


    @Override
    public void start(Song song) {
        try {
            Log.d(tag, "khởi tạo bài hát đầu tiên của trình phát");
            Song firstSong = dynamicYoutubeExtractor.gsong(song.getUrl()).join();
            Log.d(tag, "lấy đuược first song title :" + firstSong.getTitle());
            if (playlist.getValue() == null) {
                playlist.setValue(new Playlist());
            }
            playlist.getValue().add(firstSong);

            Log.d(tag, "add first song to playlist");
            playSong(firstSong);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void next() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (audioPlayer == null) {
            audioPlayer = new ExoPlayer.Builder(this).build();
        }
        if (mediaSession == null) {
            mediaSession = new MediaSession.Builder(this, audioPlayer).setCallback(new MediaSession.Callback() {
            }).build();
        }
        if (dynamicYoutubeExtractor == null) {
            dynamicYoutubeExtractor = new DynamicSoundCloudExtractor();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "audio",
                    "Audio Playback",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }

    @Override
    public void back() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void seekTo(Duration duration) {

    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void playSong(Song song) {
        try {
            Log.d(tag, String.valueOf(playlist.getValue().size()));
            Log.d(tag, playlist.getValue().getFirst().getUrl().toString());
            song = playlist.getValue().gCurrentSong();
            MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(song.getAudioLink().getFirst().getUrl())
                    .setMediaMetadata(
                            new MediaMetadata.Builder()
                                    .setTitle(song.getTitle())
                                    .setArtworkUri(Uri.parse(song.getImages().getFirst().getUrl()))
                                    .build()
                    )
                    .build();


            audioPlayer.setMediaItem(mediaItem);
            Log.d(tag, "set Mediaitem thành công");
            audioPlayer.prepare();
            Log.d(tag, "audioplayer prepare");

            audioPlayer.play();
            Log.d(tag, "audioplayer play");
            mediaSession.setPlayer(audioPlayer);
            // Sửa notification của bạn - PHẢI có setSmallIcon()
            Notification noti = new NotificationCompat.Builder(this, "audio")
                    .setContentTitle("Track title")
                    .setContentText("Artist - Album")
                    .setSmallIcon(android.R.drawable.ic_media_play) // ← QUAN TRỌNG: Thêm dòng này!
                    .setStyle(new MediaStyleNotificationHelper.MediaStyle(mediaSession))
                    .setOngoing(true) // Không cho phép swipe để xóa
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            startForeground(1, noti);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
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

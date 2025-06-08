package com.khoadonguyen.java_music_streaming.Service.AudioPlayer;

import androidx.lifecycle.ViewModel;

import com.khoadonguyen.java_music_streaming.Model.Song;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public interface DynamicAudioPlayer  {
 void start(Song song);

 void next();

 void back();

 void pause();

 void seekTo(Duration duration);

void playSong(Song song);
}

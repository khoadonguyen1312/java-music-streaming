package com.khoadonguyen.java_music_streaming.Service.manager;

import com.khoadonguyen.java_music_streaming.Service.AudioPlayer.impl.DynamicAudioPlayerImpl;

public class AudioPlayerManager {
    private static DynamicAudioPlayerImpl audioService;

    public static void setAudioService(DynamicAudioPlayerImpl service) {
        audioService = service;
    }

    public static DynamicAudioPlayerImpl getAudioService() {
        return audioService;
    }

    public static boolean isReady() {
        return audioService != null;
    }
}

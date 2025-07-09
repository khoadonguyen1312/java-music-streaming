package com.khoadonguyen.java_music_streaming.Util;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class AudioOutputUtil {


    public static String gAudioOutPutName(Context context) {
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            if (audioManager.isBluetoothA2dpOn()) {
                // Đang sử dụng Bluetooth A2DP
                Log.d("Audio", "Using Bluetooth A2DP");
                return "bluetooth";
            } else if (audioManager.isSpeakerphoneOn()) {
                // Đang sử dụng loa ngoài
                Log.d("Audio", "Using Speaker");
                return "speaker";
            } else if (audioManager.isWiredHeadsetOn()) {
                // Đang sử dụng tai nghe có dây
                Log.d("Audio", "Using Wired Headset");
                return "headphone";
            } else {
                // Đang sử dụng loa trong (earpiece)
                Log.d("Audio", "Using Earpiece");
                return "earpiece";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}

package com.khoadonguyen.java_music_streaming.Service.extractor;

import android.content.Context;
import android.os.storage.StorageManager;
import android.util.Log;

import com.khoadonguyen.java_music_streaming.Service.extractor.impl.DynamicSoundCloudExtractor;
import com.khoadonguyen.java_music_streaming.Service.extractor.impl.DynamicYoutubeExtractor;
import com.khoadonguyen.java_music_streaming.storage.KeyNames;
import com.khoadonguyen.java_music_streaming.storage.SharePreferencesHelper;

public class SourceExtractor {

    private DynamicSoundCloudExtractor dynamicSoundCloudExtractor;
    private DynamicYoutubeExtractor dynamicYoutubeExtractor;

    public int gSource(Context context) {
        try {
            int source_id = SharePreferencesHelper.getInt(KeyNames.SOURCEMODE, context);
            Log.d("gsource", String.valueOf(source_id));
            return source_id;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public Extractor gExtractor(Context context) {
        try {
            Log.d("gsource", "lấy extractor");
            if (gSource(context) == 0) {

                Log.d("", "youtube service");
                return new DynamicYoutubeExtractor();
            } else {

                Log.d("", "Soundcloud service");
                return new DynamicSoundCloudExtractor();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    public void changeSource(Context context) {
        try {
            if (gSource(context) == 0) {
                SharePreferencesHelper.setInt(KeyNames.SOURCEMODE, 1, context);

            } else {
                SharePreferencesHelper.setInt(KeyNames.SOURCEMODE, 0, context);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

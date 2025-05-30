package com.khoadonguyen.java_music_streaming.Service.impl;

import android.location.Location;
import android.util.Log;

import com.khoadonguyen.java_music_streaming.Model.Song;
import com.khoadonguyen.java_music_streaming.Model.Source;
import com.khoadonguyen.java_music_streaming.Service.DynamicDownloader;
import com.khoadonguyen.java_music_streaming.Service.Extractor;

import org.schabi.newpipe.extractor.MediaFormat;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.services.soundcloud.SoundcloudService;

import org.schabi.newpipe.extractor.stream.StreamExtractor;

import java.io.IOException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DynamicSoundCloudExtractor implements Extractor {
    static final String tag = "DynamicSoundCloudExtractor";

    @Override
    public CompletableFuture<Song> gsong(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Song song;
                SoundcloudService soundcloudService = initService();
                StreamExtractor streamExtractor = soundcloudService.getStreamExtractor(url);
                if (streamExtractor != null) {
                    streamExtractor.fetchPage();
                    song = new Song(streamExtractor.getId(),
                            streamExtractor.getUrl(),
                            streamExtractor.getAudioStreams(),
                            Source.SOUNDCLOUD, streamExtractor.getName(), streamExtractor.getThumbnails(), streamExtractor.getSubtitles(MediaFormat.VTT));
                    Log.d(tag, "lấy thành công bài hát từ soundcloud có url :" + url);

                    return song;

                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            } catch (ExtractionException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            return null;
        });
    }

    @Override
    public CompletableFuture<List<Song>> search(String query, Location location) {
        return null;
    }

    @Override
    public Song gsong(List<String> urls) {
        return null;
    }

    @Override
    public SoundcloudService initService() {
        try {
            Downloader downloader = new DynamicDownloader();
            NewPipe.init(downloader);

            SoundcloudService soundcloudService = (SoundcloudService) NewPipe.getService(1);
            Log.d(tag, "Khởi tạo thành công soundcloud service");

            return soundcloudService;

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (ExtractionException e) {
            throw new RuntimeException(e);
        }

    }
}

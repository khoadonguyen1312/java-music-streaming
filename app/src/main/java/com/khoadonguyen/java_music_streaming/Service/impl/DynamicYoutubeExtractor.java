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
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.services.youtube.YoutubeService;
import org.schabi.newpipe.extractor.stream.StreamExtractor;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DynamicYoutubeExtractor implements Extractor {
    final static String tag = "DynamicYoutubeExtractor";

    @Override
    public CompletableFuture<Song> gsong(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Song song;
                YoutubeService youtubeService = initService();

                StreamExtractor streamExtractor = youtubeService.getStreamExtractor(url);

                if (streamExtractor != null) {
                    streamExtractor.fetchPage();
                    Log.d(tag, "lấy thành công streamExtractor cho video có url :" + url);
                    song = new Song(streamExtractor.getId(),
                            streamExtractor.getUrl(),
                            streamExtractor.getAudioStreams(), Source.YOUTUBE, streamExtractor.getName(),
                            streamExtractor.getThumbnails(), streamExtractor.getSubtitles(MediaFormat.VTT));
                    Log.d(tag, "tra ve thanh cong song object voi ten :" + song.getTitle());
                    return song;
                }
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
    public YoutubeService initService() {
        try {
            Downloader downloader = new DynamicDownloader();

            NewPipe.init(downloader);

            YoutubeService youtubeService = (YoutubeService) NewPipe.getService(0);
            Log.d(tag, "khởi tạo thành công youtube service");
            return youtubeService;
        } catch (ExtractionException e) {
            throw new RuntimeException(e);
        }
    }
}
